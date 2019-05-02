package com.example.canyoudodgetheball;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    GameView shapes;
    public TextView score;
    public Button pause_resume;
    public Button new_start_end;
    public TextView lives;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shapes = findViewById(R.id.gameView);
        score = findViewById(R.id.Score);
        pause_resume = findViewById(R.id.PauseResume);
        new_start_end = findViewById(R.id.NewStartEnd);
        lives = findViewById(R.id.Lives);
        shapes.setButtonNames(score, pause_resume, new_start_end, lives);
    }
}