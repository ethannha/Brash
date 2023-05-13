package tage.networking;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * A general abstract class to implement methods not specific to a game
 * connection.
 * 
 * @author Kyle Matz
 * 
 * Modified from the SAGE Networking package for the RAGE game engine by Juan E. Ruiz.
 * Ported to TAGE by Scott Gordon.
 * 
 */
public abstract class AbstractGameConnection implements IGameConnection {
	/**
	 * A method to get the local IP address. This will return the default IP;
	 * meaning if the machine is connected to more than one network the returned
	 * IP address could be different than the one the user wishes to use.
	 * 
	 * See {@link java.net.InetAddress#getLocalHost()} for the return value.
	 * 
	 * @return the default local address
	 */
	@Override
	public InetAddress getLocalInetAddress() throws UnknownHostException {
		InetAddress address = InetAddress.getLocalHost();
		return address;
	}

	/**
	 * {@inheritDoc} This is intended to be used when connected to several
	 * networks as {@link #getLocalInetAddress() getLocalInetAddress()} may
	 * return a different address than the one desired.
	 * 
	 */
	@Override
	public Collection<InetAddress> getAllLocalAddress() throws SocketException {
		ArrayList<InetAddress> addressList = new ArrayList<InetAddress>();

		Enumeration<NetworkInterface> networkInterfaces = NetworkInterface
				.getNetworkInterfaces();
		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface ni = networkInterfaces.nextElement();
			Enumeration<InetAddress> ina = ni.getInetAddresses();

			while (ina.hasMoreElements()) {
				addressList.add(ina.nextElement());
			}
		}

		return addressList;
	}
}
