package tage.audio.joal;

import java.nio.*;

import com.jogamp.openal.*;
import com.jogamp.openal.util.*;

import tage.audio.*;

/**
 * This class represents an OpenAL (via JOAL) audio resource.  
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * 
 * @see <a href="http://www.openal.org/documentation/openal-1.1-specification.pdf">OpenAL Specification</a>
 */
public class JOALAudioResource extends AudioResource 
{
	private int[] bufferID;			// The ID of the OpenAL sound buffer the sound data is stored in. 
	private int[] format;			// The format buffer of the sound data
	private int[] size;			// The size buffer of the sound data
	private int[] freq;			// The frequency buffer of the sound data
	private int[] loop;			// The loop buffer of the sound data
	private ByteBuffer[] data;		// The buffer for that actual sound data
	private AL al;				// Local reference to the JOAL AL object
	
	/**
	 * Returns the ID of the OpenAL buffer using to load this audio resource into memory.
	 */
	public int getBufferID() { return bufferID[0]; }

	public JOALAudioResource() 
	{
		super();
	}

	public JOALAudioResource(String soundFile, AudioResourceType type, AL al) 
	{
		super(soundFile, type);
		this.al = al;
	}

	/**
	 * Load the audio resource into memory.  
	 */
	public void load() 
	{
		if(!isLoaded)
		{
			// Initialize buffers
			bufferID = new int[1];
			format = new int[1];
			size = new int[1];
			freq = new int[1];
			loop = new int[1];
			data = new ByteBuffer[1];
			
			int result = AL.AL_FALSE;
			
			switch(audioFormat)
			{
				case FORMAT_WAVE:
					result = loadWAVFileData();
					break;
					
				case FORMAT_OGG:
					result = loadOGGFileData();
					break;
				
				default:
					result = AL.AL_FALSE;
					break;
			}
			
			if(result == AL.AL_TRUE)
				isLoaded = true;
			else
				isLoaded = false;
		}
	}

	/**
	 * Release the audio resource back to memory.
	 */
	public void unload() 
	{
		al.alDeleteBuffers(1, bufferID, 0);
		bufferID = null;
		format = null;
		size = null;
		freq = null;
		loop = null;
		data = null;
		isLoaded = false;
	}

	/**
	 * Load the .wav audio file into memory using an OpenAL buffer.
	 * 
	 * @return True if the file was successfully loaded; false otherwise. 
	 */
	private int loadWAVFileData()
	{
		// Load WAV information from the sound file into program arrays
		ALut.alutLoadWAVFile(soundFile, format, data, size, freq, loop);
		
		// Get an OpenAL buffer ID
		al.alGenBuffers(1, bufferID, 0);
		
		if(al.alGetError() != AL.AL_NO_ERROR)
			return AL.AL_FALSE;
		
		// Load the WAV file data into an OpenAL buffer
		al.alBufferData(bufferID[0], format[0], data[0], size[0], freq[0]);
		
		return AL.AL_TRUE;
	}
	
	/**
	 * Load the .ogg audio file into memory using an OpenAL buffer.
	 * 
	 * @return True if the file was successfully loaded; false otherwise. 
	 */
	private int loadOGGFileData()
	{
		return AL.AL_FALSE;
	}
}
