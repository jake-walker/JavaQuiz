package me.jakew.javaquiz;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizApi {
    private JSONObject doJsonRequest(String url) throws Exception {
        // Create the request
        StringBuilder response = new StringBuilder();
        URL requestUrl = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();
        connection.setRequestMethod("GET");

        // Read the response
        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        // Clear up request objects
        reader.close();
        connection.disconnect();

        // Decode the JSON string
        return new JSONObject(response.toString());
    }

    private Question[] toQuestionArray(JSONArray questions) {
        Question[] output = new Question[questions.length()];

        for (int i = 0; i < questions.length(); i++) {
            // Get the question from the list of questions
            JSONObject questionObject = questions.getJSONObject(i);

            // Get the category, type, difficulty and question strings from the question object
            String category = questionObject.getString("category");
            String type = questionObject.getString("type");
            String difficulty = questionObject.getString("difficulty");
            String question = questionObject.getString("question");

            // Unescape all of the strings
            category = StringEscapeUtils.unescapeHtml4(category);
            type = StringEscapeUtils.unescapeHtml4(type);
            difficulty = StringEscapeUtils.unescapeHtml4(difficulty);
            question = StringEscapeUtils.unescapeHtml4(question);

            // Create a new list for storing answers
            List<Answer> answerList = new ArrayList<>();

            // Get the correct answer from the question object and add to the answer list
            String correctAnswer = questionObject.getString("correct_answer");
            correctAnswer = StringEscapeUtils.unescapeHtml4(correctAnswer);
            answerList.add(new Answer(correctAnswer, true));

            // Get each of the incorrect answers and add them each to the answer list
            JSONArray incorrectAnswers = questionObject.getJSONArray("incorrect_answers");
            for (int j = 0; j < incorrectAnswers.length(); j++) {
                String incorrectAnswer = incorrectAnswers.getString(j);
                incorrectAnswer = StringEscapeUtils.unescapeHtml4(incorrectAnswer);
                answerList.add(new Answer(incorrectAnswer, false));
            }

            // Randomize answer order
            Collections.shuffle(answerList);

            // Add the parsed question to the question list.
            output[i] = new Question(question, answerList.toArray(new Answer[0]), category, type, difficulty);
        }

        return output;
    }

    private Category[] toCategoryArray(JSONArray categories) {
        Category[] output = new Category[categories.length()];

        for (int i = 0; i < categories.length(); i++) {
            // Get the category from the list of categories
            JSONObject categoriesObject = categories.getJSONObject(i);

            // Get the id and name from the category object
            String name = categoriesObject.getString("name");
            int id = categoriesObject.getInt("id");

            // Unescape all of the strings
            name = StringEscapeUtils.unescapeHtml4(name);

            // Add the parsed question to the category list.
            output[i] = new Category(id, name);
        }

        return output;
    }

    private String difficultyToString(int difficulty) {
        switch (difficulty) {
            case 1:
                return "medium";
            case 2:
                return "hard";
            default:
                return "easy";
        }
    }

    public Category[] getCategories() throws Exception {
        String url = "https://opentdb.com/api_category.php";
        JSONObject response = doJsonRequest(url);
        return toCategoryArray(response.getJSONArray("trivia_categories"));
    }

    public Question[] getQuestions(int amount) throws Exception {
        String url = String.format("https://opentdb.com/api.php?amount=%d", amount);
        JSONObject response = doJsonRequest(url);
        return toQuestionArray(response.getJSONArray("results"));
    }

    public Question[] getQuestions(int amount, Category category) throws Exception {
        String url = String.format("https://opentdb.com/api.php?amount=%d&category=%d", amount, category.id);
        JSONObject response = doJsonRequest(url);
        return toQuestionArray(response.getJSONArray("results"));
    }

    public Question[] getQuestions(int amount, int difficulty) throws Exception {

        String url = String.format("https://opentdb.com/api.php?amount=%d&difficulty=%s",
                amount,
                difficultyToString(difficulty));
        JSONObject response = doJsonRequest(url);
        return toQuestionArray(response.getJSONArray("results"));
    }

    public Question[] getQuestions(int amount, Category category, int difficulty) throws Exception {
        String url = String.format("https://opentdb.com/api.php?amount=%d&difficulty=%s&category=%d",
                amount,
                difficultyToString(difficulty),
                category.id);
        JSONObject response = doJsonRequest(url);
        return toQuestionArray(response.getJSONArray("results"));
    }
}
