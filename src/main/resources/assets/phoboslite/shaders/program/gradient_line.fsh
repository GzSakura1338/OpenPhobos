#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MaskSampler;
uniform sampler2D PageSampler;

uniform float alpha2;
uniform float alpha2F;
uniform float alpha2E;

uniform int overlay;
uniform int sharp;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

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
    vec4 pageColor = vec4(texture2D(PageSampler, texCoord).rgb, 1.0);

    if (overlay < 0) {
        fragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    vec4 centerColor = texture2D(MaskSampler, texCoord);

    if (overlay <= 1 && centerColor.a > 0.0) { // Glow or Overlay
        fragColor = vec4(pageColor.rgb, getFillAlpha(centerColor));
        return;
    }

    vec4 blurred = texture2D(DiffuseSampler, texCoord);

    if (centerColor.a != 0.0) {
        fragColor = blurred;
        return;
    }

    if (overlay > 0 && sharp > 0) {
        for (float x = -1.0; x <= 1.0; x += 1.0) {
            for (float y = -1.0; y <= 1.0; y += 1.0) {
                if (x == 0.0 && y == 0.0) continue;
                vec2 offset = vec2(x * oneTexel.x, y * oneTexel.y);
                if (texture2D(MaskSampler, texCoord + offset).a != 0.0) {
                    fragColor = pageColor;
                    return;
                }
            }
        }
    }

    fragColor = blurred;
}