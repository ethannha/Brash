package tage.shapes;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import org.joml.*;

/**
 * An <i>Animation</i> describes a sequence of frames that can be applied to an AnimatedSkeleton
 * object. Each frame holds a list of transformations that are applied to each bone.
 * <p>
 * The number of bones in an animation must match the number of bones in the AnimatedSkeleton.
 * Loading specific Animations is typically handled by the client at runtime to allow manual selection of
 * Animations to be loaded per individual objects.
 * <p>
 * The game application should not need to interact with this class at all.
 * Instead, it should use the loadAnimation() and playAnimation() tools in AnimationShape.
 * @author Luis Gutierrez.
 * Adapted for TAGE by Scott Gordon.
 */

public final class Animation
{
	/** for engine use only */
	protected Animation() { };

	// Used for compatibility checking, to ensure an animation is compatible with a Skeleton.
	private int boneCount;

	// Stores the number of frames in the animation
	private int frameCount;

	// This framesList holds a list of FloatBuffers.
	// Each FloatBuffer holds the transformation data of all bones for that frame.
	// Each bone transform consists of 10 floats: locX, locY, locZ, rotW, rotX, rotY, rotZ, scaleX, scaleY, scaleZ

	private ArrayList<FloatBuffer> framesList = new ArrayList<>();

	protected void setBoneCount(int boneCount) { this.boneCount = boneCount; }
	protected int getBoneCount() { return boneCount; }
	protected void setFrameCount(int frameCount) { this.frameCount = frameCount; }
	protected int getFrameCount() { return frameCount; }
	protected FloatBuffer getFrame(int frameIndex) { return framesList.get(frameIndex); }

	protected void setFrame(int frameIndex, FloatBuffer frame)
	{	if (frame == null) throw new NullPointerException();
		if (frameIndex >= framesList.size() || frameIndex < 0) throw new IndexOutOfBoundsException();
		framesList.set(frameIndex, frame);
	}

	protected void appendFrame(FloatBuffer frame) { framesList.add(frame); }

	protected tage.rml.Vector3 getFrameBoneLoc(int frameIndex, int boneIndex)
	{	if(frameIndex >= framesList.size() || frameIndex < 0)
			throw new IndexOutOfBoundsException();
		FloatBuffer frame = framesList.get(frameIndex);
		if (boneIndex < 0 || (boneIndex * 10) + 9 >= frame.capacity())
			throw new IndexOutOfBoundsException();
		float locX = frame.get(boneIndex * 10);
		float locY = frame.get(boneIndex * 10 + 1);
		float locZ = frame.get(boneIndex * 10 + 2);
		return tage.rml.Vector3f.createFrom(locX, locY, locZ);
	}

	protected tage.rml.Quaternion getFrameBoneRot(int frameIndex, int boneIndex)
	{	if(frameIndex >= framesList.size() || frameIndex < 0)
			throw new IndexOutOfBoundsException();
		FloatBuffer frame = framesList.get(frameIndex);
		if (boneIndex < 0 || (boneIndex * 10) + 9 >= frame.capacity())
			throw new IndexOutOfBoundsException();
		float rotW = frame.get(boneIndex * 10 + 3);
		float rotX = frame.get(boneIndex * 10 + 4);
		float rotY = frame.get(boneIndex * 10 + 5);
		float rotZ = frame.get(boneIndex * 10 + 6);
		return tage.rml.Quaternionf.createFrom(rotW, rotX, rotY, rotZ);
	}

	protected tage.rml.Vector3 getFrameBoneScl(int frameIndex, int boneIndex)
	{	if (frameIndex >= framesList.size() || frameIndex < 0)
			throw new IndexOutOfBoundsException();
		FloatBuffer frame = framesList.get(frameIndex);
		if (boneIndex < 0 || (boneIndex * 10) + 9 >= frame.capacity())
			throw new IndexOutOfBoundsException();
		float scaleX = frame.get(boneIndex * 10 + 7);
		float scaleY = frame.get(boneIndex * 10 + 8);
		float scaleZ = frame.get(boneIndex * 10 + 9);
		return tage.rml.Vector3f.createFrom(scaleX, scaleY, scaleZ);
	}
}