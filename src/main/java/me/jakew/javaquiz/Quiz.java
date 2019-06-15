package me.jakew.javaquiz;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Quiz {
    Question[] questions;
    int current;

    public Quiz(int questionCount) {
        this.current = 0;

        try {
            this.questions = fetchQuestions(questionCount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Question[] fetchQuestions(int amount) throws Exception {
        String url = String.format("https://opentdb.com/api.php?amount=%d&difficulty=easy", amount);

        // Fetch questions from trivia API
        StringBuilder response = new StringBuilder();
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        // Clean up after the request has finished
        reader.close();
        connection.disconnect();

        // Decode the JSON string
        JSONObject obj = new JSONObject(response.toString());
        JSONArray results = obj.getJSONArray("results");

        Question[] questions = new Question[amount];

        for (int i = 0; i < results.length(); i++) {
            JSONObject questionObject = results.getJSONObject(i);
            String category = questionObject.getString("category");
            String type = questionObject.getString("type");
            String difficulty = questionObject.getString("difficulty");
            String question = questionObject.getString("question");

            category = StringEscapeUtils.unescapeHtml4(category);
            type = StringEscapeUtils.unescapeHtml4(type);
            difficulty = StringEscapeUtils.unescapeHtml4(difficulty);
            question = StringEscapeUtils.unescapeHtml4(question);

            List<Answer> answerList = new ArrayList<Answer>();

            String correctAnswer = questionObject.getString("correct_answer");
            correctAnswer = StringEscapeUtils.unescapeHtml4(correctAnswer);
            answerList.add(new Answer(correctAnswer, true));

            JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");
            for (int j = 0; j < incorrectAnswers.length(); j++) {
                String incorrectAnswer = incorrectAnswers.getString(j);
                incorrectAnswer = StringEscapeUtils.unescapeHtml4(incorrectAnswer);
                answerList.add(new Answer(incorrectAnswer, false));
            }

            questions[i] = new Question(question, answerList.toArray(new Answer[0]), category, type, difficulty);
        }

        return questions;
    }

    public Question getCurrentQuestion() {
        return this.questions[this.current];
    }

    public void nextQuestion() {
        if (this.current < (this.questions.length + 1)) {
            this.current += 1;
        }
    }

    public boolean checkAnswer(int answer) {
        Question currentQuestion = getCurrentQuestion();
        for (int i = 0; i < currentQuestion.answers.length; i++) {
            Answer a = currentQuestion.answers[i];
            if (i == answer) {
                return a.correct;
            }
        }
        return false;
    }

    public Answer correctAnswer() {
        Question currentQuestion = getCurrentQuestion();
        for (Answer answer: currentQuestion.answers) {
            if (answer.correct) {
                return answer;
            }
        }
        return null;
    }

    public Answer[] shuffledAnswers() {
        Question currentQuestion = getCurrentQuestion();
        List<Answer> answerList = Arrays.asList(currentQuestion.answers);
        Collections.shuffle(answerList);
        return answerList.toArray(new Answer[0]);
    }
}
