package network;

import network.request.HeartbeatRequest;
import util.FancyLog;

import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

//  This class has a reference to all the sockets, so that it can close them all
//    when the server needs to shut down.
//  We need this because otherwise threads are blocked waiting for the packages.
//  Since we don't know all the sockets we need at the start, the threads are still responsible
//    for creating their own sockets. They then just register those with this thread.
public class ServerManager {
    private final Server server;

    // references for all the existing sockets, so we can close everything
    private final List<ServerSocket> serverSocketList;
    private final List<Socket> tcpSocketList;
    private final List<DatagramSocket> udpSocketList;
    private final Object socketListLock = new Object();

    public ServerManager(Server server) {
        this.server = server;
        this.serverSocketList = new ArrayList<>();
        this.tcpSocketList = new ArrayList<>();
        this.udpSocketList = new ArrayList<>();
    }

    public boolean getIsRunning() {
        return server.getIsRunning();
    }

    public void setIsRunning(boolean newValue) {
        server.setIsRunning(newValue);
    }

    public boolean isMainServer() {
        return server.getMainServerSocketAddress().equals(server.getThisServerSocketAddress());
    }

    public void updateMainServerAddress(String address, int port) {
        InetSocketAddress newMainServerAddress = new InetSocketAddress(address, port);
        server.setMainServerSocketAddress(newMainServerAddress);
    }

    // This method can be called by any thread to shut down the server
    public synchronized void errorShutdown() {
        server.setIsRunning(false);
    }

    public boolean isHeartBeatFromMainServer(HeartbeatRequest request) {
        try {
            InetSocketAddress requestSocketAddress = new InetSocketAddress(request.address(), request.serverReceiverPort());
            return requestSocketAddress.equals(server.getMainServerSocketAddress());
        } catch (Exception _) {
            return false;
        }
    }

    public String getLocalAddress() {
        return server.getThisServerSocketAddress().getAddress().getHostAddress();
    }

    public void registerSocket(ServerSocket socket){
        synchronized (socketListLock) {
            serverSocketList.add(socket);
        }
    }

    public void registerSocket(Socket socket){
        synchronized (socketListLock) {
            tcpSocketList.add(socket);
        }
    }

    public void registerSocket(DatagramSocket socket){
        synchronized (socketListLock) {
            udpSocketList.add(socket);
        }
    }

    public void closeAllSockets(){
        synchronized (socketListLock) {
            serverSocketList.forEach(s -> {
                try {
                    s.close();
                } catch (Exception _) {
                    FancyLog.println("Closed busy server socket.");
                }
            });
            tcpSocketList.forEach(s -> {
                try {
                    s.close();
                } catch (Exception _) {
                    FancyLog.println("Closed busy socket.");
                }
            });
            udpSocketList.forEach(s -> {
                try {
                    s.close();
                } catch (Exception _) {
                    FancyLog.println("Closed busy datagram socket.");
                }
            });
        }
    }


    public void signalShutdown(){
        server.signalShutdown();
    }

}
