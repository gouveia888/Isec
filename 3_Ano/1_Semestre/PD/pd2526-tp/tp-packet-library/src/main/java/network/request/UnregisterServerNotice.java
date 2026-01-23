package network.request;

import java.io.Serializable;

// Used by the Server to tell the Directory that it is shutting down
public record UnregisterServerNotice(
        String address,
        int serverReceiverPort,
        int clientReceiverPort
) implements Serializable {
}
