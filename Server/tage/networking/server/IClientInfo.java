package tage.networking.server;

import java.io.IOException;
import java.io.Serializable;

/**
 * General data structure to store client information necessary to implement the
 * {@link #sendPacket(Serializable)}.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public interface IClientInfo {
	/**
	 * Sends the {@link Serializable} object to the client whose information
	 * this data structure is storing.
	 * 
	 * @param object
	 * @throws IOException
	 */
	public void sendPacket(Serializable object) throws IOException;
}
