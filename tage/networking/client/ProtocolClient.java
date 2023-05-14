package tage.networking.client;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Iterator;
import java.util.UUID;
import java.util.Vector;

import org.joml.*;

import client.BoxManager;
import client.GhostAvatar;
import client.GhostManager;
import client.GhostNPC;
import client.MyGame;

/**
 * This class is used to communicate with a server and receive server packets.
 * @author Steven Ho and Ethan Ha
 */
public class ProtocolClient extends GameConnectionClient
{
	private MyGame game;
	private GhostManager ghostManager;
	private BoxManager boxManager;
	private UUID id;
	private GhostNPC ghostNPC;
	
	/**
	 * Initialization of the ProtocolClient in which takes in the IP address of client, port number, type of protocol (usually UDP)
	 * and the game of the client (MyGame). This will also generate a random UUID for the client and take in the ghostManager and
	 * box manager from the game.
	 * @param remoteAddr
	 * @param remotePort
	 * @param protocolType
	 * @param game
	 * @throws IOException
	 */
	public ProtocolClient(InetAddress remoteAddr, int remotePort, ProtocolType protocolType, MyGame game) throws IOException 
	{	
		super(remoteAddr, remotePort, protocolType);
		this.game = game;
		this.id = UUID.randomUUID();
		ghostManager = game.getGhostManager();
		boxManager = game.getBoxManager();
	}
	
	/**
	 * Get method that returns the UUID of the client
	 * @return clientID
	 */
	public UUID getID() { return id; }
	
