package tage.input;

import java.util.ArrayList;

import net.java.games.input.Component;
import net.java.games.input.Controller;
import net.java.games.input.Event;
import tage.input.action.IAction;

/**
 * This interface specifies the functions provided by an input
 * manager implementation, independent of what underlying input package the
 * input manager uses. <br>
 * An <code>IInputManager</code> implementation could be based for example on
 * the open-source Java Gaming Initiative (JGI) "JInput" package
 * (https://jinput.dev.java.net), or on the open-source
 * "Object-oriented Input System (OIS)" package
 * (http://sourceforge.net/projects/wgois), or on the Microsoft "DirectInput"
 * component of DirectX -- as long as the <code>IInputManager</code>
 * implementation provides all the functionality specified by this interface. <br>
 * This allows an implementation to manage and forward device input to a game
 * application in a device handler package-independent way. <br>
 * NB: the current
 * IInputManager interface is not package-independent; it is tied to the JInput
 * package. 
 * <p>
 * Three types of input controllers are recognized: keyboard, mouse, and "other" (for example,
 * gamepads, steering wheels, etc.)  Controllers are collections of <I>components</i>, of which there
 * are three types:  Axis, Button, and Key.  Each component (axis, button, or key) has a <I>name</i> and an
 * <I>identifier</i>.  The <I>identifier</i> associated with a component is the primary means of
 * accessing the component's value or for associating an action with the component.
 * <p>
 * Axis components have a single range and can hold information for motion (linear or rotational), velocity, force, or acceleration.
 * The following Identifiers are recognized for Axis
 * components (that is, the Identifier parameter for a given Axis
 * component may be any of the following):
 * <ul>
 * 	<li>X, Y, or Z (horizontal, vertical, and third-dimensional axes respectively);
 * 	<li>RX, RY, or RZ (rotational data)
 * 	<li>SLIDER (a slider or mouse wheel)
 * 	<li>SLIDER_ACCELERATION, SLIDER_FORCE, or SLIDER_VELOCITY (respective physics
 * 		attributes for the SLIDER axis)
 * 	<li>X_ACCELERATION, X_FORCE, or X_VELOCITY (respective physics attributes for
 * 		the X axis)
 * 	<li>Y_ACCELERATION, Y_FORCE, or Y_VELOCITY (respective physics attributes for
 * 		the Y axis)
 * 	<li>Z_ACCELERATION, Z_FORCE, or Z_VELOCITY (respective physics attributes for
 * 		the Z axis)
 * 	<li>RX_ACCELERATION, RX_FORCE, or RX_VELOCITY (respective physics attributes for
 * 		the RX axis)
 * 	<li>RY_ACCELERATION, RY_FORCE, or RY_VELOCITY (respective physics attributes for
 * 		the RY axis)
 * 	<li>RZ_ACCELERATION, RZ_FORCE, or RZ_VELOCITY (respective physics attributes for
 * 		the RZ axis)
 * 	<li>POV (axis for point-of-view control)
 * </ul>
 * Note that each of the above constants, declared in <code>net.java.jinput.Component.Identifier.Axis</code>,
 * defines a <I>different controller axis</i>, and that not all controllers support all axes.
 * <p>
 * Examples:
 * <ul>
 * <li>The Logitech "Precision" controller returns the name
 * "Logitech(R) Precision(TM) Gamepad" and has two Axis components with
 * Identifiers 'x' and 'y' and 10 Button components with Identifiers '0',
 * '1', ... '9'.  'x' and 'y' are independent axes for the horizontal and vertical "D-pad" device.
 * <li>The PDP "Afterglow" XBox controller returns the name
 * "Afterglow Gamepad for Xbox 360 (Controller)" and has 6 Axis controllers with
 * Identifiers 'x', 'y', 'z', 'rx', 'ry', and 'pov' and 10 Button components
 * with Identifiers '0', '1', ... '9'.
 * </ul>
 * <p>
 * A list of installed controllers can be obtained by calling
 * {@link #getControllers()}, which returns an {@link ArrayList} of controllers.
 * The components in a given controller can be obtained by calling
 * {@link Controller#getComponents()}, which returns an array (not an
 * {@link ArrayList}) of {@link Component}s. The Identifier for a given
 * {@link Component} can be obtained by calling
 * {@link Component#getIdentifier()}.<br>
 * A given Axis component name may be referenced by using the Axis Identifiers declared in 
 * <code>net.java.jinput.Component.Identifier.Axis</code>;
 * for example,  <code>net.java.games.jinput.Component.Identifier.Axis.RX</code>  
 * for the rotational X axis component (if such is supported by the controller).
 * 
 * 
 * 
 * 
 * @author John Clevenger, based on a development version by Russell Bolles
 */
public interface IInputManager {

