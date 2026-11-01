#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D TextureSampler;
in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;


void main() {
    fragColor = texture(TextureSampler, texCoord);
}