import org.joml.Vector3f;

public class Box
{
    Vector3f boxLocation;

    public Box()
    {
        boxLocation = new Vector3f();
    }

    public void setPosition(Vector3f loc) { boxLocation = loc; }
    public Vector3f getPosition() { return boxLocation; }
    public void boxAlive()
    {
        System.out.println("BOX UPDATEEEEEEEEEEEEEEEEEEE");
    }
}
