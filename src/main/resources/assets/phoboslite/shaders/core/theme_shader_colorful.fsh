#version 150

uniform vec2 InputResolution;
uniform float time;
uniform float alpha;

in vec2 texCoord;

out vec4 fragColor;

#define NUM_LAYERS 7.

float rand(vec2 p) {
    return fract(sin(p.x * 317. + p.y * 137.) * 132.);
}

float star(vec2 uv, float rays) {
    float d = length(uv);
    float m = .1 / d;

    m += 1. / exp(100. * abs(uv.x * uv.y)) * rays;

    m *= smoothstep(1.0, 0.2, d);

    return m;
}

vec3 starLayer(vec2 uv) {
    float T = time * 1.8;
    vec2 gv = fract(uv) - .5;
    vec2 id = floor(uv);

    vec3 col = vec3(0.);

    for (float y = -1.; y <= 1.; ++y) {
        for (float x = -1.; x <= 1.; ++x) {
            vec2 offs = vec2(x, y);
            float r = rand(id + offs);
            float size = max(0.3, fract(r * 127.));
            float rays = exp((size - 1.) * 50.) * 2.;
            float blink = sin(T * r) * 0.5 + 0.5;
            vec3 c = 2. * normalize(vec3(0.8, 0.4, 0.9)) * fract(vec3(r * 123., r * 987., r * 567.));
            col += blink * c * size * star(gv - offs - vec2(r, fract(r * 275.)) + .5, rays);
        }
    }

    return col;
}

mat2 rot(in float a) {
    float s = sin(a);
    float c = cos(a);
    return mat2(c, -s, s, c);
}

void main() {
    vec2 fragCoord = gl_FragCoord.xy;
    vec2 uv = (fragCoord - InputResolution.xy * .5) / InputResolution.y;

    vec3 col = vec3(0.);

    float t = time * .08;
    uv *= rot(t);
    for (float i = 0.; i < 1.; i += 1. / NUM_LAYERS) {
        float depth = fract(i + t);
        float scale = mix(20., 2., depth);
        float fade = depth * smoothstep(1., .9, depth);
        col += fade * starLayer(uv * scale + 323. * i);
    }

    fragColor = vec4(col, alpha);
}