package server;

import org.joml.Vector3f;

public class Box
{
    Vector3f boxLocation;
    Boolean boxStatus = true;

    public Box()
    {
        boxLocation = new Vector3f();
    }

    public void setPosition(Vector3f loc) { boxLocation = loc; }
    public void setBoxStatus(Boolean c) { boxStatus = c; }
    public Vector3f getPosition() { return boxLocation; }
    public Boolean getBoxStatus() { return boxStatus; }
}
