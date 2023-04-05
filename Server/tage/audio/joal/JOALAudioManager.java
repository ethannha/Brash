package tage.audio.joal;

import java.util.*;

import org.joml.*;

import com.jogamp.openal.*;
import com.jogamp.openal.util.*;

import tage.audio.*;

/**
 * This class extends the {@link AudioManager} abstract class and uses the JOAL Java interface to OpenAL 
 * (http://jogamp.org) to provide a JOAL-based audio manager.  This class is responsible for the audio playback
 * of the collection of sounds.
 * <p>
 * <code>JOALAudioManager</code> uses the Inverse Distance Clamped Rolloff model for distance attenuation. The
 * follow is the OpenAL specification's description of this model:
 * <p>
 * <i>
 * This is the Inverse Distance Rolloff Model model, extended to guarantee that for distances below AL_REFERENCE_DISTANCE, 
 * gain is clamped. This mode is equivalent to the IASIG I3DL2 distance model.
 * </i>
 * <p>
 * <code>
 * 	distance = max(distance,AL_REFERENCE_DISTANCE);		<br>
 * 	distance = min(distance,AL_MAX_DISTANCE);		<br>
 * 	gain = AL_REFERENCE_DISTANCE / (AL_REFERENCE_DISTANCE + AL_ROLLOFF_FACTOR * (distance – AL_REFERENCE_DISTANCE));
 * </code>
 * 
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * Adapted for TAGE by Scott Gordon.
 * 
 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
 */
public class JOALAudioManager extends AudioManager 
{
	private AL al;		// The JOAL AL object
	
	/**
	 * Returns the AL object of this audio manager.
	 */
	public AL getAL() { return al; }
	
	public JOALAudioManager() 
	{
		super();
		al = null;
	}

	/**
	 * Returns true if the sound system is active; false otherwise. 
	 */
	public boolean getIsActive() 
	{
		return (al != null && !isAllPaused);
	}
	
	/**
	 * Returns the master (global) volume of this {@link AudioManager} by getting the AL_GAIN from the {@link JOALAudioEar}.
	 */
	public int getMasterVolume() 
	{ 
		if(audioEar == null)
			return 0;
		
		return audioEar.getVolume(); 
	}
	
	/**
	 * Sets the master (global) volume of this {@link AudioManager} by setting the AL_GAIN for the {@link JOALAudioEar}.
	 */
	public void setMasterVolume(int volume) 
	{
		if(audioEar == null)
			return;
		
		audioEar.setVolume(volume);
	}

	/**
	 * Returns a {@link JOALAudioResource} suing the specified sound file and {@link AudioResourceType}. 
	 */
	public AudioResource createAudioResource(String soundName, AudioResourceType type) 
	{
		JOALAudioResource resource = null;
		
		resource = new JOALAudioResource(soundName, type, al);
		
		return resource;
	}

	/**
	 * Returns a new {@link JOALAudioPlayer} after creating it and adding it to the collection of currently active sounds. 
	 */
	public IAudioPlayer initAudioPlayer(AudioResource resource, SoundType type) 
	{
		if(al == null)
			return null;
		
		switch(resource.getAudioFormat())
		{
			case FORMAT_WAVE:
				// WAV is supported
				break;
				
			case FORMAT_OGG:
				// OGG is not supported
				System.out.println("OGG is not supported.");
				return null;
				
			default:
				System.out.println("Unknown file format.");
				return null;
			
		}
		
		// Get an OpenAL source ID
		int[] sourceID = new int[1];
		al.alGenSources(1, sourceID, 0);
		
		if(al.alGetError() != AL.AL_NO_ERROR)
			return null;
		
		// Bind buffer with source
		al.alSourcei(sourceID[0], AL.AL_BUFFER, ((JOALAudioResource)resource).getBufferID());
		
		// Create the audio player
		IAudioPlayer audioPlayer = null;
		
		audioPlayer = new JOALAudioPlayer(resource, type, sourceID[0], this);
		
		allAudioPlayers.add(audioPlayer);
		
		return audioPlayer;
	}

	/**
	 * Removes the specified {@link IAudioPlayer} from the collection of active sounds and released the player from memory.
	 */
	public void releaseAudioPlayer(IAudioPlayer audioPlayer) 
	{
		audioPlayer.stop();
		allAudioPlayers.remove(audioPlayer);
		audioPlayer.release();
	}

	/**
	 * Initialize the JOALAudioManager.  This includes getting an AL object, initializes ALUT, creating a JOALAudioEar, 
	 * and clearing the {@link sage.audio.AudioPlayer} collection. 
	 */
	public boolean initialize() 
	{
		if(isInitialized)
			return true;
		
		// Create the collection of AudioPlayers
		allAudioPlayers = new Vector<IAudioPlayer>();
		
		if(allAudioPlayers == null)
			return false;
		
		// Get an initialized AL object
		al = ALFactory.getAL();
		
		if(al == null)
			return false;
			
		// Initialize the AL toolkit
		ALut.alutInit();
			
		// Clear the error bit
		al.alGetError();
		
		// Set the distance attenuation model
		al.alDistanceModel(AL.AL_INVERSE_DISTANCE_CLAMPED);
			
		// Initialize the audio listener (Ear)
		audioEar = new JOALAudioEar(al);
		audioEar.setLocation(new Vector3f(0.0f, 0.0f, 0.0f));
		audioEar.setVelocity(new Vector3f(0.0f, 0.0f, 0.0f));
		audioEar.setOrientation(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(0.0f, 1.0f, 0.0f));
			
		allAudioPlayers.clear();
		isInitialized = true;
			
		return true;
	}
	
	/**
	 * Shuts down the sound manager by stopping all currently playing sounds and 
	 * releasing the sound back to memory.  It also nulls the AL object. 
	 */
	public void shutdown()
	{
		al = null;
		ALut.alutExit();
		super.shutdown();
	}
	
	/**
	 * Returns the equivalent OpenAL boolean value of the passed in Java boolean value.
	 * @param b Java boolean value
	 * @return OpenAL boolean value as an int.
	 */
	public static int getALBoolean(boolean b)
	{
		if(b)
			return AL.AL_TRUE;
		else
			return AL.AL_FALSE;
	}
}
