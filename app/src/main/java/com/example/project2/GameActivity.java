package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

public class GameActivity extends AppCompatActivity {

    //Screen Size
    private int screenWidth;
    private int screenHeight;

    //objects
    private ImageView Rock;
    private ImageView Roll;

    //Positions
    private float RockX;
    private float RockY;
    private float RollX;
    private float RollY;
    private float PlayerX;

    // Player
    private GestureDetector gestureDetector;
    private ImageView player;
    private ImageView hitBox;
    private float hitBoxX;
    private float hitBoxY;
    private int lives = 3;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // dummy var / delete later
    private boolean hit = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
    }
}