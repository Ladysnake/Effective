#version 150

uniform sampler2D Sampler0;

uniform vec4 ColorModulator;
uniform vec2 ScreenSize;
uniform float GameTime;
uniform float Intensity;
uniform float Size;
uniform float Speed;
uniform float Brightness;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;


float rand(vec2 n) {
    return fract(sin(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);
}

float noise(vec2 p){
    vec2 ip = floor(p);
    vec2 u = fract(p);
    u = u*u*(3.0-2.0*u);

    float res = mix(
        mix(rand(ip),rand(ip+vec2(1.0,0.0)),u.x),
        mix(rand(ip+vec2(0.0,1.0)),rand(ip+vec2(1.0,1.0)),u.x),u.y);
    return res*res;
}

float layeredNoise(vec2 uv){
    float n = 0.;
    vec2 intenseUv = uv*Intensity;
    vec2 timedUv = uv+(GameTime*Speed);
    n += 0.5*noise(timedUv);
    n += 0.25*noise(intenseUv);
    n += 0.125*noise(timedUv);
    n += 0.0625*noise(intenseUv);
    n += 0.03215*noise(timedUv);
    return n;
}
float pattern(vec2 uv){
    return layeredNoise(uv+layeredNoise(uv+layeredNoise(uv)));
}
void main() {
    vec2 uv = texCoord0;
    float n = pattern(Size*uv);
    vec4 color = texture(Sampler0, uv) * vec4(sin(n) * n*vertexColor);
    fragColor = color * Brightness * ColorModulator;
}
