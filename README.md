# Java Quiz

In my quest to learn Java, I needed a project to apply the things that I have learnt.

I thought a Quiz might be a cool project and pulling the questions from an online API would add a bit of extra complexity.

I started off by looking for an API that I could use to pull questions from, I settled on using the
[Open Trivia Database API](https://opentdb.com/api_config.php) which seemed to have lots of questions and be easiest to
use out of the handful of APIs that I found.

All of the 'logic' for creating and managing the quiz is contained in the `src/me.jakew.quiz/Quiz.java` file.

## Building

```bash
git clone https://github.com/jake-walker/java-quiz
cd java-quiz
ant build
java 
```