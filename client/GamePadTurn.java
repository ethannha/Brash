package client;
import tage.input.action.AbstractInputAction;
import tage.networking.client.ProtocolClient;
import net.java.games.input.Event;

public class GamePadTurn extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public GamePadTurn (MyGame g, ProtocolClient c)
    {
        game = g;
        protClient = c;
    }

    @Override
    public void performAction(float time, Event e) {

        if (e.getValue() < -0.5)
        {
            game.getAvatar().turnObjLeft(e, game.getElapseTime());
        }
        else if (e.getValue() > 0.5)
        {
            game.getAvatar().turnObjRight(e, game.getElapseTime());
        }
        protClient.sendRotateMessage(game.getAvatar().getLocalRotation());

    }
}
