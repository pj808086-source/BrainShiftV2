package com.brainshift.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    static class Question {
        String text, answer, hint, topic;

        Question(String text, String answer, String hint, String topic) {
            this.text = text;
            this.answer = answer;
            this.hint = hint;
            this.topic = topic;
        }
    }

    private final List<Question> questions = new ArrayList<>();

    private int current = 0;
    private int score = 0;
    private int solved = 0;
    private int streak = 0;
    private int xp = 0;

    private TextView levelView, xpView, scoreView;
    private TextView streakView, solvedView, progressView;
    private TextView topicView, questionView, feedbackView;
    private EditText answerBox;
    private Button actionButton;

    private boolean waitingForNext = false;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("brain_shift", MODE_PRIVATE);

        loadProgress();
        createQuestions();
        buildUI();
        showQuestion();
    }

    private void createQuestions() {

        questions.add(new Question(
                "What is 15% of 200?",
                "30",
                "10% = 20 and 5% = 10.",
                "QUANT"));

        questions.add(new Question(
                "If CP is ₹500 and SP is ₹600, what is profit?",
                "100",
                "Profit = SP - CP.",
                "QUANT"));

        questions.add(new Question(
                "What is 12 × 8?",
                "96",
                "Multiply 12 by 8.",
                "QUANT"));

        questions.add(new Question(
                "What is the average of 10 and 20?",
                "15",
                "Add them and divide by 2.",
                "QUANT"));

        questions.add(new Question(
                "What is 25% of 400?",
                "100",
                "One-fourth of 400.",
                "QUANT"));

        questions.add(new Question(
                "What is the meaning of assets?",
                "resources",
                "Assets are economic resources controlled by a business.",
                "FINANCE"));

        questions.add(new Question(
                "What is the basic accounting equation?",
                "assets=liabilities+capital",
                "Think: what the business owns = what it owes + owner's claim.",
                "FINANCE"));

        questions.add(new Question(
                "What does ROI stand for?",
                "return on investment",
                "It measures return relative to investment.",
                "FINANCE"));

        questions.add(new Question(
                "What is revenue?",
                "income",
                "Revenue is income earned from ordinary business activities.",
                "FINANCE"));

        questions.add(new Question(
                "What is GST?",
                "goods and services tax",
                "It is an indirect tax on goods and services.",
                "FINANCE"));

        questions.add(new Question(
                "If all cats are animals and Tom is a cat, is Tom an animal?",
                "yes",
                "Use the first statement logically.",
                "LOGIC"));

        questions.add(new Question(
                "What comes next: 2, 4, 6, 8?",
                "10",
                "The numbers increase by 2.",
                "LOGIC"));

        questions.add(new Question(
                "If A is greater than B and B is greater than C, is A greater than C?",
                "yes",
                "Follow the chain of inequalities.",
                "LOGIC"));

        questions.add(new Question(
                "What comes next: 5, 10, 15, 20?",
                "25",
                "Add 5 each time.",
                "LOGIC"));

        questions.add(new Question(
                "If today is Monday, what day is after 2 days?",
                "wednesday",
                "Count Tuesday as day 1.",
                "LOGIC"));

        questions.add(new Question(
                "How many minutes should you focus before taking a short break?",
                "25",
                "Use a short focused study session.",
                "FOCUS"));

        questions.add(new Question(
                "What is better for concentration: multitasking or one task?",
                "one task",
                "Give your attention to one task.",
                "FOCUS"));

        questions.add(new Question(
                "What should you do first when starting a study session?",
                "start",
                "Do not wait for motivation. Begin.",
                "FOCUS"));

        questions.add(new Question(
                "What helps reduce phone distraction while studying?",
                "silent mode",
                "Reduce unnecessary notifications.",
                "FOCUS"));

        questions.add(new Question(
                "What is more important for consistency: motivation or discipline?",
                "discipline",
                "Motivation changes; discipline keeps the routine.",
                "FOCUS"));

        Collections.shuffle(questions);
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    private TextView makeText(String text, float size, int color) {

        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    private void buildUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(18), dp(18), dp(18));
        root.setBackgroundColor(Color.rgb(248, 249, 252));

        TextView title = makeText(
                "BRAIN SHIFT",
                28,
                Color.rgb(70, 50, 180));

        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        root.addView(title,
                new LinearLayout.LayoutParams(
                        -1, dp(55)));

        TextView subtitle = makeText(
                "CA / CMA • DAILY BRAIN TRAINING",
                12,
                Color.DKGRAY);

        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        LinearLayout stats = new LinearLayout(this);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, dp(12), 0, dp(12));

        levelView = makeText("LEVEL 1", 15, Color.rgb(70, 50, 180));
        xpView = makeText("⚡ 0 XP", 15, Color.rgb(70, 50, 180));
        scoreView = makeText("Score 0", 15, Color.DKGRAY);
        streakView = makeText("🔥 0", 15, Color.DKGRAY);

        stats.addView(levelView, weightParams());
        stats.addView(xpView, weightParams());
        stats.addView(scoreView, weightParams());
        stats.addView(streakView, weightParams());

        root.addView(stats);

        progressView = makeText(
                "TODAY • 0 / 20 COMPLETED",
                15,
                Color.rgb(50, 120, 70));

        progressView.setGravity(Gravity.CENTER);
        progressView.setPadding(0, dp(8), 0, dp(12));
        root.addView(progressView);

        topicView = makeText(
                "QUANT",
                13,
                Color.rgb(90, 70, 180));

        topicView.setTypeface(Typeface.DEFAULT_BOLD);
        topicView.setGravity(Gravity.CENTER);
        root.addView(topicView);

        questionView = makeText(
                "",
                21,
                Color.rgb(30, 30, 30));

        questionView.setTypeface(Typeface.DEFAULT_BOLD);
        questionView.setGravity(Gravity.CENTER);
        questionView.setPadding(
                dp(10), dp(25), dp(10), dp(25));

        root.addView(questionView);

        answerBox = new EditText(this);
        answerBox.setHint("Your answer");
        answerBox.setSingleLine(true);
        answerBox.setTextSize(17);
        answerBox.setPadding(
                dp(14), dp(5), dp(14), dp(5));

        root.addView(answerBox,
                new LinearLayout.LayoutParams(
                        -1, dp(58)));

        actionButton = new Button(this);
        actionButton.setText("CHECK");
        actionButton.setTextSize(16);
        actionButton.setTextColor(Color.WHITE);
        actionButton.setBackgroundColor(
                Color.rgb(91, 75, 219));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1, dp(58));

        buttonParams.topMargin = dp(12);

        root.addView(actionButton, buttonParams);

        feedbackView = makeText(
                "",
                15,
                Color.rgb(21, 128, 61));

        feedbackView.setGravity(Gravity.CENTER);
        feedbackView.setPadding(
                dp(5), dp(15), dp(5), dp(5));

        root.addView(feedbackView);

        solvedView = makeText(
                "Solved 0",
                13,
                Color.GRAY);

        solvedView.setGravity(Gravity.CENTER);
        root.addView(solvedView);

        setContentView(root);

        actionButton.setOnClickListener(v -> checkAnswer());

        answerBox.setOnEditorActionListener(
                (v, actionId, event) -> {
                    checkAnswer();
                    return true;
                });
    }

    private LinearLayout.LayoutParams weightParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(35),
                1);
    }

    private void showQuestion() {

        if (questions.isEmpty()) return;

        current = current % questions.size();

        Question q = questions.get(current);

        topicView.setText(q.topic);
        questionView.setText(q.text);

        answerBox.setText("");
        feedbackView.setText("");

        actionButton.setText("CHECK");

        waitingForNext = false;

        updateStats();

        answerBox.requestFocus();
    }

    private void checkAnswer() {

        if (waitingForNext) {
            current++;
            showQuestion();
            return;
        }

        String userAnswer = answerBox.getText()
                .toString()
                .trim()
                .toLowerCase();

        if (userAnswer.isEmpty()) {

            feedbackView.setTextColor(
                    Color.rgb(185, 28, 28));

            feedbackView.setText(
                    "Enter your answer.");

            return;
        }

        Question q = questions.get(current);

        String correct = q.answer
                .trim()
                .toLowerCase();

        boolean correctAnswer =
                normalize(userAnswer)
                        .equals(normalize(correct));

        if (correctAnswer) {

            score++;
            solved++;
            streak++;
            xp += 10;

            if (streak % 5 == 0) {
                xp += 10;

                feedbackView.setText(
                        "✓ Correct! +10 XP\n🔥 5-streak bonus +10 XP");
            } else {
                feedbackView.setText(
                        "✓ Correct! +10 XP");
            }

            feedbackView.setTextColor(
                    Color.rgb(21, 128, 61));

        } else {

            streak = 0;

            feedbackView.setTextColor(
                    Color.rgb(185, 28, 28));

            feedbackView.setText(
                    "✗ Not quite.\nHint: " + q.hint);
        }

        saveProgress();

        actionButton.setText("NEXT");
        waitingForNext = true;

        updateStats();
    }

    private String normalize(String value) {

        return value
                .replace("₹", "")
                .replace("%", "")
                .replace(",", "")
                .replace(" ", "")
                .replace(".", "")
                .toLowerCase();
    }

    private void updateStats() {

        int level = 1 + (xp / 100);

        levelView.setText(
                "LEVEL " + level);

        xpView.setText(
                "⚡ " + xp + " XP");

        scoreView.setText(
                "Score " + score);

        streakView.setText(
                "🔥 " + streak);

        solvedView.setText(
                "Solved " + solved);

        int today = solved % 20;

        if (solved > 0 && today == 0) {
            today = 20;
        }

        int remaining = Math.max(0, 20 - today);

        progressView.setText(
                "TODAY • " + today +
                " / 20 COMPLETED\n" +
                remaining + " remaining • " +
                "5 Quant • 5 Finance • 5 Logic • 5 Focus");
    }

    private void saveProgress() {

        prefs.edit()
                .putInt("score", score)
                .putInt("solved", solved)
                .putInt("streak", streak)
                .putInt("xp", xp)
                .apply();
    }

    private void loadProgress() {

        score = prefs.getInt("score", 0);
        solved = prefs.getInt("solved", 0);
        streak = prefs.getInt("streak", 0);
        xp = prefs.getInt("xp", 0);
    }
          }package com.brainshift.app;

