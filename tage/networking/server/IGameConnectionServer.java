package tage.networking.server;

import java.net.InetAddress;

import tage.networking.IGameConnection;

/**
 * Defines the server side for a game.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IGameConnectionServer extends IGameConnection {
	/**
	 * Responsible for accepting new clients to the server.
	 * 
	 * @param clientInfo
	 *            client information
	 * @param firstPacket
	 *            the first packet send by the client
	 */
	public void acceptClient(IClientInfo clientInfo, Object firstPacket);

	/**
	 * Responsible for processing a single packet received by the server. Method
	 * provides the sender's address and sender's port.
	 * 
	 * @param object
	 *            object received by the server
	 * @param senderAddress
	 *            sender's address
	 * @param senderPort
	 *            sender's port
	 */
	public void processPacket(Object object, InetAddress senderAddress,
			int senderPort);
}
