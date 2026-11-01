#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 outline1;
uniform vec4 outline2;
uniform float time;
uniform float steps;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

vec4 wave(vec2 pos, vec4 col1, vec4 col2) {
    return mix(col1, col2, sin((distance(vec2(0), pos) - time * 60.0) / steps) * 0.5 + 0.5);
}

void main() {
    fragColor = wave(gl_FragCoord.xy, outline1, outline2);
}