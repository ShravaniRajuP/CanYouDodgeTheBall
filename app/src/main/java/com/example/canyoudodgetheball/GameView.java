package com.example.canyoudodgetheball;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Thread.sleep;

public class GameView extends View {

    private Button pause_resume;
    private Button new_start_end;
    private TextView Score;
    private TextView Lives;
    private String buttonName;
    MainActivity activity;
    BlackCircleMoves basync;
    WhiteCirclesFall wasync;
    GameStates states;
    private boolean isTouched=true;
    List<Circles> points = new ArrayList<Circles>();
    private Paint white;
    private Paint black;
    private float x_circle;
    private float y_circle;
    private float x_coordinate;
    private float radius;
    float distance_centers;
    int lives;
    int score;
    int scoreinc;
    float speedinc;
    float speed;
    int number_of_touches;

    public GameView(Context context){
        super(context);
        game_vars(null);
    }

    public GameView(Context context, AttributeSet attrs){
        super(context, attrs);
        game_vars(attrs);
    }

    private void game_vars(@Nullable AttributeSet attrs){
        states = new GameStates();
        activity = new MainActivity();
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        score = 0;
        scoreinc = 0;
        speed = 3;
        speedinc = 4;
        lives = 3;
        for (Circles c: points){
            c.setScoreInc(0);
            c.setSpeed(2);
        }
        radius = 30;
        x_circle = 500;
        y_circle = 1400;
    }

