package a3;

import java.io.IOException;
import java.net.InetAddress;    
import java.util.UUID;

import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID>
{

    public GameServerUDP(int localPort) throws IOException 
    {
        super(localPort, ProtocolType.UDP);
    }
    
    @Override
    public void processPacket(Object o, InetAddress senderIP, int sndPort)
    {
        String message = (String) o;
        String[] msgTokens = message.split(",");

        if (msgTokens.length > 0)
        {
            // case where server receives a JOIN message
            // format: join, localid
            if (msgTokens[0].compareTo("join") == 0)
            {
                try
                {
                    IClientInfo ci;
                    ci = getServerSocket().createClientInfo(senderIP, sndPort);
                    UUID clientID = UUID.fromString(msgTokens[1]);
                    addClient(ci, clientID);
                    sendJoinedMessage(clientID, true);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            // case where server receives a CREATE message
            // format: create, localid, x, y, z
            if (msgTokens[0].compareTo("create") == 0)
            {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendCreateMessage(clientID, pos);
                sendWantsDetailsMessage(clientID);
            }
            
            // case where server receives a BYE message
            // format: bye, localid
            if (msgTokens[0].compareTo("bye") == 0)
            {
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendByeMessage(clientID);
                removeClient(clientID);
            }

            // case where server receives a DETAILS-FOR message
            if (msgTokens[0].compareTo("dsfr") == 0)
            {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendWantsDetailsMessage(clientID);
            }

            // case where server receives a MOVE message
            if (msgTokens[0].compareTo("move") == 0)
            {
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendMoveMessage(clientID, pos);
            }

            // // NPC/AI
            
            // Case where server receives a CREATE NPC message

            if (msgTokens[0].compareTo("startNPC") == 0)
            {
                System.out.println("STARTING NPC");
                UUID clientID = UUID.fromString(msgTokens[1]);
                sendNPCstart(clientID);
            }

            if (msgTokens[0].compareTo("createNPC") == 0)
            {
                System.out.println("CREATTING NPCCCCCCCCCCCCCCCCCCC");
                UUID clientID = UUID.fromString(msgTokens[1]);
                String[] pos = {msgTokens[2], msgTokens[3], msgTokens[4]};
                sendCreateNPCmsg(clientID, pos);
            }

        }
    }

    public void sendJoinedMessage(UUID clientID, boolean success)
    {
        // format: join, success or join failure
        try 
        {
            String message = new String("join, ");
            if (success)
            {
                message += "success";
            }
            else
            {
                message += "failure";
            }
            sendPacket(message, clientID);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();    
        }
    }

    public void sendCreateMessage(UUID clientID, String[] position)
    {
        // format: create, remotedID, x, y, z
        try 
        {
            String message = new String("create, " +    clientID.toString());
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

    public void sndDetailsMsg(UUID clientID, UUID remoteID, String[] position)
    {
        try 
        {
            String message = new String("dsfr, " + clientID);
            message += ", " + remoteID;
            message += "," + position[0];
            message += "," + position[1];
            message += "," + position[2];
            sendPacket(message, clientID);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void sendWantsDetailsMessage(UUID clientID)
    {
        try 
        {
            String message = new String("wsds, " + clientID);
            sendPacket(message, clientID);
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }

    public void sendMoveMessage(UUID clientID, String[] position)
    {
        try 
        {
            String message = new String("wsds, " + clientID);
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

    public void sendByeMessage(UUID clientID)
    {
        try 
        {
            String message = new String("bye, " + clientID);
            forwardPacketToAll(message, clientID);    
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public void sendNPCstart(UUID clientID)
    {
        try
        {
            String message = new String("createNPC, " + clientID);
            forwardPacketToAll(message, clientID);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void sendCreateNPCmsg(UUID clientID, String[] position)
    {
        try 
        {
            String message = new String("createNPC, " +    clientID.toString());
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
