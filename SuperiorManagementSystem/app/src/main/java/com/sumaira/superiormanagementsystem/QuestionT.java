package com.sumaira.superiormanagementsystem;

public class QuestionT {

        String question;
        String answer;
        String student;
        String teacher;

        public QuestionT() {
            this.answer = "";
            this.question = "";
            this.teacher = "";
            this.student = "";
        }

        public QuestionT(String question, String answer, String student, String teacher) {
            this.question = question;
            this.answer = answer;
            this.student = student;
            this.teacher = teacher;
        }

        public String getquestion() {
            return question;
        }

        public void setquestion(String question) {
            this.question = question;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getStudent() {
            return student;
        }

        public void setStudent(String student) {
            this.student = student;
        }

        public String getTeacher() {
            return teacher;
        }

        public void setTeacher(String teacher) {
            this.teacher = teacher;
        }
    }



