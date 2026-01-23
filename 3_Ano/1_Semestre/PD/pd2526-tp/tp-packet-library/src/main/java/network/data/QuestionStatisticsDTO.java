package network.data;

import java.io.Serializable;
import java.util.List;

public class QuestionStatisticsDTO implements Serializable {
    private QuestionDTO questionDTO;
    private List<StudentAnswerDTO> studentAnswerDTOList;

    public QuestionStatisticsDTO(QuestionDTO questionDTO, List<StudentAnswerDTO> studentAnswerDTOList) {
        this.questionDTO = questionDTO;
        this.studentAnswerDTOList = studentAnswerDTOList;
    }

    public QuestionDTO getQuestionDTO() {
        return questionDTO;
    }

    public void setQuestionDTO(QuestionDTO questionDTO) {
        this.questionDTO = questionDTO;
    }

    public List<StudentAnswerDTO> getStudentAnswerDTOList() {
        return studentAnswerDTOList;
    }

    public void setStudentAnswerDTOList(List<StudentAnswerDTO> studentAnswerDTOList) {
        this.studentAnswerDTOList = studentAnswerDTOList;
    }
}
