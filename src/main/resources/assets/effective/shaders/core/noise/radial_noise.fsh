#version 150

uniform sampler2D Sampler0;
uniform float GameTime;
uniform float Speed;
uniform float XFrequency;
uniform float YFrequency;
uniform float Intensity;
uniform float DistanceFalloff;

uniform vec4 ColorModulator;

in vec4 vertexColor;
in vec2 texCoord0;
in vec2 texCoord2;

out vec4 fragColor;

mat2 rot(float a) {
    return mat2(cos(a), sin(a), -sin(a), cos(a));
}

void main() {
    float gameTime = GameTime * Speed;
    vec2 uv = texCoord0;
    float toCenter = distance(vec2(0.5), uv);
    float time = Intensity*toCenter;
    uv = uv*rot(time+gameTime);
    uv = uv*rot(sin(time*XFrequency));
    uv = uv*rot(cos(time*YFrequency));
    fragColor = vertexColor * texture(Sampler0, uv) * ColorModulator - toCenter*DistanceFalloff;
}
