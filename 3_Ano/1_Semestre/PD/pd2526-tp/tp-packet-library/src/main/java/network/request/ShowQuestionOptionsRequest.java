package network.request;

import java.io.Serializable;

public record ShowQuestionOptionsRequest(
        int accessCode
) implements Serializable {
}
