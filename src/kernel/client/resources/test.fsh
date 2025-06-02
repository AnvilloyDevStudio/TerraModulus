#version 110

uniform sampler2D tex; // The 2D texture
varying vec2 texCoord; // Interpolated texture coordinates

void main() {
    gl_FragColor = texture2D(tex, texCoord);
}
