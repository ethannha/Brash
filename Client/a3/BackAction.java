package a3;
import tage.input.action.AbstractInputAction;
import tage.shapes.AnimatedShape;
import net.java.games.input.Event;

public class BackAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public BackAction(MyGame g, ProtocolClient p)
    {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().moveObjBack(e, game.getElapseTime());
        game.getAvatarAnimatedShape().stopAnimation();
        game.getAvatarAnimatedShape().playAnimation("RUN", 0.75f, AnimatedShape.EndType.NONE, 0);

        game.updateAvatarPhysicsObject();
        //System.out.println("UPDATING PHYSICS OBJECT MOVE BACKWARD");

        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
    }   
}
