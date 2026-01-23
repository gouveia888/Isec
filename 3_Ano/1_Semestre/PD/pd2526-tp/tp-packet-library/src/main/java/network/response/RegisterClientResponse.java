package network.response;

import java.io.Serializable;

public record RegisterClientResponse(
        String activeServerIp,
        int activeServerPort
) implements Serializable {
}
