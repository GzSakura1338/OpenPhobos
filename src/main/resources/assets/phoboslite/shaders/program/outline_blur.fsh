#version 150

uniform sampler2D DiffuseSampler;
uniform int quality;

in vec2 texCoord;
in vec2 oneTexel;

out vec4 fragColor;

void main() {
    vec2 pixOffset = oneTexel;

    const float weights[5] = float[](1.0, 0.8413, 0.6065, 0.1645, 0.0093);

    vec4 blurred = vec4(0.0);
    float totalWeight = 0.0;

    for (int y = -quality; y <= quality; y++) {
        float yWeight = weights[abs(y)];
        for (int x = -quality; x <= quality; x++) {
            float xWeight = weights[abs(x)];
            float weight = xWeight * yWeight;
            vec2 offset = vec2(float(x) * pixOffset.x, float(y) * pixOffset.y);
            blurred += weight * texture2D(DiffuseSampler, texCoord + offset);
            totalWeight += weight;
        }
    }

    fragColor = blurred / totalWeight;
}