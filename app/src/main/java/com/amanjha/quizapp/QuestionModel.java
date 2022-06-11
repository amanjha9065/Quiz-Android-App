package com.amanjha.quizapp;

import java.util.ArrayList;

class QuestionModel {
    String question;
    String correctOption;
    ArrayList<String> options;

    public QuestionModel(String question, String correctOption, ArrayList<String> options) {
        this.question = question;
        this.correctOption = correctOption;
        this.options = options;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectOption() {
        return correctOption;
    }

    public void setCorrectOption(String correctOption) {
        this.correctOption = correctOption;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }
}
