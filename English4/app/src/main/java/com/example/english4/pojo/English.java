package com.example.english4.pojo;

import java.io.Serializable;

public class English implements Serializable {
    private String Eng;
    private String Chinise;
    private boolean b;

    public English() {
    }

    @Override
    public String toString() {
        return "English{" +
                "Eng='" + Eng + '\'' +
                ", Chinise='" + Chinise + '\'' +
                ", b=" + b +
                '}';
    }

    public String getEng() {
        return Eng;
    }

    public void setEng(String eng) {
        Eng = eng;
    }

    public String getChinise() {
        return Chinise;
    }

    public void setChinise(String chinise) {
        Chinise = chinise;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public English(String eng, String chinise, boolean b) {
        Eng = eng;
        Chinise = chinise;
        this.b = b;
    }
}
