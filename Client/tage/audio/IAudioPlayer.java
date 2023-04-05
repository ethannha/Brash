package tage.audio;

import org.joml.*;

/**
 * The <code>IAudioPlayer</code> interface exposes the public methods that all audio players must implement.
 *  
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordon.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public interface IAudioPlayer 
{
	/**
	 * Returns the ID of the audio source.  
	 * <p>
	 * NOTE: This method is implementation dependent.
	 */
	public int getSourceID();
	
	/**
	 * Returns the {@link AudioResource} attached to the {@link IAudioPlayer}. 
	 */
	public AudioResource getResource();
	
	/**
	 * Releases the {@link IAudioPlayer} back to memory. 
	 * <p>
	 * NOTE: This method is implementation dependent.
	 */
	public void release();
	
	/**
	 * Plays the {@link IAudioPlayer}'s sound.
	 * 
	 * @param volume - The volume at which to play the sound
	 * @param looping - True if the sound should loop when finished; false to only play the sound once. 
	 * @return True if the sound has successfully started playing; false otherwise.
	 */
	public boolean play(int volume, boolean looping);
	
	/**
	 * Stops playing the {@link IAudioPlayer}'s sound. 
	 * 
	 * @return True if the sound was successfully stopped; false otherwise.
	 */
	public boolean stop();
	
	/**
	 * Pauses the {@link IAudioPlayer}'s sound.
	 * 
	 * @return True if the sound was successfully paused; false otherwise. 
	 */
	public boolean pause();
	
	/**
	 * Resume playing the {@link IAudioPlayer}'s sound. 
	 * 
	 * @return True if the sound was successfully resumed; false otherwise. 
	 */
	public boolean resume();
	
	/**
	 * Pauses/Unpauses the {@link IAudioPlayer}'s sound.
	 * 
	 * @return True if the sound was successfully toggled; false otherwise.
	 */
	public boolean togglePause();
	
	/**
	 * Returns true if the {@link IAudioPlayer}'s sound is paused; false otherwise. 
	 */
	public boolean getIsPaused();
	
	/**
	 * Returns true if the {@link IAudioPlayer}'s sound is currently playing; false otherwise. 
	 */
	public boolean getIsPlaying();
	
	/**
	 * Returns true if the {@link IAudioPlayer}'s sound is looping when it finishes; false otherwise.  
	 */
	public boolean getIsLooping();
	
	/**
	 * Set whether or not the {@link IAudioPlayer} is looping the sound.
	 */
	public void setIsLooping(boolean looping);
	
	/**
	 * Sets the pitch of the {@link IAudioPlayer}'s sound. 
	 * 
	 * @param pitch - The pitch value that is greater than 0. 
	 */
	public void setPitch(float pitch);
	
	/**
	 * Sets the volume of the {@link IAudioPlayer}.
	 * 
	 * @param volume - The volume value between 0 and 100 inclusive.
	 */
	public void setVolume(int volume);
	
	/**
	 * Returns the volume of the {@link IAudioPlayer}.
	 */
	public int getVolume();
	
	/**
	 * Returns the current playback progress of the {@link IAudioPlayer} in seconds. 
	 */
	public float getProgress();
	
	/**
	 * Returns the {@link SoundType} of the {@link IAudioPlayer}. 
	 */
	public SoundType getSoundType();
	
	
	// Methods for Audio Players placed in 3D space...
	
	/**
	 * Sets the location of the {@link IAudioPlayer} in 3D space. 
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param point - The point in 3D space the audio player is located at. 
	 */
	public void setLocation(Vector3f point);
	
	/**
	 * Sets the velocity of the {@link IAudioPlayer}.
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param velocity - The velocity vector of the audio player.
	 */
	public void setVelocity(Vector3f velocity);
	
	/**
	 * Sets the emit direction of the {@link IAudioPlayer}. Visually you can think of this as
	 * setting a "cone" around the audio player which will direct the sound in a particular
	 * direction. 
	 * <p>
	 * The zero vector (0,0,0) is the default direction which indicated that the audio source emitted
	 * from the audio player is not direction. Specifying a non-zero vector will make the source directional. 
	 * Specifying a zero vector for a directional source will effectively mark it as non-direction. 
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param direction - The direction in which the sound is emitting from the audio player.
	 * @param coneAngle - The angle (in degrees) of the "cone" around the audio player. Default value is 360 degrees
	 * 					  means the sound being emitted from the audio player is omni-directional. 
	 */
	public void setEmitDirection(Vector3f direction, float coneAngle);
	
	/**
	 * Sets the roll off factor of the {@link IAudioPlayer}. This is used for distance 
	 * attenuation calculations based on some attenuation model set by the API
	 * implementation of the audio manager. 
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param rollOff - Values G.T. 1 make the sound get quieter much quicker the further the ear gets from the source.  For a value = 0 distance attenuation is disabled. Range: [0, any]
	 */
	public void setRollOff(float rollOff);
	
	/**
	 * Sets the minimum distance at which distance attenuation begins.
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param distance - The sound can't get any louder if we move closer to the emit point than this value; Range: [0, any]
	 */
	public void setMinDistance(float distance);
	
	/**
	 * Sets the maximum distance at which distance attenuation stops. This is used for distance 
	 * attenuation calculations based on some attenuation model set by the API
	 * implementation of the audio manager. 
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 * 
	 * @param distance - The sound can't get any quieter if we move further away from the emit point than this value; Range: [0, any]
	 */
	public void setMaxDistance(float distance);
	
	/**
	 * Returns the location in 3D space of the {@link IAudioPlayer}. 
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 */
	public Vector3f getLocation();
	
	/**
	 * Returns the velocity vector of the {@link IAudioPlayer}.
	 * <p>
	 * NOTE: Method only implemented if the concrete audio system supports 3D sound. 
	 */
	public Vector3f getVelocity();
}
