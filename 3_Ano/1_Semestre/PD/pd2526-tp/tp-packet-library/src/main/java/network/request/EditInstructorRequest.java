package network.request;

import java.io.Serializable;

public record EditInstructorRequest(
        String name,
        String email,
        String passwordHash
) implements Serializable {
}
