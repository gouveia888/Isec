package util;

import java.io.*;
import java.net.*;

public class DatagramUtil {

    public static boolean send(DatagramSocket socket, Serializable object, SocketAddress socketAddress) {
        ByteArrayOutputStream bOut = new ByteArrayOutputStream();
        try (ObjectOutputStream out = new ObjectOutputStream(bOut)) {
            out.writeObject(object);
            DatagramPacket packet = new DatagramPacket(
                    bOut.toByteArray(),
                    bOut.size(),
                    socketAddress
            );
            socket.send(packet);
        } catch (Exception _) {
            return false;
        }

        return true;
    }

    public static Object receive(DatagramSocket socket, SocketAddress socketAddress) {
        try {
            DatagramPacket responsePacket = new DatagramPacket(new byte[5000], 5000);
            socket.receive(responsePacket);
            ByteArrayInputStream bIn = new ByteArrayInputStream(responsePacket.getData(), 0, responsePacket.getLength());
            ObjectInputStream in = new ObjectInputStream(bIn);
            return in.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }


    public static MulticastSocket createMulticastSocket(InetSocketAddress multicastSocketAddress) throws IOException {
        //String groupAddress, int port
        MulticastSocket socket = new MulticastSocket(multicastSocketAddress.getPort()); // port
        //InetAddress group = InetAddress.getByName(inetSocketAddress.getHostName()); // groupAddress

        // Need to select the right network interface
        NetworkInterface networkInterface = NetworkInterface.getByInetAddress(InetAddress.getLocalHost());
        socket.joinGroup(multicastSocketAddress, networkInterface);

        return socket;
    }
}
