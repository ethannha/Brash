package tage.physics;

public interface PhysicsConstraint {
	/**
	 * Returns the unique ID associated with this {@code IPhysicsConstraint}
	 * 
	 * @return This object's ID
	 */
	public int getUID();
	/**
	 * Returns the first body contained in the constraint
	 * @return IPhysicsObject in the "A" spot
	 */
	public PhysicsObject getBodyA();
	
	/**
	 * Returns the second body contained in the constraint
	 * @return IPhysicsObject in the "B" spot
	 */
	public PhysicsObject getBodyB();

}
