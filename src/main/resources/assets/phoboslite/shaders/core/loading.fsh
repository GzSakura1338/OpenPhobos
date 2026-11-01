#version 150

const float PI = 3.1415926535897932384626433832795;
const float GLOBAL_RADIUS = .5;

uniform vec2 InputResolution;
uniform float time;
uniform vec4 color;

in vec2 texCoord;

out vec4 fragColor;

vec3 palette(float t) {
    vec3 a = vec3(0.388, 0.478, 0.500);
    vec3 b = vec3(-0.122, -0.164, 0.355);
    vec3 c = vec3(0.340, 0.340, 0.340);
    vec3 d = vec3(0.000, 0.333, 0.667);

    return a + b * cos(6.28318 * (c * t + d));
}

void main() {
    float CIRCLE_COUNT = (sin(time)) * 100.;
    vec2 uv = (gl_FragCoord.xy * 2.0 - InputResolution.xy) / InputResolution.y;

    vec3 baseColor = color.rgb;

    vec3 col = vec3(.0);

    float unitAngle = (2. * PI) / CIRCLE_COUNT;
    for (float i = 0.; i < abs(CIRCLE_COUNT); i++) {
        float angle;
        if (CIRCLE_COUNT > 0.) {
            angle = unitAngle * i;
        } else {
            angle = unitAngle * i;
        }

        vec2 center = vec2(cos(angle), sin(angle)) * GLOBAL_RADIUS;
        float a = sin(.01 / (length(uv - center) - ((sin(time + (1. * i))) / 10.))) / 2.;
        col += baseColor * a;
    }


    fragColor = vec4(col, color.a);
}