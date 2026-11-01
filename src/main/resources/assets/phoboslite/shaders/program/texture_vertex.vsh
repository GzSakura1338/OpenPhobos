#version 150

in vec2 Position;
in vec2 TexCoord;
out vec2 texCoord;
out vec2 oneTexel;

uniform mat4 ProjMat;
uniform vec2 InSize;
uniform vec2 OutSize;

void main() {
    gl_Position = ProjMat * vec4(Position, 0.0, 1.0);
    texCoord = TexCoord;
    oneTexel = 1.0 / InSize;
}