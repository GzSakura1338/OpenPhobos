#version 150

uniform vec4 color;

uniform vec2 uSize;
uniform vec2 uLocation;

uniform float radius;

//uniform float blend;
//uniform float alpha;

//uniform float outline;
//uniform float glow;

out vec4 fragColor;

float roundedBoxSDF(vec2 center, vec2 size) {
    return length(max(abs(center) - size, 0.0));
}

void main() {
    vec2 centeredCoord = gl_FragCoord.xy - uLocation - (uSize / 2.0);
    vec2 normCoord = (gl_FragCoord.xy - uLocation) / uSize;

    float distance = roundedBoxSDF(centeredCoord, uSize / 2.0);
    float smoothedAlpha = (1.0 - smoothstep(-10, 10, distance)) * color.a;
    float smoothedAlpha2 = (1.0 - smoothstep(-1., 1., distance)) * color.a;

    if (smoothedAlpha2 < 1.0f) {
        fragColor = color;
    } else {
        float distance1 = roundedBoxSDF(centeredCoord, (uSize / 2.0) + 0.5 - 1.0f);
        float blendAmount = smoothstep(0., 2., abs(distance1) - 1.0f);
        vec4 insideColorVec = (distance1 < 0.) ? color : vec4(insideColor, 0.0);
        fragColor = mix(vec4(color, 1.), insideColorVec, blendAmount);
    }
}