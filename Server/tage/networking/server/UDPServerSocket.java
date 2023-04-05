package tage.networking.server;

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
 * A UDP implementation of a {@link IServerSocket}. This socket is able to send
 * and receive {@link Serializable} objects from server side of a game.
 * 
 * An underlying {@link DatagramSocket} is used in this implementation.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public class UDPServerSocket extends DatagramSocket implements IServerSocket {
	private GameConnectionServer server;
	private volatile boolean running;
	private ObjectOutputStream objOutputStream;
	private ByteArrayOutputStream byteOutputStream;

	/**
	 * Creates a UDPServerSocket bound to the local port. The server passed in
	 * is used to process the packets received by this socket.
	 * 
	 * See {@link DatagramSocket#DatagramSocket(int)} for information about
	 * local binding of socket.
	 * 
	 * @param localPort
	 *            local port to bind socket to
	 * @param server
	 *            server to process packets
	 * @throws SocketException
	 */
	public UDPServerSocket(int localPort, GameConnectionServer server)
			throws SocketException {
		super(localPort);

		this.server = server;
		byteOutputStream = new ByteArrayOutputStream();
		running = true;

		Thread thread = new ServerLoop();
		thread.start();
	}

	@Override
	public void sendPacket(InetAddress addr, int port, Serializable object)
			throws IOException {
		objOutputStream = new ObjectOutputStream(byteOutputStream);
		objOutputStream.writeObject(object);

		byte[] data = byteOutputStream.toByteArray();
		byteOutputStream.reset();

		DatagramPacket sendPacket = new DatagramPacket(data, data.length, addr,
				port);

		send(sendPacket);
	}

	@Override
	public IClientInfo createClientInfo(InetAddress clientAddr, int clientPort) {
		return new UDPClientInfo(clientAddr, clientPort, this);
	}

	@Override
	public void shutdown() {
		running = false;
		close();
	}

	/**
	 * Thread used for the server loop.
	 * 
	 * @author Kyle
	 * 
	 */
	private class ServerLoop extends Thread {
		private ObjectInputStream objInputStream;
		private ByteArrayInputStream byteInputStream;

		@Override
		public void run() {
			byte[] data;
			DatagramPacket recvPacket;
			while (running) {
				try {
					// array to hold the packet's data
					data = new byte[getReceiveBufferSize()];
					// constructs a packet for received data
					recvPacket = new DatagramPacket(data, data.length);

					// Receives a packet, assigning it to the recvPacket
					receive(recvPacket);

					byteInputStream = new ByteArrayInputStream(
							recvPacket.getData());
					objInputStream = new ObjectInputStream(byteInputStream);

					server.processPacket(objInputStream.readObject(),
							recvPacket.getAddress(), recvPacket.getPort());
				} catch (IOException | ClassNotFoundException e) {
					if (isClosed()) {
						break;
					}

					System.err
							.println("Exception generated while trying to recieve packet from client.");
					e.printStackTrace();
				}
			}
		}
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

	protected GameConnectionServer getServer() {
		return server;
	}

	protected boolean isRunning() {
		return running;
	}
}
