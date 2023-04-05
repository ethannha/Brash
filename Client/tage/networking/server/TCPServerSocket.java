package tage.networking.server;

import tage.networking.client.TCPClientSocket;

import java.io.EOFException;
import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A TCP implementation of a {@link IServerSocket}. This socket is able to send
 * and receive {@link Serializable} objects from server side of a game.
 * 
 * An underlying {@link ServerSocket} is used in this implementation.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public class TCPServerSocket extends ServerSocket implements IServerSocket {
	private GameConnectionServer server;
	private volatile boolean running;

	/**
	 * Creates a TCPServerSocket bound to the local port. The server passed in
	 * is used to process the packets received by this socket.
	 * 
	 * See {@link ServerSocket#ServerSocket(int)} for information on local
	 * binding.
	 * 
	 * @param localPort
	 *            local port to bind to
	 * @param server
	 *            server to process packets
	 * @throws IOException
	 */
	public TCPServerSocket(int localPort, GameConnectionServer server)
			throws IOException {
		super(localPort);

		this.server = server;
		running = true;

		Thread thread = new Thread() {
			@Override
			public void run() {
				acceptClientsLoop();
			}
		};
		thread.start();
	}

	/**
	 * The server loop to be ran on a different Thread.
	 * 
	 */
	private void acceptClientsLoop() {
		while (running) {
			try {
				TCPClientSocket socket = (TCPClientSocket) this.accept();
				TCPClientInfo clientinfo = new TCPClientInfo(socket);
				server.acceptClient(clientinfo, socket.receive());

				Thread thread = new ClientHandlerThread(socket);
				thread.start();
			} catch (IOException | ClassNotFoundException e) {
				if (this.isClosed()) {
					break;
				}

				System.err
						.println("Exception generated while trying to accept new client.\n");
				e.printStackTrace();
			}
		}
	}

	/**
	 * Overrides accept to use the {@link #implAccept(Socket)} instead which is
	 * used to create a TCPClientSocket.
	 */
	@Override
	public Socket accept() throws IOException {
		Socket socket = new TCPClientSocket();
		implAccept(socket);
		return socket;
	}

	@Override
	public void sendPacket(InetAddress addr, int port, Serializable object)
			throws IOException {
		TCPClientSocket socket;

		socket = new TCPClientSocket(addr, port);
		socket.send(object);
		socket.shutdown();
	}

	@Override
	public IClientInfo createClientInfo(InetAddress clientAddr, int clientPort)
			throws IOException {
		TCPClientSocket clientSocket = new TCPClientSocket(clientAddr,
				clientPort, this.getInetAddress(), this.getLocalPort());
		TCPClientInfo clientInfo = new TCPClientInfo(clientSocket);

		return clientInfo;
	}

	@Override
	public void shutdown() throws IOException {
		running = false;
		close();
	}

	/**
	 * Thread to handle each client. Each thread should be responsible for a
	 * single client.
	 * 
	 * @author Kyle
	 * 
	 */
	private class ClientHandlerThread extends Thread {
		private TCPClientSocket socket;

		public ClientHandlerThread(TCPClientSocket socket) {
			this.socket = socket;
		}

		@Override
		public void run() {
			Object object;
			while (running) {
				try {
					object = socket.receive();
					server.processPacket(object, socket.getInetAddress(),
							socket.getPort());
				} catch (IOException | ClassNotFoundException e) {
					if (e instanceof EOFException) {
						break;
					}

					System.err
							.println("Exception generated while trying to recieve packet from client.");
					e.printStackTrace();
				}
			}

			try {
				socket.shutdown();
			} catch (IOException e) {
				System.err
						.println("Exception generated while trying to shutdown a connection with a client.");
				e.printStackTrace();
			}
		}
	}

	protected GameConnectionServer getServer() {
		return server;
	}

	protected boolean isRunning() {
		return running;
	}
}
