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

        // Set the maximum height the avatar can jump
        float maxJumpHeight = 5.0f;
        // Keep track of the current jump height
        float currentJumpHeight = 0.0f;

        // game.getAvatar().isOnGround() && // need this for restricting player jump
        if (currentJumpHeight < maxJumpHeight) {
            // Get the local up direction of the avatar
            Vector3f up = game.getAvatar().getLocalUpVector();
            game.setRunning(true);
            // Apply a force in the local y-direction of the avatar
            avatarP.applyForce(0.0f, 8.0f, 0.0f, up.x, up.y, up.z);
            // Increase the current jump height by the amount of force applied
            currentJumpHeight += 20.0f * Math.sqrt(up.y * up.y + up.z * up.z);
        }
    }
}
