package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class TurnRightAction extends AbstractInputAction
{
    private MyGame game;

    public TurnRightAction (MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().turnObjRight(e, game.getElapseTime());
    }
}
