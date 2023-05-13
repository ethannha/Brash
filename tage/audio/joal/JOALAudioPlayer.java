package tage.audio.joal;

import com.jogamp.openal.*;

import tage.audio.*;
import org.joml.*;

/**
 * This class extends the {@link AudioPlayer} abstract class and uses the JOAL Java interface to OpenAL 
 * (http://jogamp.org) to provide a JOAL-based audio player.  This class is responsible for playing an
 * individual piece of audio.
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordonl
 * 
 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
 */
public class JOALAudioPlayer extends AudioPlayer
{
	private int sourceID;			// The ID of the audio source
	private AL al;				// Local reference to the JOAL AL object
	private JOALAudioManager joalMgr;	// Local reference to the Audio Manager
	
	public JOALAudioPlayer(AudioResource resource, SoundType type, int sourceID, JOALAudioManager mgr) 
	{
		super(resource, type);
		this.sourceID = sourceID;
		this.joalMgr = mgr;
		this.al = mgr.getAL();
	}

	/**
	 * Returns the ID of the audio source.
	 */
	public int getSourceID() 
	{
		return sourceID;
	}
	
	/**
	 * Releases the audio source used by this {@link JOALAudioPlayer} back to memory.
	 */
	public void release()
	{
		int[] vSourceID = new int[1];
		vSourceID[0] = sourceID;
		
		al.alDeleteSources(1, vSourceID, 0);
	}

	/**
	 * Plays this {@link JOALAudioPlayer}'s sound.
	 */
	public boolean play(int volume, boolean looping) 
	{
		if(al == null)
			return false;
		
		if(isPaused)
			return false;
		
		stop();
		
		setVolume(volume);
		setIsLooping(looping);
		
		al.alSourcePlay(sourceID);
		
		return true;
	}

	/**
	 * Stops this {@link JOALAudioPlayer}'s sound from playing.
	 */
	public boolean stop() 
	{
		if(!joalMgr.getIsActive())
			return false;
		
		if(al == null)
			return false;
		
		//isPaused = true;
		
		al.alSourceStop(sourceID);
		
		return true;
	}
	
	public boolean pause()
	{
		if(isPaused)
			return true;
		
		if(al == null)
			return false;
		
		isPaused = true;
		
		al.alSourcePause(sourceID);
		
		return true;
	}

	/**
	 * Resumes playing this {@link JOALAudioPlayer}'s sound.
	 */
	public boolean resume() 
	{
		isPaused = false;
		
		return play(getVolume(), getIsLooping());
	}

	/**
	 * Toggles the pause state of this {@link JOALAudioPlayer}'s sound. 
	 */
	public boolean togglePause() 
	{
		if(!joalMgr.getIsActive())
			return false;
		
		if(isPaused)
		{
			resume();
		}
		else
		{
			pause();
		}
		
		return true;
	}

	/**
	 * Returns true if this {@link JOALAudioPlayer}'s sound is currently playing; false otherwise. 
	 */
	public boolean getIsPlaying() 
	{
		if(!joalMgr.getIsActive())
			return false;
		
		int[] state = new int[1];
		
		al.alGetSourcei(sourceID, AL.AL_SOURCE_STATE, state, 0);
		
		return (state[0] == AL.AL_PLAYING);
	}
	
	/**
	 * Set the <code>AL_LOOPING</code> property of this {@link JOALAudioPlayer}.
	 * 
	 * @param looping - True if the audio player should loop the sound; false if it should not loop the sound.
	 */
	public void setIsLooping(boolean looping)
	{
		isLooping = looping;
		
		al.alSourcei(sourceID, AL.AL_LOOPING, JOALAudioManager.getALBoolean(looping));
	}

	/**
	 * Sets the <code>AL_GAIN</code> of this {@link JOALAudioPlayer}'s sound. 
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
	 * @param volume - The volume value between 0 and 100 inclusive.
	 */
	public void setVolume(int volume) 
	{
		if(!joalMgr.getIsActive())
			return;
		
		if(volume < 0)
			this.volume = 0;
		else if(volume > 100)
			this.volume = 100;
		else
			this.volume = volume;
		
		float fVolume = this.volume / 100.0f;
		
		al.alSourcef(sourceID, AL.AL_GAIN, fVolume);
	}

