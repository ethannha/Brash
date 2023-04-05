package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.CapsuleShapeZ;

public class JBulletCapsuleZObject extends JBulletPhysicsObject {
    private float radius;
    private float height;

    public JBulletCapsuleZObject(int uid, float mass, double[] transform, float radius, float height) {

        super(uid, mass, transform, new CapsuleShapeZ(radius, height));
        this.radius = radius;
        this.height = height;
        
    }
}
