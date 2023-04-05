package tage.physics.JBullet;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.HingeConstraint;
import tage.physics.PhysicsHingeConstraint;

public class JBulletHingeConstraint extends JBulletConstraint implements PhysicsHingeConstraint {
	private HingeConstraint hingeConstraint;
	private float[] axis;

	public JBulletHingeConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB, float axisX, float axisY, float axisZ) {
		super(uid, bodyA, bodyB);
		RigidBody rigidA = bodyA.getRigidBody();
		RigidBody rigidB = bodyB.getRigidBody();
		float []pivotInA = new float[]{0, 0, 0};
		float []pivotInB = new float[]{(float) (bodyA.getTransform()[12]-bodyB.getTransform()[12]),(float) (bodyA.getTransform()[13]-bodyB.getTransform()[13]),(float) (bodyA.getTransform()[14]-bodyB.getTransform()[14])};
		axis = new float[]{axisX, axisY, axisZ};
		hingeConstraint = new HingeConstraint(rigidA, rigidB, new Vector3f(pivotInA), new Vector3f(pivotInB), new Vector3f(axisX, axisY, axisZ), new Vector3f(axisX, axisY, axisZ));
		rigidA.addConstraintRef(hingeConstraint);
		rigidB.addConstraintRef(hingeConstraint);
	}
	
	/**
	 * Returns the JBullet specific hinge constraint
	 * @return The hinge constraint as a JBullet HingeConstraint
	 */
	public HingeConstraint getConstraint(){
		return hingeConstraint;
	}

	@Override
	public float getAngle() {
		return hingeConstraint.getHingeAngle();
	}

	@Override
	public float[] getAxis() {
		return axis;
	}

}