	/**
	 * <code> INPUT_ACTION_TYPE </code> enumerates the possible manners in which
	 * an {@link IAction} associated with a button or key will be invoked when
	 * the button or key generates an input event. Note that {@link IAction}s
	 * associated with Axis devices are invoked on every change (input event)
	 * from the Axis, regardless of any <code>INPUT_ACTION_TYPE</code>
	 * associated with the specified {@link IAction}.
	 * 
	 * @see IInputManager#associateAction(Component.Identifier, IAction,
	 *      INPUT_ACTION_TYPE)
	 */
	public enum INPUT_ACTION_TYPE {
		ON_PRESS_ONLY, ON_PRESS_AND_RELEASE, REPEAT_WHILE_DOWN
	}

	/**
	 * <code>update(float time)</code> instructs the <code>IInputManager</code>
	 * object to update its notion of the state of all input devices about which
	 * it knows, and then to invoke any {@link IAction}s associated with device
	 * components based on the {@link IInputManager.INPUT_ACTION_TYPE} of the
	 * {@link IAction}.
	 * 
	 * @param time
	 *            - the amount of time, normally given in milliseconds, which has 
	 *            	passed since the last call to update().
	 */
	public void update(float time);

	/**<code>associateAction()</code> causes the {@link InputManager}
	 * to associate an {@link IAction} (for example,
	 * "walk forward", "fire weapon", etc) 
	 * with the activation of a specific component 
	 * (for example, key 'A', etc).
	 * Associating an {@link IAction} with a component
	 * causes {@link InputManager#update(float)} to automatically 
	 * invoke that {@link IAction}
	 * by calling its {@link tage.input.action.IAction#performAction(float,net.java.games.input.Event)} method
	 * based on the associated {@link tage.input.IInputManager.INPUT_ACTION_TYPE}.
	 * <P>
	 * NOTE: this method is <I>deprecated</i> and is provided for backward 
	 * compatibility with older versions of Sage;
	 * use {@link IInputManager#associateAction(String, Component.Identifier,
	 * IAction, INPUT_ACTION_TYPE)} instead.  If this method is called it 
	 * defaults to associating the specified IAction with the keyboard returned 
	 * by {@link tage.input.IInputManager#getKeyboardName()}; if the specified
	 * component is not an instance of {@link net.java.games.input.Component.Identifier.Key}
	 * a warning is printed and the request is ignored.
	 *
	 * @param component - identifier of the component with which the action 
	 * 						is to be associated
	 * @param action - the action which is to be associated with the
	 * 						specified component
	 * @param actionType -- indicates when the action is to be invoked in
	 * 						relation to the occurrence of component events
	 * 
	 * @return the action which was previously associated with the specified
	 * 				component, or null if there was no previously associated
	 * 				action.
	 */
	@Deprecated
	public IAction associateAction(Component.Identifier component,
			IAction action, INPUT_ACTION_TYPE actionType);

	/**
	 * <code>associateAction()</code> causes the <code>IInputManager</code>
	 * object to associate an {@link IAction} (for example, "walk forward",
	 * "fire weapon", etc) with the activation of a specific
	 * component <I>of a specific controller</i>. 
	 * Associating an
	 * {@link IAction} with a controller and component causes
	 * {@link IInputManager#update(float)} to automatically invoke that
	 * {@link IAction}, by calling its
	 * {@link IAction#performAction(float,Event)} method, based on the
	 * associated {@link IInputManager.INPUT_ACTION_TYPE} (for example, each time the
	 * specified component on the specified controller changes state).
	 * 
	 * @param controllerName
	 * 				- the name of the controller, as returned by {@link Controller#getName()}
	 * 					 with which the action is to be associated
	 * @param component
	 *            - identifier of the component with which the action is to be
	 *            associated
	 * @param action
	 *            - the action which is to be associated with the specified
	 *            component
	 * @param actionType
	 *            -- indicates when the action is to be invoked in relation to
	 *            the occurrence of component events
	 * 
	 * @return the action which was previously associated with the specified
	 *         component, or null if there was no previously associated action.
	 */
	public IAction associateAction(String controllerName, Component.Identifier component,
			IAction action, INPUT_ACTION_TYPE actionType);
	
	/**
	 * <code>associateAction()</code> causes the <code>IInputManager</code>
	 * object to associate an {@link IAction} (for example, "walk forward",
	 * "fire weapon", etc) with the activation of a specific
	 * component <I>of a specific controller</i>. 
	 * Associating an
	 * {@link IAction} with a controller and component causes
	 * {@link IInputManager#update(float)} to automatically invoke that
	 * {@link IAction}, by calling its
	 * {@link IAction#performAction(float,Event)} method, based on the
	 * associated {@link IInputManager.INPUT_ACTION_TYPE} (for example, each time the
	 * specified component on the specified controller changes state).
	 * 
	 * @param controller
	 * 				- the controller with which the action is to be associated
	 * @param component
	 *            - identifier of the component with which the action is to be
	 *            associated
	 * @param action
	 *            - the action which is to be associated with the specified
	 *            component
	 * @param actionType
	 *            -- indicates when the action is to be invoked in relation to
	 *            the occurrence of component events
	 * 
	 * @return the action which was previously associated with the specified
	 *         component, or null if there was no previously associated action.
	 */
	public IAction associateAction(Controller controller, Component.Identifier component,
			IAction action, INPUT_ACTION_TYPE actionType);

