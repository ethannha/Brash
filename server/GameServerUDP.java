package server;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import org.joml.Vector3f;

import tage.networking.server.GameConnectionServer;
import tage.networking.server.IClientInfo;

public class GameServerUDP extends GameConnectionServer<UUID> 
{

	NPCcontroller npcCtrl;
	BoxController boxCtrl;

	public GameServerUDP(int localPort, NPCcontroller npc, BoxController box) throws IOException 
	{	
		super(localPort, ProtocolType.UDP);
		npcCtrl = npc;
		boxCtrl = box;
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
				int score = Integer.parseInt(messageTokens[2]);
				System.out.println("Exit request received from - " + clientID.toString() + "; their score was: " + score);
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			
			// CREATE -- Case where server receives a create message (to specify avatar location)
			// Received Message Format: (create,localId,x,y,z)
			if(messageTokens[0].compareTo("create") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				int score = Integer.parseInt(messageTokens[5]);
				boolean crownOn = Boolean.parseBoolean(messageTokens[6]);
				sendCreateMessages(clientID, pos, score, crownOn);
				sendWantsDetailsMessages(clientID);
			}
			
			// DETAILS-FOR --- Case where server receives a details for message
			// Received Message Format: (dsfr,remoteId,localId,x,y,z)
			if(messageTokens[0].compareTo("dsfr") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				UUID remoteID = UUID.fromString(messageTokens[2]);
				String[] pos = {messageTokens[3], messageTokens[4], messageTokens[5]};
				int score = Integer.parseInt(messageTokens[6]);
				boolean crownOn = Boolean.parseBoolean(messageTokens[7]);
				sendDetailsForMessage(clientID, remoteID, pos, score, crownOn);
			}
			
			// MOVE --- Case where server receives a move message
			// Received Message Format: (move,localId,x,y,z)
			if(messageTokens[0].compareTo("move") == 0)
			{	
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendMoveMessages(clientID, pos);
			}

			if (messageTokens[0].compareTo("animate") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);
				String aniName = messageTokens[2];
				sendAnimation(clientID, aniName);
			}

