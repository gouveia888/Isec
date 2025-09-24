package App;

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class RootPane extends VBox {
    BatteryManager modelo;

    Button btnCharge;
    Button btnDischarge;
    Menu menu;
    MenuItem Undo, Redo;

    Canvas batteryCanvas;
    final int batteryWidth = 200;
    final int batteryHeight = 60;
    final int terminalWidth = 10;
    final int terminalHeight = 16;

    public RootPane(BatteryManager modelo) {
        this.modelo = modelo;

        createViews();
        registerHandlers();
        update();
    }

    private MenuBar createMenu(){
        MenuBar menuBar = new MenuBar();

        menu = new Menu("Menu");
        Undo = new MenuItem("Undo");
        Redo = new MenuItem("Redo");

        menu.getItems().addAll(Undo,Redo);
        menuBar.getMenus().addAll(menu);
        return menuBar;
    }

    private void createViews() {
        // Margens e espaçamento

        MenuBar menuBar = createMenu();

        setPadding(new Insets(10));
        setSpacing(10);

        batteryCanvas = new Canvas(batteryWidth + terminalWidth + 2 * 10, batteryHeight + 2 * 10);
        btnCharge = new Button("Charge");
        btnDischarge = new Button("Discharge");

        HBox buttonBox = new HBox(10, btnDischarge, btnCharge);
        /*buttonBox.setPrefWidth(batteryCanvas.getWidth());*/
        btnCharge.setMaxWidth(Double.MAX_VALUE);
        btnDischarge.setMaxWidth(Double.MAX_VALUE);

        HBox.setHgrow(btnCharge, Priority.ALWAYS);
        HBox.setHgrow(btnDischarge, Priority.ALWAYS);

        this.getChildren().addAll(menuBar,batteryCanvas, buttonBox);
    }

    private void registerHandlers() {
        btnCharge.setOnAction(e -> modelo.charge());
        btnDischarge.setOnAction(e -> modelo.discharge());
        //Undo.setOnAction(e -> modelo.undo());
        //Redo.setOnAction(e -> modelo.redo());

        // Ouvinte para alterações do nível da bateria
        modelo.addPropertyChangeListener(evt -> update());
    }

    private void update() {
        GraphicsContext gc = batteryCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, batteryCanvas.getWidth(), batteryCanvas.getHeight());

        // Desenhar corpo da bateria
        double x = 10, y = 10;
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, batteryWidth, batteryHeight);

        // Desenhar terminal
        gc.setFill(Color.BLACK);
        gc.fillRect(x + batteryWidth, y + batteryHeight / 2 - terminalHeight / 2, terminalWidth, terminalHeight);

        // Desenhar nível
        int level = modelo.getLevel();
        double fillWidth = (batteryWidth - 4) * level / 100.0; // 2px de margem de cada lado

        Color fillColor;
        if (level <= 20) {
            fillColor = Color.rgb(255, 0, 0); // Vermelho
        } else if (level <= 90) {
            fillColor = Color.rgb(150, 255, 150); // Verde-claro
        } else {
            fillColor = Color.rgb(0, 140, 0); // Verde-escuro
        }

        gc.setFill(fillColor);
        gc.fillRect(x +2, y +2, fillWidth, batteryHeight-4); // Margens internas
    }
}

