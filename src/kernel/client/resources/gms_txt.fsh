#version 110

varying vec2 texCoord;

uniform sampler2D msdfTex; // The generated MSDF atlas texture
uniform vec4 textColor;    // Desired text color

// Helper function to extract the median of the RGB channels
float median(vec3 rgb) {
    return max(min(rgb.r, rgb.g), min(max(rgb.r, rgb.g), rgb.b));
}

float smoothstep_110(float edge0, float edge1, float x) {
    float t = clamp((x - edge0) / (edge1 - edge0), 0.0, 1.0);
    return t * t * (3.0 - 2.0 * t);
}

void main() {
    // 1. Sample the multi-channel signed distance field texture
    vec3 msdfSample = texture2D(msdfTex, texCoord).rgb;

    // 2. Extract the pseudo-distance by taking the median
    float sd = median(msdfSample);

    // 3. Screen-space derivatives ensure perfect antialiasing at any scale
    float pixelWindow = fwidth(sd);
    float opacity = smoothstep_110(0.5 - pixelWindow, 0.5 + pixelWindow, sd);

    // 4. Discard invisible pixels or mix color based on opacity
    if (opacity < 0.001) discard;

    gl_FragColor = vec4(textColor.rgb, textColor.a * opacity);
}
