#version 110

attribute vec2 pos;
attribute vec2 coord;

varying vec2 texCoord;
varying mat4 texFilter;

uniform mat4 model;
uniform mat4 projection;

void main() {
    gl_Position = projection * model * vec4(pos, 0.0, 1.0);
    texCoord = coord;
}
