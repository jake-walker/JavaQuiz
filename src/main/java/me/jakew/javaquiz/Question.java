package me.jakew.javaquiz;

public class Question {
    String category;
    String type;
    String difficulty;
    String question;
    Answer[] answers;

    public Question(String question, Answer[] answers, String category, String type, String difficulty) {
        this.category = category;
        this.type = type;
        this.difficulty = difficulty;
        this.question = question;
        this.answers = answers;
    }
}
