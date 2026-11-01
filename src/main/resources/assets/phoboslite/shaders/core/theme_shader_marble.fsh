#version 150

uniform vec2 InputResolution;
uniform float time;
uniform vec4 color;

in vec2 texCoord;

out vec4 fragColor;

void main() {
    vec2 uv =  (2.0 * gl_FragCoord.xy - InputResolution.xy) / min(InputResolution.x, InputResolution.y);

    for(float i = 1.0; i < 10.0; i++){
        uv.x += 0.6 / i * cos(i * 2.5* uv.y + time);
        uv.y += 0.6 / i * cos(i * 1.5 * uv.x + time);
    }

    vec3 baseColor = vec3(0.1) / abs(sin(time - uv.y - uv.x));
    fragColor = vec4(baseColor * color.rgb, color.a);
}