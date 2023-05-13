package server;

import tage.ai.behaviortrees.BTCondition;

public class AvatarNear extends BTCondition
{
    NPC npc;
    NPCcontroller npcc;
    GameServerUDP server;

    public AvatarNear(GameServerUDP s, NPCcontroller c, NPC n, boolean toNegate)
    {
        super(toNegate);
        server = s;
        npcc = c;
        npc = n;
    }

    protected boolean check()
    {
        server.sendCheckForAvatarNear();
        System.out.println("AVATAR IS NEARRRRRR");
        return npcc.getNearFlag();
    }
}
