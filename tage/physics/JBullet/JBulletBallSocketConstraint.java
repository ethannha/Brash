package tage.physics.JBullet;

import javax.vecmath.Vector3f;

import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.constraintsolver.Point2PointConstraint;

import tage.physics.PhysicsBallSocketConstraint;

/**
 * Defines JBullet ball socket constraint
 * @author Oscar Solorzano
 *
 */

public class JBulletBallSocketConstraint extends JBulletConstraint implements PhysicsBallSocketConstraint{
	private Point2PointConstraint p2pConstraint;
	public JBulletBallSocketConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB) {
		super(uid, bodyA, bodyB);
		RigidBody rbA = bodyA.getRigidBody();
		RigidBody rbB = bodyB.getRigidBody();
		float[] pivotInA = new float[]{0, 0, 0};
		float[] pivotInB = new float[]{(float) (bodyA.getTransform()[12]-bodyB.getTransform()[12]),(float) (bodyA.getTransform()[13]-bodyB.getTransform()[13]),(float) (bodyA.getTransform()[14]-bodyB.getTransform()[14])};
		p2pConstraint = new Point2PointConstraint(rbA, rbB, new Vector3f(pivotInA), new Vector3f(pivotInB));
		rbA.addConstraintRef(p2pConstraint);
		rbB.addConstraintRef(p2pConstraint);
	}
	/**
	 * Returns the JBullet specific ball socket constraint
	 * @return The ball socket constraint as a JBullet Point2PointConstraint
	 */
	public Point2PointConstraint getConstraint(){
		return p2pConstraint;
	}
}
