import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import tage.networking.server.GameConnectionServer;

public class GameAIServerUDP extends GameConnectionServer<UUID>
{
    NPCcontroller npcCtrl;

    public GameAIServerUDP(int localPort, NPCcontroller npc) throws IOException
    {
        super(localPort, ProtocolType.UDP);
        npcCtrl = npc;
    }

    // ========================================

    @Override
    public void processPacket(Object o, InetAddress senderIP, int port)
    {
        String message = (String)o;
		String[] messageTokens = message.split(",");

        // Case where server receives request for NPCs
        // Received message format: (needNPC, id)
        if (messageTokens[0].compareTo("needNPC") == 0)
        {
            System.out.println("Server got a needNPC message");
            UUID clientID = UUID.fromString(messageTokens[1]);
            sendNPCstart(clientID);
        }

        // Case where server receies notice that an avatar is close to the NPC
        // Received message format: (isnear, id)
        if (messageTokens[0].compareTo("isnear") == 0)
        {
            UUID clientID = UUID.fromString(messageTokens[1]);
            handleNearTiming(clientID);
        }

        if (messageTokens[0].compareTo("npcinfo") == 0)
        {
        }

        if (messageTokens[0].compareTo("startNPC") == 0)
        {
            System.out.print("SERVER RECEIVE MESSAGE FROM CLIENT THAT IT WANTS TO START NPC");
            UUID clientID = UUID.fromString(messageTokens[1]);
        }
    }

    public void handleNearTiming(UUID clientID)
    {
        npcCtrl.setNearFlag(true);
    }

    // -- additional protocol for NPCs ---

    public void sendCheckForAvatarNear()
    {
        try 
        {
            String message = new String("isnr");
            message += "," + (npcCtrl.getNPC()).getX();
            message += "," + (npcCtrl.getNPC()).getY();
            message += "," + (npcCtrl.getNPC()).getZ();
            message += "," + (npcCtrl.getCriteria());
            sendPacketToAll(message);    
        } 
        catch (IOException e) 
        {
            System.out.println("Couldnt send message");
            e.printStackTrace();
        }
    }

    public void sendNPCinfo()
    {
        try 
        {
            String message = new String("npcinfo");
            message += "," + (npcCtrl.getNPC()).getX();
            message += "," + (npcCtrl.getNPC()).getY();
            message += "," + (npcCtrl.getNPC()).getZ();
            sendPacketToAll(message);
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
    public void sendNPCstart(UUID clientID)
    {
        try
        {
            String message = new String("createNPC," + clientID);
            sendPacket(message, clientID);
        }
        catch (IOException e)
        {
            System.out.println("STARTING NPC ERROR");
        }
    }

    // ----------- SENDING NPC MESSAGES -----------------
    // informs clients of the whereabouts of the NPCs
    public void sendCreateNPCmsg(UUID clientID, String[] position)
    {
        try 
        {
            System.out.println("Server telling clients about an NPC");
            String message = new String("createNPC," + clientID.toString());
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            forwardPacketToAll(message, clientID);   
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }


}