package a3;

import org.joml.Vector3f;
import net.java.games.input.Event;
import tage.GameObject;
import tage.input.action.AbstractInputAction;
import tage.physics.PhysicsEngine;
import tage.physics.PhysicsObject;
import tage.shapes.AnimatedShape;

public class JumpAction extends AbstractInputAction 
{

    private MyGame game;
    private PhysicsObject avatarP;
    private ProtocolClient protClient;
    private float forceApplied = 8.0f;


    public JumpAction(MyGame g, PhysicsObject aP, ProtocolClient pClient)
    {
        game = g;
        avatarP = aP;
        protClient = pClient;
    }

    protected void reverseForce()
    {
        forceApplied = -8.0f;
    }

    protected void revertForce()
    {
        forceApplied = 8.0f;
    }

    @Override
    public void performAction(float time, Event evt) 
    {
        float keyValue = evt.getValue();
        if (keyValue > -0.2 && keyValue < 0.2) return; //deadzone

        System.out.println("Transform VEL: " + avatarP.getTransform()[13]);
        
        // Get the local up direction of the avatar
        Vector3f up = game.getAvatar().getLocalUpVector();

        if (avatarP.getTransform()[13] > 3.0f)
        {
            reverseForce();
            //System.out.println("REVERSING IN THE OPPOSITE WAY");
        }
        else if (avatarP.getTransform()[13] <= 0.0f)
        {
            revertForce();
            //System.out.println("REVERSING WHEN HITTING 0");
        }

        game.setRunning(true);

        // Apply a force in the local y-direction of the avatar
        avatarP.applyForce(0.0f, forceApplied, 0.0f, up.x, up.y, up.z);

        if (!game.getAvatarAnimatedShape().isPlayingAnimation("JUMP")) {
            game.playGrassSound();;
            game.getAvatarAnimatedShape().stopAnimation();
            game.getAvatarAnimatedShape().playAnimation("JUMP", 0.5f, AnimatedShape.EndType.NONE, 0);
        }

        //System.out.println("Linear VEL: " + avatarP.getLinearVelocity()[0] + ", " + avatarP.getLinearVelocity()[1] + ", " + avatarP.getLinearVelocity()[2]);
        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
    }
}
