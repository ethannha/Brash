package tage.nodeControllers;
import org.joml.Matrix4f;

import tage.GameObject;
import tage.NodeController;


/**
* An AttachController is a node controller that, when enabled, causes any object
* it is attached to to attach itself to the avatar. It essentailly makes it so that
* the object becomes a child of the avatar GameObject, creating a hierarchical object
* with the avatar.
* @author Steven Wang Ho
*/
public class AttachController extends NodeController
{
    private GameObject avatar;

    /** Creates the attach controller by getting a GameObject that the target(s) will attach to */
    public AttachController(GameObject avatar)
    {
        this.avatar = avatar;
    }

    /** This is called automatically by the RenderSystem (via SceneGraph) once per frame
	*   during display().  It is for engine use and should not be called by the application.
	*/
    @Override
    public void apply(GameObject t) 
    {   
        Matrix4f intialTranslation = (new Matrix4f()).translation(0, 1, 0);
        t.setLocalTranslation(intialTranslation);
        t.setParent(avatar);
        t.propagateTranslation(true);
        t.propagateRotation(true);
        t.propagateScale(false);
        t.applyParentRotationToPosition(true);
        t.getRenderStates().setTiling(1);
    }
}
