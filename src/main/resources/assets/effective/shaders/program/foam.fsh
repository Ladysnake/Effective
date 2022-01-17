#version 150

vec2 center = vec2(0.5,0.5);
float speed = 0.035;

out vec4 fragColor;

uniform vec2 OutSize;

uniform sampler2D DiffuseSampler;
uniform sampler2D TransparencyDepthSampler;
uniform sampler2D EntityDepthSampler;
uniform ivec4 ViewPort;
uniform mat4 InverseTransformMatrix;

in vec2 texCoord;
in vec4 vPosition;


vec4 CalcEyeFromWindow(in float depth) {
    // derived from https://www.khronos.org/opengl/wiki/Compute_eye_space_from_window_space
    // ndc = Normalized Device Coordinates
    vec3 ndcPos;
    ndcPos.xy = ((2.0 * gl_FragCoord.xy) - (2.0 * ViewPort.xy)) / (ViewPort.zw) - 1;
    ndcPos.z = (2.0 * depth - gl_DepthRange.near - gl_DepthRange.far) / (gl_DepthRange.far - gl_DepthRange.near);
    vec4 clipPos = vec4(ndcPos, 1.);
    vec4 homogeneous = InverseTransformMatrix * clipPos;
    vec4 eyePos = vec4(homogeneous.xyz / homogeneous.w, homogeneous.w);
    return eyePos;
}

void main() {
//    vec4 transparencyDepth = texture(TransparencyDepthSampler, texCoord);
//    vec4 entityDepth = texture(EntityDepthSampler, texCoord);

//    if (distance(transparencyDepth, entityDepth) < 0.1 && distance(transparencyDepth, entityDepth) > 0.01) {
//        fragColor = vec4(1.0);
//    } else {
//        fragColor = vec4(texture(DiffuseSampler, texCoord).rgb, 1.0);
//    }

    vec3 ndc = vPosition.xyz / vPosition.w; //perspective divide/normalize
    vec2 viewportCoord = ndc.xy * 0.5 + 0.5; //ndc is -1 to 1 in GL. scale for 0 to 1
    float translucentDepth = texture(TransparencyDepthSampler, viewportCoord).x;
    float entityDepth = texture(EntityDepthSampler, viewportCoord).x;

    vec3 translucentPixelPosition = CalcEyeFromWindow(translucentDepth).xyz;
    vec3 entityPixelPosition = CalcEyeFromWindow(entityDepth).xyz;

    vec3 col = texture(DiffuseSampler, texCoord).rgb;
    if (texCoord.x < 0.33) {
        fragColor = vec4(vec3(length(translucentPixelPosition) / 30.0), 1.0);
    } else if (texCoord.x < 0.66) {
        fragColor = vec4(vec3(length(entityPixelPosition) / 30.0), 1.0);
    } else {
        fragColor = vec4(col, 1.0);
    }
}
