package com.brainshift.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class MainActivity extends Activity {

    private static class Question {

        String text;
        String answer;
        String hint;
        String topic;

        Question(
                String text,
                String answer,
                String hint,
                String topic
        ) {
            this.text = text;
            this.answer = answer;
            this.hint = hint;
            this.topic = topic;
        }
    }

    private final List<Question> questions = new ArrayList<>();

    // Questions answered in the CURRENT revision cycle
    private final Set<Integer> answeredQuestions = new HashSet<>();

    private final Random random = new Random();

    private int currentQuestion = -1;

    private int score = 0;
    private int solved = 0;
    private int streak = 0;
    private int xp = 0;
    private int revisionCycle = 1;

    private boolean waitingForNext = false;

    private SharedPreferences prefs;

    private TextView levelView;
    private TextView xpView;
    private TextView scoreView;
    private TextView streakView;
    private TextView solvedView;
    private TextView progressView;
    private TextView questionView;
    private TextView topicView;
    private TextView feedbackView;
    private TextView cycleView;

    private EditText answerBox;
    private Button actionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences(
                "brain_shift",
                MODE_PRIVATE
        );

        loadProgress();

        createQuestions();

        buildUI();

        chooseNextQuestion();
    }


    // =========================================================
    // QUESTION BANK
    // =========================================================

    private void createQuestions() {

        questions.clear();

        questions.add(new Question(
                "What is 15% of 200?",
                "30",
                "15% × 200",
                "QUANT"
        ));

        questions.add(new Question(
                "If CP is ₹500 and SP is ₹600, what is profit?",
                "100",
                "Profit = SP - CP",
                "FINANCE"
        ));

        questions.add(new Question(
                "What is 20% of 500?",
                "100",
                "20/100 × 500",
                "QUANT"
        ));

        questions.add(new Question(
                "If assets are ₹1,00,000 and liabilities are ₹40,000, what is capital?",
                "60000",
                "Capital = Assets - Liabilities",
                "ACCOUNTING"
        ));

        questions.add(new Question(
                "What is the full form of GST?",
                "goods and services tax",
                "It is an indirect tax.",
                "TAX"
        ));

        questions.add(new Question(
                "If revenue is ₹80,000 and cost is ₹50,000, what is profit?",
                "30000",
                "Profit = Revenue - Cost",
                "FINANCE"
        ));

        questions.add(new Question(
                "What is 25% of 400?",
                "100",
                "One-fourth of 400",
                "QUANT"
        ));

        questions.add(new Question(
                "What is the basic accounting equation?",
                "assets = liabilities + capital",
                "A = L + C",
                "ACCOUNTING"
        ));

        questions.add(new Question(
                "What is the full form of CA?",
                "chartered accountant",
                "Professional accounting qualification",
                "CA"
        ));

        questions.add(new Question(
                "What is 10% of ₹2,000?",
                "200",
                "10/100 × 2000",
                "QUANT"
        ));

        questions.add(new Question(
                "If SP is greater than CP, the result is?",
                "profit",
                "Selling price > Cost price",
                "FINANCE"
        ));

        questions.add(new Question(
                "What is the full form of CMA?",
                "cost and management accountant",
                "Professional management accounting qualification",
                "CMA"
        ));

        questions.add(new Question(
                "What is 50% of 800?",
                "400",
                "Half of 800",
                "QUANT"
        ));

        questions.add(new Question(
                "What is the opposite of profit?",
                "loss",
                "When cost exceeds selling price",
                "FINANCE"
        ));

        questions.add(new Question(
                "If assets are ₹2,00,000 and capital is ₹1,20,000, liabilities are?",
                "80000",
                "Liabilities = Assets - Capital",
                "ACCOUNTING"
        ));

        questions.add(new Question(
                "What is 5% of 1,000?",
                "50",
                "5/100 × 1000",
                "QUANT"
        ));

        questions.add(new Question(
                "What does CA Foundation prepare students for?",
                "chartered accountancy",
                "It is the entry-level CA examination.",
                "CA"
        ));

        questions.add(new Question(
                "What is revenue minus expenses?",
                "profit",
                "Income left after expenses",
                "ACCOUNTING"
        ));

        questions.add(new Question(
                "What is 30% of 300?",
                "90",
                "30/100 × 300",
                "QUANT"
        ));

        questions.add(new Question(
                "What is the full form of PAN?",
                "permanent account number",
                "Tax identification number in India",
                "TAX"
        ));
    }


    // =========================================================
    // UI
    // =========================================================

    private void buildUI() {

        LinearLayout root = new LinearLayout(this);

        root.setOrientation(
                LinearLayout.VERTICAL
        );

        root.setPadding(
                dp(20),
                dp(20),
                dp(20),
                dp(20)
        );

        root.setBackgroundColor(
                Color.rgb(245, 247, 250)
        );


        TextView title = makeText(
                "🧠 BrainShift",
                28,
                Color.rgb(25, 25, 25)
        );

        title.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(title);


        TextView subtitle = makeText(
                "CA / CMA Smart Revision",
                15,
                Color.DKGRAY
        );

        root.addView(subtitle);


        cycleView = makeText(
                "🔄 REVISION CYCLE 1",
                14,
                Color.rgb(80, 80, 80)
        );

        cycleView.setPadding(
                0,
                dp(10),
                0,
                dp(5)
        );

        root.addView(cycleView);


        LinearLayout stats = new LinearLayout(this);

        stats.setOrientation(
                LinearLayout.HORIZONTAL
        );

        stats.setGravity(
                Gravity.CENTER
        );

        stats.setPadding(
                0,
                dp(10),
                0,
                dp(10)
        );


        levelView = makeText(
                "LEVEL 1",
                13,
                Color.DKGRAY
        );

        xpView = makeText(
                "⚡ 0 XP",
                13,
                Color.DKGRAY
        );

        scoreView = makeText(
                "Score 0",
                13,
                Color.DKGRAY
        );

        streakView = makeText(
                "🔥 0",
                13,
                Color.DKGRAY
        );


        stats.addView(
                levelView,
                weightParams()
        );

        stats.addView(
                xpView,
                weightParams()
        );

        stats.addView(
                scoreView,
                weightParams()
        );

        stats.addView(
                streakView,
                weightParams()
        );

        root.addView(stats);


        solvedView = makeText(
                "Solved: 0",
                15,
                Color.DKGRAY
        );

        root.addView(solvedView);


        progressView = makeText(
                "Questions remaining: 0",
                15,
                Color.DKGRAY
        );

        progressView.setPadding(
                0,
                dp(8),
                0,
                dp(15)
        );

        root.addView(progressView);


        topicView = makeText(
                "TOPIC",
                14,
                Color.rgb(80, 80, 80)
        );

        topicView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        root.addView(topicView);


        questionView = makeText(
                "",
                20,
                Color.BLACK
        );

        questionView.setTypeface(
                Typeface.DEFAULT,
                Typeface.BOLD
        );

        questionView.setPadding(
                0,
                dp(15),
                0,
                dp(15)
        );

        root.addView(questionView);


        answerBox = new EditText(this);

        answerBox.setHint(
                "Type your answer..."
        );

        answerBox.setSingleLine(true);

        answerBox.setTextSize(17);

        root.addView(
                answerBox,
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                )
        );


        actionButton = new Button(this);

        actionButton.setText(
                "CHECK ANSWER"
        );

        actionButton.setOnClickListener(
                v -> handleAction()
        );


        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                );

        buttonParams.topMargin = dp(15);

        root.addView(
                actionButton,
                buttonParams
        );


        feedbackView = makeText(
                "",
                16,
                Color.DKGRAY
        );

        feedbackView.setPadding(
                0,
                dp(15),
                0,
                dp(10)
        );

        root.addView(feedbackView);


        setContentView(root);
    }


    // =========================================================
    // QUESTION FLOW
    // =========================================================

    private void handleAction() {

        if (questions.isEmpty()) {
            return;
        }


        if (waitingForNext) {

            chooseNextQuestion();

            return;
        }


        checkAnswer();
    }


    private void chooseNextQuestion() {

        List<Integer> availableQuestions =
                new ArrayList<>();


        for (int i = 0;
             i < questions.size();
             i++) {

            if (!answeredQuestions.contains(i)) {

                availableQuestions.add(i);
            }
        }


        // -----------------------------------------------------
        // CURRENT CYCLE FINISHED
        // -----------------------------------------------------

        if (availableQuestions.isEmpty()) {

            startNewRevisionCycle();

            for (int i = 0;
                 i < questions.size();
                 i++) {

                availableQuestions.add(i);
            }
        }


        if (availableQuestions.isEmpty()) {

            questionView.setText(
                    "No questions available."
            );

            return;
        }


        currentQuestion =
                availableQuestions.get(
                        random.nextInt(
                                availableQuestions.size()
                        )
                );


        waitingForNext = false;

        actionButton.setText(
                "CHECK ANSWER"
        );

        answerBox.setText("");

        feedbackView.setText("");

        showQuestion();
    }


    private void startNewRevisionCycle() {

        revisionCycle++;

        answeredQuestions.clear();

        saveProgress();

        feedbackView.setTextColor(
                Color.rgb(20, 120, 60)
        );

        feedbackView.setText(
                "🎉 Revision Cycle completed!\n" +
                "Starting Cycle " +
                revisionCycle +
                " with fresh questions."
        );
    }


    private void showQuestion() {

        if (questions.isEmpty()) {

            questionView.setText(
                    "No questions available."
            );

            return;
        }


        if (currentQuestion < 0 ||
                currentQuestion >= questions.size()) {

            return;
        }


        Question question =
                questions.get(currentQuestion);


        topicView.setText(
                "📚 " + question.topic
        );


        questionView.setText(
                question.text
        );


        answerBox.setText("");


        answerBox.requestFocus();


        updateStats();
    }


    // =========================================================
    // ANSWER CHECKING
    // =========================================================

    private void checkAnswer() {

        if (currentQuestion < 0 ||
                currentQuestion >= questions.size()) {

            return;
        }


        Question question =
                questions.get(currentQuestion);


        String userAnswer =
                normalize(
                        answerBox
                                .getText()
                                .toString()
                );


        String correctAnswer =
                normalize(
                        question.answer
                );


        if (userAnswer.isEmpty()) {

            feedbackView.setTextColor(
                    Color.rgb(180, 120, 20)
            );

            feedbackView.setText(
                    "⚠️ Please enter an answer."
            );

            return;
        }


        // -----------------------------------------------------
        // VERY IMPORTANT
        //
        // Once answered, this question is marked as seen.
        // It CANNOT appear again in this revision cycle.
        // -----------------------------------------------------

        answeredQuestions.add(
                currentQuestion
        );


        if (userAnswer.equals(correctAnswer)) {

            score += 10;

            xp += 10;

            solved++;

            streak++;


            feedbackView.setTextColor(
                    Color.rgb(20, 120, 60)
            );


            feedbackView.setText(
                    "✓ Correct!\n" +
                    "+10 XP\n" +
                    "This question won't repeat " +
                    "in this cycle."
            );

        } else {

            solved++;

            streak = 0;


            feedbackView.setTextColor(
                    Color.rgb(180, 40, 40)
            );


            feedbackView.setText(
                    "✗ Not quite.\n" +
                    "Hint: " +
                    question.hint +
                    "\nCorrect answer: " +
                    question.answer +
                    "\n\nThis question won't repeat " +
                    "in this cycle."
            );
        }


        saveProgress();


        waitingForNext = true;


        actionButton.setText(
                "NEXT"
        );


        updateStats();
    }


    // =========================================================
    // NORMALIZE ANSWER
    // =========================================================

    private String normalize(String value) {

        return value
                .toLowerCase(Locale.ROOT)
                .trim()
                .replace("₹", "")
                .replace("rs.", "")
                .replace("rs", "")
                .replace(",", "")
                .replace(".", "")
                .replace("%", "")
                .replace(" ", "");
    }


    // =========================================================
    // STATS
    // =========================================================

    private void updateStats() {

        int level =
                1 + (xp / 100);


        levelView.setText(
                "LEVEL " + level
        );


        xpView.setText(
                "⚡ " + xp + " XP"
        );


        scoreView.setText(
                "Score " + score
        );


        streakView.setText(
                "🔥 " + streak
        );


        solvedView.setText(
                "Solved: " + solved
        );


        int remaining =
                questions.size()
                        - answeredQuestions.size();


        progressView.setText(
                "Cycle " +
                revisionCycle +
                " • " +
                remaining +
                " questions remaining"
        );


        cycleView.setText(
                "🔄 REVISION CYCLE " +
                revisionCycle
        );
    }


    // =========================================================
    // SAVE PROGRESS
    // =========================================================

    private void saveProgress() {

        StringBuilder answered =
                new StringBuilder();


        for (Integer index :
                answeredQuestions) {

            if (answered.length() > 0) {

                answered.append(",");
            }

            answered.append(index);
        }


        prefs.edit()

                .putInt(
                        "score",
                        score
                )

                .putInt(
                        "solved",
                        solved
                )

                .putInt(
                        "streak",
                        streak
                )

                .putInt(
                        "xp",
                        xp
                )

                .putInt(
                        "revision_cycle",
                        revisionCycle
                )

                .putString(
                        "answered_questions",
                        answered.toString()
                )

                .apply();
    }


    // =========================================================
    // LOAD PROGRESS
    // =========================================================

    private void loadProgress() {

        score =
                prefs.getInt(
                        "score",
                        0
                );


        solved =
                prefs.getInt(
                        "solved",
                        0
                );


        streak =
                prefs.getInt(
                        "streak",
                        0
                );


        xp =
                prefs.getInt(
                        "xp",
                        0
                );


        revisionCycle =
                prefs.getInt(
                        "revi