import android.app.Activity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {

    static class Question {
        String text, answer, hint, topic;

        Question(String text, String answer, String hint, String topic) {
            this.text = text;
            this.answer = answer;
            this.hint = hint;
            this.topic = topic;
        }
    }

    private final List<Question> questions = new ArrayList<>();

    private int current = 0;
    private int score = 0;
    private int solved = 0;
    private int streak = 0;
    private int xp = 0;

    private TextView levelView, xpView, scoreView;
    private TextView streakView, solvedView, progressView;
    private TextView topicView, questionView, feedbackView;
    private EditText answerBox;
    private Button actionButton;

    private boolean waitingForNext = false;

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = getSharedPreferences("brain_shift", MODE_PRIVATE);

        loadProgress();
        createQuestions();
        buildUI();
        showQuestion();
    }

    private void createQuestions() {

        questions.add(new Question(
                "What is 15% of 200?",
                "30",
                "10% = 20 and 5% = 10.",
                "QUANT"));

        questions.add(new Question(
                "If CP is ₹500 and SP is ₹600, what is profit?",
                "100",
                "Profit = SP - CP.",
                "QUANT"));

        questions.add(new Question(
                "What is 12 × 8?",
                "96",
                "Multiply 12 by 8.",
                "QUANT"));

        questions.add(new Question(
                "What is the average of 10 and 20?",
                "15",
                "Add them and divide by 2.",
                "QUANT"));

        questions.add(new Question(
                "What is 25% of 400?",
                "100",
                "One-fourth of 400.",
                "QUANT"));

        questions.add(new Question(
                "What is the meaning of assets?",
                "resources",
                "Assets are economic resources controlled by a business.",
                "FINANCE"));

        questions.add(new Question(
                "What is the basic accounting equation?",
                "assets=liabilities+capital",
                "Think: what the business owns = what it owes + owner's claim.",
                "FINANCE"));

        questions.add(new Question(
                "What does ROI stand for?",
                "return on investment",
                "It measures return relative to investment.",
                "FINANCE"));

        questions.add(new Question(
                "What is revenue?",
                "income",
                "Revenue is income earned from ordinary business activities.",
                "FINANCE"));

        questions.add(new Question(
                "What is GST?",
                "goods and services tax",
                "It is an indirect tax on goods and services.",
                "FINANCE"));

        questions.add(new Question(
                "If all cats are animals and Tom is a cat, is Tom an animal?",
                "yes",
                "Use the first statement logically.",
                "LOGIC"));

        questions.add(new Question(
                "What comes next: 2, 4, 6, 8?",
                "10",
                "The numbers increase by 2.",
                "LOGIC"));

        questions.add(new Question(
                "If A is greater than B and B is greater than C, is A greater than C?",
                "yes",
                "Follow the chain of inequalities.",
                "LOGIC"));

        questions.add(new Question(
                "What comes next: 5, 10, 15, 20?",
                "25",
                "Add 5 each time.",
                "LOGIC"));

        questions.add(new Question(
                "If today is Monday, what day is after 2 days?",
                "wednesday",
                "Count Tuesday as day 1.",
                "LOGIC"));

        questions.add(new Question(
                "How many minutes should you focus before taking a short break?",
                "25",
                "Use a short focused study session.",
                "FOCUS"));

        questions.add(new Question(
                "What is better for concentration: multitasking or one task?",
                "one task",
                "Give your attention to one task.",
                "FOCUS"));

        questions.add(new Question(
                "What should you do first when starting a study session?",
                "start",
                "Do not wait for motivation. Begin.",
                "FOCUS"));

        questions.add(new Question(
                "What helps reduce phone distraction while studying?",
                "silent mode",
                "Reduce unnecessary notifications.",
                "FOCUS"));

        questions.add(new Question(
                "What is more important for consistency: motivation or discipline?",
                "discipline",
                "Motivation changes; discipline keeps the routine.",
                "FOCUS"));

        Collections.shuffle(questions);
    }

    private int dp(float value) {
        return (int) (value * getResources()
                .getDisplayMetrics().density + 0.5f);
    }

    private TextView makeText(String text, float size, int color) {

        TextView v = new TextView(this);
        v.setText(text);
        v.setTextSize(size);
        v.setTextColor(color);
        return v;
    }

    private void buildUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(18), dp(18), dp(18), dp(18));
        root.setBackgroundColor(Color.rgb(248, 249, 252));

        TextView title = makeText(
                "BRAIN SHIFT",
                28,
                Color.rgb(70, 50, 180));

        title.setTypeface(Typeface.DEFAULT_BOLD);
        title.setGravity(Gravity.CENTER);

        root.addView(title,
                new LinearLayout.LayoutParams(
                        -1, dp(55)));

        TextView subtitle = makeText(
                "CA / CMA • DAILY BRAIN TRAINING",
                12,
                Color.DKGRAY);

        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle);

        LinearLayout stats = new LinearLayout(this);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, dp(12), 0, dp(12));

        levelView = makeText("LEVEL 1", 15, Color.rgb(70, 50, 180));
        xpView = makeText("⚡ 0 XP", 15, Color.rgb(70, 50, 180));
        scoreView = makeText("Score 0", 15, Color.DKGRAY);
        streakView = makeText("🔥 0", 15, Color.DKGRAY);

        stats.addView(levelView, weightParams());
        stats.addView(xpView, weightParams());
        stats.addView(scoreView, weightParams());
        stats.addView(streakView, weightParams());

        root.addView(stats);

        progressView = makeText(
                "TODAY • 0 / 20 COMPLETED",
                15,
                Color.rgb(50, 120, 70));

        progressView.setGravity(Gravity.CENTER);
        progressView.setPadding(0, dp(8), 0, dp(12));
        root.addView(progressView);

        topicView = makeText(
                "QUANT",
                13,
                Color.rgb(90, 70, 180));

        topicView.setTypeface(Typeface.DEFAULT_BOLD);
        topicView.setGravity(Gravity.CENTER);
        root.addView(topicView);

        questionView = makeText(
                "",
                21,
                Color.rgb(30, 30, 30));

        questionView.setTypeface(Typeface.DEFAULT_BOLD);
        questionView.setGravity(Gravity.CENTER);
        questionView.setPadding(
                dp(10), dp(25), dp(10), dp(25));

        root.addView(questionView);

        answerBox = new EditText(this);
        answerBox.setHint("Your answer");
        answerBox.setSingleLine(true);
        answerBox.setTextSize(17);
        answerBox.setPadding(
                dp(14), dp(5), dp(14), dp(5));

        root.addView(answerBox,
                new LinearLayout.LayoutParams(
                        -1, dp(58)));

        actionButton = new Button(this);
        actionButton.setText("CHECK");
        actionButton.setTextSize(16);
        actionButton.setTextColor(Color.WHITE);
        actionButton.setBackgroundColor(
                Color.rgb(91, 75, 219));

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1, dp(58));

        buttonParams.topMargin = dp(12);

        root.addView(actionButton, buttonParams);

        feedbackView = makeText(
                "",
                15,
                Color.rgb(21, 128, 61));

        feedbackView.setGravity(Gravity.CENTER);
        feedbackView.setPadding(
                dp(5), dp(15), dp(5), dp(5));

        root.addView(feedbackView);

        solvedView = makeText(
                "Solved 0",
                13,
                Color.GRAY);

        solvedView.setGravity(Gravity.CENTER);
        root.addView(solvedView);

        setContentView(root);

        actionButton.setOnClickListener(v -> checkAnswer());

        answerBox.setOnEditorActionListener(
                (v, actionId, event) -> {
                    checkAnswer();
                    return true;
                });
    }

    private LinearLayout.LayoutParams weightParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(35),
                1);
    }

    private void showQuestion() {

        if (questions.isEmpty()) return;

        current = current % questions.size();

        Question q = questions.get(current);

        topicView.setText(q.topic);
        questionView.setText(q.text);

        answerBox.setText("");
        feedbackView.setText("");

        actionButton.setText("CHECK");

        waitingForNext = false;

        updateStats();

        answerBox.requestFocus();
    }

    private void checkAnswer() {

        if (waitingForNext) {
            current++;
            showQuestion();
            return;
        }

        String userAnswer = answerBox.getText()
                .toString()
                .trim()
                .toLowerCase();

        if (userAnswer.isEmpty()) {

            feedbackView.setTextColor(
                    Color.rgb(185, 28, 28));

            feedbackView.setText(
                    "Enter your answer.");

            return;
        }

        Question q = questions.get(current);

        String correct = q.answer
                .trim()
                .toLowerCase();

        boolean correctAnswer =
                normalize(userAnswer)
                        .equals(normalize(correct));

        if (correctAnswer) {

            score++;
            solved++;
            streak++;
            xp += 10;

            if (streak % 5 == 0) {
                xp += 10;

                feedbackView.setText(
                        "✓ Correct! +10 XP\n🔥 5-streak bonus +10 XP");
            } else {
                feedbackView.setText(
                        "✓ Correct! +10 XP");
            }

            feedbackView.setTextColor(
                    Color.rgb(21, 128, 61));

        } else {

            streak = 0;

            feedbackView.setTextColor(
                    Color.rgb(185, 28, 28));

            feedbackView.setText(
                    "✗ Not quite.\nHint: " + q.hint);
        }

        saveProgress();

        actionButton.setText("NEXT");
        waitingForNext = true;

        updateStats();
    }

    private String normalize(String value) {

        return value
                .replace("₹", "")
                .replace("%", "")
                .replace(",", "")
                .replace(" ", "")
                .replace(".", "")
                .toLowerCase();
    }

    private void updateStats() {

        int level = 1 + (xp / 100);

        levelView.setText(
                "LEVEL " + level);

        xpView.setText(
                "⚡ " + xp + " XP");

        scoreView.setText(
                "Score " + score);

        streakView.setText(
                "🔥 " + streak);

        solvedView.setText(
                "Solved " + solved);

        int today = solved % 20;

        if (solved > 0 && today == 0) {
            today = 20;
        }

        int remaining = Math.max(0, 20 - today);

        progressView.setText(
                "TODAY • " + today +
                " / 20 COMPLETED\n" +
                remaining + " remaining • " +
                "5 Quant • 5 Finance • 5 Logic • 5 Focus");
    }

    private void saveProgress() {

        prefs.edit()
                .putInt("score", score)
                .putInt("solved", solved)
                .putInt("streak", streak)
                .putInt("xp", xp)
                .apply();
    }

    private void loadProgress() {

        score = prefs.getInt("score", 0);
        solved = prefs.getInt("solved", 0);
        streak = prefs.getInt("streak", 0);
        xp = prefs.getInt("xp", 0);
    }}