	/**
	 * Returns the playback progress of this {@link JOALAudioPlayer}'s sound in seconds. 
	 */
	public float getProgress() 
	{
		float[] progress = new float[1];
		progress[0] = 0.0f;
		
		al.alGetSourcef(sourceID, AL.AL_SEC_OFFSET, progress, 0);
		
		return progress[0];
	}

	/**
	 * Sets the <code>AL_POSITION</code> of this {@link JOALAudioPlayer}.  The location is 
	 * where the {@link JOALAudioPlayer}'s sound will originate from.
	 * 
	 *  @param point - The point in 3D world space the audio player is located at. 
	 */
	public void setLocation(Vector3f point) 
	{
		al.alSourcefv(sourceID, AL.AL_POSITION, AudioManager.getPointValues(point), 0);
	}

	/**
	 * Sets the <code>AL_VELOCITY</code> of this {@link JOALAudioPlayer}.
	 * <p>
	 * AL_VELOCITY specifies the current velocity (speed and direction) of the object, in the 
	 * world coordinate system. Any 3-tuple of valid float/double values is allowed. The object 
	 * AL_VELOCITY does not affect the source's position. OpenAL does not calculate the velocity 
	 * from subsequent position updates, nor does it adjust the position over time based on the 
	 * specified velocity. Any such calculation is left to the application. For the purposes of 
	 * sound processing, position and velocity are independent parameters affecting different 
	 * aspects of the sounds. 
	 * 
	 * @param velocity - The velocity vector of the audio player.
	 */
	public void setVelocity(Vector3f velocity) 
	{
		al.alSourcefv(sourceID, AL.AL_VELOCITY, AudioManager.getVectorValues(velocity), 0);
	}
	
	/**
	 * Sets the emit direction of the {@link JOALAudioPlayer}. Visually you can think of this as
	 * setting a "cone" around the audio player which will direct the sound in a particular
	 * direction. 
	 * <p>
	 * This is accomplished by setting the AL_DIRECTION, AL_CONE_INNER_ANGLE, AND AL_CONE_OUTER_ANGLE
	 * source properties of the audio player. 
	 * <p>
	 * If AL_DIRECTION does not equal the zero vector, the source is directional. The sound emission 
	 * is presumed to be symmetric around the direction vector (cylinder symmetry). sources are not 
	 * oriented in full 3 degrees of freedom, only two angles are effectively needed.
	 * <p>
	 * The zero vector (0,0,0) is the default direction which indicated that the audio source emitted
	 * from the audio player is not direction. Specifying a non-zero vector will make the source directional. 
	 * Specifying a zero vector for a directional source will effectively mark it as non-direction.
	 * 
	 * @param direction - The direction in which the sound is emitting from the audio player.
	 * @param coneAngle - The angle (in degrees) of the "cone" around the audio player. Default value is 360 degrees
	 * 					  means the sound being emitted from the audio player is omni-directional. 
	 */
	public void setEmitDirection(Vector3f direction, float coneAngle)
	{
		float[] vDirection = AudioManager.getVectorValues(direction);
		
		al.alSourcefv(sourceID, AL.AL_DIRECTION, vDirection, 0);
		al.alSourcef(sourceID, AL.AL_CONE_INNER_ANGLE, coneAngle);
		al.alSourcef(sourceID, AL.AL_CONE_OUTER_ANGLE, coneAngle);
	}

	/**
	 * Returns the location of this {@link JOALAudioPlayer} as a {@link Vector3f}.  
	 */
	public Vector3f getLocation() 
	{
		float[] vLocation = new float[3];
		Vector3f location = null;
		
		al.alGetSourcefv(sourceID, AL.AL_POSITION, vLocation, 0);
		
		location = new Vector3f(vLocation[0], vLocation[1], vLocation[2]);
		
		return location;
	}

