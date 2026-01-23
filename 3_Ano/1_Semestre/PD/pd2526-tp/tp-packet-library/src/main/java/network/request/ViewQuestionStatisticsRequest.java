package network.request;

import java.io.Serializable;

public record ViewQuestionStatisticsRequest(
        int questionId
) implements Serializable {
}
