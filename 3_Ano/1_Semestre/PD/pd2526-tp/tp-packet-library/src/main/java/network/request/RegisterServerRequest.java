package network.request;

import java.io.Serializable;

public record RegisterServerRequest(
        int serverReceiverPort,
        int clientReceiverPort
) implements Serializable {
}