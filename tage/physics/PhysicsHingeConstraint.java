package tage.physics;

public interface PhysicsHingeConstraint {
	/**
	 * Returns the current angle between the two bodies
	 * @return Angle as a float
	 */
	public float getAngle();
	
	/**
	 * Returns the Axis that defines the hinge
	 * @return The hinge axis as a float array
	 */
	public float[] getAxis();

}