	/**
	 * Returns the velocity of this JOALAudioPlayer as a Vector3. 
	 */
	public Vector3f getVelocity() 
	{
		float[] vVelocity = new float[3];
		Vector3f velocity = null;
		
		al.alGetSourcefv(sourceID, AL.AL_VELOCITY, vVelocity, 0);
		
		velocity = new Vector3f(vVelocity[0], vVelocity[1], vVelocity[2]);
		
		return velocity;
	}

	/**
	 * Sets the <code>AL_ROLLOFF_FACTOR</code> of this {@link JOALAudioPlayer}.
	 * <p>
	 * This is used for distance attenuation calculations based on inverse distance with rolloff.
	 * For distances smaller than AL_MAX_DISTANCE (and, depending on the distance model, larger 
	 * than AL_REFERENCE_DISTANCE), this will scale the distance attenuation over the applicable 
	 * range. See section on distance models for details how the attenuation is computed as a function 
	 * of the distance.
	 * <p>
	 * In particular, AL_ROLLOFF_FACTOR can be set to zero for those sources that are supposed to be 
	 * exempt from distance attenuation. The implementation is encouraged to optimize this case, bypassing 
	 * distance attenuation calculation entirely on a per-source basis.
	 * 
	 * @param rollOff Values G.T. 1 make the sound get quieter much quicker the further the ear gets from the source.  For a value = 0 distance attenuation is disabled. Range: [0, any]
	 * 
	 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
	 */
	public void setRollOff(float rollOff) 
	{
		// Values > 1 make the sound get quieter much quicker the further we go. 0 < Values < 1 'even out' the curve and make it not so steep.
		al.alSourcef(sourceID, AL.AL_ROLLOFF_FACTOR, rollOff);
	}

	/**
	 * Sets the <code>AL_REFERENCE_DISTANCE</code> of this {@link JOALAudioPlayer}.
	 * <p>
	 * <code>AL_REFERENCE_DISTANCE</code> is used for distance attenuation calculations 
	 * based on inverse distance with rolloff. Depending on the distance model it will 
	 * also act as a distance threshold below which gain is clamped.
	 * <p>
	 * Sets the minimum distance at which distance attenuation begins.
	 * 
	 * @param distance - The sound can't get any louder if we move closer to the emit point than this value; Range: [0, any]
	 * 
	 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
	 */
	public void setMinDistance(float distance) 
	{
		if(distance < 0)
			al.alSourcef(sourceID, AL.AL_REFERENCE_DISTANCE, 0);
		else
			al.alSourcef(sourceID, AL.AL_REFERENCE_DISTANCE, distance);
	}

	/**
	 * Sets the <code>AL_MAX_DISTANCE</code> of this {@link JOALAudioPlayer}.
	 * <p>
	 * <code>AL_MAX_DISTANCE</code> is used for distance attenuation calculations 
	 * based on inverse distance with rolloff. Distances greater than AL_MAX_DISTANCE 
	 * will be clamped to AL_MAX_DISTANCE.
	 * <p>
	 * Sets the max distance at which the distance attenuation stops.
	 *  
	 * @param distance - The sound can't get any quieter if we move further away from the emit point than this value; Range: [0, any]
	 * 
	 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
	 */
	public void setMaxDistance(float distance) 
	{
		if(distance < 0)
			al.alSourcef(sourceID, AL.AL_MAX_DISTANCE, 0);
		else
			al.alSourcef(sourceID, AL.AL_MAX_DISTANCE, distance);
	}

	/**
	 * Sets the <code>AL_PITCH</code> of this {@link JOALAudioPlayer}.
	 * <p>
	 * Desired pitch shift, where 1.0 equals identity. Each reduction by 50 percent equals a pitch 
	 * shift of -12 semitones (one octave reduction). Each doubling equals a pitch shift of 12 
	 * semitones (one octave increase). Zero is not a legal value.
	 * 
	 * @param pitch Value must be greater than 0. 
	 */
	public void setPitch(float pitch) 
	{
		if(pitch <= 0)
			al.alSourcef(sourceID, AL.AL_PITCH, 0.01f);
		else
			al.alSourcef(sourceID, AL.AL_PITCH, pitch);
	}

}
