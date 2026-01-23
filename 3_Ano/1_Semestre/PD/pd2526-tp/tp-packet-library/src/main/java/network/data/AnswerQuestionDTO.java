package network.data;

import java.io.Serializable;

public class AnswerQuestionDTO implements Serializable {

    private QuestionDTO question;
    private int selectedOption;

    public AnswerQuestionDTO(QuestionDTO question, int selectedOption) {
        this.question = question;
        this.selectedOption = selectedOption;
    }

    public QuestionDTO getQuestion() {
        return question;
    }
    public int getSelectedOption() {
        return selectedOption;
    }

}
