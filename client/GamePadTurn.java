package client;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class GamePadTurn extends AbstractInputAction
{
    private MyGame game;

    public GamePadTurn (MyGame g)
    {
        game = g;
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
    }
}
