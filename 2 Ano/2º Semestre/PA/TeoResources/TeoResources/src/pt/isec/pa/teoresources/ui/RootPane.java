package pt.isec.pa.teoresources.ui;

import javafx.event.ActionEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import pt.isec.pa.teoresources.model.ModelData;
import pt.isec.pa.teoresources.ui.resources.CSSManager;
import pt.isec.pa.teoresources.ui.resources.FontManager;
import pt.isec.pa.teoresources.ui.resources.ImageManager;
import pt.isec.pa.teoresources.ui.resources.SoundManager;

import java.util.Arrays;
import java.util.List;

public class RootPane extends BorderPane {
    public static final int NR_BUTTONS = 6;
    public static final int BUTTON_WIDTH = 60;
    public static final int BUTTON_HEIGHT = 40;

    ModelData model;

    //a
    Button btns[];
    //c
    Canvas canvas;
    ScrollPane canvasPane;

    public RootPane(ModelData model) {
        this.model = model;

        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        //a
        btns = new Button[NR_BUTTONS];
        for (int i = 0; i < btns.length; i++) {
            btns[i] = new Button(String.format("B%02d",i+1));
            btns[i].setPrefSize(BUTTON_WIDTH,BUTTON_HEIGHT);

            btns[i].setUserData(0);
        }
        ToolBar toolBar = new ToolBar(btns);
        setTop(toolBar);

        //b - criar CSSManager dentro de um novo package resources
        CSSManager.applyCSS(toolBar,"mystyles.css");
        btns[1].setId("specialButton");

        //b1 - verificar a árvore onde se aplica o CSS
        Button testA= new Button("Test A");
        testA.setId("specialButton");
        Button testB= new Button("Test b");
        testB.setId("specialButton");
        setBottom(new ToolBar(testA,new Button("Test 1"),new Button("Test 2")));
        toolBar.getItems().add(testB);

        //c
        canvas = new Canvas(1000,1000);
        canvasPane = new ScrollPane(canvas);
        setCenter(canvasPane);

        //d - testar texto + fontes pre-instaladas
        Label lbTestF1 = new Label("Coimbra");
        lbTestF1.setTextFill(Color.INDIGO);
        toolBar.getItems().add(lbTestF1);
        //lbTestF1.setFont(new Font("Helvetiva",24));

            //e - criar class FontManager e subpackage fonts. colocar no directório uma fonte ttf ou otf
            lbTestF1.setFont(FontManager.loadFont("greatvibes.otf",24));

                //f - criar class ImageManager e subpackage images. colocar no directório imagens jpg e/ou png
                Image image = ImageManager.getImage("isec.jpg");

                ImageView imgView1 = new ImageView(image);
                imgView1.setPreserveRatio(true);
                imgView1.setFitHeight(BUTTON_HEIGHT-5);
                Button btTestI1 = new Button("Test1", imgView1);
                btTestI1.setPrefHeight(BUTTON_HEIGHT);

                ImageView imgView2 = new ImageView(image);
                imgView2.setPreserveRatio(true);
                imgView2.setFitHeight(BUTTON_HEIGHT-5);
                Button btTestI2 = new Button(null, imgView2);
                btTestI2.setPrefHeight(BUTTON_HEIGHT);

                ImageView imgView3 = new ImageView(image);
                imgView3.setPreserveRatio(true);
                imgView3.setFitHeight(BUTTON_HEIGHT);

                toolBar.getItems().addAll(btTestI1,btTestI2,imgView3);

                //f3
                VBox vBox  = new VBox(10);
                for (int i = 0; i < 5; i++) {
                    vBox.getChildren().add(new ImageView(image));
                }
                ScrollPane scrollPane2 = new ScrollPane(vBox);
                setRight(scrollPane2);

                //f4
                Image background = ImageManager.getImage("background.jpeg");
                if (background!=null)
                    toolBar.setBackground(new Background(
                            new BackgroundImage(
                                    background,
                                    BackgroundRepeat.REPEAT,BackgroundRepeat.REPEAT,
                                    BackgroundPosition.DEFAULT,null
                            )
                    ));
    }

