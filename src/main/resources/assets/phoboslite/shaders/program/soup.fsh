#version 150

#define OCTAVES 6

uniform sampler2D DiffuseSampler;
in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;
uniform float alpha0;
uniform vec4 outlineColor;
uniform vec4 color;
uniform int quality;
uniform int overlay;

uniform vec2 resolution;
uniform float time;

float random(vec2 p) {
    return fract(sin(dot(p.xy, vec2(12.9898, 78.233))) * 43758.5453123);
}

float noise(vec2 p) {
    vec2 s = floor(p);

    float a = random(s);
    float b = random(s + vec2(1.0, 0.0));
    float c = random(s + vec2(0.0, 1.0));
    float d = random(s + vec2(1.0, 1.0));

    vec2 f = smoothstep(0.0, 1.0, fract(p));

    float ab = mix(a, b, f.x);
    float cd = mix(c, d, f.x);

    float o = mix(ab, cd, f.y);

    return o;

}

float fractal(vec2 p) {
    float o = 0.0;
    float strength = 0.5;
    vec2 position = p;

    for (int i = 0; i < OCTAVES; i++) {

        o += noise(position) * strength;
        position *= 2.0;
        strength *= 0.5;

    }

    o /= 1.0 - 0.5 * pow(0.5, float(OCTAVES - 1));

    return o;

}

mat2 rotate(float r) {
    return mat2(
        cos(r), -sin(r),
        sin(r), cos(r)
    );
}

vec3 getColor() {
    vec2 uv = texCoord / resolution.xy * 2.0 - 1.0;
    uv.x *= resolution.x / resolution.y;
    uv /= 0.02;
    uv += vec2(4.0);

    float n = fractal((uv + time / 200.0) * 1.0);
    float r = n * 3.1415 * 2.0;

    uv += vec2(0.0, 1.0) * rotate(r);

    float n_0 = fractal((uv + time / 100.0) * 8.0);
    float r_0 = n_0 * 3.1415 * 2.0;

    float n_1 = fractal((uv + vec2(0.0, 0.1) * rotate(r_0) - time / 200.0) * 8.0);
    float r_1 = n_1 * 3.1415 * 2.0;

    uv += vec2(0.0, 0.1) * rotate(r_1);

    float m1 = sin(uv.y * 60.0/* + time / 1.0*/) / 2.0 + 0.5;
    float m2 = sin(uv.y * 5.0/* + time / 2.0*/) / 2.0 + 0.5;
    float m3 = sin(uv.y * 10.0/* + time / 5.0*/) / 2.0 + 0.5;

    return vec3(color.r * m1, color.g * m2, color.b * m3);
}

void iterate(vec4 outlinecolor, float side) {
    float alphaOutline = 0.0;
    vec3 colorFinal = outlinecolor.rgb;
    bool hasOutline = false;

    for (int x = -quality; x <= quality; x++) {
        for (int y = -quality; y <= quality; y++) {
            vec2 offset = vec2(x, y);
            vec2 coord = texCoord + offset * oneTexel;
            vec4 t = texture(DiffuseSampler, coord);

            if (t.a != side) {
                if (alpha0 == -1.0) {
                    float dst = length(offset); // Use length instead of distance

                    if (dst <= 1.5) {
                        hasOutline = true;
                        break;
                    }

                    float falloff = exp((-dst * 100.0) / 500.0);
                    alphaOutline += max(0.0, falloff) * 0.01 * outlinecolor.a;
                    continue;
                }

                fragColor = outlinecolor;
                return;
            }
        }
    }

    if (hasOutline && (overlay == 1 || overlay == 2) && side == 0.0 /* Outer, first line */) {
        fragColor = vec4(outlinecolor.rgb, 1.0); // overlay
    } else if (alphaOutline > 0.0) {
        fragColor = vec4(colorFinal, alphaOutline); // normal glow
    } else {
        discard; // solid (its just setup up there in the loop)
    }
}

void main() {
    vec4 centerCol = texture(DiffuseSampler, texCoord);
    vec3 col0 = getColor();
    vec4 outlinecolor = vec4(col0, outlineColor.a);

    if (centerCol.a != 0.0) {
        if (overlay == 2) {
            iterate(outlinecolor, 1.0);
            return;
        }

        fragColor = vec4(col0, color.a);
        return;
    }

    iterate(outlinecolor, 0.0);


//    float alphaOutline = 0;
//    bool hasOutline = false;
//
//    for (int x = -quality; x <= quality; x++) {
//        for (int y = -quality; y <= quality; y++) {
//            vec2 offset = vec2(x, y);
//            vec2 coord = texCoord + offset * oneTexel;
//            vec4 t = texture(DiffuseSampler, coord);
//
//            if (t.a != 0) {
//                if (alpha0 == -1.0) {
//                    float dst = length(offset);
//
//                    if (dst <= 1.5) {
//                        hasOutline = true;
//                    }
//
//                    float falloff = exp((-dst * 100.0) / 500.0);
//                    alphaOutline += max(0.0, falloff) * 0.01 * outlineColor.a;
//                    continue;
//                }
//
//                fragColor = vec4(col0, outlineColor.a);
//                return;
//            }
//        }
//    }
//
//    if (hasOutline && overlay == 1) {
//        fragColor = vec4(col0, 1.0); // overlay
//    } else if (alphaOutline > 0.0) {
//        fragColor = vec4(col0, alphaOutline); // normal glow
//    } else {
//        discard; // solid (its just setup up there in the loop)
//    }
}