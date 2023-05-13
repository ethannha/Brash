package tage.input.action;

import net.java.games.input.Event;
import tage.input.InputManager;

/** <code>AbstractInputAction</code> is the base class from which all TAGE
 * input actions are derived.  It maintains a <I>speed</i> associated with
 * the action, and specifies a method <code>performAction()</code> which
 * must be implemented by all concrete subclasses. 
 * 
 * @author John Clevenger; based on a development version by Russell Bolles
 * 
 * @see IAction
 * @see InputManager#associateAction(net.java.games.input.Component.Identifier, 
 * 					IAction, tage.input.IInputManager.INPUT_ACTION_TYPE)
 * 
 */
public abstract class AbstractInputAction implements IAction {
	
	// speed at which the action can occur
	private float speed = 1; 

	/** Sets the speed at which the Action occurs (default = 1). 
	 * The interpretation of speed units is
	 * application (game) dependent.
	 */
	public void setSpeed(float newSpeed) {
		speed = newSpeed;
	}

	/** Returns the current speed setting for the Action */
	public float getSpeed() {
		return speed;
	}
	
	/** <code> performAction() </code> is invoked to execute this input action.
	 * It must be implemented by all concrete subclasses. 
	 * @param time -- the time at which the action is invoked
	 * @param evt -- the event associated with invoking this action
	 */
	public abstract void performAction(float time, Event evt);

}
