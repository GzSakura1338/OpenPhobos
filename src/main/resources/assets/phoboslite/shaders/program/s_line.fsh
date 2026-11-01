#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MaskSampler;

uniform vec4 color;
uniform vec4 colorF;
uniform vec4 colorE;

uniform vec4 outlinecolor;
uniform int overlay;
uniform int sharp;

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

void main() {
    if (overlay < 0) { // Solid
        fragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    vec4 centerColor = texture2D(MaskSampler, texCoord);

    if (overlay <= 1 && centerColor.a > 0.0) { // Glow or Overlay
        fragColor = getFillColor(centerColor);
        return;
    }

    vec4 blurred = texture2D(DiffuseSampler, texCoord);

    if (centerColor.a != 0.0) {
        fragColor = blurred;
        return;
    }

    // "Overlay"
    if (overlay > 0 && sharp > 0) {
        for (float x = -1.0; x <= 1.0; x += 1.0) {
            for (float y = -1.0; y <= 1.0; y += 1.0) {
                if (x == 0.0 && y == 0.0) continue;

                vec2 offset = vec2(x * oneTexel.x, y * oneTexel.y);

                if (texture2D(MaskSampler, texCoord + offset).a != 0.0) {
                    fragColor = outlinecolor;
                    return;
                }
            }
        }
    }

    fragColor = blurred;
}