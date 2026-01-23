package network.response;

import network.enums.AccountType;

import java.io.Serializable;

public record EditUserResponse(
        boolean success,
        String message,
        AccountType accountType,
        String username,
        String email,
        String studentNumber
) implements Serializable {

    public static EditUserResponse failure(String message) {
        return new EditUserResponse(false, message, AccountType.STUDENT, "", "", "");
    }
}
