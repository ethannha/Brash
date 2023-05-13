package tage.networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collection;

/**
 * A general interface for game connections.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IGameConnection {
	/**
	 * Enum for specifying the protocol type to be used in the network
	 * communications.
	 * 
	 * @author Kyle
	 * 
	 */
	public enum ProtocolType {
		UDP, TCP
	}

	/**
	 * Gets the local port the game connection is listening on.
	 * 
	 * @return the local port of the game connection
	 */
	public int getLocalPort();

	/**
	 * Gets the local IP address for the current machine.
	 * 
	 * @return local IP address
	 */
	public InetAddress getLocalInetAddress() throws UnknownHostException;

	/**
	 * Retrieves ALL of IP addresses for the local machine.
	 * 
	 * @return a collection of all of the local InetAddresses
	 */
	public Collection<InetAddress> getAllLocalAddress() throws SocketException;

	/**
	 * This method is responsible for all clean up necessary for the specific
	 * game connection.
	 * 
	 * @throws IOException
	 */
	public void shutdown() throws IOException;
}
