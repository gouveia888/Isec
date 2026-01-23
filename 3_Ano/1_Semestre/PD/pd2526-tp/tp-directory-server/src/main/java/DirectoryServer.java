import model.ServerDirectory;
import model.SocketListener;
import java.net.DatagramSocket;


/**
 * DirectoryServer implements a directory server that keeps track of available servers.
 * It listens for server registrations and maintains a list of active servers.
 * When it receives a heartbeat from a server that is not registered, it ignores the heartbeat.
 * When it receives a request from a client for the list of active servers, it responds with the oldest registered server.
 * If a server fails to send heartbeats for 17 seconds, it is considered inactive and removed from the list.
 * The directory server needs to be the first component to be started in the system.
 * If for any reason it stops running, all servers should be shut down before restarting the directory server.
 */
public class DirectoryServer {
    public static void main(String[] args) {
        ServerDirectory serverDirectory = new ServerDirectory();
        DatagramSocket socket;
        int listeningPort = -1;

        // Validate arguments
        if (args.length != 1) {
            System.out.println("Usage: java DirectoryServer <listeningPort>");
            return;
        }
        try {
            listeningPort = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("Invalid port number.");
            System.exit(1);
        }


        try {
            // Create UDP socket to listen for incoming packets from servers and clients
            socket = new DatagramSocket(listeningPort);

            // Announce that the directory server has started
            System.out.println("Directory server started on port " + listeningPort + ".");

            // Start listening to connections
            SocketListener listener = new SocketListener(serverDirectory, socket);
            listener.run();

        } catch (Exception e) {
            System.exit(1);
        }

        System.out.println("Directory server closing...");
    }
}