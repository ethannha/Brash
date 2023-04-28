package a3;

import org.joml.Vector3f;
import net.java.games.input.Event;
import tage.GameObject;
import tage.input.action.AbstractInputAction;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;

public class JumpAction extends AbstractInputAction 
{

    private MyGame game;
    private PhysicsEngine physicsEngine;
    private PhysicsObject avatarP;

    public JumpAction(MyGame g, PhysicsEngine p , PhysicsObject aP)
    {
        game = g;
        physicsEngine = p;
        avatarP = aP;
    }

    @Override
    public void performAction(float time, Event evt) 
    {
        float keyValue = evt.getValue();
        if (keyValue > -0.2 && keyValue < 0.2) return; //deadzone

        // Get the local up direction of the avatar
        Vector3f up = game.getAvatar().getLocalUpVector();
        
        
        game.setRunning(true);
        //avatarP.applyForce(0.0f, 5.0f, 0.0f, game.getAvatar().getWorldLocation().x(), game.getAvatar().getWorldLocation().y(), game.getAvatar().getWorldLocation().z());
        
        // Apply a force in the local y-direction of the avatar
        avatarP.applyForce(0.0f, 5.0f, 0.0f, up.x, up.y, up.z);
    }
}
