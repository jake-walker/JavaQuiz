package me.jakew.javaquiz;

import java.util.Scanner;

public class App
{
    private static void pause() {
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {}
    }

    public static void main( String[] args )
    {
        int total;
        Quiz quiz;

        System.out.println("Welcome to the Quiz!");

        Scanner in = new Scanner(System.in);
        System.out.print("How many questions would you like to answer? ");
        total = in.nextInt();

        quiz = new Quiz(total);

        for (int i = 0; i < total; i++) {
            Question question = quiz.getCurrentQuestion();

            System.out.println();
            System.out.println(String.format("Question %d: %s", (i + 1), question.question));
            System.out.println(String.format("[%d/%d] CATEGORY: %s", (i + 1), total, question.category));

            Answer[] answers = quiz.shuffledAnswers();

            for (int j = 0; j < answers.length; j++) {
                Answer answer = answers[j];
                System.out.println(String.format("  %d. %s", j, answer.text));
            }

            int input = -1;

            while (input < 0 || input >= question.answers.length) {
                System.out.print("What is the answer? ");
                input = in.nextInt();
            }

            boolean correct = quiz.checkAnswer(input);

            System.out.println(correct ? "CORRECT!" : String.format("INCORRECT! The correct answer was '%s'.", quiz.correctAnswer().text));
            pause();
            quiz.nextQuestion();
        }

        System.out.println("Thanks for playing!");
    }
}
