package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Defines a server side socket.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IServerSocket {
	// methods common to both ServerSocket and DatagramSocket
	public void bind(SocketAddress bindPoint) throws IOException;

	public void close() throws IOException;

	public InetAddress getInetAddress();

	public int getLocalPort();

	public SocketAddress getLocalSocketAddress();

	public int getReceiveBufferSize() throws SocketException;

	public boolean getReuseAddress() throws SocketException;

	public int getSoTimeout() throws IOException;

	public boolean isBound();

	public boolean isClosed();

	public void setReceiveBufferSize(int size) throws SocketException;

	public void setReuseAddress(boolean on) throws SocketException;

	public void setSoTimeout(int timeout) throws SocketException;

	/**
	 * Sends a {@link Serializable} object to the specified address and port.
	 * 
	 * @param addr
	 *            address to send object to
	 * @param port
	 *            port to send object to
	 * @param object
	 *            object to send
	 * @throws IOException
	 */
	public void sendPacket(InetAddress addr, int port, Serializable object)
			throws IOException;

	/**
	 * Creates a {@link IClientInfo} with the appropriate information for a
	 * client.
	 * 
	 * @param clientAddr
	 *            client's address
	 * @param clientPort
	 *            client's port
	 * @return a client information data structure for the client information
	 * @throws IOException
	 */
	public IClientInfo createClientInfo(InetAddress clientAddr, int clientPort)
			throws IOException;

	/**
	 * Shuts down this server socket. Responsible for cleaning up any necessary
	 * objects used by this socket.
	 * 
	 * @throws IOException
	 */
	public void shutdown() throws IOException;
}