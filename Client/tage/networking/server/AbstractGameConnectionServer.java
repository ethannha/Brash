package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;

import tage.networking.AbstractGameConnection;

/**
 * Abstract class to define the protected methods necessary for a game
 * connection server.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 * @param <K>
 *            the key type used to identify each client (such as UUID)
 */
public abstract class AbstractGameConnectionServer<K> extends
		AbstractGameConnection implements IGameConnectionServer {
	/**
	 * Responsible for initializing all of the variables for the server socket.
	 */
	protected abstract void initializeServer();

	/**
	 * Forwards a packet to all of the other clients, excluding the client who
	 * originally sent the packet.
	 * 
	 * @param object
	 *            object to send
	 * @param originalClientUID
	 *            the id of the client who originally sent the packet
	 * @throws IOException
	 */
	protected abstract void forwardPacketToAll(Serializable object,
			K originalClientUID) throws IOException;

	/**
	 * Sends the object to the specified client.
	 * 
	 * @param object
	 * @param clientUID
	 * @throws IOException
	 */
	protected abstract void sendPacket(Serializable object, K clientUID)
			throws IOException;

	/**
	 * Responsible for sending a packet to every client connected to this
	 * server.
	 * 
	 * @param object
	 * @throws IOException
	 */
	protected abstract void sendPacketToAll(Serializable object)
			throws IOException;

	/**
	 * This method should add a client to the server's client list.
	 * 
	 * @param clientInfo
	 *            the data structure for the client's information
	 * @param clientUID
	 *            the client's unique id
	 */
	protected abstract void addClient(IClientInfo clientInfo, K clientUID);

	/**
	 * Removes a client from the client list of this server.
	 * 
	 * @param clientUID
	 *            the client's unique ID
	 */
	protected abstract void removeClient(K clientUID);
}
