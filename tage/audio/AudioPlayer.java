package tage.audio;

/**  
 * This class acts as the abstract base class from which all concrete Audio Players are derived.  
 * It provides functions common to all audio players, deferring the implementation of most {@link IAudioPlayer}
 * methods to concrete subclasses.  This class is responsible for playing an individual piece of audio. 
 * 
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public abstract class AudioPlayer implements IAudioPlayer 
{
	protected boolean isPaused;		// A flag signaling if the sound coming from the audio player is paused
	protected boolean isLooping;		// A flag signaling if the sound coming from the audio player is looping
	protected int volume;			// The volume the audio player plays the sound at
	protected SoundType type;		// The type of sound the audio player is playing
	protected AudioResource resource;	// Local reference to the audio resource used by the audio player
	
	/**
	 * Returns the {@link AudioResource} used by this {@link AudioPlayer}.
	 */
	public AudioResource getResource() { return resource; }
	
	/**
	 * Returns true if this {@link AudioPlayer} is paused; false otherwise. 
	 */
	public boolean getIsPaused() { return isPaused; }
	
	/**
	 * Returns true if this {@link AudioPlayer} is looping the sound; false otherwise.
	 */
	public boolean getIsLooping() { return isLooping; }
	
	/**
	 * Returns the volume that this {@link AudioPlayer} is playing the sound at.  
	 */
	public int getVolume() { return volume; }
	
	/**
	 * Returns the type of sound that this {@link AudioPlayer} is responsible for playing.
	 */
	public SoundType getSoundType() { return type; }
	
	protected AudioPlayer(AudioResource resource, SoundType type)	// disable public construction...
	{
		this.isPaused = false;
		this.isLooping = false;
		this.volume = 0;
		this.resource = resource;
		this.type = type;
	}
}
