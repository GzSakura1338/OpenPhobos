#version 150

#define rot(x) mat2(cos(x), -sin(x), sin(x), cos(x))

uniform vec4 ColorModulator;
uniform float time;
uniform vec2 resolution;

in float vertexDistance;

out vec4 fragColor;

float height(vec2 p){
    return sin(p.x) + sin(p.x + p.y) + cos(p.y) / 1.5 + sin(time + p.x) + 5.0;
}

float map(vec3 p){
    return p.y - height(p.xz);
}

void main() {
    vec2 uv = (gl_FragCoord.xy * 2.0 - resolution.xy) / min(resolution.x, resolution.y);

    vec3 ray = normalize(vec3(uv,1.));
    ray.yz *= rot((sin(time)/3.+1.5));
    ray.xz *= rot((sin(time)/2.+1.)/5.);

    float t = 0.;
    for(int i = 0; i < 29 ; ++i)
    t += map(vec3(time, 0.0, time / 2.0) + ray * t) * 0.5;

    float fog = 1.0 / (1.0 + t * t * 0.005);
    vec3 fc = vec3(fog * fog, fog / 2.0, fog);
    fragColor = vec4(mix(fc, ColorModulator.rgb, 0.5), ColorModulator.a);
}