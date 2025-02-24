package com.example.practicevoca.object;

public class Word {
    private String eng;
    private String kor;

    public Word(String eng, String kor){
        this.eng = eng;
        this.kor = kor;
    }

    public String getEng(){
        return eng;
    }

    public String getKor(){
        return kor;
    }

    public void setEng(String eng) {
        this.eng = eng;
    }

    public void setKor(String kor) {
        this.kor = kor;
    }
}
