package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.CylinderShapeX;
import javax.vecmath.Vector3f;

public class JBulletCylinderXObject extends JBulletPhysicsObject {
	
    private float[] halfExtents;

    public JBulletCylinderXObject(int uid, float mass, double[] transform, float[] halfExtents) {

        super(uid, mass, transform, new CylinderShapeX(new Vector3f(halfExtents)));
        this.halfExtents = halfExtents;
    }

}
