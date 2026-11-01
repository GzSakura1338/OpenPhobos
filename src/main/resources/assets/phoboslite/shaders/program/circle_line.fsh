#version 150

uniform sampler2D DiffuseSampler;
uniform sampler2D MaskSampler;
uniform sampler2D PageSampler;

uniform vec4 color1;
uniform vec4 color2;

uniform float steps;
uniform float time;

uniform int overlay;
uniform int sharp;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

vec4 wave(vec2 pos, vec4 col1, vec4 col2) {
    return mix(col1, col2, sin((distance(vec2(0), pos) - time * 60.0) / steps) * 0.5 + 0.5);
}

void main() {
    vec4 outlinecolor = vec4(texture2D(PageSampler, texCoord).rgb, 1.0);

    if (overlay < 0) {
        fragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    vec4 centerColor = texture2D(MaskSampler, texCoord);

    if (overlay <= 1 && centerColor.a > 0.0) { // Glow or Overlay
        fragColor = wave(gl_FragCoord.xy, color1, color2);
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
                    fragColor = outlinecolor;
                    return;
                }
            }
        }
    }

    fragColor = blurred;
}