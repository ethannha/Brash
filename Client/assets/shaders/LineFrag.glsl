#version 430

out vec4 fragColor;

uniform mat4 m_matrix;
uniform mat4 v_matrix;	 
uniform mat4 p_matrix;
uniform vec3 lineColor;

void main(void)
{	fragColor = vec4(lineColor, 1.0);
}
