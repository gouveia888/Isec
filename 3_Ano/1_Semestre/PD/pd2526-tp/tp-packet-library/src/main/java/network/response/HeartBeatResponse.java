package network.response;

import java.io.Serializable;

public record HeartBeatResponse(
        String address,
        int serverReceiverPort
) implements Serializable {
}
