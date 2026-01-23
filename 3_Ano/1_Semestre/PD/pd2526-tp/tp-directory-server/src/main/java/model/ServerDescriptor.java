package model;


import network.request.RegisterServerRequest;

import java.time.Instant;
import java.util.Objects;

public class ServerDescriptor {
    private final String address;
    private final int clientReceiverPort;
    private final int serverReceiverPort;
    private Instant lastHeartbeat;

    public ServerDescriptor(String address, int clientReceiverPort, int serverReceiverPort) {
        this.address = address;
        this.clientReceiverPort = clientReceiverPort;
        this.serverReceiverPort = serverReceiverPort;
        this.lastHeartbeat = Instant.now();
    }

    public static ServerDescriptor fromRegisterRequest(RegisterServerRequest request, String address) {
        return new ServerDescriptor(
                address,
                request.clientReceiverPort(),
                request.serverReceiverPort()
        );
    }

    public String getAddress() {
        return address;
    }

    public int getClientReceiverPort() {
        return clientReceiverPort;
    }

    public int getServerReceiverPort() {
        return serverReceiverPort;
    }

    public Instant getLastHeartbeat() {
        return lastHeartbeat;
    }

    public void updateLastHeartbeat() {
        this.lastHeartbeat = Instant.now();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass()) return false;
        ServerDescriptor that = (ServerDescriptor) object;
        return clientReceiverPort == that.clientReceiverPort && serverReceiverPort == that.serverReceiverPort && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, clientReceiverPort, serverReceiverPort);
    }

    @Override
    public String toString() {
        return "ServerDescriptor{" +
                "address='" + address + '\'' +
                ", clientReceiverPort=" + clientReceiverPort +
                ", serverReceiverPort=" + serverReceiverPort +
                '}';
    }
}
