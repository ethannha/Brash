package tage.nodeControllers;
import tage.*;
import org.joml.*;

/**
* A RotationController is a node controller that, when enabled, causes any object
* it is attached to to rotate in place around the tilt axis specified.
* @author Scott Gordon
*/
public class RotationController extends NodeController
{
	private Vector3f rotationAxis = new Vector3f(0.0f, 1.0f, 0.0f);
	private float rotationSpeed = 1.0f;
	private Matrix4f curRotation, rotMatrix, newRotation;
	private Engine engine;

	/** Creates a rotation controller with vertical axis, and speed=1.0. */
	public RotationController() { super(); }

	/** Creates a rotation controller with rotation axis and speed as specified. */
	public RotationController(Engine e, Vector3f axis, float speed)
	{	super();
		rotationAxis = new Vector3f(axis);
		rotationSpeed = speed;
		engine = e;
		rotMatrix = new Matrix4f();
	}

	/** sets the rotation speed when the controller is enabled */
	public void setSpeed(float s) { rotationSpeed = s; }

	/** This is called automatically by the RenderSystem (via SceneGraph) once per frame
	*   during display().  It is for engine use and should not be called by the application.
	*/
	public void apply(GameObject go)
	{	float elapsedTime = super.getElapsedTime();
		curRotation = go.getLocalRotation();
		float rotAmt = elapsedTime * rotationSpeed;
		rotMatrix.rotation(rotAmt, rotationAxis);
		newRotation = curRotation.mul(rotMatrix);
		go.setLocalRotation(newRotation);
	}
}