package threads;

import dao.interfaces.SystemConfigDAO;
import dao.sqlite.SQLiteSystemConfigDAO;
import database.DatabaseManager;
import network.ServerManager;
import network.request.HeartbeatRequest;
import util.DatagramUtil;
import util.FancyLog;

import java.io.IOException;
import java.net.*;

public class HeartbeatReceiver implements Runnable {

    // server management
    private final ServerManager serverManager;
    private final DatabaseManager databaseManager;
    private final SystemConfigDAO systemConfigDAO;

    // communication
    private final InetSocketAddress multicastSocketAddress;
    private final MulticastSocket multicastReceiverSocket;

    public HeartbeatReceiver(
            ServerManager serverManager,
            DatabaseManager databaseManager,
            InetSocketAddress multicastSocketAddress
    ) throws IOException {
        this.serverManager = serverManager;
        this.databaseManager = databaseManager;
        this.multicastSocketAddress = multicastSocketAddress;

        systemConfigDAO = new SQLiteSystemConfigDAO(databaseManager);
        multicastReceiverSocket = DatagramUtil.createMulticastSocket(multicastSocketAddress);

        serverManager.registerSocket(multicastReceiverSocket);
    }

    @Override
    public void run() {
        FancyLog.println("Started HeartbeatReceiver thread.", FancyLog.Status.OK);

        while (serverManager.getIsRunning()) {
            Object obj = DatagramUtil.receive(multicastReceiverSocket, multicastSocketAddress);
            if(obj == null){
                break;
            }
            if (!(obj instanceof HeartbeatRequest heartbeatRequest)) {
                FancyLog.println("HeartbeatReceiver received invalid packet. Ignoring", FancyLog.Status.FAILED);
                continue;
            }
            // Ignore Heartbeats if we are the main server, or they came someone other than the server
            if(serverManager.isMainServer()||!serverManager.isHeartBeatFromMainServer(heartbeatRequest)){
                continue;
            }

            FancyLog.println("DEBUG: " + heartbeatRequest);
            int databaseVersion = systemConfigDAO.getDatabaseVersion();

            if (heartbeatRequest.databaseQuery().isBlank() && heartbeatRequest.databaseVersion() != databaseVersion) {
                break;
            } else if (!heartbeatRequest.databaseQuery().isBlank() && heartbeatRequest.databaseVersion() != databaseVersion + 1) {
                break;
            } else if (!heartbeatRequest.databaseQuery().isBlank() && heartbeatRequest.databaseVersion() == databaseVersion + 1) {
                if (!databaseManager.executeNetworkQuery(heartbeatRequest.databaseQuery())) {
                    break;
                }
            }
        }

        serverManager.signalShutdown();
        FancyLog.println("Closed HeartbeatReceiver thread.");
    }
}
