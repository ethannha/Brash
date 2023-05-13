package tage.audio;

/**  
 * This class acts as the abstract base class from which all concrete Audio Ears are derived.  
 * It provides functions common to all audio ears, deferring the implementation of most {@link IAudioEar}
 * methods to concrete subclasses.  This class is responsible for "hearing" pieces of audio being played
 * in 3D space.
 * 
 * @author Kenneth Barnett
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 */

public abstract class AudioEar implements IAudioEar 
{
	protected int volume;	// The volume of the ear
	
	public int getVolume() { return volume; }
}
