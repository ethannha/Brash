package a3;

import org.joml.Vector3f;

import tage.GameObject;
import tage.ObjShape;
import tage.TextureImage;

public class Box extends GameObject
{

    private int id;
    private Boolean boxStatus;

    public Box(int index, ObjShape o, TextureImage t, Vector3f loc, Boolean boxStatus)
    {
        super(GameObject.root(), o, t);
        id = index;
        this.boxStatus = boxStatus;
        setPosition(loc);
    }

    public void setPosition(Vector3f loc) { this.setLocalLocation(loc); }

    public Vector3f getPosition() { return this.getWorldLocation(); }

    public boolean isBoxAlive()
    {
        return boxStatus;
    }

    public int getID() { return id; }

    
}
