#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PageSampler;
in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;
uniform int quality;
uniform int overlay;

uniform float alpha1;

uniform float alpha2;
uniform float alpha2F;
uniform float alpha2E;

void iterate(vec4 outlinecolor, float side) {
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

float getFillAlpha(vec4 centerCol) {
    if (centerCol.g > 0.0) {
        return alpha2;
    }

    if (centerCol.b > 0.0) {
        return alpha2F;
    }

    return alpha2E;
}

void main() {
    vec4 centerCol = texture(DiffuseSampler, texCoord);
    vec3 result = texture2D(PageSampler, texCoord).rgb;
    vec4 outlineColor = vec4(result, alpha1);

    if (centerCol.a != 0) {
        if (overlay == 2) {
            fragColor = vec4(outlineColor.rgb, 0.0);
            return;
        }

        fragColor = vec4(result, getFillAlpha(centerCol));
        return;
    }

    iterate(outlineColor, 0.0);
}