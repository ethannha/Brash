package a3;
import tage.input.action.AbstractInputAction;
import tage.shapes.AnimatedShape;
import net.java.games.input.Event;

public class FwdAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public FwdAction(MyGame g, ProtocolClient p)
    {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().moveObjFwd(e, game.getElapseTime());
        game.getAvatarAnimatedShape().stopAnimation();
        game.getAvatarAnimatedShape().playAnimation("RUN", 0.75f, AnimatedShape.EndType.NONE, 0);

        game.updateAvatarPhysicsObject();
        //System.out.println("UPDATING PHYSICS OBJECT MOVE FORWARD");

        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
    }   
}
