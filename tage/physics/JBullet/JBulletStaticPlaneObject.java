package tage.physics.JBullet;

import com.bulletphysics.collision.shapes.StaticPlaneShape;
import javax.vecmath.Vector3f;

public class JBulletStaticPlaneObject extends JBulletPhysicsObject {
	
    private float[] up_vector;
    private float plane_constant;

    public JBulletStaticPlaneObject(int uid, double[] transform, float[] up_vector, float plane_constant) {

        super(uid, 0, transform, new StaticPlaneShape(new Vector3f(up_vector), plane_constant));
        this.up_vector = up_vector;
        this.plane_constant = plane_constant;
    }

}
