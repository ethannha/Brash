package tage.audio;

/**
 * This class extends {@link Sound}.  It represents a sound with more specific behavior than a typical sound. 
 * <code>NIterationSound</code> is a sound that replays itself a specific number of times.
 * 
 * @author Kenneth Barnett
 *
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 */
public class NIterationSound extends Sound 
{
	private int numRepeats;			// The number of times the sound with repeat
	private int repeatCount;		// The current repeat 
	private float repeatDelay;		// The delay in MSEC between repeats
	private float currentDelay;		// The current repeat delay
	
	protected NIterationSound() 
	{
		// disable default constructor
	}

	public NIterationSound(AudioResource resource, SoundType type, int volume, int repeats, float delayInSec) 
	{
		super(resource, type, volume, false);
		this.numRepeats = repeats;
		this.repeatCount = 0;
		this.repeatDelay = this.currentDelay = delayInSec * 1000;
	}

	public void update(float elapsedTime)
	{
		if(isInitialized)
		{
			// When the sound finishes...
			if(!getIsPlaying())
			{
				// Check current repeat count...
				if(repeatCount < numRepeats)
				{
					currentDelay += elapsedTime;
					
					// Check the current delay...
					if(currentDelay >= repeatDelay)
					{
						play();
						repeatCount++;
						currentDelay = 0;
					}
				}
			}
		}
		
		super.update(elapsedTime);
	}
	
	/**
	 * Resets this <code>NIterationSound</code> so that it begins iterating its playback again. 
	 */
	public void reset()
	{
		stop();
		currentDelay = repeatDelay;
		repeatCount = 0;
	}
	
	public void play()
	{
		if(repeatCount < numRepeats)
		{
			super.play();
		}
	}
}
