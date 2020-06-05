package com.example.english2.dao;

public class English {

    String eng;
    String chinese;
    boolean b;

    public English(String eng, String chinese, boolean b) {
        this.eng = eng;
        this.chinese = chinese;
        this.b = b;
    }

    public String getEng() {
        return eng;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }

    public boolean isB() {
        return b;
    }

    public void setB(boolean b) {
        this.b = b;
    }

    public English() {
    }
}
