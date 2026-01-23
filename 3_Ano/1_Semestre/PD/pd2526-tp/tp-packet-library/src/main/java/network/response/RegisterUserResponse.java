package network.response;

import network.enums.AccountType;

import java.io.Serializable;

public record RegisterUserResponse(
        boolean success,
        String message
) implements Serializable {

    public static RegisterUserResponse success(String message) {
        return new RegisterUserResponse(true, message);
    }

    public static RegisterUserResponse failure(String message) {
        return new RegisterUserResponse(false, message);
    }
}
