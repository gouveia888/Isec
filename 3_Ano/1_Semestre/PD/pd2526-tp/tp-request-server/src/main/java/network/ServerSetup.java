package network;

import config.ServerConfig;
import database.DatabaseManager;
import database.DatabaseManagerFactory;
import network.registry.DirectoryRegistrar;
import network.response.RegisterServerResponse;
import util.FancyLog;
import util.SocketUtil;

import java.net.*;
import java.sql.SQLException;
import java.util.Enumeration;


public class ServerSetup {
    private static final int MULTICAST_PORT = 3030;
    private static final String DEFAULT_INSTRUCTOR_CODE = "Isec2025";

    private final ServerConfig config;
    private boolean registeredWithDirectory_;

    public ServerSetup(ServerConfig config) {
        this.config = config;
        this.registeredWithDirectory_ = false;
    }

    public boolean registeredWithDirectory(){
        return this.registeredWithDirectory_;
    }

    public Server createServer() {
        boolean isMainServer;
        ServerSocket serverReceiverSocket;
        ServerSocket clientReceiverSocket;
        InetSocketAddress multicastSocketAddress;
        InetSocketAddress directorySocketAddress;
        InetSocketAddress mainServerSocketAddress;
        InetSocketAddress thisServerSocketAddress;


        FancyLog.println("Starting server setup...");


        // create client and server TCP sockets (we must know the ports for registration)
        try {
            FancyLog.println("Opening client and server listener TCP ports...");
            serverReceiverSocket = new ServerSocket(0);
            clientReceiverSocket = new ServerSocket(0);
            {
                String formatString = """
                        Opened TCP ports.
                        Server<=>Server port: %d
                        Client<=>Server port: %s""";
                String formattedString = formatString.formatted(
                        serverReceiverSocket.getLocalPort(),
                        clientReceiverSocket.getLocalPort());
                FancyLog.println(formattedString, FancyLog.Status.OK);
            }
        } catch (Exception e) {
            FancyLog.println("Could not create server sockets.", FancyLog.Status.FAILED);
            return null;
        }


        // create receiver and sender multicast sockets
        try {
            multicastSocketAddress = new InetSocketAddress(config.getMulticastAddress(), MULTICAST_PORT);
        } catch (Exception e) {
            FancyLog.println("Failed to get multicast sockets address", FancyLog.Status.FAILED);
            return null;
        }

        // register with directory
        try {
            directorySocketAddress = new InetSocketAddress(
                    config.getDirectoryServerAddress(), config.getDirectoryServerPort()
            );
        } catch (Exception _) {
            FancyLog.println("Invalid directory socket address.", FancyLog.Status.FAILED);
            return null;
        }
        DirectoryRegistrar registrar = new DirectoryRegistrar(
                directorySocketAddress,
                serverReceiverSocket.getLocalPort(),
                clientReceiverSocket.getLocalPort());
        FancyLog.println("Registering with DirectoryServer...");
        RegisterServerResponse response = registrar.register();
        if (response == null) {
            FancyLog.println("Registration failed. Server will exit.", FancyLog.Status.FAILED);
            return null;
        }
        registeredWithDirectory_ = true;
        try {
            mainServerSocketAddress = new InetSocketAddress(response.mainServerAddress(), response.mainServerPort());
            FancyLog.println("MAIN server at: " + mainServerSocketAddress.getAddress().getHostAddress() + ":" + mainServerSocketAddress.getPort());

            thisServerSocketAddress = new InetSocketAddress(response.requesterAddress(), response.requesterPort());
            FancyLog.println("THIS server at: " + thisServerSocketAddress.getAddress().getHostAddress() + ":" + thisServerSocketAddress.getPort());
        } catch (Exception e) {
            FancyLog.println("Couldn't determine the MAIN server address.", FancyLog.Status.FAILED);
            registrar.unregister();
            return null;
        }


        // determine if we are the main server
        //isMainServer = checkMainServer(response, serverReceiverSocket);
        isMainServer = mainServerSocketAddress.equals(thisServerSocketAddress);
        if (isMainServer) {
            FancyLog.println("Acting as MAIN server.", FancyLog.Status.INFO);
        } else {
            FancyLog.println("Acting as BACKUP server.", FancyLog.Status.INFO);
        }


        // get the correct database
        DatabaseManager databaseManager = createDatabaseManager(config.getDatabaseDirectory(), isMainServer);
        if (databaseManager == null) {
            FancyLog.println("Couldn't connect to the SQLite database.", FancyLog.Status.FAILED);
            registrar.unregister();
            return null;
        }
        FancyLog.println("Connection to the SQLite database \"" + databaseManager.getDbFilePath() + "\" established.", FancyLog.Status.OK);


        // if it's a backup server, sync the database
        if (!isMainServer) {
            FancyLog.println("Requesting up to date database.");
            if(!requestDatabaseSync(databaseManager, mainServerSocketAddress)){
                FancyLog.println("Couldn't get up to date database.", FancyLog.Status.FAILED);
                registrar.unregister();
                return null;
            }
            FancyLog.println("Database received.", FancyLog.Status.OK);
        }


        // build the server
        return new Server(
                config,
                mainServerSocketAddress,
                thisServerSocketAddress,
                databaseManager,
                clientReceiverSocket,
                serverReceiverSocket,
                multicastSocketAddress,
                directorySocketAddress
        );
    }

    private boolean requestDatabaseSync(DatabaseManager databaseManager, InetSocketAddress mainServerSocketAddress) {
        try (Socket socket = new Socket(
                mainServerSocketAddress.getAddress().getHostAddress(),
                mainServerSocketAddress.getPort())) {
            Object object = SocketUtil.receive(socket);
            if(!(object instanceof byte[] bytes)){
                return false;
            }
            databaseManager.setDatabaseBytes(bytes);

            return true;
        } catch (Exception _) {
            return false;
        }
    }


    private static DatabaseManager createDatabaseManager(String databaseDirectory, boolean isMain) {
        try {
            DatabaseManager databaseManager;
            if (isMain) {
                databaseManager = DatabaseManagerFactory.loadLatestOrCreateNew(databaseDirectory, DEFAULT_INSTRUCTOR_CODE);
            } else {
                databaseManager = DatabaseManagerFactory.createNewDatabase(databaseDirectory, DEFAULT_INSTRUCTOR_CODE);
            }
            return databaseManager;
        } catch (SQLException e) {
            return null;
        }
    }


    private static boolean checkMainServer(RegisterServerResponse registerResponse, ServerSocket serverReceiverSocket) {
        try {
            boolean isSamePort = registerResponse.mainServerPort() == serverReceiverSocket.getLocalPort();
            boolean isSameAddress = registerResponse.mainServerAddress().equalsIgnoreCase(serverReceiverSocket.getInetAddress().getHostAddress());
            boolean isLocalAddress = isLocalAddress(InetAddress.getByName(registerResponse.mainServerAddress()));

            return (isSamePort && (isSameAddress || isLocalAddress));
        } catch (Exception _) {
            return false;
        }
    }

    private static boolean isLocalAddress(InetAddress addr) {
        try {
            // Check if it’s a loopback address (127.x.x.x or ::1)
            if (addr.isLoopbackAddress()) return true;

            // Check all network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface nif = interfaces.nextElement();
                Enumeration<InetAddress> addresses = nif.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress localAddr = addresses.nextElement();
                    if (addr.equals(localAddr)) {
                        return true; // It's one of our local addresses
                    }
                }
            }
        } catch (SocketException _) {
            return false;
        }
        return false;
    }
}