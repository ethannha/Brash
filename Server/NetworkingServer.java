import java.io.IOException;

import org.joml.Vector3f;

import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer 
{
	private GameServerUDP thisUDPServer;
	private GameServerTCP thisTCPServer;

	// NPC / AI
	private NPCcontroller npcCtrl;

	// Box
	private BoxController box;

	public NetworkingServer(int serverPort, String protocol) 
	{
		npcCtrl = new NPCcontroller();
		box = new BoxController();
		try 
		{	if(protocol.toUpperCase().compareTo("TCP") == 0)
			{	
				thisTCPServer = new GameServerTCP(serverPort);
			}
			else
			{	
				thisUDPServer = new GameServerUDP(serverPort, npcCtrl, box);
			}
		}
		catch (IOException e) 
		{
			System.out.println("NPC/AI Server didnt start");
			e.printStackTrace();
		}
		box.start(thisUDPServer);
		npcCtrl.start(thisUDPServer);
		
		
	}

	public static void main(String[] args) 
	{	if(args.length > 1)
		{	
			NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		}
	}

}
