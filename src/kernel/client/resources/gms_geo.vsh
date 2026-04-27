#version 110

attribute vec2 pos;
attribute vec4 color;

varying vec4 texColor;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = projection * model * vec4(pos, 0.0, 1.0);
    texColor = color;
}
