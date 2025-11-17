package pt.isec.pd.ex19;

import java.io.Serializable;

public class Hora implements Serializable {

    static final long serialVersionUID = 1L;
    private int horas;
    private int minutos;
    private int segundos;

    public Hora(int h, int m, int s){
        horas = h;
        minutos = m;
        segundos = s;
    }

    public int getHoras() {
        return horas;
    }
    public int getMinutos() {
        return minutos;
    }
    public int getSegundos() {
        return segundos;
    }

    @Override
    public String toString() {
        return String.format("%02d:%02d:%02d", horas, minutos, segundos);
    }
}
