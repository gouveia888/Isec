package model;

import util.FancyLog;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class ServerDirectory {
    List<ServerDescriptor> registeredServers;

    public ServerDirectory() {
        registeredServers = new LinkedList<ServerDescriptor>();
    }

    public synchronized void addServer(ServerDescriptor server) {
        registeredServers.add(server);
        FancyLog.println("Registered server: " + server);
    }

    public synchronized ServerDescriptor getActiveServer() {
        // For simplicity, return the first server in the list
        if (!registeredServers.isEmpty()) {
            return registeredServers.getFirst();
        }
        return new ServerDescriptor("", -1, -1);
    }

    public synchronized boolean hasServer(String serverAddress, int serverReceiverPort) {
        ServerDescriptor serverDescriptor = getServer(serverAddress, serverReceiverPort);
        return serverDescriptor != null;
    }

    private synchronized ServerDescriptor getServer(String serverAddress, int serverReceiverPort) {
        for (ServerDescriptor server : registeredServers) {
            if (server.getAddress().equalsIgnoreCase(serverAddress) && server.getServerReceiverPort() == serverReceiverPort) {
                return server;
            }
        }
        return null;
    }

    public synchronized void updateServerHeartbeat(String serverAddress, int serverReceiverPort) {
        ServerDescriptor server = getServer(serverAddress, serverReceiverPort);
        if (server != null) {
            server.updateLastHeartbeat();
        }
    }

    public synchronized void removeInactiveServers() {
        Instant timeNow = Instant.now();

        var descriptorIterator = registeredServers.iterator();

        while (descriptorIterator.hasNext()) {
            ServerDescriptor server = descriptorIterator.next();
            if (Duration.between(server.getLastHeartbeat(), timeNow).getSeconds() > 17) {
                FancyLog.println("Server timed out. Removing: " + server);
                descriptorIterator.remove();
            }
        }
    }

    public void removeServer(ServerDescriptor serverDescriptor) {
        Iterator<ServerDescriptor> it = registeredServers.iterator();
        while (it.hasNext()) {
            ServerDescriptor current = it.next();

            if (current.equals(serverDescriptor)) {
                FancyLog.println("Unregistered server : " + current);
                it.remove();
                break;
            }
        }
    }
}