	/**
	 * <code>getControllers()</code> returns an {@link ArrayList} of the
	 * controllers currently recognized as attached to the execution
	 * environment. The controllers are returned in the order in which the
	 * underlying implementation (currently JInput) returns them.
	 * 
	 * @return a list of the available device controllers
	 */
	public ArrayList<Controller> getControllers();
	
	/**
	 * Returns the name string for the Keyboard controller (if more than one keyboard is present,
	 * the first one is returned).  If no keyboard controller is found, null is returned.
	 */
	public String getKeyboardName();
	
	/**
	 * Returns the name string for the Mouse controller (if more than one mouse is present,
	 * the first one is returned).  If no mouse controller is found, null is returned.
	 */
	public String getMouseName();
	
	/**
	 * Returns the name string for the first gamepad controller.  If no gamepad controller is found, 
	 * null is returned.
	 */
	public String getFirstGamepadName();
	
	/**
	 * Returns the name string for the second gamepad controller (if less than two gamepad controllers
	 * are present, null is returned).
	 */
	public String getSecondGamepadName();
	
	/**
	 * Returns the {@link net.java.games.input.Controller} with the specified name, or null if
	 * no controller of the specified name is installed.
	 */
	public Controller getControllerByName(String name);
	
	
	/* NEW CODE */
	
	/**
	 * Returns the Keyboard controller (if more than one keyboard is present,
	 * the first one is returned).  If no keyboard controller is found, null is returned.
	 */
	public Controller getKeyboardController();
	
	/**
	 * Returns the <code>nth</code> occurrence of a keyboard {@link net.java.games.input.Controller}.
	 * If no keyboard controller is found, null is returned.
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a keyboard, or 2 for second occurrence of a keyboard.
	 */
	public Controller getKeyboardController(int n);
	
	/**
	 * Returns the first keyboard controller with <code>n</code> or more components. 
	 * If no such keyboard controller is found, null is returned.
	 * 
	 * @param n The minimum number of components the keyboard should have. 
	 */
	public Controller getKeyboardControllerWithNComponents(int n);
	
	/**
	 * Returns the Mouse controller (if more than one mouse is present,
	 * the first one is returned).  If no mouse controller is found, null is returned.
	 */
	public Controller getMouseController();
	
	/**
	 * Returns the <code>nth</code> occurrence of a Mouse {@link net.java.games.input.Controller}.
	 * If no mouse controller is found, null is returned.
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a mouse, or 2 for second occurrence of a mouse.
	 */
	public Controller getMouseController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Gamepad {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a gamepad, or 2 for second occurrence of a gamepad. 
	 */
	public Controller getGamepadController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a joystick {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a joystick, or 2 for second occurrence of a joystick. 
	 */
	public Controller getJoystickController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Fingerstick {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Fingerstick, or 2 for second occurrence of a Fingerstick. 
	 */
	public Controller getFingerstickController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Headtracker {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Headtracker, or 2 for second occurrence of a Headtracker. 
	 */
	public Controller getHeadtrackerController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Rudder {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Rudder, or 2 for second occurrence of a Rudder. 
	 */
	public Controller getRudderController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Trackball {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Trackball, or 2 for second occurrence of a Trackball. 
	 */
	public Controller getTrackballController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Trackpad {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Trackpad, or 2 for second occurrence of a Trackpad. 
	 */
	public Controller getTrackpadController(int n);
	
	/**
	 * Returns the <code>nth</code> occurrence of a Wheel {@link net.java.games.input.Controller}. 
	 * 
	 * @param n Occurrence number, such as 1 for first occurrence of a Wheel, or 2 for second occurrence of a Wheel. 
	 */
	public Controller getWheelController(int n);
	
	/**
	 * Prints a list of all connected controllers to the console.
	 * @param printComponents <code>TRUE</code> if all the components of the controller should also be printed. 
	 * <code>FALSE</code> if not. 
	 */
	public void printControllers(boolean printComponents);
	
	/**
	 * Prints a specific controllers information to the console.
	 * @param c The controller whose information is to be printed. 
	 * @param printComponents <code>TRUE</code> if all the components of the controller should also be printed. 
	 * <code>FALSE</code> if not.
	 */
	public void printController(Controller c, boolean printComponents);
}