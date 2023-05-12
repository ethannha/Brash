import org.joml.Vector3f;

public class BoxController 
{
    private Box box;
    GameServerUDP server;
    long thinkStartTime, tickStartTime, lastThinkUpdateTime, lastTickUpdateTime;
    double criteria = 1.0;
    int boxAmount = 5;

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

    public int getBoxAmount()
    {
        return boxAmount;
    }

    private void setupBox()
    {
        box = new Box();
        box.setPosition(new Vector3f(-5.0f, 1.0f, 0.0f));
    }
}