    public void setButtonNames(TextView score, final Button pauseresume, Button newstartend, TextView lives){
        Score = score;
        Lives = lives;
        pause_resume = pauseresume;
        pause_resume.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if((states.isNew || states.isEnd)){
                    states.isPaused=false;
                    states.isResume=false;
                    states.isStart=false;
                }
                else if(!states.isPaused) {
                    states.isPaused=true;
                    states.isResume=false;
                    states.isStart=false;
                    pause_resume.setText("Resume");
                }
                else if(!states.isResume) {
                    states.isResume=true;
                    states.isPaused=false;
                    states.isStart=true;
                    pause_resume.setText("Pause");
                    whiteCirclesFall();
                }
            }
        });

        new_start_end = newstartend;
        new_start_end.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                buttonName = new_start_end.getText().toString();
                switch (buttonName) {
                    case "New":
                        states.isStart = false;
                        states.isNew = true;
                        states.isEnd = false;
                        states.isPaused = false;
                        states.isResume = false;
                        new_start_end.setText("Start");
                        pause_resume.setText("Pause");
                        points.clear();
                        break;
                    case "Start":
                        states.isStart = true;
                        states.isNew = false;
                        states.isEnd = false;
                        new_start_end.setText("End");
                        whiteCirclesFall();
                        break;
                    case "End":
                        states.isStart = false;
                        states.isNew = false;
                        states.isEnd = true;
                        states.isPaused = false;
                        states.isResume = false;
                        new_start_end.setText("New");
                        pause_resume.setText("Pause");
                }
            }
        });
    }

    private class WhiteCirclesFall extends AsyncTask<Float,Float, Float> {
        int a = points.size();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            points.get(a-1).setRadius(10f);
        }

        @Override
        protected Float doInBackground(Float... inte) {
            float radi;
            radi=points.get(a-1).getRadius();
            while(isTouched) {
                try {
                    sleep(200);
                    radi+=7f;
                    points.get(a-1).setRadius(radi);
                    publishProgress(points.get(a-1).getRadius());
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return points.get(a-1).getRadius();
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            points.get(a-1).setRadius(values[0]);
            invalidate();
        }
        @Override
        protected void onPostExecute(Float fl) {
            super.onPostExecute(fl);
            invalidate();
        }
    }

    private class BlackCircleMoves extends AsyncTask<Float, Float, Float> {
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }

        @Override
        protected Float doInBackground(Float... x) {
            while (isTouched && number_of_touches == 1) {
                try {
                    sleep(20);
                    if (x_coordinate > x_circle)
                        x_circle += 5;
                    else if (x_coordinate < x_circle)
                        x_circle -= 5;
                    else if (x_coordinate == x_circle)
                        x_circle = x_coordinate;
                    publishProgress(x_circle);
                    postInvalidate();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return x_coordinate;
        }

        @Override
        protected void onProgressUpdate(Float... values) {
            super.onProgressUpdate(values);
            invalidate();
        }

        @Override
        protected void onPostExecute(Float fl) {
            super.onPostExecute(fl);
            invalidate();
        }
    }

    public void whiteCirclesFall() {
        if(states.isStart || lives!=0) {
            for(Circles c:points) {
                distance_centers= (float) Math.sqrt(Math.pow( ((double) (c.getX()-x_circle)),2) + Math.pow(((double) (c.getY()-y_circle)),2));
                if(c.getY() > getHeight()) {
                    c.setY(100);
                    c.setScoreInc(c.scoreInc + 1);
                    scoreinc = c.getScoreInc();
                    score += scoreinc;
                    if(c.getSpeed() != 0)
                        speed = c.getSpeed();
                    else
                        speed = 2;
                    if(c.getSpeedInc() != 0)
                        speedinc = c.getSpeedInc();
                    else
                        speedinc = 4;
                    c.setSpeed(speed + speed / speedinc);
                    speed = c.getSpeed();
                    c.setSpeedInc(speedinc / 2);
                }
                else if(distance_centers<(radius+c.getRadius())) {
                    states.isTouched=true;
                    c.setY(100);
                    lives-=1;
                }
                c.setY(c.getY()+speed);
            }
            if(states.isTouched) {
                Lives.setText("Lives: " +String.valueOf(lives));
                states.isTouched=false;
            }
        }
        if(lives==0) {
            Toast.makeText(getContext(),"You Lost!",Toast.LENGTH_LONG).show();
            states.isStart = false;
            states.isPaused = false;
            states.isNew = false;
            states.isResume = false;
            states.isEnd = true;
            new_start_end.setText("New");
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas){
        white.setColor(Color.BLACK);
        white.setStyle(Paint.Style.STROKE);
        black.setColor(Color.BLACK);
        if(states.isStart) {
            black.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x_circle, y_circle, radius, black);
            Score.setText("Score: "+String.valueOf(score));
            Lives.setText("Lives: "+String.valueOf(lives));
            for (Circles c : points) {
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), white);
            }
            whiteCirclesFall();
        }
        else if(states.isPaused){
            black.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x_circle, y_circle, radius, black);
            if(!states.isTouched)
                Score.setText("Score: "+String.valueOf(score));
            for (Circles c : points)
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), white);
            Score.setText("Score: "+String.valueOf(score));
        }
        else if(states.isEnd) {
            lives = 3;
            Score.setText("Score: "+String.valueOf(score));
            black.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x_circle, y_circle, radius, black);
            for (Circles c : points)
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), white);
            pause_resume.setText("Pause");
        }
        else if(states.isNew){
            score = 0;
            lives = 3;
            Score.setText("Score: "+String.valueOf(score));
            Lives.setText("Lives: "+lives);
            black.setStyle(Paint.Style.FILL_AND_STROKE);
            canvas.drawCircle(x_circle, y_circle, radius, black);
            for (Circles c : points)
                canvas.drawCircle(c.getX(), c.getY(), c.getRadius(), white);
            pause_resume.setText("Pause");
            new_start_end.setText("Start");
        }
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean value = super.onTouchEvent(event);
        float x = 0f;
        float y = 0f;
        this.number_of_touches = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (new_start_end.getText().toString().equals("Start")) {
                    isTouched = true;
                    Circles c1 = new Circles();
                    c1.setX(event.getX());
                    c1.setY(event.getY());
                    c1.setRadius(10f);
                    if ((c1.getY() - 100) < y_circle) {
                        points.add(c1);
                        wasync = new WhiteCirclesFall();
                        wasync.execute(10f);
                    }
                } else if (new_start_end.getText().toString().equals("End")) {
                    isTouched = true;
                    basync = new BlackCircleMoves();
                    this.x_coordinate = event.getX();
                    basync.execute(event.getX());
                }
                postInvalidate();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                if (new_start_end.getText().toString().equals("Start") || new_start_end.getText().toString().equals("End"))
                    isTouched = false;
                postInvalidate();
                return true;
            }
        }
        return value;
    }
}
