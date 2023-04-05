package tage.audio.joal;

import com.jogamp.openal.AL;

import org.joml.*;
import tage.audio.*;

/**
 * This class extends the {@link AudioEar} abstract class and uses the JOAL Java interface to OpenAL 
 * (http://jogamp.org) to provide a JOAL-based audio ear.  This class is responsible for managing
 * the audio ear (listener) of the OpenAL based 3D sound system.
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordon.
 * 
 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
 */
public class JOALAudioEar extends AudioEar 
{
	private AL al;
	
	public JOALAudioEar(AL al) 
	{
		this.al = al;
		setVolume(100);
	}

	/**
	 * Sets the AL_GAIN Listener property.
	 * <p>
	 * AL_GAIN defines a scalar amplitude multiplier. As a source attribute, it applies to that particular 
	 * source only. As a listener attribute, it effectively applies to all sources in the current context. 
	 * The default 1.0 means that the sound is unattenuated. An AL_GAIN value of 0.5 is equivalent to an 
	 * attenuation of 6 dB. The value zero equals silence (no contribution to the output mix). Driver 
	 * implementations are free to optimize this case and skip mixing and processing stages where applicable. 
	 * The implementation is in charge of ensuring artifact-free (click-free) changes of gain values and is 
	 * free to defer actual modification of the sound samples, within the limits of acceptable latencies.
	 * <p>
	 * AL_GAIN larger than one (i.e. amplification) is permitted for source and listener. However, the 
	 * implementation is free to clamp the total gain (effective gain per-source multiplied by the listener gain) 
	 * to one to prevent overflow.
	 * <p>
	 * <code>setVolume()</code> is passed a value between 0 to 100 and converts it to the range 0.0 to 1.0. 
	 * 
	 * @param volume The volume value between 0 and 100 that will be normalized to 0 and 1. 
	 */
	public void setVolume(int volume) 
	{
		if(volume < 0)
			this.volume = 0;
		else if(volume > 100)
			this.volume = 100;
		else
			this.volume = volume;
		
		float fVolume = this.volume / 100.0f;
		
		al.alListenerf(AL.AL_GAIN, fVolume);
	}

	/**
	 * Sets the AL_POSITION Listener property. 
	 */
	public void setLocation(Vector3f point) 
	{
		al.alListenerfv(AL.AL_POSITION, AudioManager.getPointValues(point), 0);
	}

	/**
	 * Sets the AL_VELOCITY Listener property. 
	 * <p>
	 * AL_VELOCITY specifies the current velocity (speed and direction) of the object, in the 
	 * world coordinate system. Any 3-tuple of valid float/double values is allowed. The object 
	 * AL_VELOCITY does not affect the source's position. OpenAL does not calculate the velocity 
	 * from subsequent position updates, nor does it adjust the position over time based on the 
	 * specified velocity. Any such calculation is left to the application. For the purposes of 
	 * sound processing, position and velocity are independent parameters affecting different 
	 * aspects of the sounds.
	 * 
	 * @param velocity - The velocity vector.
	 */
	public void setVelocity(Vector3f velocity) 
	{
		al.alListenerfv(AL.AL_VELOCITY, AudioManager.getVectorValues(velocity), 0);
	}

	/**
	 * Sets the AL_ORIENTATION Listener property. 
	 * <p>
	 * Think of "FORWARD" as a string attached to your nose, and think of "UP" as a string attached to the top of your head.
	 * Without the string attached to the top of your head, you could tilt your head clockwise/counterclockwise and still be 
	 * facing "FORWARD". But since you can tilt your head, there's no way for the computer to be sure whether something to the canonical 
	 * "right" should sound in your right ear (the top of your head faces "upwards") or your left ear (the top of your head faces 
	 * "downwards" because you're upside down). The "FORWARD" and "UP" vectors pin the listener's "head" such that there's no ambiguity 
	 * for which way it's facing, and which way it's oriented.
	 * <p>
	 * There are actually 3 vectors you need to set: Position, "FORWARD", and "UP". Position 0,0,0 means the head is at the center of the 
	 * universe. "FORWARD" 0,0,-1 means the head is looking into the screen, and "UP" is usually 0,1,0, such that the top of the "head" is 
	 * pointing up. With this setup, anything the user sees on the left side of the screen will sound in his left ear. The only time 
	 * you'd choose something different is in a first-person style game where the player moves around in a virtual 3d world. The vectors 
	 * don't have to be normalized, actually, so you could use 0,42,0 for "UP" and it would do the same thing as 0,1,0.
	 * <p>
	 * If you do change "FORWARD" and "UP" from their canonical values, the vectors MUST be perpendicular.
	 */
	public void setOrientation(Vector3f forward, Vector3f up) 
	{
		float[] vTarget = AudioManager.getVectorValues(forward);
		float[] vUp = AudioManager.getVectorValues(up);
		float[] orientation = new float[6];
		orientation[0] = vTarget[0];
		orientation[1] = vTarget[1];
		orientation[2] = vTarget[2];
		orientation[3] = vUp[0];
		orientation[4] = vUp[1];
		orientation[5] = vUp[2];
		
		al.alListenerfv(AL.AL_ORIENTATION, orientation, 0);
	}

	/**
	 * Returns the Listener AL_POSITION value as a Point3D.
	 */
	public Vector3f getLocation() 
	{
		float[] vLocation = new float[3];
		al.alGetListenerfv(AL.AL_POSITION, vLocation, 0);
		Vector3f location = new Vector3f(vLocation[0], vLocation[1], vLocation[2]);
		return location;
	}

	/**
	 * Returns the Listener AL_VELOCITY value as a Vector3D.
	 */
	public Vector3f getVelocity() 
	{
		float[] vVelocity = new float[3];
		Vector3f velocity = null;
		
		al.alGetListenerfv(AL.AL_VELOCITY, vVelocity, 0);
		
		velocity = new Vector3f(vVelocity[0], vVelocity[1], vVelocity[2]);
		
		return velocity;
	}
}
