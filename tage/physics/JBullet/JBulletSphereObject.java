package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.SphereShape;

public class JBulletSphereObject extends JBulletPhysicsObject {
	
    private float radius;
    
    public JBulletSphereObject(int uid, float mass, double[] transform, float radius) {
   
    	super(uid, mass, transform, new SphereShape(radius));
        this.radius = radius;
    
    }

}
