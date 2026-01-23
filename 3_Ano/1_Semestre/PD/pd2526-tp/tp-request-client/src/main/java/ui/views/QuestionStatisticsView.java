package ui.views;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import network.data.QuestionDTO;
import network.data.QuestionStatisticsDTO;
import network.data.StudentAnswerDTO;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

public class QuestionStatisticsView {

    public Parent createView(QuestionStatisticsDTO stats) {

        // === TITLE ===
        Label title = new Label("Question Statistics");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // === QUESTION INFORMATION ===
        QuestionDTO q = stats.getQuestionDTO();

        Label statement = new Label("Statement: " + q.getStatement());
        statement.setWrapText(true);

        // Show options in a VBox
        VBox optionsBox = new VBox(5);
        optionsBox.getChildren().add(new Label("Options:"));
        for (int i = 0; i < q.getOptions().size(); i++) {
            String opt = q.getOptions().get(i);

            Label optionLabel = new Label((i + 1) + ". " + opt);

            // Highlight correct option
            if (i == q.getCorrectOption()) {
                optionLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
            }

            optionsBox.getChildren().add(optionLabel);
        }

        Label startDate = new Label("Start: " + q.getStartDate());
        Label endDate = new Label("End: " + q.getEndDate());
        Label state = new Label("State: " + q.getState());

        VBox questionBox = new VBox(10, statement, optionsBox, startDate, endDate, state);
        questionBox.setPadding(new Insets(10));
        questionBox.setStyle("-fx-background-color: #f5f5f5; -fx-padding: 10; -fx-background-radius: 8;");

        // === STUDENT ANSWERS TABLE ===
        TableView<StudentAnswerDTO> table = new TableView<>();

        TableColumn<StudentAnswerDTO, String> colNum = new TableColumn<>("Student Number");
        colNum.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getStudentNumber())
        );

        TableColumn<StudentAnswerDTO, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getStudentName())
        );

        TableColumn<StudentAnswerDTO, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cell ->
                new ReadOnlyStringWrapper(cell.getValue().getStudentEmail())
        );

        TableColumn<StudentAnswerDTO, Number> colAnswer = new TableColumn<>("Answer");
        colAnswer.setCellValueFactory(cell ->
                new ReadOnlyIntegerWrapper(cell.getValue().getStudentQuestionAnswer() + 1)
        );


        table.getColumns().addAll(colNum, colName, colEmail, colAnswer);
        table.setItems(FXCollections.observableArrayList(stats.getStudentAnswerDTOList()));

        Button exportBtn = new Button("Export to CSV");
        exportBtn.setOnAction(e -> exportToCSV(stats));

        VBox layout = new VBox(20, title, questionBox, table, exportBtn);
        layout.setPadding(new Insets(15));

        return layout;
    }


    private void exportToCSV(QuestionStatisticsDTO stats) {
        QuestionDTO q = stats.getQuestionDTO();

        // Choose output file
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Statistics");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Text Files", "*.csv")
        );
        fileChooser.setInitialFileName("question_stats.csv");

        File file = fileChooser.showSaveDialog(null);
        if (file == null) return;

        try (PrintWriter pw = new PrintWriter(file, StandardCharsets.UTF_8)) {

            // Format date/time
            String day = q.getStartDate().toLocalDate().toString();
            String start = q.getStartDate().toLocalTime().toString();
            String end = q.getEndDate().toLocalTime().toString();
            String statement = q.getStatement();
            String correctOptLetter = String.valueOf((char) ('a' + q.getCorrectOption()));

            // SECTION 1 — Question Header
            pw.println("\"dia\";\"hora inicial\";\"hora final\";\"enunciado da pergunta\";\"opção certa\"");
            pw.printf("\"%s\";\"%s\";\"%s\";\"%s\";\"%s\"%n",
                    day, start, end, statement, correctOptLetter
            );

            // SECTION 2 — Options
            pw.println("\"opção\";\"texto da opção\"");
            for (int i = 0; i < q.getOptions().size(); i++) {
                char letter = (char) ('a' + i);
                pw.printf("\"%c\";\"%s\"%n", letter, q.getOptions().get(i));
            }

            // SECTION 3 — Student answers
            pw.println("\"número de estudante\";\"nome\";\"e-mail\";\"resposta\"");
            for (StudentAnswerDTO s : stats.getStudentAnswerDTOList()) {
                char answerLetter = (char) ('a' + s.getStudentQuestionAnswer());
                pw.printf("\"%s\";\"%s\";\"%s\";\"%c\"%n",
                        s.getStudentNumber(),
                        s.getStudentName(),
                        s.getStudentEmail(),
                        answerLetter
                );
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}

