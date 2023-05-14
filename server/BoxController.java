package server;

import java.util.Random;
import java.util.Vector;

import org.joml.Vector3f;

public class BoxController 
{
    private Box box;
    public Vector<Box> boxList = new Vector<Box>();
    GameServerUDP server;
    long thinkStartTime, tickStartTime, lastThinkUpdateTime, lastTickUpdateTime;
    double criteria = 1.0;
    int boxAmount = 15;
	Random rand = new Random();

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

    public Box getBoxLocation(int i)
    {
        return boxList.get(i);
    }

    public int getBoxAmount()
    {
        return boxAmount;
    }

    private void setupBox()
    {
        for (int i = 0; i < boxAmount; i++)
        {
            box = new Box(i);
            box.setPosition(new Vector3f((float)(rand.nextInt(20) + (-rand.nextInt(20))), 1.0f, (-rand.nextInt(20))));
            boxList.add(i, box);
        }
        
    }
}
