package tage.audio;

import java.util.*;

/**Modified from SAGE. 
 * 
 * This class encapsulates the notion of a "game resource" within SAGE.
 *  A Resource can be one of several types: Graphic (e.g. JPG, PNG, etc.),
 *  Audio (e.g. .wav, .mp3, etc.), or other resource type; 
 *  see {@link ResourceType} for a list of all supported Resources).
 *  Each resource type is typically implemented via a subclass of this class
 *  (for example, <code>class AudioResource extends Resource</code>).
 *  
 * @author John Clevenger
 * 
 * Modified from the SAGE Audio package for the RAGE game engine by Juan E. Ruiz.
 */
abstract public class Resource {

	private UUID id ;		//this resource's unique ID
	protected int scope ;		//bitmap indicating scenes requiring this resource;
					// 0=all (global), 0x06 = scenes 2&3, etc.
	protected String relativePath;	//path to file, relative to "."
	protected ResourceType type ;	//type of this resource
	
	
	/** Returns the UUID (Universally Unique ID) associated with this resource
	 * 
	 * @return this resource's id
	 */
	public UUID getID() {
		return id;
	}

	/**Returns the game scope within which this resource applies; a '1' in
	 * a bit position indicates the resource is required by the corresponding
	 * game scene/level, with the rightmost bit indicating "level 1".  
	 * For example, a scope value of 0x0006 indicates the
	 * resource is required in game levels 2 and 3.  A value of zero indicates
	 * the resource is global and must remain loaded for the entire game */
	public int getScope() {
		return scope;
	}

	/**Returns the relative path to the file defining this resource */
	public String getRelativePath() {
		return relativePath;
	}

	/**Returns the type of this resource as defined by {@link ResourceType} */
	public ResourceType getType() {
		return type;
	}
	
	/**Implemented by subclasses to load the content of this resource*/
	abstract public void load() ;
	
	/**Implemented by subclasses to unload (release) this resource*/
	abstract public void unload() ;
	
	protected Resource() {
		id = UUID.randomUUID();
		type = ResourceType.RESOURCE_NULL;
		relativePath = null;
		scope = 0;  //default is "game-wide"
	}
	
	
}
