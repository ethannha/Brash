#version 430
out vec4 color;
uniform vec3 hudc;
void main(void)
{ color = vec4(hudc.x, hudc.y, hudc.z, 1); }