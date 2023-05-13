package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.ConeShapeX;

public class JBulletConeXObject extends JBulletPhysicsObject {
	
	private float radius;
    private float height;
	
    public JBulletConeXObject(int uid, float mass, double[] transform, float radius, float height) {
    	

        super(uid, mass, transform, new ConeShapeX(radius, height));
        this.radius = radius;
        this.height = height;
        
    }

}
