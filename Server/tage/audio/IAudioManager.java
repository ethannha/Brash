package tage.audio;

/**
 * The <code>IAudioManager</code> interface exposes the public methods that all audio managers must implement.   
 * 
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public interface IAudioManager 
{
	/**
	 * Returns true if the audio system is active; false otherwise.
	 * <p>  
	 * NOTE: This method is implementation dependent.
	 */
	public boolean getIsActive();
	
	/**
	 * Creates and returns a new {@link AudioResource}.
	 * <p>
	 * NOTE: This method is implementation dependent.
	 * 
	 * @param soundName - The name of the sound file (full path and file extension required)
	 * @param type - The {@link AudioResourceType} of audio resource being loaded.  
	 */
	public AudioResource createAudioResource(String soundName, AudioResourceType type);
	
	/**
	 * Creates a new {@link IAudioPlayer} and registers it to the {@link IAudioManager}.
	 * <p>
	 * NOTE: This method is implementation dependent.
	 * 
	 * @param resource - The {@link AudioResource} containing the sound data.
	 * @param type - The {@link SoundType} of the audio player. 
	 * @return The new {@link IAudioPlayer}
	 */
	public IAudioPlayer initAudioPlayer(AudioResource resource, SoundType type);
	
	/**
	 * Unregisters the {@link IAudioPlayer} from the {@link IAudioManager}, and
	 * releases the player back to memory.  
	 * <p>
	 * NOTE: This method is implementation dependent.
	 * 
	 * @param audioPlayer - The audio player to release.
	 */
	public void releaseAudioPlayer(IAudioPlayer audioPlayer);
	
	/**
	 * Returns the {@link IAudioEar} attached to the {@link IAudioManager}. 
	 * <p>
	 * NOTE: The implementation of this method is dependent upon whether or not the {@link IAudioManager} implementation
	 * supports 3D sound.
	 */
	public IAudioEar getEar();
	
	/**
	 * Stops all sounds registered with the {@link IAudioManager} from playing.
	 */
	public void stopAllSounds();
	
	/**
	 * Stops all sounds of a specific {@link SoundType} registered with the {@link IAudioManager} from playing.
	 */
	public void stopAllSounds(SoundType type);
	
	/**
	 * Pauses all sounds registered with the {@link IAudioManager}.
	 */
	public void pauseAllSounds();
	
	/**
	 * Pauses all sounds of a specific {@link SoundType} registered with the {@link IAudioManager}.
	 */
	public void pauseAllSounds(SoundType type);
	
	/**
	 * Resumes playing all sounds registered with the {@link IAudioManager}.
	 */
	public void resumeAllSounds();
	
	/**
	 * Resumes playing all sounds of a specific {@link SoundType} registered with the {@link IAudioManager}.
	 */
	public void resumeAllSounds(SoundType type);
	
	/**
	 * Returns the master (global) volume of the sound system.
	 * <p>
	 * NOTE: This method is implementation dependent.
	 */
	public int getMasterVolume();
	
	/**
	 * Sets the master (global) volume for all sounds registered with the {@link IAudioManager}.
	 * <p>
	 * NOTE: This method is implementation dependent.
	 */
	public void setMasterVolume(int volume);
	
	/**
	 * Sets the volume for for sounds of a specific {@link SoundType}.
	 */
	public void setVolumeForType(SoundType type, int volume);
	
	/**
	 * Initializes the audio system. 
	 * <p>
	 * NOTE: This method is implementation dependent.
	 * 
	 * @return True if initialization was successful; false otherwise.  
	 */
	public boolean initialize();	// Implementation dependent
	
	/**
	 * Shutdown the audio system.
	 * <p>
	 * NOTE: This method is implementation dependent.
	 */
	public void shutdown();			// Implementation dependent
}
