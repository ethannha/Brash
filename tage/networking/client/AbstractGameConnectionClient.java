package tage.networking.client;

import tage.networking.AbstractGameConnection;

/**
 * Abstract class to define the protected methods necessary for a game
 * connection client.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public abstract class AbstractGameConnectionClient extends
		AbstractGameConnection implements IGameConnectionClient {
	/**
	 * This method is responsible for initializing variables and should call
	 * {@link #startClient()} when done.
	 */
	protected abstract void initClient();

	/**
	 * Responsible for calling {{@link #createReceivePacketsThread()} and the
	 * starting the thread.
	 */
	protected abstract void startClient();

	/**
	 * Creates the Thread responsible for receiving packets by the client.
	 * 
	 * @return the Thread which receives network packets
	 */
	protected abstract Thread createReceivePacketsThread();
}
