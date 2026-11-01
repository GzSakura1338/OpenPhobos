#version 150

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;
uniform float alpha0;
uniform vec4 outlineColor;
uniform int quality;
uniform int overlay;
uniform float alpha;

uniform vec2 resolution;
uniform float time;

#define NUM_LAYERS 4.0

float random2d(vec2 coord) {
    return fract(sin(dot(coord.xy, vec2(12.9898, 78.233))) * 43758.5453);
}

float flower(vec2 uv, float size, float a) {
    float c = 0.0;
    vec2 polar = vec2(atan(uv.x, uv.y), length(uv) - size);
    uv = vec2(polar.x / 6.2831 + time * 0.2, polar.y);
    float x = uv.x * a;
    float m = min(fract(x), 1.0 - fract(x));
    c += smoothstep(0.0, 0.1, m * 0.5 + 0.2 - uv.y);
    return c;
}

vec3 Flowerlayout(vec2 uv) {
    vec3 col = vec3(0.0);
    vec2 gv = fract(uv) - 0.5;
    vec2 id = floor(uv);

    for (int y = -1; y <= 1; y++) {
        for (int x = -1; x <= 1; x++) {
            vec2 offs = vec2(x, y);
            float n = random2d(id + offs);
            float size = fract(n * 325.2984);
            float a = clamp(0.1, 8.0, time);
            float star = flower(gv - offs - (vec2(n, fract(n * 10.0)) - 0.5), smoothstep(0.2, 0.1, size) * 0.8, a);
            vec3 color = sin(vec3(0.6, 0.8, 0.2) * fract(n * 6132.6) * 16.2831) * 0.5 + 0.5;
            star *= sin(time * 3.0 + n * 6.2831) + 1.0;
            col += star * size * color;
        }
    }

    return col;
}

vec3 getColor(vec4 centralCol) {
    vec2 uv = (gl_FragCoord.xy * 2.0 - resolution.xy) / resolution.y;
    vec3 col = vec3(0.0);

    for (float i = 0.0; i < 1.0; i += 1.0 / NUM_LAYERS) {
        float t = time * 0.02;
        float depth = fract(i + t);
        float fade = depth * smoothstep(1.0, 0.9, depth);
        float scale = mix(20.0, 0.5, depth);
        col += Flowerlayout(uv * scale + i * 664.4) * fade;
    }

    return col;
}

void main() {
    vec4 centerCol = texture(DiffuseSampler, texCoord);
    vec3 col0 = getColor(centerCol);

    if (centerCol.a != 0.0) {
        fragColor = vec4(col0, alpha);
        return;
    }

    float alphaOutline = 0;
    bool hasOutline = false;

    for (int x = -quality; x <= quality; x++) {
        for (int y = -quality; y <= quality; y++) {
            vec2 offset = vec2(x, y);
            vec2 coord = texCoord + offset * oneTexel;
            vec4 t = texture(DiffuseSampler, coord);

            if (t.a != 0) {
                if (alpha0 == -1.0) {
                    float dst = length(offset);

                    if (dst <= 1.5) {
                        hasOutline = true;
                    }

                    float falloff = exp((-dst * 100.0) / 500.0);
                    alphaOutline += max(0.0, falloff) * 0.01 * outlineColor.a;
                    continue;
                }

                fragColor = vec4(col0, outlineColor.a);
                return;
            }
        }
    }

    if (hasOutline && overlay == 1) {
        fragColor = vec4(col0, 1.0); // overlay
    } else if (alphaOutline > 0.0) {
        fragColor = vec4(col0, alphaOutline); // normal glow
    } else {
        discard; // solid (its just setup up there in the loop)
    }
}