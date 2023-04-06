package a3;
import tage.input.action.AbstractInputAction;
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
        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
    }   
}
