#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PrevSampler;

in vec2 texCoord;
in vec2 oneTexel;

uniform vec2 resolution;

uniform float BlendFactor = 0.75;
uniform float Sample0 = 0;
uniform float Falloff;

out vec4 fragColor;

void main() {
    if (Sample0 == 1.0) {

        vec2 uv = texCoord.xy / resolution.xy;
        vec2 coord = (uv - 0.5) * (resolution.x / resolution.y) * 2.0;
        float rf = sqrt(dot(coord, coord)) * Falloff;
        float rf2_1 = rf * rf + 1.0;
        float e = 1.0 / (rf2_1 * rf2_1);

        vec4 CurrTexel = texture(DiffuseSampler, texCoord);
        vec4 PrevTexel = texture(PrevSampler, texCoord);

        fragColor = mix(CurrTexel, PrevTexel, BlendFactor * (1.0 - e));
        fragColor.w = 1.0;
    } else {
        vec4 CurrTexel = texture(DiffuseSampler, texCoord);
        vec4 PrevTexel = texture(PrevSampler, texCoord);

        fragColor = mix(CurrTexel, PrevTexel, BlendFactor);
        fragColor.w = 1.0;
    }
}