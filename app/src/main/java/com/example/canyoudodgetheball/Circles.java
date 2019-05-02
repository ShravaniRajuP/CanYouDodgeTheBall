package com.example.canyoudodgetheball;

public class Circles {
    private float x;
    private float y;
    private float radius;
    private float speedInc;
    int scoreInc;
    private float speed;

    public void setX(float value) {
        x=value;
    }
    public void setY(float value) {
        y=value;
    }
    public void setRadius(float value) {
        radius=value;
    }
    public void setSpeedInc(float speedInc){
        this.speedInc = speedInc;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public void setScoreInc(int scoreInc){
        this.scoreInc = scoreInc;
    }
    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
    public float getRadius() {
        return radius;
    }
    public int getScoreInc(){
        return scoreInc;
    }
    public float getSpeedInc(){
        return speedInc;
    }
    public float getSpeed(){
        return speed;
    }
}