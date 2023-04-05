package tage.input.action;

import net.java.games.input.Event;

/** This interface specifies the methods which must be implemented by all
 * concrete subclasses of {@link AbstractInputAction}.
 *
 * @author Russell Bolles
 */
public interface IAction {
    
	/** <code>performAction()</code> is invoked to perform the action 
	 * defined by this <code>IAction</code> object.
	 * @param time -- the elapsed time for which the <code>IAction</code> is being invoked
	 * @param evt -- the event associated with invoking this <code>IAction</code>
	 */
    public void performAction(float time, Event evt);

}
