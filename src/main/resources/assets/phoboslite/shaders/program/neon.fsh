#version 150


in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D DiffuseSampler;
uniform vec2 resolution;
uniform float time;
uniform vec4 color;
uniform vec4 outlinecolor;
uniform int quality;
uniform int overlay;
uniform vec2 InSize;
uniform float alpha0;

out vec4 fragColor;

const float PI = 3.14159;

vec3 getColor() {
    vec2 uv = gl_FragCoord.xy / resolution;

    float d = 0.0;
    int n = 18;
    vec3 col = vec3(0.);

    for (int i = 0; i < n; i++) {
        float findex = float(i) / float(n);
        float speed = 1.0 + 1.0 - findex;
        vec2 uv2 = vec2(uv.x, min(1., max(0., uv.y + 0.1 * sin(uv.x * 2. * PI + time * speed + findex * 10.1))));
        float offset = 3. * findex * PI + 0.3 * time * (0.2 + findex * 0.3);
        float f = 0.5 * (1. + sin(offset));
        float d = abs(f - uv2.y);
        float fcol = pow(1. - pow(d, 0.4), 3.5 + 4. * 0.5 * (1. + sin(time * (1. + findex) + 4. * findex * PI)));
        float r = 360. * findex * fcol;
        float b = i % 2 == 1 ? fcol * 0.9 : 0.;
        col += vec3(b, fcol * findex, fcol);
    }

    return col;
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
    vec4 outlineCol = vec4(col0, outlinecolor.a);

    if (centerCol.a != 0.0) {
        if (overlay == 2) {
            iterate(outlineCol, 1.0);
            return;
        }

        fragColor = vec4(col0, color.a);
        return;
    }

    iterate(outlineCol, 0.0);

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
//                    alphaOutline += max(0.0, falloff) * 0.01 * outlinecolor.a;
//                    continue;
//                }
//
//                fragColor = vec4(col0, outlinecolor.a);
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