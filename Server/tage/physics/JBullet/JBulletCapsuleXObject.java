package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.CapsuleShapeX;

public class JBulletCapsuleXObject extends JBulletPhysicsObject{
	
	private float radius;
	private float height;

    public JBulletCapsuleXObject(int uid, float mass, double[] transform, float radius, float height)
    {

        super(uid, mass, transform, new CapsuleShapeX(radius, height));
        this.radius = radius;
        this.height = height;
        
    }

}
