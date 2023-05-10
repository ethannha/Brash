package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class TurnLeftAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public TurnLeftAction (MyGame g, ProtocolClient p)
    {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().turnObjLeft(e, game.getElapseTime());
        game.updateAvatarPhysicsObject();
        protClient.sendRotateMessage(game.getAvatar().getLocalRotation());
        //System.out.println("UPDATING PHYSICS OBJECT MOVE LEFT");
    }
}
