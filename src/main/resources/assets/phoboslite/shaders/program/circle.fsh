#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D PageSampler;
uniform vec4 color1;
uniform vec4 color2;
uniform float time;
uniform float alpha0;
uniform int quality;
uniform int overlay;
uniform float steps;

in vec2 texCoord;
in vec2 oneTexel;
out vec4 fragColor;

vec4 wave(vec2 pos, vec4 col1, vec4 col2) {
    return mix(col1, col2, sin((distance(vec2(0), pos) - time * 60.0) / steps) * 0.5 + 0.5);
}

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

void main() {
    vec4 centerCol = texture(DiffuseSampler, texCoord);
    vec4 fillColor = wave(gl_FragCoord.xy, color1, color2);
    vec4 outlineColor = texture2D(PageSampler, texCoord);

    if (centerCol.a != 0) {
        if (overlay == 2) {
            fragColor = vec4(outlineColor.rgb, 0.0);
            return;
        }

        fragColor = fillColor;
        return;
    }

    iterate(outlineColor, 0.0);
}