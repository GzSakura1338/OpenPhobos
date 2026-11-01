#version 150

uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec4 color1;
uniform vec4 color2;
uniform float time;

in float vertexDistance;
in vec4 vertexColor;

out vec4 fragColor;

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    if (vertexDistance <= fogStart) {
        return inColor;
    }

    float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, vertexDistance) : 1.0;
    return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}

vec4 wave(vec2 pos, vec4 col1, vec4 col2) {
    return mix(col1, col2, sin((distance(vec2(0), pos) - time * 60.0) / 60.0) * 0.5 + 0.5);
}

vec4 getColor() {
    return wave(gl_FragCoord.xy, color1, color2);
}

void main() {
    vec4 color = getColor();
    fragColor = linear_fog(color, vertexDistance, FogStart, FogEnd, FogColor);
}
