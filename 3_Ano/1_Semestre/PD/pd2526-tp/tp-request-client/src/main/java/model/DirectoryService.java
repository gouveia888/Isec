package model;

import network.request.ActiveServerRequest;
import network.response.ActiveServerResponse;
import util.DatagramUtil;

import java.net.DatagramSocket;
import java.net.SocketException;

public class DirectoryService {
    private final QuizAppManager quizAppManager;

    public DirectoryService(QuizAppManager quizAppManager) {
        this.quizAppManager = quizAppManager;
    }

    public ActiveServerResponse getActiveServer() {;
        // In a real implementation, this method would query a directory service
        // to get the current active server's address and port.
        // Here, we return a placeholder response.
        try{
            ActiveServerRequest activeServerRequest = new ActiveServerRequest();
            DatagramSocket socket = new DatagramSocket();
            socket.setSoTimeout(5000);
            DatagramUtil.send(socket, activeServerRequest, quizAppManager.getDirectoryServerSocketAddress());
            return (ActiveServerResponse) DatagramUtil.receive(socket, quizAppManager.getDirectoryServerSocketAddress());
        } catch (SocketException e) {
            return null;
        }
    }


}
