package network.request;

import java.io.Serializable;

public record LoginUserRequest(
        String email,
        String password
) implements Serializable {
}
