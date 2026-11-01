#version 150

uniform sampler2D DiffuseSampler;
uniform vec2 resolution;
uniform float span;
uniform float multiplication;
uniform float minimum;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;


void main() {
    vec2 offset = 1.0/resolution.xy;
    vec2 uv = ((gl_FragCoord.xy / 2.0) / resolution.xy);

    vec3 nw = texture(DiffuseSampler, uv + vec2(-1.0, -1.0) * offset).rgb;
    vec3 ne = texture(DiffuseSampler, uv + vec2( 1.0, -1.0) * offset).rgb;
    vec3 sw = texture(DiffuseSampler, uv + vec2(-1.0,  1.0) * offset).rgb;
    vec3 se = texture(DiffuseSampler, uv + vec2( 1.0,  1.0) * offset).rgb;
    vec3 m  = texture(DiffuseSampler, uv).rgb;

    vec3 luma = vec3(0.299, 0.587, 0.114);
    float lumaNW = dot(nw, luma);
    float lumaNE = dot(ne, luma);
    float lumaSW = dot(sw, luma);
    float lumaSE = dot(se, luma);
    float lumaM  = dot(m,  luma);

    float lumaMin = min(lumaM, min(min(lumaNW, lumaNE), min(lumaSW, lumaSE)));
    float lumaMax = max(lumaM, max(max(lumaNW, lumaNE), max(lumaSW, lumaSE)));
    vec2 dir = vec2(
    -((lumaNW + lumaNE) - (lumaSW + lumaSE)),
    ((lumaNW + lumaSW) - (lumaNE + lumaSE)));

    float dirReduce = max((lumaNW + lumaNE + lumaSW + lumaSE) * (0.25 * multiplication), minimum);
    float rcpDirMin = 1.0 / (min(abs(dir.x), abs(dir.y)) + dirReduce);
    dir = min(vec2(span), max(vec2(-span), dir * rcpDirMin)) * offset;

    vec3 rgbA = 0.5 * (texture(DiffuseSampler, uv + dir * (1.0 / 3.0 - 0.5)).xyz + texture(DiffuseSampler, uv + dir * (2.0 / 3.0 - 0.5)).xyz);
    vec3 rgbB = rgbA * 0.5 + 0.25 * (texture(DiffuseSampler, uv + dir * -0.5).xyz + texture(DiffuseSampler, uv + dir * 0.5).xyz);
    float lumaB = dot(rgbB, luma);
    if (lumaB < lumaMin || lumaB > lumaMax) {
        fragColor = vec4(rgbA, 1.0);
    } else {
        fragColor = vec4(rgbB, 1.0);
    }
}