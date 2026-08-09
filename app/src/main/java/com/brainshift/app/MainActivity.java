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

    private void buildUI() {

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(20), dp(20), dp(20));
        root.setBackgroundColor(Color.rgb(245, 247, 250));

        TextView title = makeText(
                "🧠 BrainShift",
                28,
                Color.rgb(25, 25, 25)
        );
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(title);

        TextView subtitle = makeText(
                "CA / CMA Practice",
                15,
                Color.DKGRAY
        );
        root.addView(subtitle);

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setGravity(Gravity.CENTER);
        stats.setPadding(0, dp(15), 0, dp(15));

        levelView = makeText("LEVEL 1", 14, Color.DKGRAY);
        xpView = makeText("⚡ 0 XP", 14, Color.DKGRAY);
        scoreView = makeText("Score 0", 14, Color.DKGRAY);
        streakView = makeText("🔥 0", 14, Color.DKGRAY);

        stats.addView(levelView, weightParams());
        stats.addView(xpView, weightParams());
        stats.addView(scoreView, weightParams());
        stats.addView(streakView, weightParams());

        root.addView(stats);

        solvedView = makeText(
                "Solved: 0",
                15,
                Color.DKGRAY
        );
        root.addView(solvedView);

        progressView = makeText(
                "TODAY • 0 / 20 COMPLETED",
                15,
                Color.DKGRAY
        );
        progressView.setPadding(0, dp(8), 0, dp(15));
        root.addView(progressView);

        topicView = makeText(
                "TOPIC",
                14,
                Color.rgb(80, 80, 80)
        );
        topicView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        root.addView(topicView);

        questionView = makeText(
                "",
                20,
                Color.BLACK
        );
        questionView.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        questionView.setPadding(0, dp(15), 0, dp(15));
        root.addView(questionView);

        answerBox = new EditText(this);
        answerBox.setHint("Type your answer...");
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
        actionButton.setText("CHECK ANSWER");
        actionButton.setOnClickListener(v -> handleAction());

        LinearLayout.LayoutParams buttonParams =
                new LinearLayout.LayoutParams(
                        -1,
                        dp(55)
                );

        buttonParams.topMargin = dp(15);
        root.addView(actionButton, buttonParams);

        feedbackView = makeText(
                "",
                16,
                Color.DKGRAY
        );
        feedbackView.setPadding(0, dp(15), 0, dp(10));
        root.addView(feedbackView);

        setContentView(root);
    }

    private void handleAction() {

        if (questions.isEmpty()) {
            return;
        }

        if (waitingForNext) {
            current++;

            if (current >= questions.size()) {
                current = 0;
            }

            waitingForNext = false;
            actionButton.setText("CHECK ANSWER");
            answerBox.setText("");
            feedbackView.setText("");
            showQuestion();

            return;
        }

        checkAnswer();
    }

    private void showQuestion() {

        if (questions.isEmpty()) {
            questionView.setText("No questions available.");
            return;
        }

        Question q = questions.get(current);

        topicView.setText("📚 " + q.topic);
        questionView.setText(q.text);
        answerBox.setText("");
        answerBox.requestFocus();

        updateStats();
    }

    private void checkAnswer() {

        Question q = questions.get(current);

        String userAnswer = normalize(answerBox.getText().toString());
        String correctAnswer = normalize(q.answer);

        if (userAnswer.isEmpty()) {
            feedbackView.setText("⚠️ Please enter an answer.");
            return;
        }

        if (userAnswer.equals(correctAnswer)) {

            score += 10;
            xp += 10;
            solved++;
            streak++;

            feedbackView.setTextColor(
                    Color.rgb(20, 120, 60)
            );

            feedbackView.setText(
                    "✓ Correct!\n+10 XP"
            );

        } else {

            streak = 0;

            feedbackView.setTextColor(
                    Color.rgb(180, 40, 40)
            );

            feedbackView.setText(
                    "✗ Not quite.\nHint: " + q.hint +
                    "\nCorrect answer: " + q.answer
            );
        }

        saveProgress();

        actionButton.setText("NEXT");
        waitingForNext = true;

        updateStats();
    }

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

    private void updateStats() {

        int level = 1 + (xp / 100);

        levelView.setText("LEVEL " + level);
        xpView.setText("⚡ " + xp + " XP");
        scoreView.setText("Score " + score);
        streakView.setText("🔥 " + streak);
        solvedView.setText("Solved: " + solved);

        int today = solved % 20;

        if (solved > 0 && today == 0) {
            today = 20;
        }

        int remaining = Math.max(0, 20 - today);

        progressView.setText(
                "TODAY • " + today +
                " / 20 COMPLETED\n" +
                remaining + " remaining • " +
                "5 Quant • 5 Finance • 5 Logic • 5 Focus"
        );
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

    private TextView makeText(
            String text,
            float size,
            int color
    ) {

        TextView view = new TextView(this);

        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(color);

        return view;
    }

    private LinearLayout.LayoutParams weightParams() {

        return new LinearLayout.LayoutParams(
                0,
                dp(45),
                1
        );
    }

    private int dp(int value) {

        return (int) (
                value * getResources()
                        .getDisplayMetrics()
                        .density
        );
    }
                     }
        
