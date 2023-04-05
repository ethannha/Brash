package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class BackAction extends AbstractInputAction
{
    private MyGame game;

    public BackAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().moveObjBack(e, game.getElapseTime());
    }   
}
