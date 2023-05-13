#version 430
layout (local_size_x=1) in;
layout(binding=0) buffer outputBuffer { float outVal[]; };

uniform float x;
uniform float z;
layout (binding=0) uniform sampler2D samp;

void main()
{	outVal[0] = texture(samp, vec2(x,z)).r;
}