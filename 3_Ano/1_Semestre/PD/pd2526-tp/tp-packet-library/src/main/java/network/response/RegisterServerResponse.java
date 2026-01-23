package network.response;

import java.io.Serializable;

public record RegisterServerResponse(
        String mainServerAddress,
        int mainServerPort,
        String requesterAddress,
        int requesterPort
) implements Serializable {
}
