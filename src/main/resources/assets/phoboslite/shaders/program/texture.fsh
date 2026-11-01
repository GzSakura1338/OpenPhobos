#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PageSampler;

uniform vec4 outlinecolor;
uniform vec4 color;

uniform int quality;
uniform int overlay;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;


void main() {
    vec4 centerCol = texture2D(DiffuseSampler, texCoord);
    vec4 textured = texture2D(PageSampler, texCoord);
    vec4 outlineColor = textured * outlinecolor;

    if (centerCol.a != 0.0) {
        if (overlay == 2) {
            fragColor = vec4(outlineColor.rgb, 0.0);
            return;
        }

        fragColor = textured * color;
        return;
    }

    for (int x = -quality; x <= quality; x++) {
        for (int y = -quality; y <= quality; y++) {
            vec2 offset = vec2(x, y);
            vec2 coord = texCoord + offset * oneTexel;
            vec4 t = texture(DiffuseSampler, coord);

            if (t.a != 0) {
                fragColor = outlineColor;
                return;
            }
        }
    }

    discard;
}