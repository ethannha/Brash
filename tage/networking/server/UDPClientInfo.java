package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;

/**
 * Implementation of {@link IClientInfo} used for UDP protocol. Stores the
 * client's address, port they are listening on, and the current server socket.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public class UDPClientInfo implements IClientInfo {
	private InetAddress addr; // client's address
	private int port; // port client is listening on
	private UDPServerSocket serverSocket; // the current server socket

	/**
	 * Creates a UDPClientInfo using the params provided.
	 * 
	 * @param address
	 *            client's address
	 * @param port
	 *            port client is listening on
	 * @param serverSocket
	 *            current server socket
	 */
	public UDPClientInfo(InetAddress address, int port,
			UDPServerSocket serverSocket) {
		addr = address;
		this.port = port;
		this.serverSocket = serverSocket;
	}

	/**
	 * 
	 * @return the client's address
	 */
	public InetAddress getInetAddress() {
		return addr;
	}

	/**
	 * 
	 * @return the port the client is listening on
	 */
	public int getPort() {
		return port;
	}

	/**
	 * 
	 * @return the current server socket
	 */
	public UDPServerSocket getServerSocket() {
		return serverSocket;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Uses the current server socket to send the object.
	 */
	@Override
	public void sendPacket(Serializable object) throws IOException {
		serverSocket.sendPacket(addr, port, object);
	}
}
