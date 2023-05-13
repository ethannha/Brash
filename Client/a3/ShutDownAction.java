package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class ShutDownAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public ShutDownAction (MyGame g, ProtocolClient p)
    {
        game = g;
        protClient = p;
    }

    @Override
    public void performAction(float time, Event e)
    {
        protClient.sendByeMessage(game.getPlayerScore());
        game.shutdown();
        System.exit(0);
    }
}
