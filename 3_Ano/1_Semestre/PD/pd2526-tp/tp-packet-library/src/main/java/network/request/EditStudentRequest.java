package network.request;

import java.io.Serializable;

public record EditStudentRequest(
        String name,
        String email,
        String passwordHash,
        String studentCode
) implements Serializable { }
