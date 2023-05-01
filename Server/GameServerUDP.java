import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> 
{

	NPCcontroller npcCtrl;

	public GameServerUDP(int localPort, NPCcontroller npc) throws IOException 
	{	
		super(localPort, ProtocolType.UDP);
		npcCtrl = npc;
	}

	@Override
	public void processPacket(Object o, InetAddress senderIP, int senderPort)
	{
		String message = (String)o;
		String[] messageTokens = message.split(",");
		
		if(messageTokens.length > 0)
		{	// JOIN -- Case where client just joined the server
			// Received Message Format: (join,localId)
			if(messageTokens[0].compareTo("join") == 0)
			{	
				try 
				{	
					IClientInfo ci;					
					ci = getServerSocket().createClientInfo(senderIP, senderPort);
					UUID clientID = UUID.fromString(messageTokens[1]);
					addClient(ci, clientID);
					System.out.println("Join request received from - " + clientID.toString());
					sendJoinedMessage(clientID, true);
				} 
				catch (IOException e) 
				{	
					e.printStackTrace();
				}
			}
			
			// BYE -- Case where clients leaves the server
			// Received Message Format: (bye,localId)
			if(messageTokens[0].compareTo("bye") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				System.out.println("Exit request received from - " + clientID.toString());
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			
			// CREATE -- Case where server receives a create message (to specify avatar location)
			// Received Message Format: (create,localId,x,y,z)
			if(messageTokens[0].compareTo("create") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendCreateMessages(clientID, pos);
				sendWantsDetailsMessages(clientID);
			}
			
			// DETAILS-FOR --- Case where server receives a details for message
			// Received Message Format: (dsfr,remoteId,localId,x,y,z)
			if(messageTokens[0].compareTo("dsfr") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				UUID remoteID = UUID.fromString(messageTokens[2]);
				String[] pos = {messageTokens[3], messageTokens[4], messageTokens[5]};
				sendDetailsForMessage(clientID, remoteID, pos);
			}
			
			// MOVE --- Case where server receives a move message
			// Received Message Format: (move,localId,x,y,z)
			if(messageTokens[0].compareTo("move") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendMoveMessages(clientID, pos);
			}

			// NPC / AI

			// Case where server receives request for NPCs
			// Received message format: (needNPC, id)
			if (messageTokens[0].compareTo("needNPC") == 0)
			{
				System.out.println("Server got a needNPC message");
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendNPCstartMsg(clientID);
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
				System.out.println("SERVER GOT NPCINFOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			}

			if (messageTokens[0].compareTo("startNPC") == 0)
			{
				System.out.print("SERVER RECEIVE MESSAGE FROM CLIENT THAT IT WANTS TO START NPC");
				UUID clientID = UUID.fromString(messageTokens[1]);
			}
		}
	}

	// Informs the client who just requested to join the server if their if their 
	// request was able to be granted. 
	// Message Format: (join,success) or (join,failure)
	
	public void sendJoinedMessage(UUID clientID, boolean success)
	{	
		try 
		{	
			System.out.println("trying to confirm join");
			String message = new String("join,");
			if(success)
				message += "success";
			else
				message += "failure";
			sendPacket(message, clientID);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	// Informs a client that the avatar with the identifier remoteId has left the server. 
	// This message is meant to be sent to all client currently connected to the server 
	// when a client leaves the server.
	// Message Format: (bye,remoteId)
	
	public void sendByeMessages(UUID clientID)
	{	
		try 
		{	
			String message = new String("bye," + clientID.toString());
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	// Informs a client that a new avatar has joined the server with the unique identifier 
	// remoteId. This message is intended to be send to all clients currently connected to 
	// the server when a new client has joined the server and sent a create message to the 
	// server. This message also triggers WANTS_DETAILS messages to be sent to all client 
	// connected to the server. 
	// Message Format: (create,remoteId,x,y,z) where x, y, and z represent the position

	public void sendCreateMessages(UUID clientID, String[] position)
	{	
		try 
		{	String message = new String("create," + clientID.toString());
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
	
	// Informs a client of the details for a remote client�s avatar. This message is in response 
	// to the server receiving a DETAILS_FOR message from a remote client. That remote client�s 
	// message�s localId becomes the remoteId for this message, and the remote client�s message�s 
	// remoteId is used to send this message to the proper client. 
	// Message Format: (dsfr,remoteId,x,y,z) where x, y, and z represent the position.

	public void sendDetailsForMessage(UUID clientID, UUID remoteId, String[] position)
	{	
		try 
		{	
			String message = new String("dsfr," + remoteId.toString());
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
	
	// Informs a local client that a remote client wants the local client�s avatar�s information. 
	// This message is meant to be sent to all clients connected to the server when a new client 
	// joins the server. 
	// Message Format: (wsds,remoteId)
	
	public void sendWantsDetailsMessages(UUID clientID)
	{	
		try 
		{	
			String message = new String("wsds," + clientID.toString());	
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}
	
	// Informs a client that a remote client�s avatar has changed position. x, y, and z represent 
	// the new position of the remote avatar. This message is meant to be forwarded to all clients
	// connected to the server when it receives a MOVE message from the remote client.   
	// Message Format: (move,remoteId,x,y,z) where x, y, and z represent the position.

	public void sendMoveMessages(UUID clientID, String[] position)
	{	
		try 
		{	
			String message = new String("move," + clientID.toString());
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

	// NPC METHODS ===============================

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
    public void sendNPCstartMsg(UUID clientID)
    {
        try
        {
            String message = new String("startNPC," + clientID);
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
