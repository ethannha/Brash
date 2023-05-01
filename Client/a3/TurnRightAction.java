package a3;
import tage.input.action.AbstractInputAction;
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
        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());

        game.updateAvatarPhysicsObject();
        //System.out.println("UPDATING PHYSICS OBJECT MOVE RIGHT");
    }
}
