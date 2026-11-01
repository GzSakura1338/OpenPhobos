#version 150

uniform vec2 InputResolution;

uniform vec2 coord;
uniform float radius;
uniform vec4 color1;
in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 uv = coord / InputResolution.xy;
    vec2 pos = 0.5 - uv;
    pos.y /= InputResolution.y / InputResolution.x;
    float dist  = 1.0 / length(pos);
    dist *= radius;
    dist = pow(dist, 0.8);
    vec3 col = dist * vec3(color1.r, color1.g, color1.b);
    col = 1.0 - exp(-col);
    fragColor = vec4(col, color1.a);
}