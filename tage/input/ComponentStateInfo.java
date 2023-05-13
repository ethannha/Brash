package tage.input;

import tage.input.IInputManager.INPUT_ACTION_TYPE;
import tage.input.action.IAction;
import net.java.games.input.Event;

/**
 * <code>ComponentStateInfo</code>
 * stores state information for a specific component (key, button,
 * or axis).  Stored information includes the last Event associated with the
 * component, the manner in which any {@link IAction} associated with the
 * component is invoked, and whether
 * or not the component is currently pressed.
 * Note that for an Axis component the manner of invocation is not used
 * ({@link IAction}s associated with Axis components are invoked every time
 * the Axis changes) and the notion of "pressed" does not apply to an Axis
 * component.
 *
 * @author John Clevenger; based on a development version by Russell Bolles
 */

public class ComponentStateInfo {

	// the most recent Event associated with the component
	private Event lastEvent;

	// whether or not the last event is 'new' (just added)
	// This flag allows determining whether to repeat even
	private boolean isNewEvent ;

	// whether or not the component is currently "pressed" (relevant for
	// buttons and keys only)
	private boolean currentlyPressed;

	// the type of input action -- on Pressed, on Pressed and Released,
	//  or repeatedly while the component is down
	private IInputManager.INPUT_ACTION_TYPE actionType;

	/**Constructs a new ComponentStateInfo object containing the specified
	 * component state values.  The Event member holding the last Event
	 * associated with the component is initialized to null.
	 *
	 * @param pressed - whether or not the new ComponentState indicates
	 * 					 "pressed" or not
	 * @param actionType - the way in which an action associated with the
	 * 					component is invoked (that, is WHEN the action gets
	 * 					invoked)
	 */
	public ComponentStateInfo(boolean pressed, INPUT_ACTION_TYPE actionType) {
		this.currentlyPressed = pressed;
		this.actionType = actionType;
		this.lastEvent = null;
		this.isNewEvent = false ;
	}

	/**Returns the Event most recently associated with the component described
	 * by this {@link ComponentStateInfo} object.  The Event is initialized
	 * to null prior to the occurrence of any events from the component.
	 *
	 * @return the most recent Event associated with the component
	 */
	public Event getEvent() {
		return this.lastEvent;
	}

	/**Returns a boolean indicating whether the component described by this
	 * object is in the "pressed" state.  Note that this
	 * is not relevant for Axis components -- only for Buttons and Keys.
	 *
	 * @return whether or not the component is "pressed"
	 */
	public boolean isPressed() {
		return this.currentlyPressed;
	}

	/**Returns the {@link IInputManager.INPUT_ACTION_TYPE} currently
	 * associated with the component whose state is described by this object.
	 *
	 * @return the component's {@link IInputManager.INPUT_ACTION_TYPE}
	 */
	public INPUT_ACTION_TYPE getActionType() {
		return this.actionType;
	}

	/**Marks this {@link ComponentStateInfo} object as "pressed".
	 */
	public void markAsPressed() {
		this.currentlyPressed = true;
	}

	/**Marks this {@link ComponentStateInfo} object as "not pressed".
	 */
	public void markAsReleased() {
		this.currentlyPressed = false;
	}

	/**
	 * Attaches the specified event to the object, indicating that the specified event was the
	 * last event on the corresponding component. Marks the event as 'new'.
	 */
	public void updateEvent(Event evt) {
		//make a clone of the incoming event
		Event newEvent = new Event();
		newEvent.set(evt);

		//save the new event as this object's latest event
		this.lastEvent = newEvent ;
		this.isNewEvent = true ;
	}

	/**
	 * Returns whether the event attached to this object is "new".  Events are marked "new" when
	 * first attached to this object; they remain "new" until a client marks them otherwise.
	 */
	public boolean isNewEvent() {
		return isNewEvent ;
	}

	/**
	 * Sets the state of the event attached to this object to "new" or "not new".  Events are marked "new"
	 * when first attached to this object; they remain "new" until a client invokes this method with
	 * a parameter value of false.
	 */
	public void markEventAsNew(boolean isNew) {
		this.isNewEvent = isNew ;
	}
}