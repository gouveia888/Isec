package threads;

import database.DatabaseManager;
import util.FancyLog;
import util.SocketUtil;

import java.net.Socket;

public class ServerDatabaseTransmitter implements Runnable {
    private final DatabaseManager databaseManager;
    private final Socket serverSocket;

    public ServerDatabaseTransmitter(DatabaseManager databaseManager, Socket serverSocket) {
        this.databaseManager = databaseManager;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        FancyLog.println("Started ServerDatabaseTransmitter thread.", FancyLog.Status.OK);

        try {
            FancyLog.println("Sending data...");
            SocketUtil.send(serverSocket, databaseManager.getDatabaseBytes());
            FancyLog.println("Sent database.", FancyLog.Status.OK);
        } catch (Exception _){
            FancyLog.println("Failed to sent the database", FancyLog.Status.FAILED);
        }

        FancyLog.println("Closed ServerDatabaseTransmitter thread.");
    }
}
