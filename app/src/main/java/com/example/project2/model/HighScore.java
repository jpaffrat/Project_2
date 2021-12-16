package com.example.project2.model;


/**
 * an object used to pass in highscores to the firestore daatabase
 */
public class HighScore {

    public String user;
    public int score;



    public HighScore() {}

    public HighScore(String user, int score){

        this.user = user;
        this.score = score;

    }

    public void setUser(String user){
        this.user = user;
    }

    public void setScore(int score){
        this.score = score;
    }
}
