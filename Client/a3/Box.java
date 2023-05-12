package a3;

import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Box extends GameObject
{

    private int id;

    public Box(int index, ObjShape o, TextureImage t, Vector3f loc)
    {
        super(GameObject.root(), o, t);
        id = index;
        setPosition(loc);
    }

    public void setPosition(Vector3f loc) { this.setLocalLocation(loc); }

    public int getID() { return id; }

    
}
