package tage;
import tage.*;
import java.util.*;

/**
* Abstract class for scenegraph node controllers.
* Includes implementations of methods for keeping track of objects being controlled.
* Also includes retrievable values for total and incremental elapsed time,
* and for enabling/disabling the controller.
* <p>
* When building a custom node controller class, it should extend NodeController, and
* implement the method apply() that performs the desired functionality on each attached object.
* @author Scott Gordon
*/
public abstract class NodeController
{
	boolean enabled = false;
	long startTime, prevTime, curTime, elapsedTimeTotal, elapsedTimeTick;
	private ArrayList<GameObject> targets = new ArrayList<GameObject>();

	/** Causes the controller to start functioning (starts calling "apply" at each frame). */
	public void enable()
	{	enabled = true;
		startTime = System.currentTimeMillis();
		prevTime = startTime;
		curTime = startTime;
		elapsedTimeTotal = 0;
		elapsedTimeTick = 0;
	}

	/** Causes the controller to stop affecting nodes (stops calling "apply"). */
	public void disable() { enabled = false; }

	/** returns a boolean that is true if the node controller is currently enabled */
	public boolean isEnabled() { return enabled; }

	/** enables this controller if disabled, and disables it if it is enabled */
	public void toggle() { if (enabled) disable(); else enable(); }

	/** Adds the specified GameObject to the list of GameObjects this controller affects when enabled. */
	public void addTarget(GameObject go) { targets.add(go); }

	/** Returns the elapsed time since the last time the controller was applied (if enabled) - usually means since the last frame. */
	public float getElapsedTime() { return elapsedTimeTick; }

	/** Returns the elapsed time since the controller was last enabled. */
	public float getElapsedTimeTotal() { return elapsedTimeTotal; }

	// Perform the controller operation on each attached game object.
	// This function is called by SceneGraph, and should not be called from the game application.
	// Note that this funtion calls the concrete apply() for each object.

	protected void applyController()
	{	elapsedTimeTick = System.currentTimeMillis() - prevTime;
		elapsedTimeTotal = System.currentTimeMillis() - startTime;
		prevTime = System.currentTimeMillis();
		for (int i = 0; i < targets.size(); i++) { apply(targets.get(i)); }
	}

	/** A node controller should override this function to specify what the controller does to its target object(s). */
	public abstract void apply(GameObject t);
}