#version 430 core

layout (location = 0) in vec3 vertex_position;
layout (location = 1) in vec2 vertex_texcoord;
layout (location = 2) in vec3 vertex_normal;
layout (location = 3) in vec3 vertex_bone_indices;
layout (location = 4) in vec3 vertex_bone_weights;

out vec2 tc;
out vec3 varyingNormal;
out vec3 varyingVertPos;
out vec3 vVertPos;

uniform mat4 m_matrix;
uniform mat4 v_matrix;
uniform mat4 p_matrix;
uniform mat4 norm_matrix;

uniform mat4 skin_matrices[128];     // Skinning Matrices (supports up to 128 bones)
uniform mat3 skin_matrices_IT[128];  // IT of Skinning Matrices (used for transforming vertex normals)

void main()
{	// Calculating the model-space skinning transformation matrix for the vertex
	vec4 bone1_vert_pos;
	vec4 bone2_vert_pos;
	vec4 bone3_vert_pos;

	mat3 bone1_nor_mat3;
	mat3 bone2_nor_mat3;
	mat3 bone3_nor_mat3;

	int index;

	vec4 skinned_vert_pos = vec4(vertex_position,1.0);

	// Calculating bone 1's influence
	index = int(vertex_bone_indices.x);
	bone1_vert_pos = skin_matrices[index] * skinned_vert_pos;
	bone1_nor_mat3 = skin_matrices_IT[index];

	// Calculating bone 2's influence
	index = int(vertex_bone_indices.y);
	bone2_vert_pos = skin_matrices[index] * skinned_vert_pos;
	bone2_nor_mat3 = skin_matrices_IT[index];

	// Calculating bone 3's influence
	index = int(vertex_bone_indices.z);
	bone3_vert_pos = skin_matrices[index] * skinned_vert_pos;
	bone3_nor_mat3 = skin_matrices_IT[index];

	// Averaging all bone influences to get final vertex position
	skinned_vert_pos = bone1_vert_pos * vertex_bone_weights.x
			+ bone2_vert_pos * vertex_bone_weights.y
			+ bone3_vert_pos * vertex_bone_weights.z;

	// Calculate the skinned vertex normal
	// NOTE: We MUST normalize this result to get the correct skinned normal.
	// But don't normalize the result yet, if the vertex has no bones, normalizing will yield a normal
	// with inf / NaN values.
	// Normalize the normal matrix AFTER we handle vertices with no bones.

	vec3 skinned_vert_nor = (bone1_nor_mat3 * vertex_bone_weights.x
				+ bone2_nor_mat3 * vertex_bone_weights.y
				+ bone3_nor_mat3 * vertex_bone_weights.z) * vertex_normal;

	// If sum of weights is 0, return untransformed vertex data, else return transformed vertex data
	// This allows vertices that do not have weights for any bone.
	// Note: the file exporter ensures the total weight is either 1.0 or 0.0
	// (depending on whether or not the vertex is weighted to any bone at all)

	float total_weight = vertex_bone_weights.x + vertex_bone_weights.y + vertex_bone_weights.z;
	vec4 vert_pos = mix(vec4(vertex_position, 1.0), skinned_vert_pos, total_weight);
	vec3 vert_nor = mix(vertex_normal, skinned_vert_nor, total_weight);
	vert_nor = normalize(vert_nor);
	
	tc  = vertex_texcoord;
	varyingNormal   = (norm_matrix * vec4(vert_nor,1.0)).xyz;
	varyingVertPos  = (m_matrix * vert_pos).xyz;
	vVertPos = (v_matrix * m_matrix * vert_pos).xyz;
	gl_Position = p_matrix * v_matrix * m_matrix * vert_pos;
}
