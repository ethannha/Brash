package a3;
import tage.input.action.AbstractInputAction;
import net.java.games.input.Event;

public class FwdAction extends AbstractInputAction
{
    private MyGame game;

    public FwdAction(MyGame g)
    {
        game = g;
    }

    @Override
    public void performAction(float time, Event e)
    {
        game.getAvatar().moveObjFwd(e, game.getElapseTime());
    }   
}
