package model;

import network.request.ActiveServerRequest;
import network.request.HeartbeatRequest;
import network.request.RegisterServerRequest;
import network.request.UnregisterServerNotice;
import network.response.ActiveServerResponse;
import network.response.HeartBeatResponse;
import network.response.RegisterServerResponse;
import util.DatagramUtil;
import util.FancyLog;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SocketListener {
    private static final int BUFFER_SIZE = 4096;

    private final boolean running;
    private final ServerDirectory serverDirectory;
    private final DatagramSocket socket;

    public SocketListener(ServerDirectory serverDirectory, DatagramSocket socket) {
        this.running = true;
        this.serverDirectory = serverDirectory;
        this.socket = socket;
    }

    public void run() {
        DatagramPacket receivePacket;
        Object genericRequest;

        while (running) {
            // Await incoming packets
            if ((receivePacket = awaitPacket()) == null || (genericRequest = extractRequest(receivePacket)) == null) {
                FancyLog.println("Couldn't receive packet.", FancyLog.Status.FAILED);
                continue;
            }

            serverDirectory.removeInactiveServers();
            FancyLog.println("Active server: " + serverDirectory.getActiveServer());

            // Handle requests
            switch (genericRequest) {

                // server messages
                case RegisterServerRequest request -> {
                    if (!handleRegisterRequest(request, receivePacket)) {
                        FancyLog.println("Couldn't handle server register request.", FancyLog.Status.FAILED);
                    }
                }
                case UnregisterServerNotice notice -> {
                    handleUnregisterRequest(notice, receivePacket);
                }
                case HeartbeatRequest request -> {
                    if (!handleHeartBeat(request, receivePacket)) {
                        FancyLog.println("Couldn't handle heartbeat.", FancyLog.Status.FAILED);
                    }
                }


                // client messages
                case ActiveServerRequest request -> {
                    if (!handleActiveServerRequest(request, receivePacket)) {
                        FancyLog.println("Couldn't handle heartbeat.", FancyLog.Status.FAILED);
                    }
                }

                default -> {
                    FancyLog.println("Received Invalid message. Ignoring...", FancyLog.Status.FAILED);
                }
            }
        }
    }

    private void handleUnregisterRequest(UnregisterServerNotice notice, DatagramPacket receivePacket) {
        ServerDescriptor serverDescriptor = new ServerDescriptor(
                notice.address(),
                notice.clientReceiverPort(),
                notice.serverReceiverPort()
        );
        serverDirectory.removeServer(serverDescriptor);
    }

    private boolean handleActiveServerRequest(ActiveServerRequest request, DatagramPacket receivePacket) {
        ServerDescriptor activeAddressServer = serverDirectory.getActiveServer();
        ActiveServerResponse response = new ActiveServerResponse(
                activeAddressServer.getAddress(),
                activeAddressServer.getClientReceiverPort()
        );
        if (activeAddressServer.getClientReceiverPort() == -1) {
            FancyLog.println("Client request received, but no active server is available.", FancyLog.Status.FAILED);
        }
        return DatagramUtil.send(socket, response, receivePacket.getSocketAddress());
    }


    private DatagramPacket awaitPacket() {
        byte[] buffer = new byte[BUFFER_SIZE];
        DatagramPacket receivePacket = new DatagramPacket(buffer, BUFFER_SIZE);
        try {
            socket.receive(receivePacket);
        } catch (IOException _) {
            return null;
        }
        return receivePacket;
    }

    private Object extractRequest(DatagramPacket receivePacket) {
        Object request = null;
        try {
            // Deserialize the received object
            ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(receivePacket.getData(), 0, receivePacket.getLength()));
            request = in.readObject();
        } catch (IOException | ClassNotFoundException _) {

        }
        return request;
    }

    private boolean handleRegisterRequest(RegisterServerRequest request, DatagramPacket receivePacket) {
        String serverAddress = receivePacket.getAddress().getHostAddress();

        // Add the server to the directory
        ServerDescriptor serverDescriptor = ServerDescriptor.fromRegisterRequest(request, serverAddress);
        serverDirectory.addServer(serverDescriptor);

        // Send a response back to the server
        ServerDescriptor activeServer = serverDirectory.getActiveServer();
        RegisterServerResponse response = new RegisterServerResponse(
                activeServer.getAddress(),
                activeServer.getServerReceiverPort(),
                serverDescriptor.getAddress(),
                serverDescriptor.getServerReceiverPort()
        );
        return sendServerRegisterResponse(receivePacket, response);
    }

    private boolean handleHeartBeat(HeartbeatRequest request, DatagramPacket receivePacket) {
        String serverAddress = receivePacket.getAddress().getHostAddress();
        int serverReceiverPort = request.serverReceiverPort();

        // Check if it's an existent server
        if (!serverDirectory.hasServer(serverAddress, request.serverReceiverPort())) {
            FancyLog.println("Server is not registered. Ignoring...", FancyLog.Status.FAILED);
            return true;
        }

        // Update timestamp
        serverDirectory.updateServerHeartbeat(serverAddress, serverReceiverPort);
        ServerDescriptor activeServer = serverDirectory.getActiveServer();
        HeartBeatResponse response = new HeartBeatResponse(activeServer.getAddress(), activeServer.getServerReceiverPort());
        DatagramUtil.send(socket, response, receivePacket.getSocketAddress());
        return true;
    }


    private boolean sendServerRegisterResponse(DatagramPacket receivePacket, RegisterServerResponse response) {
        try {
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bout);
            out.writeObject(response);
            out.flush();

            DatagramPacket sendPacket = new DatagramPacket(bout.toByteArray(), bout.size(), receivePacket.getAddress(), receivePacket.getPort());
            socket.send(sendPacket);
            return true;
        } catch (IOException _) {
            return false;
        }
    }
}
