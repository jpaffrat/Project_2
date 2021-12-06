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
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

// TODO Speed up guys with time // different difficulty algorithems
// TODO Invincible once hit  - could flash
// TODO Make the object fall at variing speeds and heights

public class GameActivity extends AppCompatActivity {

    //Screen Size
    private int screenWidth;
    private int screenHeight;

    //objects
    private ImageView Rock;
    private ImageView Roll;
    private ImageView FireOb;
    private ImageView Life1;
    private ImageView Life2;
    private ImageView Life3;
    private TextView scoreBoard;
    private TextView livesBoard;

    //Positions
    private float RockX;
    private float RockY;
    private float RockSpeedBonus;
    private float RollX;
    private float RollY;
    private float RollSpeedBonus;
    private float FireObX;
    private float FireObY;
    private float FireObSpeedBonus;
    private float speed_increase;

    // Player
    private GestureDetector gestureDetector;
    private ImageView player;
    private ImageView hitBox;
    private float hitBoxX;
    private float hitBoxY;

    //Score and live
    private int lives = 4;
    private long time_int;
    private long time_current;
    private long time_elapsed;
    private long time_hit;
    private long time_system;
    private boolean Invincibility_on = false;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // dummy var / delete later
    private boolean hit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        scoreBoard = (TextView)findViewById(R.id.Score_id);
        livesBoard = (TextView)findViewById(R.id.Lives_id);

        Life1 = (ImageView)findViewById(R.id.Life1);
        Life2 = (ImageView)findViewById(R.id.Life2);
        Life3 = (ImageView)findViewById(R.id.Life3);

        Rock = (ImageView)findViewById(R.id.Angry_Rock);
        Roll = (ImageView)findViewById(R.id.Big_boulder);
        FireOb = (ImageView)findViewById(R.id.Fire);

        RockX = (float)Math.floor(Math.random() * (screenWidth - Rock.getWidth()));
        RollX = (float)Math.floor(Math.random() * (screenWidth - Roll.getWidth()));
        FireObX = (float)Math.floor(Math.random() * (screenWidth - FireOb.getWidth()));

        //Get screen Size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenHeight = size.y;
        screenWidth = size.x;

        //Move to out of screen
        Rock.setX(-80.0f);
        Rock.setY(-80.0f);
        FireOb.setY(-80.0f);

        //Move around player
        player = (ImageView) findViewById(R.id.Player_id);
        hitBox = (ImageView) findViewById(R.id.Hit_box_id);
        // gestureDetector = new GestureDetector(this, new MyGestureListener());
        // player.setOnTouchListener(touchListener);
        player.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                player.setX(motionEvent.getRawX() - player.getWidth() / 2);
                player.setY(motionEvent.getRawY() - player.getHeight() / 2);

                hitBoxX = motionEvent.getRawX() - hitBox.getWidth() / 2;
                hitBox.setX(hitBoxX);
                hitBoxY = motionEvent.getRawY() - hitBox.getHeight() + 23;
                hitBox.setY(hitBoxY);
                return true;
            }
        });


        //Start the Timer
        time_int = System.currentTimeMillis()/100;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable(){
                    @Override
                    public void run() {
                        changePos();
                        time_current = System.currentTimeMillis()/100;
                        time_elapsed = time_current - time_int;
                        scoreBoard.setText(String.format("%d", time_elapsed));
                    }
                });
            }
        }, 0 , 20);
    }

    public void changePos() {
        //Speed up as level progress
        speed_increase = time_elapsed / 10;

        // move rock
        RockY += 10 + speed_increase + RockSpeedBonus;
        if (Rock.getY() > screenHeight){
            RockX = (float)Math.floor(Math.random() * (screenWidth - Rock.getWidth()));
            RockSpeedBonus = (float)Math.floor(Math.random() * 3);
            RockY = -Rock.getHeight();
        }
        // move Boulder
        RollY += 10 + speed_increase + RollSpeedBonus;
        if (Roll.getY() > screenHeight){
            RollX = (float)Math.floor(Math.random() * (screenWidth - Roll.getWidth()));
            RollSpeedBonus = (float)Math.floor(Math.random() * 3);
            RollY = -Roll.getHeight();
        }

        FireObY += 10 + speed_increase + FireObSpeedBonus;
        if (FireOb.getY() > screenHeight){
            FireObX = (float)Math.floor(Math.random() * (screenWidth - FireOb.getWidth()));
            FireObSpeedBonus = (float)Math.floor(Math.random() * 3);
            FireObY = -FireOb.getHeight();
        }

        // Hit By Roll
        if (hitBoxX < RollX + hitBox.getWidth() - 30 && hitBoxX + hitBox.getWidth() > RollX &&
               hitBoxY < RollY + Roll.getHeight() - 20 && hitBoxY + hitBox.getHeight() > RollY){
            loseLife();
        }

        // Hit by Rock
        if (hitBoxX < RockX + hitBox.getWidth() && hitBoxX + hitBox.getWidth() > RockX &&
               hitBoxY < RockY + Roll.getHeight() && hitBoxY + hitBox.getHeight() > RockY){
            loseLife();
        }

        // Hit by Fire
        if (hitBoxX < FireObX + hitBox.getWidth() && hitBoxX + hitBox.getWidth() > FireObX &&
                hitBoxY < FireObY + Roll.getHeight() && hitBoxY + hitBox.getHeight() > FireObY){
            loseLife();
        }


        if (hit){
            loseLife();
        }

//        RockX = screenWidth / 2;
//        RockY = screenHeight / 2;

        Rock.setX(RockX);
        Rock.setY(RockY);
        Roll.setX(RollX);
        Roll.setY(RollY);
        FireOb.setX(FireObX);
        FireOb.setY(FireObY);
    }

    private void loseLife(){
        if(!Invincibility_on){
            lives -= 1;
            Invincibility_on = true;
            time_hit = System.currentTimeMillis()/100;
            livesBoard.setText(String.format("%d", lives));
            handler.postDelayed(Invincibility_invisible, 0);
        }



//        if(lives == 2){
//            Life1.setVisibility(View.INVISIBLE);
//        } else if ( lives == 1){
//            Life2.setVisibility(View.INVISIBLE);
//        } else {
//            timer.cancel();
//            Life3.setVisibility(View.INVISIBLE);
//            //Do Dalton
//        }

    }

    public Runnable Invincibility_invisible = new Runnable() {
        @Override
        public void run() {
            player.setVisibility(View.INVISIBLE);
            time_system = System.currentTimeMillis()/100;
            if(time_system - time_hit > 20){
                handler.removeCallbacks(Invincibility_invisible);
                handler.removeCallbacks(Invincibility_visible);
                player.setVisibility(View.VISIBLE);
                Invincibility_on = false;
            }else{
                handler.postDelayed(Invincibility_visible, 200);
            }
        }
    };

    public Runnable Invincibility_visible = new Runnable() {
        @Override
        public void run() {
            player.setVisibility(View.VISIBLE);
            time_system = System.currentTimeMillis()/100;
            if(time_system - time_hit > 20){
                handler.removeCallbacks(Invincibility_invisible);
                handler.removeCallbacks(Invincibility_visible);
                Invincibility_on = false;
            }else{
                handler.postDelayed(Invincibility_invisible, 200);
            }
        }
    };

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };
}