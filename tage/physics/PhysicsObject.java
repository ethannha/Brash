package tage.physics;

public interface PhysicsObject {
	/**
	 * Returns the unique ID associated with this {@code IPhysicsObject}
	 * 
	 * @return This object's ID
	 */
	public int getUID();

	/**
	 * Sets the transform applied to this {@code IPhysicsObject} to the
	 * transform represented by the specified array of doubles.
	 * 
	 * @param transform
	 *            An array of 16 doubles representing a 4x4 matrix given in
	 *            column-major order
	 */
	public void setTransform(double[] transform);

	/**
	 * Returns a 16-element array of doubles representing the 4x4 transformation
	 * matrix for this object
	 * 
	 * @return The transform applied to this object, represented in column-major
	 *         form
	 */
	public double[] getTransform();

	/**
	 * Returns the friction coefficient associated with this
	 * {@code IPhysicsObject}
	 * 
	 * @return The object's friction coefficient
	 */
	public float getFriction();

	/**
	 * Sets this {@code IPhysicsObject}'s coefficient of friction to the
	 * specified value.
	 * 
	 * @param friction
	 *            A value between 0 and 1 representing the friction coefficient
	 */
	public void setFriction(float friction);

	/**
	 * Returns the "bounciness" value (also called the <I>restitution
	 * constant</i>) associated with this {@code IPhysicsObject}.
	 * 
	 * @return A value between 0 and 1 representing the object's bounciness
	 */
	public float getBounciness();

	/**
	 * Sets the bounciness (restitution constant) of this object to the
	 * specified value.
	 * 
	 * @param bounciness
	 *            A value between 0 and 1 specifying the object's bounciness
	 */
	public void setBounciness(float bounciness);

	/**
	 * Returns a float array giving the [x,y,z] components of this
	 * {@code IPhysicsObject}'s linear velocity.
	 * 
	 * @return The [x,y,z] components of the object's linear velocity
	 */
	public float[] getLinearVelocity();

	/**
	 * Sets the linear velocity of this {@code IPhysicsObject} to the specified
	 * values.
	 * 
	 * @param velocity
	 *            An array of 3 floats specifying the [x,y,z] linear velocity
	 *            for the object
	 */
	public void setLinearVelocity(float[] velocity);

	/**
	 * Returns a float array giving the [x,y,z] components of this
	 * {@code IPhysicsObject}'s angular velocity.
	 * 
	 * @return The [x,y,z] components of the object's angular velocity
	 */
	public float[] getAngularVelocity();

	/**
	 * Sets the angular velocity of this {@code IPhysicsObject} to the specified
	 * values.
	 * 
	 * @param velocity
	 *            An array of 3 floats specifying the [x,y,z] angular velocity
	 *            for the object
	 */
	public void setAngularVelocity(float[] velocity);

	/**
	 * Returns a flag indicating whether this {@code IPhysicsObject} is dynamic
	 * or static. Dynamic objects have mass and participate in physics solutions
	 * such as collisions.
	 * 
	 * @return Whether this object is dynamic or not
	 */
	public boolean isDynamic();

	/**
	 * Sets the sleep threshold for this physics object. The sleep threshold
	 * determines at what velocity an object is determined to have come to rest
	 * and thus is no longer updated by the physics engine until another object
	 * or force affects the object.
	 * 
	 * This can slightly improve performance and help with object having a
	 * slight vibrating effect when they should come to rest.
	 * 
	 * @param linearThreshold
	 * @param angularThreshold
	 */
	public void setSleepThresholds(float linearThreshold, float angularThreshold);

	/**
	 * Returns the linear sleep threshold.
	 * 
	 * @return the linear sleep threshold for this object
	 */
	public float getLinearSleepThreshold();

	/**
	 * Returns the angular sleep threshold.
	 * 
	 * @return the angular sleep threshold for this object
	 */
	public float getAngularSleepThreshold();

	/**
	 * Sets the damping for this physics object. The damping effectively applies
	 * an opposing force to the movement of this object.
	 * 
	 * @param linearDamping
	 * @param angularDamping
	 */
	public void setDamping(float linearDamping, float angularDamping);

	/**
	 * Returns the linear damping of this object.
	 * 
	 * @return the linear damping value for this object
	 */
	public float getLinearDamping();

	/**
	 * Returns the angular damping of this object.
	 * 
	 * @return the angular dampling value for this object
	 */
	public float getAngularDamping();
	
	/**
	 * Applies a force with the force vector (fx, fy, fz) at local position (px, py, pz).
	 * For example, applyForce(0,10,0,0,0,0) will apply a force of 10 in the Y direction
	 * in the local center of the object.
	 * 
	 * @param fx the X component of the force vector
	 * @param fy the Y component of the force vector
	 * @param fz the Z component of the force vector
	 * @param px the X component of the local position
	 * @param py the Y component of the local position
	 * @param pz the Z component of the local position
	 */
	public void applyForce(float fx, float fy, float fz, float px, float py, float pz);
	
	/**
	 * Applies a torque on the object. The torque vector direction defines the axis the objects force will be applied to
	 * and the magnitude of the vector defines the actual amount of torque.
	 * For example, applyTorque(0,50,0) will cause the object to spin with a force of 50 around the vector pointed in
	 * the Y direction using the right-handed rule.
	 * 
	 * @param fx the X component of the torque vector
	 * @param fy the Y component of the torque vector
	 * @param fz the Z component of the torque vector
	 */
	public void applyTorque(float fx, float fy, float fz);

}
