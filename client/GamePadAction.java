package client;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class GamePadAction extends AbstractInputAction
{
    private MyGame game;

    public GamePadAction (MyGame g)
    {
        game = g;
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
    }
}
