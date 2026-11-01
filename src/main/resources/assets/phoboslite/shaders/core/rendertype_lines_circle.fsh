#version 150

uniform vec4 ColorModulator;
uniform vec4 color1;
uniform vec4 color2;
uniform float steps;
uniform float time;

in float vertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

vec4 wave(vec2 pos, vec4 col1, vec4 col2) {
    return mix(col1, col2, sin((distance(vec2(0), pos) - time * 60.0) / steps) * 0.5 + 0.5);
}

void main() {
    vec4 result = wave(gl_FragCoord.xy, color1, color2);
    fragColor = vec4(result.rgb, min(color1.a, result.a));
}
