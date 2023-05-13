package tage.audio;

import org.joml.*;

import java.util.Iterator;
import java.util.Vector;

/**  
 * This class acts as the abstract base class from which all concrete Audio Managers are derived.  
 * It provides functions common to all audio managers, deferring the implementation of most {@link IAudioManager}
 * methods to concrete subclasses.  It also maintains a collection of currently active {@link IAudioPlayer}s
 * 
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordon.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public abstract class AudioManager implements IAudioManager 
{
	protected boolean isAllPaused;		// A flag signaling if all of the active sounds are paused
	protected boolean isInitialized;	// A flag signaling if the audio manager has been initialized
	protected IAudioEar audioEar;		// The audio listener (ear)
	protected Vector<IAudioPlayer> allAudioPlayers;		// Collection of all of the active audio players (sounds)
	
	public AudioManager()
	{
		this.isAllPaused = false;
		this.isInitialized = false;
		this.audioEar = null;
		this.allAudioPlayers = null;
	}
	
	/**
	 * Returns the Ear (audio listener) attached to this {@link AudioManager}. 
	 */
	public IAudioEar getEar() { return audioEar; }
	
	/**
	 * Stops all currently playing sounds. 
	 */
	public void stopAllSounds()
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			player.stop();
		}
		isAllPaused = false;
	}
	
	/**
	 * Stops all currently playing sounds that belong to a specific {@link SoundType}.
	 */
	public void stopAllSounds(SoundType type)
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			if(player.getSoundType() == type) player.stop();
		}
	}
	
	/**
	 * Pauses all currently playing sounds.
	 */
	public void pauseAllSounds()
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			//player.stop();
			player.pause();
		}
		isAllPaused = true;
	}
	
	/**
	 * Pauses all currently playing sounds that belong to a specific {@link SoundType}.
	 */
	public void pauseAllSounds(SoundType type)
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			if(player.getSoundType() == type) player.pause();
		}
	}
	
	/**
	 * Resumes playing all paused sounds.
	 */
	public void resumeAllSounds()
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			player.resume();
		}
		isAllPaused = false;
	}
	
	/**
	 * Resumes playing all paused sounds that belong to a specific {@link SoundType}.
	 */
	public void resumeAllSounds(SoundType type)
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			if(player.getSoundType() == type) player.resume();
		}
	}
	
	/**
	 * Sets the volume for all sounds of the specified type. 
	 */
	public void setVolumeForType(SoundType type, int volume)
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			if(player.getSoundType() == type) player.setVolume(volume);
		}
	}
	
	/**
	 * Shuts down the sound manager by stopping all currently playing sounds and
	 * empties the collection of active sounds.
	 */
	public void shutdown()
	{	IAudioPlayer player = null;
		for(Iterator<IAudioPlayer> it = allAudioPlayers.iterator(); it.hasNext(); )
		{	player = it.next();
			player.stop();
		}
		allAudioPlayers.clear();
		isInitialized = false;
	}
	
	/**
	 * Static method that returns a three element float array composed of the X, Y, and Z values from the point passed in. 
	 */
	public static float[] getPointValues(Vector3f point)
	{	float[] values = new float[3];
		values[0] = point.x();
		values[1] = point.y();
		values[2] = point.z();
		return values;
	}
	
	/**
	 * Static method that returns a three element float array composed of the X, Y, and Z values from the vector passed in. 
	 */
	public static float[] getVectorValues(Vector3f vector)
	{	float[] values = new float[3];
		values[0] = vector.x();
		values[1] = vector.y();
		values[2] = vector.z();
		return values;
	}
}
