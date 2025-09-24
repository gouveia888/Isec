package pt.isec.pa.teojfx.ui;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import pt.isec.pa.teojfx.model.ModelData;

import java.io.File;


public class RootPane extends BorderPane {
    ModelData data;

    CenterPane cPane;
    Label lbStatus;
    Menu mnFile, mnView;
    MenuItem mnNew, mnOpen,mnSave,mnExit,mnOp1,mnOp2;

    public RootPane(ModelData data) {
        this.data=data;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        setTop(createMenu());

        cPane = new CenterPane(data);
        setCenter(cPane);

        lbStatus = new Label("AAC Sporting Benfica FCPorto Braga");
        lbStatus.setStyle("-fx-background-color: #d0d0d0;");
        lbStatus.setPrefWidth(99999);
        lbStatus.setPadding(new Insets(8));
        setBottom(lbStatus);
    }

    private MenuBar createMenu() {
        MenuBar mb = new MenuBar();
        mnFile = new Menu("File");
        mnNew = new MenuItem("_New");
        mnOpen = new MenuItem("_Open");
        mnSave = new MenuItem("Save");
        mnExit = new MenuItem("E_xit");
        mnFile.getItems().addAll(mnNew,mnOpen,mnSave,new SeparatorMenuItem(),mnExit);

        mnOp1 = new MenuItem("Op1");
        mnOp2 = new MenuItem("Op2");
        mnView = new Menu("View");
        mnView.getItems().addAll(mnOp1,mnOp2);

        mb.getMenus().addAll(mnFile,mnView);
        return mb;
    }

    private void registerHandlers() {
        mnNew.setOnAction(actionEvent -> {
            AskName askName = new AskName(data);
            askName.showAndWait();
        });
        /*mnOpen.setOnAction(actionEvent -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("TID - title");
            tid.setContentText("TID - content");
            tid.setHeaderText("TID - header");
            tid.showAndWait().ifPresent(response -> {
                System.out.println(response);
            });
        });*/
        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PA Files (*.paf)", "*.paf"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                System.out.println(hFile.getAbsolutePath());
            }
        });
        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PA Files (*.paf)", "*.paf"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                System.out.println(hFile.getAbsolutePath());
            }
        });

        mnExit.setOnAction(actionEvent -> {
            Platform.exit();
        });

        mnOp2.setOnAction(actionEvent -> {
            setCenter(new MyVHBoxPane(data));
        });
    }
    private void update() {
        mnOp1.setDisable(true);
    }
}

