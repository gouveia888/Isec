package network.request;

import java.io.Serializable;

public record HeartbeatRequest(
        int databaseVersion,
        String databaseQuery,
        int serverReceiverPort,
        int clientReceiverPort,
        String address
) implements Serializable {
}
