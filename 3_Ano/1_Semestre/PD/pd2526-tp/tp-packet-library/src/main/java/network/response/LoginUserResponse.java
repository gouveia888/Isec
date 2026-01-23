package network.response;

import network.enums.AccountType;

import java.io.Serializable;

public record LoginUserResponse(
        boolean success,
        AccountType accountType,
        String message,
        String username,
        String email,
        String studentNumber
) implements Serializable {

    public static LoginUserResponse failure(String message) {
        return new LoginUserResponse(
                false,
                AccountType.INSTRUCTOR,
                message,
                "",
                "",
                ""
        );
    }

    public static LoginUserResponse success(AccountType accountType, String username, String email,  String studentNumber) {
        return new LoginUserResponse(
                true,
                accountType,
                "Logged in successfully",
                username,
                email,
                studentNumber
        );
    }
}
