#version 150

uniform vec2 InputResolution;
uniform float time;
uniform vec4 color;

in vec2 texCoord;

out vec4 fragColor;

float rand(vec2 co) {return fract(sin(dot(co.xy,vec2(12.9898, 78.233))) * 43758.5453);}

float noise(vec2 p){
    vec2 i = floor(p);
    vec2 f = fract(p);
    vec2 u = f * f * (3.0 - 2.0 * f);
    return mix(mix(rand(i + vec2(0.0, 0.0)), rand(i + vec2(1.0, 0.0)), u.x),
               mix(rand(i + vec2(0.0, 1.0)), rand(i + vec2(1.0, 1.0)), u.x), u.y);
}

float fbm(vec2 p) {
    float value = 0.0;
    float scale = 1.5;
    float amplitude = 0.5;
    float persistence = 0.5;

    for (int i = 0; i < 5; i++) {
        value += noise(p * scale) * amplitude;
        scale *= 2.0;
        amplitude *= persistence;
    }

    return value;
}

float turbulence(vec2 p) {
    float value = 0.0;
    float scale = 1.0;

    for (int i = 0; i < 5; i++) {
        value += abs(noise(p * scale) * 2.0 - 1.0) / scale;
        scale *= 2.0;
    }

    return value;
}

void main() {
    vec2 uv = gl_FragCoord.xy / InputResolution.xy;
    vec2 p = uv * 2.0 - 1.0;
    p.x *= InputResolution.x / InputResolution.y;
    float time = time * 0.2;

    float smoke = fbm(p * 3.0 + turbulence(p * 3.0 + time));
    smoke = smoothstep(0.2, 0.8, smoke);

    fragColor = vec4(color.rgb * smoke, color.a);
}