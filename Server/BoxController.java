import org.joml.Vector3f;

public class BoxController 
{
    private Box box;
    GameServerUDP server;
    long thinkStartTime, tickStartTime, lastThinkUpdateTime, lastTickUpdateTime;
    double criteria = 1.0;

    public void start(GameServerUDP s)
    {
        thinkStartTime = System.nanoTime();
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStartTime;
        server = s;
        setupBox();
        //boxLoop();
    }

    public Box getBox()
    {
        return box;
    }

    private void setupBox()
    {
        box = new Box();
        box.setPosition(new Vector3f(0.0f, 0.65f, -5.0f));
    }
}
