package tage.audio;

import org.joml.*;

/**
 * This class represents a sound (sound effect, music, etc...).
 * Only a mono channel sound file will produce spatial effects.
 * Stereo sounds will behave like a 2D sound. 
 * 
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordon.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public class Sound 
{
	protected boolean isInitialized;
	protected boolean isLooping;		// Flag signaling if the sound is looping
	protected int volume;			// The volume that the sound is playing at
	protected SoundType type;		// The type of the sound
	protected AudioResource resource;	// The audio resource the sound uses
	protected IAudioPlayer player;		// The audio player the sound uses
	
	protected Sound()
	{
		// disable default constructor
	}
	
	/**
	 * Constructs a new <code>Sound</code> object. 
	 * 
	 * @param resource 	- The audio resource of the sound. 
	 * @param type		- The type of the sound being created. 
	 * @param volume	- The volume of the sound.
	 * @param looping	- <code>true</code> if the sound should loop its playback; <code>false</code> otherwise. 
	 */
	public Sound(AudioResource resource, SoundType type, int volume, boolean looping)
	{
		this.isInitialized = false;
		this.isLooping = looping;
		this.volume = volume;
		this.resource = resource;
		this.type = type;
	}
	
	/**
	 * Initializes the sound using the {@link AudioResource} passed in during construction of the {@link Sound} object. 
	 * @param audioMgr The {@link AudioManager} which helps with sound initialization.  
	 */
	public void initialize(IAudioManager audioMgr)
	{
		if(resource == null)
			return;
		
		resource.load();	// initialize resource
		
		player = audioMgr.initAudioPlayer(resource, type);
		
		if(player == null)
			return;
		
		player.setVolume(volume);
		player.setIsLooping(isLooping);
		
		isInitialized = true;
	}
	
	/**
	 * Updates the behavior of the sound.  
	 */
	public void update(float elapsedTime)
	{
		
	}
	
	/**
	 * Releases the sound back to memory.
	 */
	public void release(IAudioManager audioMgr)
	{
		audioMgr.releaseAudioPlayer(player);
	}
	
	/**
	 * Returns true if the sound is valid and is capable of being used; false otherwise.
	 */
	public boolean getIsSoundValid() { return (resource != null); }
	
	/**
	 * Returns the {@link SoundType} of the sound.
	 */
	public SoundType getType() { return type; }
	
	/**
	 * Returns true if the sound is currently playing; false otherwise. 
	 */
	public boolean getIsPlaying()
	{
		if(resource == null && player == null)
			return false;
		
		return player.getIsPlaying();
	}
	
	/**
	 * Returns true if the sound is looping; false otherwise.
	 */
	public boolean getIsLooping()
	{
		if(player == null)
			return false;
		
		return isLooping;
	}
	
	/**
	 * Sets the volume of the sound.  
	 * 
	 * @param volume - The volume value from 0 to 100 inclusive. 
	 */
	public void setVolume(int volume)
	{
		if(player == null)
			return;
		
		if(volume < 0)
			this.volume = 0;
		else if(volume > 100)
			this.volume = 100;
		else
			this.volume = volume;
		
		player.setVolume(this.volume);
	}
	
	/**
	 * Returns the volume of the sound. 
	 */
	public int getVolume()
	{
		if(player == null)
			return 0;
		
		volume = player.getVolume();
		
		return volume;
	}
	
	/**
	 * Sets the roll off factor for the sound.  Set to 0 to disable distance attenuation.
	 * <p>
	 * Sets the roll off factor of the {@link Sound}. This is used for distance 
	 * attenuation calculations based on some attenuation model set by the API
	 * implementation of the audio manager.  
	 * 
	 * @param rollOff - Values G.T. 1 make the sound get quieter much quicker the further the ear gets from the source.  For a value = 0 distance attenuation is disabled. Range: [0, any]
	 */
	public void setRollOff(float rollOff)
	{
		player.setRollOff(rollOff);
	}
	
	/**
	 * Sets the minimum distance at which the sound started to dissipate.
	 * <p>
	 * Sets the minimum distance at which distance attenuation begins. 
	 * 
	 * @param distance - The sound can't get any louder if we move closer to the emit point than this value; Range: [0, any]
	 */
	public void setMinDistance(float distance)
	{
		player.setMinDistance(distance);
	}
	
	/**
	 * Sets the max distance at which the sound stops dissipating.
	 * <p>
	 * Sets the maximum distance at which distance attenuation stops. This is used for distance 
	 * attenuation calculations based on some attenuation model set by the API
	 * implementation of the audio manager. 
	 * 
	 * @param distance - The sound can't get any quieter if we move further away from the emit point than this value; Range: [0, any]
	 */
	public void setMaxDistance(float distance)
	{
		player.setMaxDistance(distance);
	}
	
	/**
	 * Sets the pitch of the sound.
	 * 
	 * @param pitch - The pitch value that is greater than 0. 
	 */
	public void setPitch(float pitch)
	{
		player.setPitch(pitch);
	}
	
	/**
	 * Pauses/Unpauses the sound. 
	 */
	public void togglePause()
	{
		if(player == null)
			return;
		
		player.togglePause();
	}
	
	/**
	 * Plays the sound.
	 */
	public void play()
	{
		if(player == null)
			return;
		
		player.play(getVolume(), isLooping);
	}
	
	/**
	 * Plays the sound at the specified volume and whether or not it is looping. 
	 * @param volume
	 * @param looping
	 */
	public void play(int volume, boolean looping)
	{
		if(player == null)
			return;
		
		player.play(volume, looping);
	}
	
	/**
	 * Stops the sound from playing. 
	 */
	public void stop()
	{
		if(player == null)
			return;
		
		player.stop();
	}
	
	/**
	 * Pauses the sound.
	 */
	public void pause()
	{
		if(player == null)
			return;
		
		player.pause();
	}
	
	/**
	 * Resumes playing the sound. 
	 */
	public void resume()
	{
		if(player == null)
			return;
		
		player.resume();
	}
	
	/**
	 * Returns the playback progress of the sound in seconds. 
	 */
	public float getProgress()
	{
		if(player == null)
			return 0.0f;
		
		return player.getProgress();
	}
	
	/**
	 * Sets the location of the sound in 3D space.
	 * 
	 * @param point - The point in 3D space the sound is located at.
	 */
	public void setLocation(Vector3f point)
	{
		if(player == null)
			return;
		
		player.setLocation(point);
	}
	
	/**
	 * Sets the velocity that the sound object is moving at.
	 * 
	 * @param velocity - The velocity vector of the sound.
	 */
	public void setVelocity(Vector3f velocity)
	{
		if(player == null)
			return;
		
		player.setVelocity(velocity);
	}
	
	/**
	 * Sets the emit direction of the Sound. Visually you can think of this as
	 * setting a "cone" around the Sound which will direct the sound in a particular
	 * direction.
	 * <p>
	 * The zero vector (0,0,0) is the default direction which indicated that the audio source emitted
	 * from the audio player is not direction. Specifying a non-zero vector will make the source directional. 
	 * Specifying a zero vector for a directional source will effectively mark it as non-direction. 
	 * 
	 * @param direction - The direction in which the sound is emitting. 
	 * @param coneAngle - The angle (in degrees) of the "cone" around the Sound. Default value is 360 degrees
	 * 					  means the sound being emitted from the audio player is omni-directional. 
	 */
	public void setEmitDirection(Vector3f direction, float coneAngle)
	{
		if(player == null)
			return;
		
		player.setEmitDirection(direction, coneAngle);
	}
	
	/**
	 * Returns the location of the sound in 3D space. 
	 */
	public Vector3f getLocation()
	{
		if(player == null)
			return null;
		
		return player.getLocation();
	}
	
	/**
	 * Returns the velocity of the sound. 
	 */
	public Vector3f getVelocity()
	{
		if(player == null)
			return null;
		
		return player.getVelocity();
	}
}