			if (messageTokens[0].compareTo("rotate") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] rotatePos = {messageTokens[2], messageTokens[3], messageTokens[4], messageTokens[5]};
				sendRotateMessage(clientID, rotatePos);
			}

			if (messageTokens[0].compareTo("createPS") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendCreatePlayerScore(clientID);
			}

			if (messageTokens[0].compareTo("updatePS") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);
				int score = Integer.parseInt(messageTokens[2]);
				sendUpdatePlayerScore(clientID, score);
			}

			// NPC / AI

			// Case where server receives request for NPCs
			// Received message format: (needNPC, id)
			if (messageTokens[0].compareTo("needNPC") == 0)
			{
				System.out.println("Server got a needNPC message");
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendNPCstart(clientID);
			}

			if (messageTokens[0].compareTo("startNPC") == 0)
			{
				System.out.print("SERVER RECEIVE MESSAGE FROM CLIENT THAT IT WANTS TO START NPC");
				UUID npcID = UUID.fromString(messageTokens[1]);
				sendNPCstart(npcID);
			}

			if (messageTokens[0].compareTo("createNPC") == 0)
			{
				System.out.print("SERVER RECEIVE MESSAGE FROM CLIENT THAT IT CREATED NPC");
				UUID npcID = UUID.fromString(messageTokens[1]);
				String[] pos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				sendCreateNPCmsg(npcID, pos);
			}

			// Case where server receies notice that an avatar is close to the NPC
			// Received message format: (isnear, id)
			if (messageTokens[0].compareTo("isAvnr") == 0)
			{
				Boolean isNear = Boolean.parseBoolean(messageTokens[1]);
				String[] playerPos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				handleAvatarNearTiming(playerPos, isNear);
			}

			if (messageTokens[0].compareTo("isGhostnr") == 0)
			{
				Boolean isNear = Boolean.parseBoolean(messageTokens[1]);
				String[] ghostPos = {messageTokens[2], messageTokens[3], messageTokens[4]};
				handleGhostNearTiming(ghostPos, isNear);
			}

			if (messageTokens[0].compareTo("npcinfo") == 0)
			{
				System.out.println("SERVER GOT NPCINFOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
			}

			// Box message from client
			
			if (messageTokens[0].compareTo("needBox") == 0)
			{
				//System.out.println("RECEIVED BOX NEED MESSAGE FROM CLIENT");
				UUID clientID = UUID.fromString(messageTokens[1]);
				sendBoxInfo(clientID);
			}

			if (messageTokens[0].compareTo("rmvbox") == 0)
			{
				System.out.println("RECEIVED FROM " + messageTokens[1] + " TO REMOVE BOX: " + messageTokens[2]);
				UUID clientID = UUID.fromString(messageTokens[1]);
				int boxID = Integer.parseInt(messageTokens[2]);
				sendRemoveBox(clientID, boxID);
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

	public void sendCreateMessages(UUID clientID, String[] position, int score, boolean crownOn)
	{	
		try 
		{	String message = new String("create," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			message += "," + score;
			message += "," + crownOn;
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

	public void sendDetailsForMessage(UUID clientID, UUID remoteId, String[] position, int score, boolean crownOn)
	{	
		try 
		{	
			String message = new String("dsfr," + remoteId.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			message += "," + score;
			message += "," + crownOn;
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

	public void sendAnimation(UUID clientID, String aniName)
	{
		try
		{
			String message = new String("animate," + clientID.toString());
			message += "," + aniName;
			forwardPacketToAll(message, clientID);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendRotateMessage(UUID clientID, String[] rotatePos)
	{
		try 
		{	
			String message = new String("rotate," + clientID.toString());
			message += "," + rotatePos[0];
			message += "," + rotatePos[1];
			message += "," + rotatePos[2];
			message += "," + rotatePos[3];
			//System.out.println("=============================== SERVER MESSAGE: " + message);
			forwardPacketToAll(message, clientID);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}

	public void sendCreatePlayerScore(UUID clientID)
	{
		try
		{
			String message = new String("createPS," + clientID.toString());
			forwardPacketToAll(message, clientID);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendUpdatePlayerScore(UUID clientID, int score)
	{
		try
		{
			String message = new String("updatePS," + clientID.toString());
			message += "," + score;
			forwardPacketToAll(message, clientID);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// NPC METHODS ===============================

	public void handleAvatarNearTiming(String[] playerPos, Boolean isNear)
    {
        npcCtrl.setNearFlag(isNear);
		(npcCtrl.getNPC()).setSeePlayer(isNear);
		Vector3f player = new Vector3f(
			Float.parseFloat(playerPos[0]),
			Float.parseFloat(playerPos[1]),
			Float.parseFloat(playerPos[2])
		);
		(npcCtrl.getNPC()).setTargetLocationAsAv(player);
    }

	public void handleGhostNearTiming(String[] ghostPos, Boolean isNear)
    {
        npcCtrl.setNearFlag(isNear);
		(npcCtrl.getNPC()).setSeeGhost(isNear);
		Vector3f ghost = new Vector3f(
			Float.parseFloat(ghostPos[0]),
			Float.parseFloat(ghostPos[1]),
			Float.parseFloat(ghostPos[2])
		);
		(npcCtrl.getNPC()).setTargetLocationAsGhost(ghost);
    }

	// ----------- SENDING NPC MESSAGES -----------------
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
			message += "," + npcCtrl.getCriteria();
			message += "," + npcCtrl.getNearFlag();
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
            String message = new String("startNPC," + clientID);
            sendPacket(message, clientID);
        }
        catch (IOException e)
        {
            System.out.println("STARTING NPC ERROR");
        }
    }

    
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

	// Sending Box message
	public void sendBoxInfo(UUID clientID)
	{
		try
		{
			String message = new String("boxinfo," + clientID.toString());
			message += "," + boxCtrl.getBoxAmount();
			for (int i = 0; i < boxCtrl.boxAmount; i++)
			{
				
				Box b = boxCtrl.getBoxLocation(i);
				message += "," + b.boxID;
				message += "," + b.getPosition().x();
				message += "," + b.getPosition().y();
				message += "," + b.getPosition().z();
				message += "," + b.getBoxStatus();
			}
			sendPacket(message, clientID);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void sendRemoveBox(UUID clientID, int boxID)
	{
		try
		{
			String message = new String("rmvbox," + clientID.toString());
			message += "," + boxID;
			forwardPacketToAll(message, clientID);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
