#version 150

uniform sampler2D BufOut;  // from bufOut
uniform sampler2D Swap;  // from swap

in vec2 texCoord;
out vec4 fragColor;

void main() {
    vec4 hColor = texture(BufOut, texCoord);
    vec4 vColor = texture(Swap, texCoord);
    fragColor = (hColor + vColor) * 0.5;
}