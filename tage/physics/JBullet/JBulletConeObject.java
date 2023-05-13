package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.ConeShape;

public class JBulletConeObject extends JBulletPhysicsObject {
	
	private float radius;
    private float height;
	
    public JBulletConeObject(int uid, float mass, double[] transform, float radius, float height) {
    	

        super(uid, mass, transform, new ConeShape(radius, height));
        this.radius = radius;
        this.height = height;
        
    }

}
