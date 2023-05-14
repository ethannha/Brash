package client;
import tage.input.action.AbstractInputAction;
import tage.networking.client.ProtocolClient;
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
        game.updateAvatarPhysicsObject();
        //System.out.println("UPDATING PHYSICS OBJECT MOVE FORWARD");
        game.setAnimationName("RUN");
        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
        protClient.sendAnimation("RUN");
    }   
}
