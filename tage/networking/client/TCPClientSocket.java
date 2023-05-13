package tage.networking.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * A client socket for sending and receiving packets using the TCP protocol.
 * This socket is able to send and receive {@link Serializable} objects from
 * client side of a game.
 * 
 * The underlying {@link Socket} is used in this implementation.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 * @see Socket
 */
public class TCPClientSocket extends Socket implements IClientSocket {
	private ObjectOutputStream objOutputStream; // output stream used to send
												// objects
	private ObjectInputStream objInputStream; // input stream used to receive
												// objects

	/**
	 * Constructor to create an unconnected client socket. The socket should be
	 * connected via {@link #connectTo(InetAddress, int)} before being used.
	 * 
	 * See {@link Socket#Socket()}.
	 */
	public TCPClientSocket() {
		super();
	}

	/**
	 * Constructs a socket connected to the remote address and port specified.
	 * 
	 * See {@link Socket#Socket(String, int)}.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @throws IOException
	 */
	public TCPClientSocket(InetAddress remoteAddr, int remotePort)
			throws IOException {
		super(remoteAddr, remotePort);
	}

	/**
	 * Constructs a socket bound to the local address, local port and connected
	 * to the remote address and remote port.
	 * 
	 * See {@link Socket#Socket(InetAddress, int, InetAddress, int)}.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @param localAddr
	 *            local address to bind to.
	 * @param localPort
	 *            local port to bind to.
	 * @throws IOException
	 */
	public TCPClientSocket(InetAddress remoteAddr, int remotePort,
			InetAddress localAddr, int localPort) throws IOException {
		super(remoteAddr, remotePort, localAddr, localPort);
	}

	/**
	 * Constructs a socket connected to the remote address and remote port. The
	 * remote address string form should be able to be resolved to an IP address
	 * via {@link InetAddress#getByName(String)}.
	 * 
	 * See {@link Socket#Socket(String, int)}.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @throws IOException
	 */
	public TCPClientSocket(String remoteAddr, int remotePort)
			throws IOException {
		super(remoteAddr, remotePort);
	}

	/**
	 * Constructs a socket bound to the local address, local port and connected
	 * to the remote address and remote port. The remote address string form
	 * should be able to be resolved to an IP address via
	 * {@link InetAddress#getByName(String)}.
	 * 
	 * See {@link Socket#Socket(String, int, InetAddress, int)}.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @param localAddr
	 *            local address to bind to.
	 * @param localPort
	 *            local port to bind to.
	 * @throws IOException
	 */
	public TCPClientSocket(String remoteAddr, int remotePort,
			InetAddress localAddr, int localPort) throws IOException {
		super(remoteAddr, remotePort, localAddr, localPort);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Uses {@link Socket#connect(java.net.SocketAddress)} to connect this
	 * socket to the address and port.
	 * 
	 */
	@Override
	public void connectTo(InetAddress remoteAddress, int remotePort)
			throws IOException {
		this.connect(new InetSocketAddress(remoteAddress, remotePort));
	}

	@Override
	public void send(Serializable object) throws IOException {
		objOutputStream = new ObjectOutputStream(this.getOutputStream());
		objOutputStream.writeObject(object);
	}

	@Override
	public Object receive() throws IOException, ClassNotFoundException {
		objInputStream = new ObjectInputStream(this.getInputStream());
		return objInputStream.readObject();
	}

	@Override
	public void shutdown() throws IOException {
		if (objOutputStream != null)
			objOutputStream.close();
		if (objInputStream != null)
			objInputStream.close();
		this.close();
	}

	protected ObjectOutputStream getObjOutputStream() {
		return objOutputStream;
	}

	protected void setObjOutputStream(ObjectOutputStream objOutputStream) {
		this.objOutputStream = objOutputStream;
	}

	protected ObjectInputStream getObjInputStream() {
		return objInputStream;
	}

	protected void setObjInputStream(ObjectInputStream objInputStream) {
		this.objInputStream = objInputStream;
	}
}
