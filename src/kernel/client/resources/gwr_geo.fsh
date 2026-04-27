#version 110

varying vec4 texColor;
varying vec3 texNormal;

uniform mat4 filter;
uniform vec3 lightDir;

void main() {
    float ambientStrength = 0.1;
    vec3 lightColor = vec3(1.0, 1.0, 1.0);
    vec3 ambient = ambientStrength * lightColor;
    vec3 norm = normalize(texNormal);
    vec3 lightDirNorm = normalize(-lightDir);
    float diff = max(dot(norm, lightDirNorm), 0.0);
    vec3 diffuse = diff * lightColor;
    gl_FragColor = filter * vec4(ambient + diffuse, 1.0) * texColor;
}
