package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.ConeShapeZ;

public class JBulletConeZObject extends JBulletPhysicsObject {
	
	private float radius;
    private float height;
	
    public JBulletConeZObject(int uid, float mass, double[] transform, float radius, float height) {
    	

        super(uid, mass, transform, new ConeShapeZ(radius, height));
        this.radius = radius;
        this.height = height;
        
    }

}
