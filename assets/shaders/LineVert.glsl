#version 430

layout (location = 0) in vec3 vertPos;

uniform mat4 m_matrix;
uniform mat4 v_matrix;
uniform mat4 p_matrix;
uniform vec3 lineColor;

void main(void)
{	gl_Position = p_matrix * v_matrix * m_matrix * vec4(vertPos,1.0);
}
