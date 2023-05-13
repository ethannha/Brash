package tage.physics.JBullet;

import tage.physics.PhysicsConstraint;
import tage.physics.PhysicsObject;

public abstract class JBulletConstraint implements PhysicsConstraint {
	private int uid;
	private JBulletPhysicsObject bodyA;
	private JBulletPhysicsObject bodyB;
	
	public JBulletConstraint(int uid, JBulletPhysicsObject bodyA, JBulletPhysicsObject bodyB){
		this.uid=uid;
		this.bodyA=bodyA;
		this.bodyB=bodyB;
	}
	@Override
	public PhysicsObject getBodyA() {
		return bodyA;
	}

	@Override
	public PhysicsObject getBodyB() {
		return bodyB;
	}
	@Override
	public int getUID() {
		return uid;
	}

}
