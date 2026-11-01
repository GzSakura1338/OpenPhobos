#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 resolution;
uniform float intensity;
uniform float blur;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 sum = vec4(0);
    vec2 texcoord = ((gl_FragCoord.xy / 2.0) / resolution.xy);

    sum += texture(DiffuseSampler, vec2(texcoord.x - 4.0 * blur, texcoord.y)) * 0.05;
    sum += texture(DiffuseSampler, vec2(texcoord.x - 3.0 * blur, texcoord.y)) * 0.09;
    sum += texture(DiffuseSampler, vec2(texcoord.x - 2.0 * blur, texcoord.y)) * 0.12;
    sum += texture(DiffuseSampler, vec2(texcoord.x - blur, texcoord.y)) * 0.15;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y)) * 0.16;
    sum += texture(DiffuseSampler, vec2(texcoord.x + blur, texcoord.y)) * 0.15;
    sum += texture(DiffuseSampler, vec2(texcoord.x + 2.0 * blur, texcoord.y)) * 0.12;
    sum += texture(DiffuseSampler, vec2(texcoord.x + 3.0 * blur, texcoord.y)) * 0.09;
    sum += texture(DiffuseSampler, vec2(texcoord.x + 4.0 * blur, texcoord.y)) * 0.05;

    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y - 4.0 * blur)) * 0.05;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y - 3.0 * blur)) * 0.09;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y - 2.0 * blur)) * 0.12;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y - blur)) * 0.15;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y)) * 0.16;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y + blur)) * 0.15;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y + 2.0 * blur)) * 0.12;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y + 3.0 * blur)) * 0.09;
    sum += texture(DiffuseSampler, vec2(texcoord.x, texcoord.y + 4.0 * blur)) * 0.05;

    fragColor = sum * intensity + texture(DiffuseSampler, texcoord);
}