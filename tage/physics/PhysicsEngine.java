package tage.physics;

public interface PhysicsEngine {
	/**
	 * Constants defining the default gravity in the physics world. Note that
	 * implementations <I>are not constrained to utilize the default values
	 * defined here</i>.
	 */
	public static final float DEFAULT_GRAVITY_X = 0;
	public static final float DEFAULT_GRAVITY_Y = -10;
	public static final float DEFAULT_GRAVITY_Z = 0;

	/**
	 * Initializes the underlying physics engine. Typical initializations
	 * include providing an (empty) "physics world" along with a default
	 * collision handler, collision dispatcher, and constraint solver. Note
	 * however that <I>implementations are not constrained to perform any
	 * specific initializations when this method is called</I>; check the
	 * documentation for a specific implementation to determine what actual
	 * initial values are set by the implementation.
	 */
	public void initSystem();

	/**
	 * Set the gravity in the physics simulation to the specified [x,y,z]
	 * vector.
	 * 
	 * @param gravity_vector
	 *            The X,Y,Z components of the gravity vector in the physics
	 *            simulation
	 */
	public void setGravity(float[] gravity_vector);

	/**
	 * Add a Box object to the physics world.
	 * 
	 * @param uid
	 *            The unique ID of the box object
	 * @param mass
	 *            The mass of the box
	 * @param transform
	 *            An array of 4x4=16 floats defining the initial transform
	 *            applied to the box
	 * @param size
	 *            An array of 3 floats giving the dimensions [x,y,z] of the box
	 * 
	 * @return A reference to the box which was addded to the physics world
	 */
	public PhysicsObject addBoxObject(int uid, float mass, double[] transform,
			float[] size);

	public PhysicsObject addSphereObject(int uid, float mass,
			double[] transform, float radius);

	public PhysicsObject addConeObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addConeXObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addConeZObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addCapsuleObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addCapsuleXObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addCapsuleZObject(int uid, float mass,
			double[] transform, float radius, float height);

	public PhysicsObject addCylinderObject(int uid, float mass,
			double[] transform, float[] halfExtents);

	public PhysicsObject addCylinderXObject(int uid, float mass,
			double[] transform, float[] halfExtents);

	/**
	 * Adds a cylinder object to the physics world. This is the default cylinder
	 * used in the graphical portion of sage and is aligned length-wise with the
	 * Z-axis.
	 * 
	 * For convenience this object has altered getter/setter for it's transform
	 * to align with the sage graphical cylinder. By default cylinders are
	 * created centered at (0, 0, 0), the SAGE one however is created with the
	 * center of its base at (0, 0, 0).
	 * 
	 * NOTE: This does not change the other two cylinders available through this
	 * interface, they are still centered about (0, 0, 0).
	 * 
	 * @param uid
	 * @param mass
	 * @param transform
	 * @param halfExtents
	 * @return the cylinder object added to the physics world
	 */
	public PhysicsObject addCylinderZObject(int uid, float mass,
			double[] transform, float[] halfExtents);

	public PhysicsObject addStaticPlaneObject(int uid, double[] transform,
			float[] up_vector, float plane_constant);

	/**
	 * 
	 * @param uid
	 * 			The unique ID of the constraint
	 * @param bodyA
	 * 			The PhysicsObject of the first body
	 * @param bodyB
	 * 			The PhysicsObject of the second body
	 * @return  The newly created and added IPhysicsBallSocketConstraint
	 */
	public PhysicsBallSocketConstraint addBallSocketConstraint(int uid, PhysicsObject bodyA, PhysicsObject bodyB);
	
	/**
	 * Adds a hinge constraint to the world between two physics objects.
	 * 
	 * The pivots can be thought of as an offset in the body's local space. For example
	 * XYZ on bodyA being 0 1 0 means that the hinge will be 1 unit above body A.
	 * 
	 * The axis describes the hinge itself. For example having XYZ equal 0 1 0 means the
	 * hinge is facing the Y axis and objects constrained to this will move around that vector.
	 * 
	 * @param uid
	 *            The unique ID of the constraint
	 * @param bodyA
	 *            The PhysicsObject of the first body
	 * @param bodyB
	 *            The PhysicsObject of the second body
	 * @param axisX
	 * 			  The X value of the axis direction vector
	 * @param axisY
	 * 			  The Y value of the axis direction vector
	 * @param axisZ
	 * 			  The Z value of the axis direction vector
	 * @return The newly created and added IPhysicsHingeConstraint
	 */
	public PhysicsHingeConstraint addHingeConstraint(int uid, PhysicsObject bodyA, PhysicsObject bodyB, float axisX, float axisY, float axisZ);
	
	
	/**
	 * Removes the {@link PhysicsObject} with the specified UID from the
	 * physics world, if it exists in the world.
	 * 
	 * @param uid
	 *            The ID of the object to be removed
	 */
	public void removeObject(int uid);

	/**
	 * Forces the physics world to advance (that is, steps the physics
	 * simulation) by the specified amount of time, given in milliseconds.
	 */
	public void update(float milliseconds);

	/**
	 * Returns a unique ID used to identify physics objects.
	 */
	public int nextUID();

}
