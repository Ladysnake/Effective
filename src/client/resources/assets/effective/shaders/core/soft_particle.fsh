#version 150

#moj_import <fog.glsl>

uniform sampler2D Sampler0;
uniform sampler2D Sampler2;
uniform sampler2D DiffuseDepthSampler;

uniform mat4 ProjMat;
uniform vec4 ColorModulator;
uniform float FogStart;
uniform float FogEnd;
uniform vec4 FogColor;
uniform vec2 ScreenSize;

in float vertexDistance;
in vec2 texCoord0;
in vec4 vertexColor;

out vec4 fragColor;

const vec3 normal = vec3(0.0, 0.0, 1.0);

float linearizeDepth(float depthSample) {
    // Same calculation mojang does, to linearize depths using the projection matrix values
    return -ProjMat[3].z /  (depthSample * -2.0 + 1.0 - ProjMat[2].z);
}

void main() {
    vec4 color = texture(Sampler0, texCoord0) * vertexColor * ColorModulator;
    if (color.a < 0.001) {
        discard;
    }

    // Depth only occupies the red channel, we don't care about the other two
    float depthSample = texture(DiffuseDepthSampler, gl_FragCoord.xy / ScreenSize).r;

    float depth = linearizeDepth(depthSample);
    float particleDepth = linearizeDepth(gl_FragCoord.z);

    // Linearly blends from 1x to 0x opacity at 1+ meter depth difference to 0 depth difference
    float opacity = color.a * min(depth - particleDepth, 1.0);

    fragColor = linear_fog(vec4(color.rgb, opacity), vertexDistance, FogStart, FogEnd, FogColor);
}
