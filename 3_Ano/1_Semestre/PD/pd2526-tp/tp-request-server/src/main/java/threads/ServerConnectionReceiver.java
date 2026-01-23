package threads;

import database.DatabaseManager;
import network.ServerManager;
import util.FancyLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerConnectionReceiver implements Runnable {

    // server management
    private final ServerManager serverManager;
    private final DatabaseManager databaseManager;

    // server communication
    private final ServerSocket serverReceiverSocket;

    List<Thread> threads;


    public ServerConnectionReceiver(
            ServerManager serverManager,
            DatabaseManager databaseManager,
            ServerSocket serverReceiverSocket
    ) {
        this.serverManager = serverManager;
        this.databaseManager = databaseManager;
        this.serverReceiverSocket = serverReceiverSocket;

        threads = new ArrayList<>();
    }

    // ServerDatabaseTransmitter
    @Override
    public void run() {
        FancyLog.println("Started ServerConnectionReceiver thread.", FancyLog.Status.OK);

        while (serverManager.getIsRunning()) {
            try {
                Socket serverSocket = serverReceiverSocket.accept();
                serverManager.registerSocket(serverSocket);
                if (serverManager.isMainServer()) {
                    FancyLog.println("Received database sync request.");
                    //FancyLog.println("Received database sync request. Sending data...");
                    //SocketUtil.send(serverSocket, databaseManager.getDatabaseBytes());
                    //FancyLog.println("Sent database.", FancyLog.Status.OK);
                    Thread transmitterThread = new Thread(
                            new ServerDatabaseTransmitter(databaseManager, serverSocket));
                    threads.add(transmitterThread);
                    transmitterThread.start();
                } else {
                    FancyLog.println("Received database sync request. But is not MAIN server. Ignoring...", FancyLog.Status.FAILED);
                }
            } catch (IOException e) {
                break;
            }
        }

        FancyLog.println("ServerConnectionReceiver shutting down.");
        serverManager.signalShutdown();
        FancyLog.println("ServerConnectionReceiver awaiting threads.");

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        FancyLog.println("Closed ServerConnectionReceiver thread.");
    }
}
