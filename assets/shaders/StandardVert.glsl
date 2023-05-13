#version 430

layout (location = 0) in vec3 vertPos;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 vertNormal;

out vec2 tc;
out vec3 varyingNormal;
out vec3 varyingVertPos;
out vec3 vVertPos;

struct Light
{	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	vec3 position;
	float constantAttenuation;
	float linearAttenuation;
	float quadraticAttenuation;
	float range;
	vec3 direction;
	float cutoffAngle;
	float offAxisExponent;
	float type;
};
struct Material
{	vec4 ambient;
	vec4 diffuse;
	vec4 specular;
	float shininess;
};

Light light;

uniform vec4 globalAmbient;
uniform Material material;
uniform mat4 m_matrix;
uniform mat4 v_matrix;
uniform mat4 p_matrix;
uniform mat4 norm_matrix;
uniform int envMapped;
uniform int has_texture;
uniform int heightMapped;
uniform int hasLighting;
uniform int solidColor;
uniform vec3 color;
uniform int num_lights;
uniform int fields_per_light;

layout (std430, binding=0) buffer lightBuffer { float lightArray[]; };
layout (binding = 0) uniform sampler2D samp;
layout (binding = 1) uniform samplerCube t;
layout (binding = 2) uniform sampler2D height;

void main(void)
{	vVertPos = (v_matrix * m_matrix * vec4(vertPos,1.0)).xyz;
	varyingVertPos = (m_matrix * vec4(vertPos,1.0)).xyz;
	varyingNormal = (norm_matrix * vec4(vertNormal,1.0)).xyz;

	// Most of the time this height offset is 0.
	// If this is a terrain plane, and has a height map, then this will do the height mapping.
	vec4 p = vec4(vertPos.x, vertPos.y + (texture(height,texCoord)).r, vertPos.z, 1.0);

	tc = texCoord;
	gl_Position = p_matrix * v_matrix * m_matrix * p;
}
