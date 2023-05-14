package client;

import java.util.Iterator;
import java.util.Vector;

import net.java.games.input.Event;
import tage.input.action.AbstractInputAction;

public class PunchAction extends AbstractInputAction
{
    private MyGame game;
    private ProtocolClient protClient;


    public PunchAction(MyGame g, ProtocolClient pc)
    {
        game = g;
        protClient = pc;
    }

    @Override
    public void performAction(float time, Event evt) 
    {
        game.playHitSound();

        Vector<Box> boxList = game.getBoxManager().getBoxList();
        Iterator<Box> it = boxList.iterator();

        while(it.hasNext())
        {
            Box b = it.next();

            if (game.getAvatar().getWorldLocation().distance(b.getLocalLocation().x(), b.getLocalLocation().y(), b.getLocalLocation().z()) < 1.1f)
            {
                game.getBoxManager().removeBox(b.getID());
                protClient.sendRemoveBoxObject(b.getID());
                game.increasePlayerScore();

                if (game.getGhostManager().hasHighestPlayerScore(game.getPlayerScore()))
                {
                    game.setCrownAttach(true);
                    game.toggleAttachController();
                    game.playCollectSound();
                }

                break;
            }
        }

        protClient.sendAnimation("PUNCHR");
    }
    
}
