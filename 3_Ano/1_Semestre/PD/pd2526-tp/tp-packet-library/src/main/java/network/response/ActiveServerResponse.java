package network.response;

import java.io.Serializable;

public record ActiveServerResponse(
        String address,
        int port
) implements Serializable {
}
