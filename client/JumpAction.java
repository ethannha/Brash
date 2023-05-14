package client;

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
    private float[] linVel = {0.0f, 5.0f, 0.0f};


    public JumpAction(MyGame g, PhysicsObject aP, ProtocolClient pClient)
    {
        game = g;
        avatarP = aP;
        protClient = pClient;
    }


    @Override
    public void performAction(float time, Event evt) 
    {
        float keyValue = evt.getValue();
        if (keyValue > -0.2 && keyValue < 0.2) return; //deadzone

        //System.out.println("Transform VEL: " + avatarP.getTransform()[13]);

        // game.setRunning(true);

        if (game.getAvatarCanJump())
        {
            // Apply a force in the local y-direction of the avatar
            avatarP.setLinearVelocity(linVel);
            game.stopJumpAction();
            //System.out.println("Linear VEL: " + avatarP.getLinearVelocity()[0] + ", " + avatarP.getLinearVelocity()[1] + ", " + avatarP.getLinearVelocity()[2]);
            protClient.sendMoveMessage(game.getAvatar().getWorldLocation());   
            protClient.sendAnimation("JUMP");
        }
    }
}
