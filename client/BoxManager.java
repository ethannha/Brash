package client;

import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import tage.ObjShape;
import tage.TextureImage;
import tage.VariableFrameRateGame;

public class BoxManager 
{
    private MyGame game;
	private Vector<Box> boxs = new Vector<Box>();

    public BoxManager(VariableFrameRateGame vfrg)
    {
        game = (MyGame)vfrg;
    }

    public void createBoxObject(int index, Vector3f pos, Boolean boxStatus) throws IOException
    {
        System.out.println("adding box with ID --> " + index + "; Pos: " + pos.x() + ", " + pos.y() + ", " + pos.z());
		ObjShape s = game.getBoxShape();
		TextureImage t = game.getBoxTexture();
		Box newBox = new Box(index, s, t, pos, boxStatus);
		Matrix4f initialScale = (new Matrix4f()).scaling(0.5f);
		newBox.setLocalScale(initialScale);
		boxs.add(newBox);
    }

	public Vector<Box> getBoxList()
	{
		return boxs;
	}

    public void removeBox(int index)
	{	
		Box boxObj = findBox(index);
		if(boxObj != null)
		{	
			game.getEngine().getSceneGraph().removeGameObject(boxObj);
			boxs.remove(boxObj);
		}
		else
		{	
			System.out.println("tried to remove, but unable to find box in list");
		}
	}

    private Box findBox(int index)
	{	
		Box boxObj;
		Iterator<Box> it = boxs.iterator();
		while(it.hasNext())
		{	
			boxObj = it.next();
			if(boxObj.getID() == index)
			{	
				return boxObj;
			}
		}		
		return null;
	}
}
