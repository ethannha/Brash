package tage.networking.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

import tage.networking.IGameConnection;

/**
 * Defines a game connection client. This is the client side for networking
 * within a game.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IGameConnectionClient extends IGameConnection {
	/**
	 * Processes all of the packets received by the client since the last time
	 * this method was called. This method should handle the game specific
	 * protocol for what to do with each packet.
	 */
	public void processPackets();

	/**
	 * Connects the client to the specified InetAddress and port.
	 * 
	 * @param remoteAddress
	 *            the address to connect the client to.
	 * @param remotePort
	 *            the port to connect the client to.
	 * @throws IOException
	 */
	public void connectTo(InetAddress remoteAddress, int remotePort)
			throws IOException;

	/**
	 * Responsible for sending a {@link Serializable} object over the network.
	 * 
	 * @param object
	 *            object to send.
	 * @throws IOException
	 */
	public void sendPacket(Serializable object) throws IOException;
}
