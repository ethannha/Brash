package tage.audio;

/**
 * This class represents an audio resource in memory.  It encapsulates the data loaded
 * from a sound file.  
 * 
 * @author Kenneth Barnett
 * @author Mike McShaffry
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 * 
 * @see <a href="http://www.mcshaffry.com/mrmike/index.php/about-mrmike/17-game-coding-complete-2003-present">Game Coding Complete</a>
 */
public abstract class AudioResource extends Resource 
{
	protected boolean isLoaded;			// A flag signaling if the sound file has been loaded into memory.
	protected String soundFile;			// The location of the sound file to be loaded.
	protected AudioResourceType audioType;		// The type of audio resource (sample or stream) the sound is.
	protected AudioFormat audioFormat;		// The format of the sound file. 
	
	/**
	 * Returns true if the sound file has successfully been loaded into memory; false otherwise. 
	 */
	public boolean getIsLoaded() { return isLoaded; }
	
	/**
	 * Returns the name of the sound file.  
	 */
	public String getFileName() { return soundFile; }
	
	/**
	 * Returns the {@link AudioResourceType} of the sound file. 
	 */
	public AudioResourceType getAudioType() { return audioType; }
	
	/**
	 * Returns the {@link AudioFormat} of the sound file. 
	 */
	public AudioFormat getAudioFormat() { return audioFormat; }
	
	public AudioResource() 
	{
		super();
		this.isLoaded = false;
		this.soundFile = null;
		this.audioType = null;
		this.audioFormat = null;
	}
	
	public AudioResource(String soundFile, AudioResourceType type)
	{
		super();
		this.isLoaded = false;
		this.soundFile = soundFile;
		this.audioType = type;
		this.audioFormat = findFormatFromFileName(soundFile);
	}
	
	/**
	 * Returns the {@link AudioFormat} of the sound file by looking at the file extension. 
	 * 
	 * @param fileName The name of the sound file to be loaded into memory.  The name includes the file extension. 
	 * @return The {@link AudioFormat} the file represents.
	 */
	private AudioFormat findFormatFromFileName(String fileName)
	{
		switch(fileName.substring(fileName.lastIndexOf(".")))
		{
			case ".wav":
				return AudioFormat.FORMAT_WAVE;
				
			case ".ogg":
				return AudioFormat.FORMAT_OGG;
			
			default:
				return AudioFormat.FORMAT_UNKNOWN;
		}
	}

}
