#version 110

varying vec2 texCoord;

uniform sampler2D tex;
uniform mat4 filter;

void main() {
    gl_FragColor = filter * texture2D(tex, texCoord);
}
