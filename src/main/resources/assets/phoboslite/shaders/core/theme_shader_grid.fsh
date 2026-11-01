#version 150

uniform vec2 InputResolution;
uniform float time;
uniform vec4 color;

in vec2 texCoord;

out vec4 fragColor;

vec2 getScreenSpace() {
    vec2 uv = (gl_FragCoord.xy - 0.5 * InputResolution.xy) / min(InputResolution.y, InputResolution.x);

    return uv;
}

vec3 permute(vec3 x) { return mod(((x * 34.0) + 1.0) * x, 289.0); }

float snoise(vec2 v) {
    const vec4 C = vec4(0.211324865405187, 0.366025403784439,
    -0.577350269189626, 0.024390243902439);
    vec2 i = floor(v + dot(v, C.yy));
    vec2 x0 = v - i + dot(i, C.xx);
    vec2 i1;
    i1 = (x0.x > x0.y) ? vec2(1.0, 0.0) : vec2(0.0, 1.0);
    vec4 x12 = x0.xyxy + C.xxzz;
    x12.xy -= i1;
    i = mod(i, 289.0);
    vec3 p = permute( permute(i.y + vec3(0.0, i1.y, 1.0)) + i.x + vec3(0.0, i1.x, 1.0 ));
    vec3 m = max(0.5 - vec3(dot(x0, x0), dot(x12.xy, x12.xy),
    dot(x12.zw, x12.zw)), 0.0);
    m = m * m ;
    m = m * m ;
    vec3 x = 2.0 * fract(p * C.www) - 1.0;
    vec3 h = abs(x) - 0.5;
    vec3 ox = floor(x + 0.5);
    vec3 a0 = x - ox;
    m *= 1.79284291400159 - 0.85373472095314 * (a0 * a0 + h * h);
    vec3 g;
    g.x  = a0.x  * x0.x + h.x  * x0.y;
    g.yz = a0.yz * x12.xz + h.yz * x12.yw;
    return 130.0 * dot(m, g);
}

void main() {
    vec2 uv = getScreenSpace();

    float w = 30. / InputResolution.y;

    uv *= 30.;

    vec2 grid = floor(uv);
    vec2 subuv = fract(uv);

    vec3 col = vec3(0.);
    float d = 0.;

    for (int x = -1; x < 2; x++) {
        for (int y = -1; y < 2; y++) {
            vec2 os = vec2(x, y);
            vec2 _grid = grid + os;
            vec2 _subuv = subuv - os;

            float r = snoise(_grid * 0.1 + time * 0.5) * 0.2 + 0.3;
            d = r;
            col += vec3(smoothstep(r + w, r - w, length(_subuv - 0.5)));
        }
    }

    fragColor = vec4(col.rgb * color.rgb, color.a);
}