package tage.networking.client;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;

/**
 * Defines a client side socket responsible for sending an receiving packets for
 * the client side of a game.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IClientSocket {
	// common methods shared by Socket and DatagramSocket
	public void bind(SocketAddress bindPoint) throws IOException;

	public void close() throws IOException;

	public InetAddress getInetAddress();

	public InetAddress getLocalAddress();

	public int getLocalPort();

	public SocketAddress getLocalSocketAddress();

	public int getPort();

	public int getReceiveBufferSize() throws SocketException;

	public SocketAddress getRemoteSocketAddress();

	public boolean getReuseAddress() throws SocketException;

	public int getSendBufferSize() throws SocketException;

	public int getSoTimeout() throws SocketException;

	public int getTrafficClass() throws SocketException;

	public boolean isBound();

	public boolean isClosed();

	public boolean isConnected();

	public void setReceiveBufferSize(int size) throws SocketException;

	public void setReuseAddress(boolean on) throws SocketException;

	public void setSendBufferSize(int size) throws SocketException;

	public void setSoTimeout(int timeout) throws SocketException;

	public void setTrafficClass(int tc) throws SocketException;

	/**
	 * Connects the client socket to the specified InetAddress and port. The
	 * client socket should be connected before being used.
	 * 
	 * @param remoteAddress
	 *            the address to connect the socket to.
	 * @param remotePort
	 *            the port to connect the socket to.
	 * @throws IOException
	 */
	public void connectTo(InetAddress remoteAddress, int remotePort)
			throws IOException;

	/**
	 * Sends a {@link Serializable} object to the remote InetAddress and port
	 * that this client socket is connected to.
	 * 
	 * @param object
	 *            the {@link Serializable} objet to send.
	 * @throws IOException
	 */
	public void send(Serializable object) throws IOException;

	/**
	 * Receives a packet from the remote InetAddress and port that this client
	 * socket is connected to.
	 * 
	 * @return the Object received from the remote connection.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Object receive() throws IOException, ClassNotFoundException;

	/**
	 * Shuts down this client socket. Responsible for cleaning up any necessary
	 * objects used by this socket.
	 * 
	 * @throws IOException
	 */
	public void shutdown() throws IOException;
}
