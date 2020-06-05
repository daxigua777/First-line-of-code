package com.example.english2.dao;

public class Error {
    String eng;
    String chinese;

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

    public Error(String eng, String chinese) {
        this.eng = eng;
        this.chinese = chinese;
    }

    public Error() {
    }
}
