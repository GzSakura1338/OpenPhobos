#version 150

uniform vec2 InputResolution;
uniform float time;
uniform vec4 color;

in vec2 texCoord;

out vec4 fragColor;

float plasma(vec2 uv, float scale, float time) {
    float v = cos(uv.x * uv.y * scale) - cos(uv.x / (0.4 + uv.y) + time);
    float f = floor(v);
    float c = ceil(v);
    float e = min(c - v, v - f);
 	float r = min(pow(e * 1.8, 0.7), 1.0);

    return r;
}

void main() {
    float scale = 10.;
    float deform = 0.6;
    vec2 uv = (gl_FragCoord.xy / InputResolution.xy) - 0.5;
    uv.y *= deform;

    float r0 = plasma(uv, scale, time);
    float r1 = plasma(uv, scale * 2., time * 1.5 + 0.32);

    float r = r0 * r1;

    vec2 centeruv = (gl_FragCoord.xy / InputResolution.xy * 2.0) - 1.0;
    r *= 1.0 - pow(length(centeruv * centeruv) * 0.8,6.0);
    vec4 col = r * color;
    fragColor = vec4(col.rgb, color.a);
}