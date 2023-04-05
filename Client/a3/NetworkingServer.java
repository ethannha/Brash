package a3;

import java.io.IOException;
import tage.networking.IGameConnection.ProtocolType;

public class NetworkingServer
{
    private GameServerUDP thisUDPServer;
    
    public NetworkingServer(int serverPort, String protocol)
    {
        try 
        {
            thisUDPServer = new GameServerUDP(serverPort);
        }
        catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
        if (args.length > 1)
        {
            NetworkingServer app = new NetworkingServer(Integer.parseInt(args[0]), args[1]);
        }
    }
    
}
