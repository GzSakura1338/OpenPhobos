#version 150

uniform sampler2D DiffuseSampler;
uniform vec4 outlinecolor;
uniform int quality;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec4 centerColor = texture2D(DiffuseSampler, texCoord);
    vec2 pixOffset = oneTexel;

    if (centerColor.a == 0.0) {
        for (int x = -quality; x <= quality; x++) {
            for (int y = -quality; y <= quality; y++) {
                vec2 offset = vec2(float(x) * oneTexel.x, float(y) * oneTexel.y);
                if (texture2D(DiffuseSampler, texCoord + offset).a != 0.0) {
                    fragColor = outlinecolor;
                    return;
                }
            }
        }

        return;
    }

    fragColor = vec4(outlinecolor.rgb, 0.0);
}