package tage.shapes;

import org.joml.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * A Skeleton describes the hierarchy and structure of any number of bones.
 * These bones are to be used to modify the vertices of submesh for skeletal animation.
 * <p>
 * Within the submesh, each vertex has up to 3 bone parents and corresponding weights that determine
 * the influence that up to 3 bones have on said vertex. This is kept track through the vertex
 * having 3 bone indices that directly correlate to data stored in various buffers within the Skeleton class.
 * <p>
 * A vertex can be influenced by no bones, or up to 3 bones.
 * <p>
 * The game application should not need to interact with this class at all.
 * Instead, it should use the loadAnimation() and playAnimation() tools in AnimationShape.
 * @author Luis Gutierrez.
 * Adapted for TAGE by Scott Gordon.
 *
 */
public final class AnimatedSkeleton
{
	protected AnimatedSkeleton() { }

	private int boneCount;
	private String[] boneNames;

	// The bone's length (distance between bone head and bone tail)
	private FloatBuffer boneLengthsBuffer;

	// The rest rotation of each bone(a list of quaternions)
	private FloatBuffer boneRestRotationsBuffer;

	// The rest position of each bone ( a list of 3D vectors, relative parent bone tail)
	private FloatBuffer boneRestLocationsBuffer;

	// The index of a bone's parent bone
	private IntBuffer boneParentsBuffer;

	protected void setBoneCount(int boneCount) { this.boneCount = boneCount;	}
	protected int getBoneCount() { return boneCount; }
	protected void setBoneNames(String[] bn) { boneNames = bn; }
	protected String[] getBoneNames() { return boneNames; }

	protected void setBoneLengthsBuffer(FloatBuffer bl) { boneLengthsBuffer = bl; }
	protected void setBoneRestRotationsBuffer(FloatBuffer brr) { boneRestRotationsBuffer = brr; }
	protected void setBoneRestLocationsBuffer(FloatBuffer brl) { boneRestLocationsBuffer = brl; }
	protected void setBoneParentsBuffer(IntBuffer bp) { boneParentsBuffer = bp; }

	protected FloatBuffer getBoneLengthsBuffer()
	{	if (boneLengthsBuffer != null)
			return boneLengthsBuffer.duplicate();
		return null;
	}

	protected FloatBuffer getBoneRestRotationsBuffer()
	{	if (boneRestRotationsBuffer != null)
			return boneRestRotationsBuffer.duplicate();
		return null;
	}

	protected FloatBuffer getBoneRestLocationsBuffer()
	{	if (boneRestLocationsBuffer != null)
			return boneRestLocationsBuffer.duplicate();
		return null;
	}

	protected IntBuffer getBoneParentsBuffer()
	{	if (boneParentsBuffer != null)
			return boneParentsBuffer.duplicate();
		return null;
	}

	protected tage.rml.Quaternion getBoneRestRot(int boneIndex)
	{	if (boneIndex < 0 || boneIndex > boneCount)
			throw new IndexOutOfBoundsException("Attempted to get rest rot of out of bounds bone " + boneIndex + ".");
		float w = boneRestRotationsBuffer.get(4*boneIndex);
		float x = boneRestRotationsBuffer.get(4*boneIndex + 1);
		float y = boneRestRotationsBuffer.get(4*boneIndex + 2);
		float z = boneRestRotationsBuffer.get(4*boneIndex + 3);
		return tage.rml.Quaternionf.createFrom(w,x,y,z);
	}

	// Returns the rest location vector3 of the bone with the index boneIndex in this skeleton's data.

	protected tage.rml.Vector3 getBoneRestLoc(int boneIndex)
	{	if (boneIndex < 0 || boneIndex > boneCount)
			throw new IndexOutOfBoundsException("Attempted to get rest loc of out of bounds bone " + boneIndex + ".");
		float x = boneRestLocationsBuffer.get(3*boneIndex);
		float y = boneRestLocationsBuffer.get(3*boneIndex + 1);
		float z = boneRestLocationsBuffer.get(3*boneIndex + 2);
		return tage.rml.Vector3f.createFrom(x,y,z);
	}

	protected float getBoneLength(int boneIndex)
	{	if (boneIndex < 0 || boneIndex > boneCount)
			throw new IndexOutOfBoundsException("Attempted to get bone length of out of bounds bone " + boneIndex + ".");
		return boneLengthsBuffer.get(boneIndex);
	}

	protected int getBoneParentIndex(int boneIndex)
	{	if (boneIndex < 0 || boneIndex > boneCount)
			throw new IndexOutOfBoundsException("Attempted to get bone length of out of bounds bone " + boneIndex + ".");
		return boneParentsBuffer.get(boneIndex);
	}
}
