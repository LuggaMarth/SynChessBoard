package at.synchess.boardsoftware.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * NetworkManager is a static class in which methods used for
 * retrieving information about networks is processed and returned.
 *
 * @author Luca Marth
 */
public class NetworkManager {
    private static final String LOCALHOST = "127.0.0.1";

    public static String getIpV4AddressAsString(String interfaceName) {
        try {
            // get interface by name
            NetworkInterface intf = NetworkInterface.getByName(interfaceName);

            // pre-check if not available
            if (intf == null) return LOCALHOST;

            // fetch all addresses
            Enumeration<InetAddress> addresses = intf.getInetAddresses();
            while(addresses.hasMoreElements()) {
                // only return the IPv4 address
                InetAddress curr = addresses.nextElement();
                if(curr instanceof Inet4Address) return curr.getHostAddress();
            }

            // if no match, return nothing
            return LOCALHOST;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }
}
