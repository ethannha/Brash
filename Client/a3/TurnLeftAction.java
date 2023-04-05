package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class TurnLeftAction extends AbstractInputAction
{
    private MyGame game;

    public TurnLeftAction (MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().turnObjLeft(e, game.getElapseTime());
    }
}
