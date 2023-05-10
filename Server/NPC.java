import org.joml.Vector3f;

public class NPC 
{
    double locationX, locationY, locationZ;
    float dir = 0.1f;
    double size = 1.0;
    boolean seePlayer = false;
    Vector3f targetLocation;
    float speed = 0.01f;
    Vector3f npcLocation;


    public NPC()
    {
        locationX = 0.0;
        locationY = 0.0;
        locationZ = 0.0;
        npcLocation = new Vector3f();
    }

    public void randomizeLocation(int seedX, int seedZ)
    {
        npcLocation.x = (float)(((double)seedX) / 4.0 - 5.0);
        npcLocation.y = 0;
        npcLocation.z = -2;
    }

    public double getX() { return npcLocation.x; }
    public double getY() { return npcLocation.y; }
    public double getZ() { return npcLocation.z; }
    public void getBig() { size = 5.0; }
    public void getSmall() { size = 3.0; }
    public double getSize() { return size; }

    public void setTargetLocation(Vector3f playerPos)
    {
        targetLocation = playerPos;
    }

    public void setSeePlayer(boolean c)
    {
        seePlayer = c;
    }

    public void updateLocation()
    {
        if (seePlayer)
        {
            Vector3f direction = new Vector3f();
            targetLocation.sub(npcLocation, direction).normalize();
            direction.mul(speed);
            npcLocation.add(direction);
        }
        else
        {
            if (npcLocation.x > 10) dir = -0.1f;
            if (npcLocation.x < -10) dir = 0.1f;
            npcLocation.add(dir, 0, 0);
        }
        
    }

}
