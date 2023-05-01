import java.io.IOException;
import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer 
{
	private GameServerUDP thisUDPServer;
	private GameServerTCP thisTCPServer;

	// NPC / AI
	private GameAIServerUDP UDPServer;
	private NPCcontroller npcCtrl;

	public NetworkingServer(int serverPort, String protocol) 
	{
		npcCtrl = new NPCcontroller();
		try 
		{	if(protocol.toUpperCase().compareTo("TCP") == 0)
			{	
				thisTCPServer = new GameServerTCP(serverPort);
			}
			else
			{	
				thisUDPServer = new GameServerUDP(serverPort, npcCtrl);
				//UDPServer = new GameAIServerUDP(serverPort, npcCtrl);
			}
		}
		catch (IOException e) 
		{
			System.out.println("NPC/AI Server didnt start");
			e.printStackTrace();
		}
		npcCtrl.start(thisUDPServer);
	}

	public static void main(String[] args) 
	{	if(args.length > 1)
		{	
			NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
		}
	}

}
