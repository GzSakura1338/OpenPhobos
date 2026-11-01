#version 150

//#moj_import <fog.glsl>

#define MOD3 vec3(0.1031, 0.11369, 0.13787)

uniform mat4 ModelViewMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;

uniform vec2 resolution;
uniform float time;
uniform vec3 lead;
uniform vec3 trail;
uniform int mode;

in float vertexDistance;

out vec4 fragColor;

float hash13(vec3 p3) {
    p3  = fract(p3 * 443.8975);
    p3 += dot(p3, p3.yzx + 19.19);
    return fract((p3.x + p3.y) * p3.z);
}

vec4 linear_fog(vec4 inColor, float vertexDistance, float fogStart, float fogEnd, vec4 fogColor) {
    if (vertexDistance <= fogStart) {
        return inColor;
    }

    float fogValue = vertexDistance < fogEnd ? smoothstep(fogStart, fogEnd, vertexDistance) : 1.0;
    return vec4(mix(inColor.rgb, fogColor.rgb, fogValue * fogColor.a), inColor.a);
}

vec3 hash33(vec3 p3) {
    p3 = fract(p3 * MOD3);
    p3 += dot(p3, p3.yxz + 19.19);
    return -1.0 + 2.0 * fract(vec3((p3.x + p3.y) * p3.z, (p3.x + p3.z) * p3.y, (p3.y + p3.z) * p3.x));
}

float noise(vec3 p) {
    vec3 pi = floor(p);
    vec3 pf = p - pi;

    vec3 w = pf * pf * (3.0 - 2.0 * pf);

    return 	mix(
        mix(
            mix(dot(pf - vec3(0, 0, 0), hash33(pi + vec3(0, 0, 0))),
                dot(pf - vec3(1, 0, 0), hash33(pi + vec3(1, 0, 0))),
                w.x),
            mix(dot(pf - vec3(0, 0, 1), hash33(pi + vec3(0, 0, 1))),
                dot(pf - vec3(1, 0, 1), hash33(pi + vec3(1, 0, 1))),
                w.x),
            w.z),
        mix(
            mix(dot(pf - vec3(0, 1, 0), hash33(pi + vec3(0, 1, 0))),
                dot(pf - vec3(1, 1, 0), hash33(pi + vec3(1, 1, 0))),
                w.x),
            mix(dot(pf - vec3(0, 1, 1), hash33(pi + vec3(0, 1, 1))),
                dot(pf - vec3(1, 1, 1), hash33(pi + vec3(1, 1, 1))),
                w.x),
            w.z),
        w.y);
}

float fbm0(vec2 p) {
    float d = 0.0;
    float m = 4.0;
    d += 0.5 * noise(p.xyy); p = m * p;
    d += 0.25 * noise(p.xyy); p = m * p;
    d += 0.125 * noise(p.xyy); p = m * p;
    d += 0.0625 * noise(p.xyy); p = m * p;
    return d;
}

float fbm1(vec2 p) {
    float d = 0.0;
    float m = 1.5;
    d -= 0.5 * noise(p.xyy); p = m * p;
    d -= 0.25 * noise(p.xyy); p = m * p;
    d -= 0.125 * noise(p.xyy); p = m * p;
    d -= 0.0625 * noise(p.xyy); p = m * p;
    return d;
}

float FBM(vec2 p) {
    float d = 0.0;
    p.xy -= 0.5;
    vec2 op = p;
    p *= 4.0;
    if (p.x < 10.0) {
        float w = 1.0;
        d += w * noise(p.xyy);

        p *= 4.0;
        w /= 8.0;
        const mat2 m = mat2(1.6, 1.2, -1.2, 1.6);
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
        d -= w * noise(p.xyy); p = m * p; w *= 0.5;
    } else {
        if (p.x < 0.0) {
            vec2 ff = p + vec2(0.5, 0.0);
            d = noise(ff.xyy * 10.0);
        }
        else {
            d = fbm1(p * 2.0);
        }
        d = fbm1(p * 1.0);
    }

    d += 0.5;

    float x = clamp(length(op) / (0.5 * sqrt(2.0)), 0.0, 1.0);
    x = pow(x,1.0);
    d *= x;

    d -= 0.5;

    return max(d, -0.5);
}

void main() {
    vec4 position_color = linear_fog(ColorModulator, vertexDistance, FogStart, FogEnd, FogColor);

    vec2 uv = gl_FragCoord.xy / resolution.xy;

    //vec3 heightmap = texture(iChannel2, uv).rrr;
    vec3 heightmap  = vec3(FBM(uv) + 0.5);
    vec3 background = vec3(0.0);
    vec3 foreground = position_color.rgb;

    float t = fract(-time * 0.4);
    //vec3 erosion = smoothstep(t-0.1*length(uv-0.5)*4., t, heightmap);
    vec3 erosion = smoothstep(t-0.2, t, heightmap);
    //erosion = erosion * erosion * erosion;

    //vec3 col = mix(foreground, background, erosion);


    vec3 border = smoothstep(0.0, 0.1, erosion) - smoothstep(0.1, 1.0, erosion);
    vec3 bordercolor = mix(lead, trail, smoothstep(0.9, 1.0, border));
    vec3 col = mix(foreground, bordercolor * border, step(0.01, border));
    col = mix(col, background, erosion);

    //col = heightmap;
    fragColor = vec4(col, min(position_color.a, 1.0 - erosion.x));

    if (mode == 1) {
        vec4 ncd = gl_FragCoord / vec4(resolution, 1.0, 1.0);
        vec4 clip = ncd * 2.0 - 1.0;
        mat4 inverted = inverse(ModelViewMat);
        vec4 pos = clip * inverted;
        pos *= 0.3;
        fragColor *= pos;
    }
}