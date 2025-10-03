package pt.isec.pd.aula4;

import java.io.Serializable;

public class Time{

    private int hour;
    private int minute;
    private int second;

    private static final long serialVersionUID = 20L; //verificar o numero de versao serializable

    public Time(int hour, int minute, int second) {
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    @Override
    public String toString() {
        return "Time{" +
                "hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }


    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public Time(int hour) {
        this.hour = hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

}