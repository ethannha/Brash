import java.util.Random;

import tage.ai.behaviortrees.BTCompositeType;
import tage.ai.behaviortrees.BTSequence;
import tage.ai.behaviortrees.BehaviorTree;

public class NPCcontroller 
{
    private NPC npc;
    Random rn = new Random();
    BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
    boolean nearFlag = false;
    long thinkStartTime, tickStartTime, lastThinkUpdateTime, lastTickUpdateTime;
    GameServerUDP server;
    double criteria = 4.0;

    public void updateNPCs()
    {
        npc.updateLocation();
    }

    public void start(GameServerUDP s)
    {
        thinkStartTime = System.nanoTime();
        tickStartTime = System.nanoTime();
        lastThinkUpdateTime = thinkStartTime;
        lastTickUpdateTime = tickStartTime;
        server = s;
        setupNPCs();
        setupBehaviorTree();
        npcLoop();
    }

    public void setupNPCs()
    {
        npc = new NPC();
        npc.randomizeLocation(rn.nextInt(5), rn.nextInt(5));
    }

    public void npcLoop()
    {
        while (true)
        {
            long currentTime = System.nanoTime();
            float elapsedThinkMilliSecs = (currentTime - lastThinkUpdateTime) / (1000000.0f);
            float elapsedTickMilliSecs = (currentTime - lastTickUpdateTime) / (1000000.0f);

            // TICK
            if (elapsedTickMilliSecs >= 25.0f)
            {
                lastTickUpdateTime = currentTime;
                npc.updateLocation();
                server.sendNPCinfo();
            }

            // THINK
            if (elapsedThinkMilliSecs >= 250.0f)
            {
                lastThinkUpdateTime = currentTime;
                bt.update(elapsedThinkMilliSecs);
            }

            Thread.yield();

        }
    }

    public void setNearFlag(boolean c)
    {
        nearFlag = c;
    }

    public boolean getNearFlag()
    {
        //System.out.println("============================================= NPC NEAR FLAG: " + nearFlag);
        return nearFlag;
    }

    public NPC getNPC()
    {
        return npc;
    }

    public double getCriteria()
    {
        return criteria;
    }

    public void setupBehaviorTree()
    {
        bt.insertAtRoot(new BTSequence(10));
        bt.insertAtRoot(new BTSequence(20));
        
        bt.insert(20, new AvatarNear(server, this, npc, false));
    }
}
