package tage.networking.client;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * A client socket for sending and receiving packets using the UDP protocol.
 * This socket is able to send and receive {@link Serializable} objects from
 * client side of a game.
 * 
 * The underlying {@link DatagramSocket} is used in this implementation.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 * @see DatagramSocket
 */
public class UDPClientSocket extends DatagramSocket implements IClientSocket {
	private InetAddress remoteAddr; // remote address to send packets to
	private int remotePort; // remote port to send packets to
	private ObjectOutputStream objOutputStream; // stream to send objects
	private ByteArrayOutputStream byteOutputStream; // byte stream to send data
	private ObjectInputStream objInputStream; // stream to receive objects
	private ByteArrayInputStream byteInputStream; // byte stream to receive data
	private boolean connected = false; // true if this socket is fully initialized

	/**
	 * Creates an unconnected socket. This socket should be connected via
	 * {@link #connectTo(InetAddress, int)} before being used.
	 * 
	 * See {@link DatagramSocket#DatagramSocket()} for local binding
	 * information.
	 * 
	 * @throws IOException
	 */
	public UDPClientSocket() throws IOException {
		super();
	}

	/**
	 * Constructs a socket connected to the remote address and remote port
	 * specified.
	 * 
	 * Connected should be true upon returning from this method.
	 * 
	 * See {@link DatagramSocket#DatagramSocket()} for local binding
	 * information.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @throws IOException
	 */
	public UDPClientSocket(InetAddress remoteAddr, int remotePort)
			throws IOException {
		super();

		initSocket(remotePort, remoteAddr);
	}

	/**
	 * Constructs a socket bound to the local port and connected to the remote
	 * address and remote port.
	 * 
	 * Connected should be true upon returning from this method.
	 * 
	 * See {@link DatagramSocket#DatagramSocket(int)} for local binding
	 * information.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @param localPort
	 *            local port to bind to.
	 * @throws IOException
	 */
	public UDPClientSocket(InetAddress remoteAddr, int remotePort, int localPort)
			throws IOException {
		super(localPort);

		initSocket(remotePort, remoteAddr);
	}

	/**
	 * Constructs a socket bound to the local port and address and connected to
	 * the remote address and remote port.
	 * 
	 * Connected should be true upon returning from this method.
	 * 
	 * See {@link DatagramSocket#DatagramSocket(int, InetAddress)} for local
	 * binding information.
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
	public UDPClientSocket(InetAddress remoteAddr, int remotePort,
			InetAddress localAddr, int localPort) throws IOException {
		super(localPort, localAddr);

		initSocket(remotePort, remoteAddr);
	}

	/**
	 * Initialized the socket by creating and assigning variables.
	 * 
	 * @param remotePort
	 * @param remoteAddr
	 * @throws IOException
	 */
	private void initSocket(int remotePort, InetAddress remoteAddr)
			throws IOException {
		this.remotePort = remotePort;
		this.remoteAddr = remoteAddr;
		setConnected(true);
		byteOutputStream = new ByteArrayOutputStream();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Connected should be true upon returning from this method.
	 * 
	 */
	@Override
	public void connectTo(InetAddress remoteAddress, int remotePort)
			throws IOException {
		initSocket(remotePort, remoteAddress);
	}

	/**
	 * Getter method for connected field. True when this socket is fully
	 * initialized with remote address and port to send packets to.
	 * 
	 */
	@Override
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Setter method for the connected field.
	 * 
	 * @param connected
	 */
	protected void setConnected(boolean connected) {
		this.connected = connected;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Will throw an exception if {@link #isConnected()} is false.
	 * 
	 */
	@Override
	public void send(Serializable object) throws IOException {
		if (connected) {
			objOutputStream = new ObjectOutputStream(byteOutputStream);
			objOutputStream.writeObject(object);

			byte[] data = byteOutputStream.toByteArray();
			byteOutputStream.reset();

			DatagramPacket sendPacket = new DatagramPacket(data, data.length,
					remoteAddr, remotePort);

			send(sendPacket);
		} else {
			throw new SocketException(
					"Socket must be connected before sending a packet.");
		}
	}

	@Override
	public Object receive() throws IOException, ClassNotFoundException {
		// array to hold the packet's data
		byte[] data = new byte[this.getReceiveBufferSize()];
		// constructs a packet for received data
		DatagramPacket recvPacket = new DatagramPacket(data, data.length);

		// Receives a packet, assigning it to the recvPacket
		receive(recvPacket);

		byteInputStream = new ByteArrayInputStream(recvPacket.getData());
		objInputStream = new ObjectInputStream(byteInputStream);

		return objInputStream.readObject();
	}

	@Override
	public void shutdown() throws IOException {
		if (objOutputStream != null)
			objOutputStream.close();
		if (byteOutputStream != null)
			byteOutputStream.close();
		if (objInputStream != null)
			objInputStream.close();
		if (byteInputStream != null)
			byteInputStream.close();
		this.close();
	}

	protected InetAddress getRemoteAddr() {
		return remoteAddr;
	}

	protected int getRemotePort() {
		return remotePort;
	}

	protected ObjectOutputStream getObjOutputStream() {
		return objOutputStream;
	}

	protected void setObjOutputStream(ObjectOutputStream objOutputStream) {
		this.objOutputStream = objOutputStream;
	}

	protected ByteArrayOutputStream getByteOutputStream() {
		return byteOutputStream;
	}

	protected void setByteOutputStream(ByteArrayOutputStream byteOutputStream) {
		this.byteOutputStream = byteOutputStream;
	}

	protected ObjectInputStream getObjInputStream() {
		return objInputStream;
	}

	protected void setObjInputStream(ObjectInputStream objInputStream) {
		this.objInputStream = objInputStream;
	}

	protected ByteArrayInputStream getByteInputStream() {
		return byteInputStream;
	}

	protected void setByteInputStream(ByteArrayInputStream byteInputStream) {
		this.byteInputStream = byteInputStream;
	}
}
