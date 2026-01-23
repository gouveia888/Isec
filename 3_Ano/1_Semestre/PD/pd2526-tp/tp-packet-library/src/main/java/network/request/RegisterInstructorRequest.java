package network.request;

import java.io.Serializable;

public record RegisterInstructorRequest(
        String name,
        String email,
        String passwordHash,
        String instructor_code
) implements Serializable {
}
