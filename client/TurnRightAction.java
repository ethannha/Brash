package client;
import tage.input.action.AbstractInputAction;
import tage.networking.client.ProtocolClient;
import net.java.games.input.Event;

public class TurnRightAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public TurnRightAction (MyGame g, ProtocolClient p)
    {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().turnObjRight(e, game.getElapseTime());
        game.updateAvatarPhysicsObject();
        protClient.sendRotateMessage(game.getAvatar().getLocalRotation());
        //System.out.println("UPDATING PHYSICS OBJECT MOVE RIGHT");
    }
}