	/**
	 * ProcessPacket is a method that will process any incoming pakcets from the server such as client joining the server
	 * creating ghost avatars, boxes, and much more.
	 */
	@Override
	protected void processPacket(Object message)
	{	
		String strMessage = (String)message;
		System.out.println("message received -->" + strMessage);
		String[] messageTokens = strMessage.split(",");
		
		// Game specific protocol to handle the message
		if(messageTokens.length > 0)
		{
			// Handle JOIN message
			// Format: (join,success) or (join,failure)
			if(messageTokens[0].compareTo("join") == 0)
			{	if(messageTokens[1].compareTo("success") == 0)
				{	
					System.out.println("join success confirmed");
					game.setIsConnected(true);
					sendCreatePlayerScore();
					sendCreateMessage(game.getGhostDefaultPosition(), game.getPlayerScore(), game.isCrownAttach());
				}
				if(messageTokens[1].compareTo("failure") == 0)
				{	
					System.out.println("join failure confirmed");
					game.setIsConnected(false);
			}	}
			
			// Handle BYE message
			// Format: (bye,remoteId)
			if(messageTokens[0].compareTo("bye") == 0)
			{	
				// remove ghost avatar with id = remoteId
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.removeGhostAvatar(ghostID);
			}
			
			// Handle CREATE message
			// Format: (create,remoteId,x,y,z)
			// AND
			// Handle DETAILS_FOR message
			// Format: (dsfr,remoteId,x,y,z)
			if (messageTokens[0].compareTo("create") == 0 || (messageTokens[0].compareTo("dsfr") == 0))
			{	
				// create a new ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4])
				);

				int score = Integer.parseInt(messageTokens[5]);

				boolean crownOn = Boolean.parseBoolean(messageTokens[6]);

				try
				{	
					ghostManager.createGhostAvatar(ghostID, ghostPosition, score, crownOn);
				}	
				catch (IOException e)
				{	
					System.out.println("error creating ghost avatar");
				}
			}
			
			// Handle WANTS_DETAILS message
			// Format: (wsds,remoteId)
			if (messageTokens[0].compareTo("wsds") == 0)
			{
				// Send the local client's avatar's information
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				sendDetailsForMessage(ghostID, game.getPlayerPosition(), game.getPlayerScore(), game.isCrownAttach());
			}
			
			// Handle MOVE message
			// Format: (move,remoteId,x,y,z)
			if (messageTokens[0].compareTo("move") == 0)
			{
				// move a ghost avatar
				// Parse out the id into a UUID
				UUID ghostID = UUID.fromString(messageTokens[1]);
				
				// Parse out the position into a Vector3f
				Vector3f ghostPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				
				ghostManager.updateGhostAvatar(ghostID, ghostPosition);
			}

			if (messageTokens[0].compareTo("animate") == 0)
			{
				UUID ghostID = UUID.fromString(messageTokens[1]);
				String aniName = messageTokens[2];

				ghostManager.changeGhostAnimation(ghostID, aniName);
			}

			if (messageTokens[0].compareTo("rotate") == 0)
			{
				UUID ghostID = UUID.fromString(messageTokens[1]);

				//System.out.println("==================================== IN PACKET: " + messageTokens[2] + ", " + messageTokens[3] + ", " + messageTokens[4] + ", " 
				//+ messageTokens[5]);
				AxisAngle4f ghostRotMat = new AxisAngle4f(
							Float.parseFloat(messageTokens[2]), Float.parseFloat(messageTokens[3]), 
							Float.parseFloat(messageTokens[4]), Float.parseFloat(messageTokens[5])
				);

				Matrix4f ghostRotation = new Matrix4f();
				ghostRotation.rotation(ghostRotMat);

				ghostManager.updateGhostAvatarRotation(ghostID, ghostRotation);
			}

			if (messageTokens[0].compareTo("createPS") == 0)
			{
				UUID ghostID = UUID.fromString(messageTokens[1]);
				ghostManager.addPlayer(ghostID, 0);
			}

			if (messageTokens[0].compareTo("updatePS") == 0)
			{
				UUID ghostID = UUID.fromString(messageTokens[1]);
				int score = Integer.parseInt(messageTokens[2]);
				ghostManager.updateGhostScore(ghostID, score);
			}

			// HANDLING GHOST NPC ============================================
			if (messageTokens[0].compareTo("createNPC") == 0)
			{
				// creating a new ghost npc
				UUID npcID = UUID.fromString(messageTokens[1]);
				// Parse out the position
				Vector3f NPCPosition = new Vector3f(
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3]),
					Float.parseFloat(messageTokens[4]));
				try
				{
					createGhostNPC(NPCPosition);
				}
				catch (IOException e) {
					System.out.println("CREATING GHOST NPC GONE WRONGGGGGGGGGGGGGGGGGGGGG");
					e.printStackTrace();
				}
				sendNPCCreateMessage(npcID, ghostNPC.getLocalLocation());
			}

			if (messageTokens[0].compareTo("npcinfo") == 0)
			{

				Vector3f ghostNPCPosition = new Vector3f(
					Float.parseFloat(messageTokens[1]),
					Float.parseFloat(messageTokens[2]),
					Float.parseFloat(messageTokens[3])
				);

				updateGhostNPC(ghostNPCPosition, 1.0f);

				if (game.getAvatar().getWorldLocation().distance(ghostNPCPosition.x(), ghostNPCPosition.y(), ghostNPCPosition.z()) < Float.parseFloat(messageTokens[4]))
				{
					System.out.println("AVATAR IS NEAR NPCCCCCC");
					ghostNPC.setSize(true);
					sendNPCisAvNear(game.getAvatar().getWorldLocation(), true);
				}
				else
				{
					sendNPCisAvNear(game.getAvatar().getWorldLocation(), false);
				}
			
				if (game.getAvatar().getWorldLocation().distance(ghostNPCPosition.x(), ghostNPCPosition.y(), ghostNPCPosition.z()) < 0.75f)
				{
					game.setIsAlive(false);
					sendNPCisAvNear(new Vector3f(0.0f, 0.0f, 0.0f), false);
				}

				Vector<GhostAvatar> ghostList = ghostManager.getAllGhost();
				Iterator<GhostAvatar> it = ghostList.iterator();
				GhostAvatar ghostAvatar;
				while(it.hasNext())
				{
					ghostAvatar = it.next();
					if (ghostAvatar.getPosition().distance(ghostNPCPosition.x(), ghostNPCPosition.y(), ghostNPCPosition.z()) < Float.parseFloat(messageTokens[4]))
					{
						System.out.println("GHOST IS NEAR NPCCCCCCCCCCCCC");
						ghostNPC.setSize(true);
						sendNPCisGhostNear(ghostAvatar.getPosition(), true);
					}
					else
					{
						sendNPCisGhostNear(ghostAvatar.getPosition(), false);
					}

					if (ghostAvatar.getPosition().distance(ghostNPCPosition.x(), ghostNPCPosition.y(), ghostNPCPosition.z()) < 0.75f)
					{
						sendNPCisGhostNear(new Vector3f(0.0f, 0.0f, 0.0f), false);
					}

				}
				//System.out.println("Successful updated ghost info ---------------------------");
			}

			if (messageTokens[0].compareTo("isnr") == 0)
			{
				System.out.println("=================================== PROTOCOL IS NEAR: " + messageTokens[1] + ", " + messageTokens[2] + ", " + messageTokens[3]);

			}

			// Box message from server

			if (messageTokens[0].compareTo("boxinfo") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);

				int i = 3;

				while (i < Integer.parseInt(messageTokens[2])*5)
				{
					int boxID = Integer.parseInt(messageTokens[i]);
					Vector3f boxLocation = new Vector3f(
					Float.parseFloat(messageTokens[i+1]), 
					Float.parseFloat(messageTokens[i+2]), 
					Float.parseFloat(messageTokens[i+3])
					);

					Boolean boxStatus = Boolean.parseBoolean(messageTokens[i+4]);

					createBoxObject(boxID, boxLocation, boxStatus);

					i+=5;
				}
			}

			if (messageTokens[0].compareTo("rmvbox") == 0)
			{
				UUID clientID = UUID.fromString(messageTokens[1]);
				int boxID = Integer.parseInt(messageTokens[2]);
				boxManager.removeBox(boxID);
			}
		}
	}
	
	/**
	 * The initial message from the game client requesting to join the 
	 * server. localId is a unique identifier for the client. Recommend 
	 * a random UUID.
	 * Message Format: (join,localId)
	 */
	public void sendJoinMessage()
	{	
		try 
		{	
			sendPacket(new String("join," + id.toString()));
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}
	
	/**
	 * Informs the server that the client is leaving the server. 
	 * Message Format: (bye,localId)
	 * @param score
	 */
	public void sendByeMessage(int score)
	{	
		try 
		{
			String message = new String("bye," + id.toString());
			message += "," + score;
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}
	
	/**
	 * Informs the server of the client�s Avatar�s position. The server 
	 * takes this message and forwards it to all other clients registered 
	 * with the server.
	 * Message Format: (create,localId,x,y,z) where x, y, and z represent the position
	 * @param position
	 * @param score
	 * @param crownOn
	 */
	public void sendCreateMessage(Vector3f position, int score, boolean crownOn)
	{	
		try 
		{	
			String message = new String("create," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			message += "," + score;
			message += "," + crownOn;
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}

	/**
	 * Informs the server of the local avatar's position. The server then 
	 * forwards this message to the client with the ID value matching remoteId. 
	 * This message is generated in response to receiving a WANTS_DETAILS message 
	 * from the server.
	 * Message Format: (dsfr,remoteId,localId,x,y,z, score, crownOn) where x, y, and z represent the position, score represents client's
	 * score, and crownOn representing if the crown is attach to this client
	 * @param remoteId
	 * @param position
	 * @param score
	 * @param crownOn
	 */
	public void sendDetailsForMessage(UUID remoteId, Vector3f position, int score, boolean crownOn)
	{	
		try 
		{	
			String message = new String("dsfr," + remoteId.toString() + "," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			message += "," + score;
			message += "," + crownOn;
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}
	
	/**
	 * Informs the server that the local avatar has changed position.  
	 * Message Format: (move,localId,x,y,z) where x, y, and z represent the position.
	 * @param position
	 */
	public void sendMoveMessage(Vector3f position)
	{	
		try 
		{	
			String message = new String("move," + id.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}

	/**
	 * Sends the client current animation name to other clients on server
	 * @param aniName
	 */
	public void sendAnimation(String aniName)
	{
		try
		{
			String message = new String("animate," + id.toString());
			message += "," + aniName;
			sendPacket(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Informs of the clients on server of this client rotation
	 * @param rotation
	 */
	public void sendRotateMessage(Matrix4f rotation)
	{

		AxisAngle4f rotMatrix = new AxisAngle4f();
		rotation.getRotation(rotMatrix);

		try 
		{	
			String message = new String("rotate," + id.toString());
			message += "," + rotMatrix.angle;
			message += "," + rotMatrix.x;
			message += "," + rotMatrix.y;
			message += "," + rotMatrix.z;
			
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}

	/**
	 * Sending message to server to create this client's player score
	 */
	public void sendCreatePlayerScore()
	{
		try
		{
			String message = new String("createPS," + id.toString());
			sendPacket(message);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sending this client new score to server so that it can be updated across the server
	 * @param score
	 */
	public void sendUpdatePlayerScore(int score)
	{
		try
		{
			String message = new String("updatePS," + id.toString());
			message += "," + score;
			sendPacket(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// ------------------- GHOST NPC SECTION -----------------------------

	/**
	 * Creates the ghost npc that will appear on every client
	 * @param position
	 * @throws IOException
	 */
	private void createGhostNPC (Vector3f position) throws IOException
	{
		if (ghostNPC == null)
		{
			ghostNPC = new GhostNPC(0, game.getNPCShape(), game.getNPCTexture(), position);
		}
	}

	/**
	 * Updates the client position and size if it were to see a player or ghost
	 * @param position
	 * @param gsize
	 */
	private void updateGhostNPC (Vector3f position, double gsize)
	{
		boolean gs;
		if (ghostNPC == null)
		{
			try 
			{
				createGhostNPC(position);	
			} 
			catch (IOException e) 
			{
				System.out.println("Error creating NPC");
			}		
		}
		ghostNPC.setPosition(position);
		if (gsize == 1.0)
		{
			gs = false;
		}
		else
		{
			gs = true;
		}
		ghostNPC.setSize(gs);
	}

	/**
	 * Sends a create NPC message if the client just join the server
	 * @param npcID
	 * @param position
	 */
	public void sendNPCCreateMessage(UUID npcID, Vector3f position)
	{	
		try 
		{	
			String message = new String("createNPC," + npcID.toString());
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}	
	}

	/**
	 * Sends the npc information to the server so that every other client can have similar npc information
	 * @param position
	 */
	public void sendNPCinfo(Vector3f position)
	{
		try 
		{	
			String message = new String("npcinfo");
			message += "," + position.x();
			message += "," + position.y();
			message += "," + position.z();
			sendPacket(message);
		} 
		catch (IOException e) 
		{	
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the server if the npc is closeby to a player
	 * @param playerPos
	 * @param isNear
	 */
	public void sendNPCisAvNear(Vector3f playerPos, boolean isNear)
	{
		try
		{
			String message = new String("isAvnr," + isNear);
			message += "," + playerPos.x();
			message += "," + playerPos.y();
			message += "," + playerPos.z();
			sendPacket(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to the server if the npc is closeby to a ghost
	 * @param ghostPos
	 * @param isNear
	 */
	public void sendNPCisGhostNear(Vector3f ghostPos, boolean isNear)
	{
		try
		{
			String message = new String("isGhostnr," + isNear);
			message += "," + ghostPos.x();
			message += "," + ghostPos.y();
			message += "," + ghostPos.z();
			sendPacket(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Creates the boxes given the id of that box, its position, and if its alive or not
	 * @param boxID
	 * @param position
	 * @param boxStatus
	 */
	private void createBoxObject (int boxID, Vector3f position, Boolean boxStatus)
	{
		try
		{
			boxManager.createBoxObject(boxID, position, boxStatus);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sends a message to server when this client needs the boxes
	 */
	public void sendNeedBoxObject()
	{
		try
		{
			String message = new String("needBox," + id.toString());
			sendPacket(message);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Removes the specified box from every client
	 * @param boxID
	 */
	public void sendRemoveBoxObject(int boxID)
	{
		try
		{
			String message = new String("rmvbox," + id.toString());
			message += "," + boxID;

			sendPacket(message);
		} 
		catch (IOException e) 
		{
			
		}
	}
}
