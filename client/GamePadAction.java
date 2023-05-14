package client;
import tage.input.action.AbstractInputAction;
import tage.networking.client.ProtocolClient;
import net.java.games.input.Event;

public class GamePadAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;

    public GamePadAction (MyGame g, ProtocolClient c)
    {
        game = g;
        protClient = c;
    }

    @Override
    public void performAction(float time, Event e) {
        
        if (e.getValue() < -0.5)
        {
            game.getAvatar().moveObjFwd(e, game.getElapseTime());
        }
        else if (e.getValue() > 0.5)
        {
            game.getAvatar().moveObjBack(e, game.getElapseTime());
        }
        game.updateAvatarPhysicsObject();
        protClient.sendMoveMessage(game.getAvatar().getWorldLocation());
        protClient.sendAnimation("RUN");
    }
}
