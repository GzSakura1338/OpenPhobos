#version 150

in vec4 Position;

out vec2 texCoord;

void main() {
    gl_Position = Position;
    texCoord = (Position.xy + 1.0) / 2.0;
}