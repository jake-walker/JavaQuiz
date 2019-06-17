package me.jakew.javaquiz;

import java.util.Scanner;

public class App
{
    private static void pause() {
        System.out.println("Press enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            System.exit(1);
        }
    }

    private static int getInt(String prompt, int min, int max) {
        prompt += " ";
        Scanner in = new Scanner(System.in);
        int input = -1;

        if (max < min) {
            System.out.print(prompt);
            input = in.nextInt();
        } else {
            while (input < min || input > max) {
                System.out.print(prompt);
                input = in.nextInt();
            }
        }

        return input;
    }

    private static Answer askQuestion(Question question) {
        System.out.println(String.format("Q) %s", question.question));

        for (int i = 0; i < question.answers.length; i++) {
            Answer answer = question.answers[i];
            System.out.println(String.format("   %d. %s", i, answer.text));
        }

        System.out.println();

        int response = getInt("What is the answer?", 0, question.answers.length - 1);
        return question.answers[response];
    }

    public static void main( String[] args )
    {
        int total;
        Quiz quiz;
        QuizApi api = new QuizApi();

        System.out.println("Welcome to the Quiz!");

        total = getInt("How many questions would you like to answer?", 1, 0);

        System.out.println("Fetching questions...");
        Question[] questions = new Question[0];
        try {
            questions = api.getQuestions(total);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        quiz = new Quiz(questions);

        while (!quiz.done()) {
            Question question = quiz.getQuestion();

            System.out.println();
            System.out.println(String.format("[%d/%d] CATEGORY: %s", (quiz.current + 1), quiz.questions.length, question.category));
            Answer answer = askQuestion(question);
            boolean correct = quiz.submitAnswer(answer);

            if (correct) {
                System.out.println(String.format("CORRECT! You now have now scored %d.", quiz.score));
            } else {
                System.out.println(String.format("INCORRECT! The correct answer was '%s'. You have %d points.", quiz.correctAnswer().text, quiz.score));
            }

            pause();
            quiz.nextQuestion();
        }

        System.out.println("Thanks for playing!");
        System.out.println(String.format("Your final score was %d out of a possible %d points.", quiz.score, quiz.current + 1));
    }
}
