package network;

import config.ServerConfig;
import controller.QuestionController;
import database.DatabaseManager;
import network.request.UnregisterServerNotice;
import threads.ClientConnectionReceiver;
import threads.HeartbeatEmitter;
import threads.HeartbeatReceiver;
import threads.ServerConnectionReceiver;
import util.DatagramUtil;
import util.FancyLog;

import java.net.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class Server {
    private final ServerConfig config;

    // server status
    private final AtomicBoolean isRunning; // package-private so that the ServerManager can access it
    private InetSocketAddress mainServerSocketAddress;
    private final InetSocketAddress thisServerSocketAddress;
    private final CountDownLatch shutdownSignal;

    // server management
    private final ServerManager serverManager;
    private final DatabaseManager databaseManager;

    // controllers
    private final QuestionController questionController;

    // directory server communication
    private final InetSocketAddress directorySocketAddress;

    // server communication
    private final ServerSocket serverReceiverSocket;
    private final InetSocketAddress multicastSocketAddress;

    // client communication
    private final ServerSocket clientReceiverSocket;

    // threading
    private Thread heartbeatEmitterThread;
    private Thread heartbeatReceiverThread;
    private Thread serverDatabaseTransmitterThread;
    private Thread clientConnectionReceiverThread;

    public Server(ServerConfig config,
                  InetSocketAddress mainServerSocketAddress,
                  InetSocketAddress thisServerSocketAddress,
                  DatabaseManager databaseManager,
                  ServerSocket clientReceiverSocket,
                  ServerSocket serverReceiverSocket,
                  InetSocketAddress multicastSocketAddress,
                  InetSocketAddress directorySocketAddress
    ) {
        this.config = config;
        this.mainServerSocketAddress = mainServerSocketAddress;
        this.thisServerSocketAddress = thisServerSocketAddress;
        this.databaseManager = databaseManager;
        this.clientReceiverSocket = clientReceiverSocket;
        this.serverReceiverSocket = serverReceiverSocket;
        this.multicastSocketAddress = multicastSocketAddress;
        this.directorySocketAddress = directorySocketAddress;

        this.isRunning = new AtomicBoolean(true);
        this.shutdownSignal = new CountDownLatch(1);
        this.serverManager = new ServerManager(this);

        //this.userController = new UserController(databaseManager);
        this.questionController = new QuestionController(databaseManager);

        // Need to register all the sockets so we can properly close it all
        serverManager.registerSocket(clientReceiverSocket);
        serverManager.registerSocket(serverReceiverSocket);
    }

    public void start() {
        FancyLog.println("Server startup complete. Running...", FancyLog.Status.OK);
        isRunning.set(true);


        try {
            heartbeatEmitterThread = new Thread(new HeartbeatEmitter(
                    serverManager,
                    serverReceiverSocket.getLocalPort(),
                    clientReceiverSocket.getLocalPort(),
                    databaseManager,
                    multicastSocketAddress,
                    directorySocketAddress
            ));
            heartbeatReceiverThread = new Thread(new HeartbeatReceiver(
                    serverManager,
                    databaseManager,
                    multicastSocketAddress
            ));
            serverDatabaseTransmitterThread = new Thread(new ServerConnectionReceiver(
                    serverManager,
                    databaseManager,
                    serverReceiverSocket
            ));
            clientConnectionReceiverThread = new Thread(new ClientConnectionReceiver(
                    serverManager,
                    databaseManager,
                    questionController,
                    clientReceiverSocket,
                    multicastSocketAddress,
                    serverReceiverSocket.getLocalPort(),
                    clientReceiverSocket.getLocalPort()
            ));


            heartbeatEmitterThread.start();
            heartbeatReceiverThread.start();
            serverDatabaseTransmitterThread.start();
            clientConnectionReceiverThread.start();

            shutdownSignal.await();
            System.exit(0);
        } catch (Exception _) {
            FancyLog.println("An error occurred setting up the server threads.", FancyLog.Status.FAILED);
        }
    }

    // --------------------------
    // Shutdown
    // --------------------------
    public void signalShutdown() {
        shutdownSignal.countDown();
    }

    public void trueShutdown(){
        if (!isRunning.compareAndSet(true, false)) {
            return;
        }

        FancyLog.println("Shutting down server...", FancyLog.Status.INFO);
        // Notify the directory server
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            UnregisterServerNotice notice = new UnregisterServerNotice(
                    serverManager.getLocalAddress(),
                    serverReceiverSocket.getLocalPort(),
                    clientReceiverSocket.getLocalPort()
            );
            DatagramUtil.send(datagramSocket, notice, directorySocketAddress);
            FancyLog.println("Directory server notified.");
        } catch (Exception _) {
            FancyLog.println("Couldn't notify directory server. Closing anyways.", FancyLog.Status.FAILED);
        }
        FancyLog.println("Closing sockets.");
        serverManager.closeAllSockets();
        FancyLog.println("Sockets closed.");

        try {
            heartbeatEmitterThread.join();
            heartbeatReceiverThread.join();
            serverDatabaseTransmitterThread.join();
            clientConnectionReceiverThread.join();
        } catch (Exception _) {
        }

        FancyLog.println("Shutdown finished.");
    }

    public synchronized InetSocketAddress getMainServerSocketAddress() {
        return mainServerSocketAddress;
    }

    public synchronized void setMainServerSocketAddress(InetSocketAddress mainServerSocketAddress) {
        InetSocketAddress oldMainServerSocketAddress = this.mainServerSocketAddress;
        this.mainServerSocketAddress = mainServerSocketAddress;
        if (!mainServerSocketAddress.equals(oldMainServerSocketAddress)) {
            FancyLog.println("New MAIN server: " + this.mainServerSocketAddress);
        }
    }

    public InetSocketAddress getThisServerSocketAddress() {
        return thisServerSocketAddress;
    }

    public boolean getIsRunning() {
        return isRunning.get();
    }

    public void setIsRunning(boolean newValue) {
        isRunning.set(newValue);
    }
}