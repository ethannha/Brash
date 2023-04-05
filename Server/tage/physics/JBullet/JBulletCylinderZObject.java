package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.CylinderShapeZ;
import javax.vecmath.Vector3f;

public class JBulletCylinderZObject extends JBulletPhysicsObject {
	
    private float[] halfExtents;

    public JBulletCylinderZObject(int uid, float mass, double[] transform, float[] halfExtents) {

        super(uid, mass, transform, new CylinderShapeZ(new Vector3f(halfExtents)));
        this.halfExtents = halfExtents;
    }

}