    private void registerHandlers() {
        //model=btns[0].getScene().getUserData();
        // g - criar class SoundManager e subpackage sounds. colocar no directório imagens mp3 e/ou wav
        btns[0].setOnAction( actionEvent -> {
            if (SoundManager.isPlaying())
                SoundManager.stop();
            else
                SoundManager.play("music.mp3");
        });

        //h
        btns[1].setOnAction( event -> {
            Alert alert = new Alert(
                    Alert.AlertType.INFORMATION,
                    "Button[1]",
                    ButtonType.CANCEL,ButtonType.APPLY,ButtonType.OK);
            alert.setTitle("MyTitle");
            //alert.setContentText("MyContent"); // previous: "Button[1]"
            alert.setHeaderText("MyHeader");
            alert.showAndWait().ifPresent(response -> {
                System.out.println(response);
            });
        });

        //h1
        btns[2].setOnAction( event -> {
            TextInputDialog tid = new TextInputDialog();
            tid.setTitle("TID - title");
            tid.setContentText("TID - content");
            tid.setHeaderText("TID - header");
            tid.showAndWait().ifPresent(response -> {
                SoundManager.play(response);
            });
        });

        //h2
        btns[3].setOnAction( event -> {
            //List<String> lst = List.of("chicken.mp3","music.mp3","muttley.mp3","wimm.mp3");
            List<String> lst = SoundManager.getSoundList();
            ChoiceDialog<String> cd = new ChoiceDialog<String>(lst.get(0),lst);
            cd.setTitle("CD - title");
            cd.setContentText("CD - content");
            cd.setHeaderText("CD - header");
            cd.showAndWait().ifPresent(response -> {
                SoundManager.play(response);
            });
        });

        btns[4].setOnAction( actionEvent -> {
            canvas.setScaleX(0.75);
            canvas.setScaleY(0.75);
        });
        btns[5].setOnAction( actionEvent -> {
            canvas.setScaleX(1);
            canvas.setScaleY(1);
        });

        for(int i = 0;i<NR_BUTTONS;i++)
            btns[i].addEventFilter(ActionEvent.ACTION, event -> {
                Button btn = (Button) event.getSource();
                if (btn.getUserData() instanceof Integer value) {
                    value++;
                    btn.setUserData(value);
                    System.out.printf("Button \"%s\" = %d\n",btn.getText(),value);
                }
                //event.consume();
            });
    }

    private void update() {
        //c
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(Color.LIGHTCORAL.brighter());
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());
            //d1
                //d2
                gc.setFont(new Font("Times New Roman",24));

                    //e2
                    gc.setFont(FontManager.loadFont("greatvibes.otf",48));

            gc.setFill(Color.WHITE);
            gc.fillText(model.getMessage(),50,50);
            gc.setStroke(Color.BLACK);
            gc.strokeText(model.getMessage(),50,100);
            gc.fillText(model.getMessage(),50,150);
            gc.strokeText(model.getMessage(),50,150);


            gc.setFill(Color.color(0.4,0.4,0.4));
            gc.fillText(model.getMessage(),51,201);
            gc.setFill(Color.WHITE);
            gc.fillText(model.getMessage(),50,200);


            //f1
            Image image = ImageManager.getImage("isec.jpg");
            gc.drawImage(image,100,250);
            gc.drawImage(image,100+image.getWidth()+50,250,50,image.getHeight());


            //f2
            Image imageE = ImageManager.getExternalImage("https://logodownload.org/wp-content/uploads/2017/04/java-logo-12.png");
            gc.drawImage(imageE,100,250+image.getHeight()+50,imageE.getWidth()/2,imageE.getHeight()/2);

        gc.setStroke(Color.INDIGO);
        gc.setFill(Color.valueOf("#8576FF"));
        gc.strokeLine(700,50,800,100);
        gc.setLineDashes(10,5);
        gc.strokeRect(700,150,100,50);
        gc.fillRect(850,150,100,50);
        gc.setLineDashes(10,5,2,5);
        gc.strokeRoundRect(700,250,100,50,10,10);
        gc.fillRoundRect(850,250,100,50,10,10);
        gc.setLineDashes(null);
        gc.strokeOval(700,350,100,50);
        gc.fillOval(850,350,100,50);
        double [] xs = {725,825,800,700};
        double [] ys = {450,450,500,500};
        gc.strokePolygon(xs,ys,4);
        xs = Arrays.stream(xs).map( x -> x+150).toArray();
        gc.fillPolygon(xs,ys,4);
    }
}
