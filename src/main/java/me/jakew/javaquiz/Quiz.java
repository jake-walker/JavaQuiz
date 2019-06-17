package me.jakew.javaquiz;

public class Quiz {
    Question[] questions;
    int current;
    int score;

    public Quiz(Question[] questions) {
        this.current = 0;
        this.score = 0;
        this.questions = questions;
    }

    public Question getQuestion() {
        return this.questions[this.current];
    }

    public boolean done() {
        return current >= (questions.length - 1);
    }

    public void nextQuestion() {
        if (this.current < (this.questions.length + 1)) {
            this.current += 1;
        }
    }

    public boolean submitAnswer(Answer answer) {
        if (answer.correct) {
            this.score += 1;
            return true;
        }
        return false;
    }

    public Answer correctAnswer() {
        Question currentQuestion = this.getQuestion();
        for (Answer answer: currentQuestion.answers) {
            if (answer.correct) {
                return answer;
            }
        }
        return null;
    }
}
