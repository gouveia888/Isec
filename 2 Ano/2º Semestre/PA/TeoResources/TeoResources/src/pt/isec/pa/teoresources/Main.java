package pt.isec.pa.teoresources;

import javafx.application.Application;
import pt.isec.pa.teoresources.model.ModelData;
import pt.isec.pa.teoresources.ui.MainJFX;

public class Main {
    public static ModelData model;
    static {
        model = new ModelData();
    }
    public static void main(String[] args) {
        Application.launch(MainJFX.class,args);
    }
}