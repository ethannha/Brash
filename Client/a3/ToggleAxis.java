package a3;

import java.awt.event.ActionEvent;

import net.java.games.input.Event;
import tage.GameObject;
import tage.input.action.AbstractInputAction;

public class ToggleAxis extends AbstractInputAction
{
    private GameObject x, y, z;
    private boolean toggle = true;

    public ToggleAxis(GameObject x, GameObject y, GameObject z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void performAction(float time, Event evt) {
        toggle = !toggle;
        
        if (toggle)
        {
            x.getRenderStates().enableRendering();
            y.getRenderStates().enableRendering();
            z.getRenderStates().enableRendering();
        }
        else
        {
            x.getRenderStates().disableRendering();
            y.getRenderStates().disableRendering();
            z.getRenderStates().disableRendering();
        }
    }
    
}
