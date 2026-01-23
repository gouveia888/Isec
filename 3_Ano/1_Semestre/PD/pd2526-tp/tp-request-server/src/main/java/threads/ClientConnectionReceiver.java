package threads;

import controller.QuestionController;
import database.DatabaseManager;
import network.ServerManager;
import util.FancyLog;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

public class ClientConnectionReceiver implements Runnable {

    // server management
    private final ServerManager serverManager;
    private final DatabaseManager databaseManager;
    private final QuestionController questionController;

    private final SocketAddress multicastSocketAddress;
    private final int serverReceiverPort;
    private final int clientReceiverPort;


    // client communication
    private ServerSocket clientReceiverSocket;
    //private final ExecutorService clientThreadPool = Executors.newFixedThreadPool(10);
    private final List<Thread> clientThreads;

    public ClientConnectionReceiver(
            ServerManager serverManager,
            DatabaseManager databaseManager,
            QuestionController questionController,
            ServerSocket clientReceiverSocket,
            SocketAddress multicastSocketAddress,
            int serverReceiverPort,
            int clientReceiverPort
    ) {
        this.serverManager = serverManager;
        this.databaseManager = databaseManager;
        this.questionController = questionController;
        this.clientReceiverSocket = clientReceiverSocket;
        this.multicastSocketAddress = multicastSocketAddress;
        this.serverReceiverPort = serverReceiverPort;
        this.clientReceiverPort = clientReceiverPort;

        this.clientThreads = new ArrayList<>();
    }

    @Override
    public void run() {
        FancyLog.println("Started ClientConnectionReceiver thread.", FancyLog.Status.OK);

        while (serverManager.getIsRunning()) {
            try {
                Socket clientSocket = clientReceiverSocket.accept();
                serverManager.registerSocket(clientSocket);
                FancyLog.println("New client connected: %s\nStarting thread.".formatted(
                        clientSocket.getRemoteSocketAddress()));

                // handle client in thread pool
                ClientSessionHandler clientSessionHandler = new ClientSessionHandler(
                        serverManager,
                        databaseManager,
                        questionController,
                        clientSocket,
                        multicastSocketAddress,
                        serverReceiverPort,
                        clientReceiverPort
                );
                Thread thread = new Thread(clientSessionHandler);
                thread.start();
                clientThreads.add(thread);
                //clientThreadPool.submit(clientSessionHandler);

            } catch (IOException e) {
                break;
            }
        }

        FancyLog.println("ClientConnectionReceiver shutting down.");
        serverManager.signalShutdown();
        FancyLog.println("ClientConnectionReceiver awaiting threads.");


//        try {
//            clientThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
//        } catch (InterruptedException _) {
//        }
        for(Thread t: clientThreads){
            try {
                t.join();
            } catch (InterruptedException _) {
            }
        }

        FancyLog.println("Closed ClientConnectionReceiver thread.");
    }
}
