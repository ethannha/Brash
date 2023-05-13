package tage.networking.client;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Concrete implementation of the client side for a game connection protocol.
 * Allows a client to send and receive {@link Serializable} objects to the IP
 * address and port the client is connected to.
 * 
 * This class is intended to be extended by a user in which they would override
 * {@link #processPacket(Object)} to handle the game specific protocol.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public class GameConnectionClient extends AbstractGameConnectionClient {
	private IClientSocket clientSocket;
	private Collection<Object> packetsToProcess;
	private Collection<Object> packetsReceived;
	private volatile boolean running;
	private Thread receivePackets;

	/**
	 * Creates an unconnected GameConnectionClient.
	 * {@link #connectTo(InetAddress, int)} should be called to connect the
	 * client and start the thread to receive packets.
	 * 
	 * @param protocolType
	 *            the protocol type to be used.
	 * @throws IOException
	 */
	public GameConnectionClient(ProtocolType protocolType) throws IOException {
		switch (protocolType) {
		case UDP:
			clientSocket = new UDPClientSocket();
			break;
		case TCP:
			clientSocket = new TCPClientSocket();
			break;
		default:
			throw new IllegalArgumentException(
					"Error in creating GameConnectionClient. Invalid protocol type.");
		}
	}

	/**
	 * Creates a GameConnectionClient connected to the specified remote address
	 * and port which uses the specified protocol type. This also initializes
	 * the client and starts the thread to receive packets.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @param protocolType
	 *            the protocol type to be used.
	 * @throws IOException
	 */
	public GameConnectionClient(InetAddress remoteAddr, int remotePort,
			ProtocolType protocolType) throws IOException {
		switch (protocolType) {
		case UDP:
			clientSocket = new UDPClientSocket(remoteAddr, remotePort);
			break;
		case TCP:
			clientSocket = new TCPClientSocket(remoteAddr, remotePort);
			break;
		default:
			throw new IllegalArgumentException(
					"Error in creating GameConnectionClient. Invalid protocol type.");
		}

		initClient();
	}

	/**
	 * Creates a GameConnectionClient connected to the specified remote address
	 * and port and bound to the specified local address and port, which uses
	 * the specified protocol type. This also initializes the client and starts
	 * the thread to receive packets.
	 * 
	 * @param remoteAddr
	 *            remote address to connect to.
	 * @param remotePort
	 *            remote port to connect to.
	 * @param localAddr
	 *            local address to bind to.
	 * @param localPort
	 *            local port to bind to.
	 * @param protocolType
	 *            the protocol type to be used.
	 * @throws IOException
	 */
	public GameConnectionClient(InetAddress remoteAddr, int remotePort,
			InetAddress localAddr, int localPort, ProtocolType protocolType)
			throws IOException {
		switch (protocolType) {
		case UDP:
			clientSocket = new UDPClientSocket(remoteAddr, remotePort,
					localAddr, localPort);
			break;
		case TCP:
			clientSocket = new TCPClientSocket(remoteAddr, remotePort,
					localAddr, localPort);
			break;
		default:
			throw new IllegalArgumentException(
					"Error in creating GameConnectionClient. Invalid protocol type.");
		}

		initClient();
	}

	/**
	 * Initializes the packetsToProcess and packetsRecieved collections, then
	 * calls {@link #startClient()}.
	 * 
	 */
	@Override
	protected void initClient() {
		packetsToProcess = new ArrayList<Object>();
		packetsReceived = new ArrayList<Object>();
		startClient();
	}

	/**
	 * Initializes the thread to receive packets by calling
	 * {@link #createReceivePacketsThread()}, sets running to true, and finally
	 * starts the thread for receiving packets.
	 * 
	 */
	@Override
	protected final void startClient() {
		receivePackets = createReceivePacketsThread();
		running = true;
		receivePackets.start();
	}

	@Override
	public void sendPacket(Serializable object) throws IOException {
		clientSocket.send(object);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * For each packet, {@link #processPacket(Object)} is called to handle each
	 * individual packet.
	 * 
	 */
	@Override
	public void processPackets() {
		synchronized (packetsReceived) {
			packetsToProcess.addAll(packetsReceived);
			packetsReceived.clear();
		}

		for (Object packet : packetsToProcess) {
			processPacket(packet);
		}

		packetsToProcess.clear();
	}

	/**
	 * Processes each individual packet received. This method should be used to
	 * implement the game specific protocol.
	 * 
	 * This method is intended to be overridden by the user so that they can
	 * implement their own specific protocol.
	 * 
	 * @param message
	 */
	protected void processPacket(Object message) {
	}

	/**
	 * {@inheritDoc}
	 * 
	 * This method then calls {@link #initClient()} to initialize the client and
	 * start the receive packets thread.
	 * 
	 */
	@Override
	public void connectTo(InetAddress remoteAddress, int remotePort)
			throws IOException {
		clientSocket.connectTo(remoteAddress, remotePort);
		initClient();
	}

	@Override
	public void shutdown() throws IOException {
		running = false;

		clientSocket.shutdown();
	}

	/**
	 * This method should return a {@link Thread} to handle the receiving of
	 * packets for the client.
	 * 
	 * A typical implementation would loop while {@code running} is true to
	 * constantly call receive on the {@code clientSocket}.
	 * 
	 * Default implementation: grab every packet and add it to the
	 * {@code packetsRecieved} Collection.
	 * 
	 */
	@Override
	protected Thread createReceivePacketsThread() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				while (running) {
					try {
						packetsReceived.add(clientSocket.receive());
					} catch (IOException | ClassNotFoundException e) {
						if (e instanceof EOFException
								|| clientSocket.isClosed()) {
							break;
						}

						System.err
								.println("Exception generated while trying to recieve packet from server.");
						e.printStackTrace();
					}
				}
			}
		};

		return thread;
	}

	@Override
	public int getLocalPort() {
		return clientSocket.getLocalPort();
	}

	/**
	 * Getter method for running field.
	 * 
	 * Determines if the client is currently running or not.
	 * 
	 * @return a flag indicating whether the client is currently running or not
	 */
	protected boolean getRunning() {
		return running;
	}

	/**
	 * Getter method for client socket.
	 * 
	 * the underlying client socket used to handle the actual network protocols.
	 * 
	 * @return the client socket
	 */
	protected IClientSocket getClientSocket() {
		return clientSocket;
	}

	/**
	 * Getter for packetsReceived field.
	 * 
	 * A holding collection for packets received.
	 * 
	 * @return a collection of the packets received
	 */
	protected Collection<Object> getPacketsReceived() {
		return packetsReceived;
	}

	/**
	 * Getter for packetsToProcess field.
	 * 
	 * This is a temporary holding collection for the packets that will be
	 * processed by a call to {@link #processPackets()}.
	 * 
	 * @return a Collection of the packets waiting to be processed
	 */
	protected Collection<Object> getPacketsToProcess() {
		return packetsToProcess;
	}

	/**
	 * Setter for the packets to process.
	 * 
	 * This is a temporary holding collection for the packets that will be
	 * processed in call to {@link #processPackets()}.
	 * 
	 * @param packetsToProcess
	 */
	protected void setPacketsToProcess(Collection<Object> packetsToProcess) {
		this.packetsToProcess = packetsToProcess;
	}

	/**
	 * Setter for the packets received collection.
	 * 
	 * A holding collection for packets received.
	 * 
	 * @param packetsReceived
	 */
	protected void setPacketsReceived(Collection<Object> packetsReceived) {
		synchronized (this.packetsReceived) {
			this.packetsReceived = packetsReceived;
		}
	}
}
