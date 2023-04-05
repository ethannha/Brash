package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A concrete implementation of a game server. Provides facilities to store a
 * client list and send packets in various ways to clients.
 * 
 * The user is intended to extend this class and implement the method
 * {@link #processPacket(Object, InetAddress, int)}, and in TCP usage, the user
 * is also responsible for implementing
 * {@link #acceptClient(IClientInfo, Object)}.
 * 
 * Utilizes an underlying {@link IServerSocket} to send and receive packets.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 * @param <K>
 *            the key type used to identify each client (such as UUID)
 */
public class GameConnectionServer<K> extends AbstractGameConnectionServer<K> {
	private IServerSocket serverSocket;
	private ConcurrentHashMap<K, IClientInfo> clients; // hashmap to hold all of the clients

	/**
	 * Creates a GameConnectionServer bound to the local port specified and uses
	 * the specified protocol.
	 * 
	 * @param localPort
	 *            local port to bind the server to
	 * @param protocolType
	 *            protocol to use
	 * @throws IOException
	 */
	public GameConnectionServer(int localPort, ProtocolType protocolType)
			throws IOException {
		switch (protocolType) {
		case UDP:
			serverSocket = new UDPServerSocket(localPort, this);
			break;
		case TCP:
			serverSocket = new TCPServerSocket(localPort, this);
			break;
		default:
			System.err
					.println("Error in creating GameConnectionServer. Invalid protocol type.");
		}

		initializeServer();
	}

	/**
	 * When a user extends this class this method should be overridden to handle
	 * accepting new clients. The method is called following the accept() method
	 * from a TCPServerSocket.
	 * 
	 * In a UDP implementation this method can be used for the same purpose but
	 * it is NOT called automatically and should be called from processPacket()
	 * given the appropriate case (a client joining the server) if the user
	 * wishes to do so. Alternatively, the user could just call addClient() if
	 * that is all they want to do.
	 * 
	 * This method should call addClient() to add the client to the server's
	 * client list.
	 * 
	 * The first packet received by the socket will be passed to the method with
	 * the intention that it will contain the client's UID used in the call to
	 * addClient().
	 * 
	 * @param clientInfo
	 * @param firstPacket
	 *            the first packet received by the socket
	 */
	@Override
	public void acceptClient(IClientInfo clientInfo, Object firstPacket) {
	}

	@Override
	protected void addClient(IClientInfo clientInfo, K clientUID) {
		clients.put(clientUID, clientInfo);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method should be overridden to implement the game specific protocol.
	 */
	@Override
	public void processPacket(Object object, InetAddress senderIP,
			int senderPort) {
	}

	@Override
	public int getLocalPort() {
		return serverSocket.getLocalPort();
	}

	@Override
	public void shutdown() throws IOException {
		serverSocket.shutdown();
	}

	@Override
	protected void initializeServer() {
		clients = new ConcurrentHashMap<K, IClientInfo>();
	}

	/**
	 * Sets the client list used by the server.
	 * 
	 * @param clients
	 */
	protected void setClients(ConcurrentHashMap<K, IClientInfo> clients) {
		this.clients = clients;
	}

	/**
	 * Similar to sendPacketToAll() with the exception that it does not send the
	 * packet to the client that the packet originated from.
	 * 
	 * Sends the message to every client on the server's clients list except the
	 * client who's UID is equal to originalClientUID.
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	protected void forwardPacketToAll(Serializable object, K originalClientUID)
			throws IOException {
		Collection<IClientInfo> clientVals = clients.values();
		List<IClientInfo> list = new ArrayList<IClientInfo>(clientVals);
		IClientInfo originalClientInfo = clients.get(originalClientUID);
		list.remove(originalClientInfo);

		for (IClientInfo ci : list) {
			ci.sendPacket(object);
		}
	}

	@Override
	protected void sendPacket(Serializable object, K clientUID)
			throws IOException {
		IClientInfo clientInfo = clients.get(clientUID);
		if (clientInfo != null) {
			clientInfo.sendPacket(object);
		}
	}

	/**
	 * Sends a packet to every client on the server's clients list.
	 * 
	 * @throws IOException
	 * 
	 */
	@Override
	protected void sendPacketToAll(Serializable object) throws IOException {
		Collection<IClientInfo> clientVals = clients.values();

		for (IClientInfo ci : clientVals) {
			ci.sendPacket(object);
		}
	}

	@Override
	protected void removeClient(K clientUID) {
		clients.remove(clientUID);
	}

	/**
	 * 
	 * @return the client list for the server
	 */
	protected ConcurrentHashMap<K, IClientInfo> getClients() {
		return clients;
	}

	/**
	 * 
	 * @return the underlying server socket
	 */
	protected IServerSocket getServerSocket() {
		return serverSocket;
	}
}
