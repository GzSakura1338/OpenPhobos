#version 150

uniform sampler2D DiffuseSampler;

uniform vec4 color;
uniform vec4 colorF;
uniform vec4 colorE;

uniform vec4 outlinecolor;

uniform vec2 resolution;
uniform vec2 InSize;
uniform float alpha0;
uniform int overlay;
uniform int quality;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

vec4 getFillColor(vec4 centerCol) {
    if (centerCol.g > 0.0) {
        return color;
    }

    if (centerCol.b > 0.0) {
        return colorF;
    }

    return colorE;
}

void iterate(vec4 outlinecolor, float side) {
    float alphaOutline = 0.0;
    vec3 colorFinal = outlinecolor.rgb;

    for (int x = -quality; x <= quality; x++) {
        for (int y = -quality; y <= quality; y++) {
            vec2 offset = vec2(x, y);
            vec2 coord = texCoord + offset * oneTexel;
            vec4 t = texture(DiffuseSampler, coord);

            if (t.a != side) {
                fragColor = outlinecolor;
                return;
            }
        }
    }

    discard;
}

void main() {
    vec4 centerCol = texture2D(DiffuseSampler, texCoord);

    if (centerCol.a != 0) {
        if (overlay == 2) {
            fragColor = vec4(outlinecolor.rgb, 0.0);
            return;
        }

        fragColor = getFillColor(centerCol);
        return;
    }

    iterate(outlinecolor, 0.0);
}