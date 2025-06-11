#version 110

varying vec4 texColor;

uniform mat4 filter;

void main() {
    gl_FragColor = filter * texColor;
}
