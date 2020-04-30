package com.sumaira.superiorcms;

public class Question {

    private String teacherid ;
    private String studendid ;
    private String question ;
    private String answer ;

    public Question() {
        this.teacherid = "";
        this.studendid = "";
        this.question = "";
        this.answer = "" ;
    }

    public Question(String teacherid, String studendid, String question, String answer) {
        this.teacherid = teacherid;
        this.studendid = studendid;
        this.question = question;
        this.answer = answer ;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setTeacherid(String teacherid) {
        this.teacherid = teacherid;
    }

    public void setStudendid(String studendid) {
        this.studendid = studendid;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTeacherid() {
        return teacherid;
    }

    public String getStudendid() {
        return studendid;
    }

    public String getQuestion() {
        return question;
    }
}
