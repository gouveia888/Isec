package pt.isec.pa.drawing.ui.gui;

import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;
import pt.isec.pa.drawing.model.DrawingManager;

import java.io.File;

public class DrawingMenu extends MenuBar {
    DrawingManager drawing;
    DrawingArea drawingArea;
    Menu mnFile;
    MenuItem mnNew,mnOpen,mnSave,mnExit;
    Menu mnEdit;
    MenuItem mnUndo,mnRedo;

    public DrawingMenu(DrawingManager drawing, DrawingArea drawingArea) {
        this.drawing = drawing;
        this.drawingArea = drawingArea;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        mnFile = new Menu("File");
        mnNew = new MenuItem("_New");
        //mnNew.setMnemonicParsing(false);
        mnOpen = new MenuItem("_Open");
        mnOpen.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        mnSave = new MenuItem("_Save");
        mnExit = new MenuItem("_Exit");
        mnFile.getItems().addAll(mnNew, mnOpen, mnSave, new SeparatorMenuItem(), mnExit);

        mnEdit = new Menu("Edit");
        mnUndo = new MenuItem("_Undo");
        mnRedo = new MenuItem("_Redo");
        mnEdit.getItems().addAll(mnUndo, mnRedo);

        this.getMenus().addAll(mnFile, mnEdit);

        /*Menu mnTest1 = new Menu("Test");
        CheckMenuItem mnT1op1 = new CheckMenuItem("Option a1");
        CheckMenuItem mnT1op2 = new CheckMenuItem("Option a2");
        CheckMenuItem mnT1op3 = new CheckMenuItem("Option a3");

        RadioMenuItem mnT2op1 = new RadioMenuItem("Option b1");
        RadioMenuItem mnT2op2 = new RadioMenuItem("Option b2");
        RadioMenuItem mnT2op3 = new RadioMenuItem("Option b3");
        ToggleGroup tgT2 = new ToggleGroup();
        mnT2op1.setToggleGroup(tgT2);
        mnT2op2.setToggleGroup(tgT2);
        mnT2op3.setToggleGroup(tgT2);

        mnTest1.getItems().addAll(mnT1op1,mnT1op2,mnT1op3,new SeparatorMenuItem(),mnT2op1,mnT2op2,mnT2op3);

        this.getMenus().addAll(mnFile, mnEdit,mnTest1);*/

        //this.setUseSystemMenuBar(true);
    }

    private void registerHandlers() {
        //mnFile.setOnShowing( e-> { Platform.exit();});
        mnNew.setOnAction( e -> {
            drawing.clearAll();
            drawingArea.update();
        });

        mnOpen.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.dat)", "*.dat"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showOpenDialog(this.getScene().getWindow());
            if (hFile != null) {
                drawing.load(hFile);
                drawingArea.update();
            }
        });

        mnSave.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Drawing (*.dat)", "*.dat"),
                    new FileChooser.ExtensionFilter("All", "*.*")
            );
            File hFile = fileChooser.showSaveDialog(this.getScene().getWindow());
            if (hFile != null) {
                drawing.save(hFile);
                drawingArea.update();
            }
        });

        mnExit.setOnAction(e -> {
            Platform.exit();
        });

        mnUndo.setOnAction(e -> {

        });
        mnRedo.setOnAction(e -> {

        });
    }

    private void update() {
        mnUndo.setDisable(true); // TODO: change 'true' to drawing.hasUndo()
        mnRedo.setDisable(true); // TODO: change 'true' to drawing.hasRedo()
    }
}
