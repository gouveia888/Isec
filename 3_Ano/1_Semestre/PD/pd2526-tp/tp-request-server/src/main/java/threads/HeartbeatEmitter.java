package threads;

import dao.interfaces.SystemConfigDAO;
import dao.sqlite.SQLiteSystemConfigDAO;
import database.DatabaseManager;
import database.QueryLogger;
import network.ServerManager;
import network.request.HeartbeatRequest;
import network.response.HeartBeatResponse;
import util.DatagramUtil;
import util.FancyLog;

import java.net.*;

public class HeartbeatEmitter implements Runnable {

    // server management
    private final ServerManager serverManager;
    private final int serverReceiverPort;
    private final int clientReceiverPort;
    private DatabaseManager databaseManager;
    private final SystemConfigDAO systemConfigDAO;

    // shared communication
    private final DatagramSocket datagramSocket;

    // server communication
    private final SocketAddress multicastSocketAddress;
    //private DatagramSocket multicastSenderSocket;

    // directory communication
    private final SocketAddress directorySocketAddress;
    //private DatagramSocket directorySocket;


    public HeartbeatEmitter(
            ServerManager serverManager,
            int serverReceiverPort,
            int clientReceiverPort,
            DatabaseManager databaseManager,
            SocketAddress multicastSocketAddress,
            SocketAddress directorySocketAddress
    ) throws SocketException {
        this.serverManager = serverManager;
        this.serverReceiverPort = serverReceiverPort;
        this.clientReceiverPort = clientReceiverPort;
        this.databaseManager = databaseManager;
        this.multicastSocketAddress = multicastSocketAddress;
        this.directorySocketAddress = directorySocketAddress;

        systemConfigDAO = new SQLiteSystemConfigDAO(databaseManager);
        datagramSocket = new DatagramSocket();
        datagramSocket.setSoTimeout(10_000);

        serverManager.registerSocket(datagramSocket);
    }

    @Override
    public void run() {
        FancyLog.println("Started HeartbeatEmitter thread.", FancyLog.Status.OK);

        while (serverManager.getIsRunning()) {
            HeartbeatRequest heartbeatRequest = createHeartbeat();
            boolean sentBroadcast;
            boolean notifiedDirectoryServer;

            // Sync to make sure we don't send heartbeats from here mid-update
            synchronized (QueryLogger.logLock) {
                if (heartbeatRequest.databaseVersion() == -1) {
                    FancyLog.println("Couldn't get the database version. Exiting...", FancyLog.Status.FAILED);
                    serverManager.errorShutdown();
                    break;
                }

                sentBroadcast = DatagramUtil.send(datagramSocket, heartbeatRequest, multicastSocketAddress);
                notifiedDirectoryServer = DatagramUtil.send(datagramSocket, heartbeatRequest, directorySocketAddress);
            }

            if (!sentBroadcast) {
                FancyLog.println("Couldn't send broadcast.", FancyLog.Status.FAILED);
            }
            if (!notifiedDirectoryServer) {
                FancyLog.println("Couldn't notify the DirectoryServer", FancyLog.Status.FAILED);
            }

            HeartBeatResponse heartBeatResponse = (HeartBeatResponse) DatagramUtil.receive(datagramSocket, directorySocketAddress);
            if(heartBeatResponse == null){
                FancyLog.println("Directory stopped responding. Closing.", FancyLog.Status.FAILED);
                break;
            }

            // Save last one just for testing
            serverManager.updateMainServerAddress(heartBeatResponse.address(), heartBeatResponse.serverReceiverPort());

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        serverManager.signalShutdown();
        FancyLog.println("Closed HeartbeatEmitter thread.");
    }

    private HeartbeatRequest createHeartbeat() {
        return new HeartbeatRequest(
                systemConfigDAO.getDatabaseVersion(),
                "",
                serverReceiverPort,
                clientReceiverPort,
                serverManager.getLocalAddress()
        );
    }
}
