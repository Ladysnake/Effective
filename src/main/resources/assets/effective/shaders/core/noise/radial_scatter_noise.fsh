#version 150

uniform sampler2D Sampler0;
uniform float GameTime;
uniform float Speed;
uniform float XFrequency;
uniform float YFrequency;
uniform float Intensity;
uniform float ScatterPower;
uniform float ScatterFrequency;
uniform float DistanceFalloff;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

mat2 rot(float a) {
    return mat2(cos(a), sin(a), -sin(a), cos(a));
}
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
float scatter(vec2 uv, float o) {
    float ts = sin(length(.5*uv)+o);
    float n = noise(uv*ScatterPower);
    return smoothstep(ts+.5, ts-.5, n);
}
void main() {
    float gameTime = GameTime * Speed;
    vec2 uv = texCoord0;
    float toCenter = distance(vec2(0.5), uv);
    float time = Intensity*toCenter;
    uv = uv*rot(time+gameTime+scatter(uv, ScatterFrequency));
    uv = uv*rot(sin(time*XFrequency));
    uv = uv*rot(cos(time*XFrequency));
    fragColor = vertexColor * texture(Sampler0, uv) * ColorModulator - toCenter*DistanceFalloff;
}
