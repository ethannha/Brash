package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;

import tage.networking.client.TCPClientSocket;

/**
 * Implementation of {@link IClientInfo} used for TCP protocol. Stores the
 * client's {@link TCPClientSocket} connected to the client.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public class TCPClientInfo implements IClientInfo {
	private TCPClientSocket socket; // socket connected to the client

	/**
	 * Creates a TCPClientInfo with the given param.
	 * 
	 * @param socket
	 */
	public TCPClientInfo(TCPClientSocket socket) {
		this.socket = socket;
	}

	/**
	 * 
	 * @return the socket connected to the client
	 */
	public TCPClientSocket getsocket() {
		return socket;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * uses the socket stored by this structure to send the specified object to
	 * the client.
	 */
	@Override
	public void sendPacket(Serializable object) throws IOException {
		socket.send(object);
	}
}
