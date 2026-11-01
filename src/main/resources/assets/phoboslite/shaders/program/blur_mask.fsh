#version 150

uniform sampler2D DiffuseSampler;
uniform float width;
uniform float scale;
uniform int overlay;
uniform vec2 direction;
uniform float radius;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

float gaussian(float x, float sigma) {
    return exp(-((x * x) / (sigma * sigma)));
}

void main() {
    if (overlay < 0) {
        fragColor = texture2D(DiffuseSampler, texCoord);
        return;
    }

    float delta = (width * radius);
    float sum = 0;

    for (float x = -delta; x <= delta; x++) {
        sum += gaussian(x, scale);
    }

    vec4 blurred = vec4(0);

    for (float x = -delta; x <= delta; x += 1.0) {
        float normalizedWeight = gaussian(x, scale) / sum;
        vec2 offset = oneTexel * x * direction;
        vec4 current = texture(DiffuseSampler, texCoord + offset);
        blurred += current * normalizedWeight;
    }

    fragColor = blurred;
}