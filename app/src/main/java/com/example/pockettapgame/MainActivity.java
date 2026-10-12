package com.example.pockettapgame;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;
import java.util.Random;

public class MainActivity extends Activity {
    private final Random random = new Random();
    private FrameLayout gameArea;
    private TextView scoreText, timeText, messageText;
    private Button startButton;
    private int score = 0;
    private boolean playing = false;
    private CountDownTimer timer;
    private final int navy = Color.rgb(22, 34, 66);
    private final int purple = Color.rgb(111, 76, 255);
    private final int mint = Color.rgb(67, 220, 177);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        buildScreen();
    }

    private void buildScreen() {
        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(dp(20), dp(22), dp(20), dp(18));
        root.setBackgroundColor(Color.rgb(246, 247, 252));

        TextView title = new TextView(this);
        title.setText("POCKET TAP");
        title.setTextColor(navy);
        title.setTextSize(27);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setGravity(Gravity.CENTER);
        root.addView(title, new LinearLayout.LayoutParams(-1, dp(46)));

        TextView subtitle = new TextView(this);
        subtitle.setText("Tap the target as many times as you can!");
        subtitle.setTextColor(Color.rgb(100, 108, 130));
        subtitle.setTextSize(14);
        subtitle.setGravity(Gravity.CENTER);
        root.addView(subtitle, new LinearLayout.LayoutParams(-1, dp(32)));

        LinearLayout stats = new LinearLayout(this);
        stats.setOrientation(LinearLayout.HORIZONTAL);
        stats.setPadding(0, dp(12), 0, dp(12));
        scoreText = makeStat("SCORE", "0");
        timeText = makeStat("TIME", "20");
        stats.addView(scoreText, new LinearLayout.LayoutParams(0, dp(74), 1));
        View gap = new View(this);
        stats.addView(gap, new LinearLayout.LayoutParams(dp(12), 1));
        stats.addView(timeText, new LinearLayout.LayoutParams(0, dp(74), 1));
        root.addView(stats);

        gameArea = new FrameLayout(this);
        gameArea.setBackground(roundRect(Color.WHITE, dp(24), Color.rgb(228, 231, 242), dp(1)));
        LinearLayout.LayoutParams areaParams = new LinearLayout.LayoutParams(-1, 0, 1);
        areaParams.setMargins(0, dp(6), 0, dp(14));
        root.addView(gameArea, areaParams);

        messageText = new TextView(this);
        messageText.setText("Ready when you are?");
        messageText.setTextColor(navy);
        messageText.setTextSize(16);
        messageText.setGravity(Gravity.CENTER);
        root.addView(messageText, new LinearLayout.LayoutParams(-1, dp(36)));

        startButton = new Button(this);
        startButton.setText("START GAME");
        startButton.setTextColor(Color.WHITE);
        startButton.setTextSize(15);
        startButton.setAllCaps(false);
        startButton.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        startButton.setBackground(roundRect(purple, dp(16), purple, 0));
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(-1, dp(54));
        buttonParams.setMargins(0, dp(6), 0, 0);
        root.addView(startButton, buttonParams);
        startButton.setOnClickListener(v -> startGame());

        setContentView(root);
    }

    private TextView makeStat(String label, String value) {
        TextView view = new TextView(this);
        view.setText(label + "\n" + value);
        view.setTextColor(navy);
        view.setTextSize(16);
        view.setGravity(Gravity.CENTER);
        view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setBackground(roundRect(Color.WHITE, dp(16), Color.rgb(228, 231, 242), dp(1)));
        return view;
    }

    private void startGame() {
        if (timer != null) timer.cancel();
        score = 0;
        playing = true;
        updateStats(20);
        messageText.setText("Go! Find the target.");
        startButton.setText("PLAYING…");
        startButton.setEnabled(false);
        gameArea.removeAllViews();
        spawnTarget();

        timer = new CountDownTimer(20000, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                updateStats((int) (millisUntilFinished / 1000));
            }
            @Override public void onFinish() {
                playing = false;
                updateStats(0);
                gameArea.removeAllViews();
                messageText.setText("Time's up! You scored " + score + ".");
                startButton.setText("PLAY AGAIN");
                startButton.setEnabled(true);
            }
        }.start();
    }

    private void spawnTarget() {
        if (!playing) return;
        gameArea.post(() -> {
            if (!playing) return;
            gameArea.removeAllViews();
            int size = dp(68);
            int maxX = Math.max(1, gameArea.getWidth() - size);
            int maxY = Math.max(1, gameArea.getHeight() - size);
            int x = random.nextInt(maxX);
            int y = random.nextInt(maxY);

            TextView target = new TextView(this);
            target.setText("TAP");
            target.setTextColor(navy);
            target.setTextSize(14);
            target.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
            target.setGravity(Gravity.CENTER);
            target.setBackground(roundRect(mint, size, mint, 0));
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(size, size);
            params.leftMargin = x;
            params.topMargin = y;
            gameArea.addView(target, params);
            target.setOnClickListener(v -> {
                if (!playing) return;
                score++;
                updateStats(Math.max(0, Integer.parseInt(timeText.getText().toString().split("\n")[1])));
                messageText.setText("Nice tap! Keep going.");
                spawnTarget();
            });
        });
    }

    private void updateStats(int seconds) {
        scoreText.setText("SCORE\n" + score);
        timeText.setText("TIME\n" + seconds);
    }

    private GradientDrawable roundRect(int color, int radius, int strokeColor, int strokeWidth) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        if (strokeWidth > 0) drawable.setStroke(strokeWidth, strokeColor);
        return drawable;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    protected void onDestroy() {
        if (timer != null) timer.cancel();
        super.onDestroy();
    }
}
