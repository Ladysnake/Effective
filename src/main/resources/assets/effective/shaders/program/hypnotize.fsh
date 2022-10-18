#version 150

vec2 center = vec2(0.5, 0.5);
float speed = 0.035;

out vec4 fragColor;

uniform vec2 OutSize;
uniform float STime;
uniform float Intensity;
uniform float Rainbow;

in vec2 texCoord;
uniform sampler2D DiffuseSampler;

void main() {
    float xOffset = sin(texCoord.y * 128.0 + STime * 3.1415926535 * 2.0) * Intensity / 100.0;
    float yOffset = cos(texCoord.x * 72.0 + STime * 3.1415926535 * 2.0) * Intensity / 100.0;
    vec2 offset = vec2(xOffset, yOffset);

    float invAr = OutSize.y / OutSize.x;

    vec2 uv = gl_FragCoord.xy / OutSize.xy;

    vec3 col = mix(vec3(0.196, 0.631, 0.631), vec3(0.412, 0.886, 0.816), uv.x*abs(sin(STime))+uv.y*abs(cos(STime)));
    if (Rainbow >= 1.0) {
        col = vec4(uv, 0.5+0.5*sin(STime), 1.0).xyz;
    }

    vec3 texcol;

    float x = (center.x-uv.x-xOffset);
    float y = (center.y-uv.y-yOffset) *invAr;

    //float r = -sqrt(x*x + y*y); //uncoment this line to symmetric ripples
    float r = -(x*x + y*y);
    float z = 1.0 + 0.5*sin((r+STime*speed)/0.013);

    texcol.x = z;
    texcol.y = z;
    texcol.z = z;

    fragColor = vec4(mix(texture(DiffuseSampler, texCoord).rgb, col*texcol, (Intensity*length(col * texcol))), 1.0);
}