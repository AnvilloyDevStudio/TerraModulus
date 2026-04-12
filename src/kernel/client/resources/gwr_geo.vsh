#version 110

attribute vec3 pos;
attribute vec4 color;

varying vec4 texColor;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;

void main() {
    gl_Position = projection * view * model * vec4(pos, 1.0);
    texColor = color;
}
