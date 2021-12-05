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

    //Positions
    private float RockX;
    private float RockY;
    private float RollX;
    private float RollY;
    private float FireObX;
    private float FireObY;

    // Player
    private GestureDetector gestureDetector;
    private ImageView player;
    private ImageView hitBox;
    private float hitBoxX;
    private float hitBoxY;

    //Score and live
    private int lives = 3;
    private long time_int;
    private long time_current;
    private long time_elapsed;

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
        // move rock
        RockY += 10;
        if (Rock.getY() > screenHeight){
            RockX = (float)Math.floor(Math.random() * (screenWidth - Rock.getWidth()));
            RockY = -Rock.getHeight();
        }
        // move Boulder
        RollY += 10;
        if (Roll.getY() > screenHeight){
            RollX = (float)Math.floor(Math.random() * (screenWidth - Roll.getWidth()));
            RollY = -Roll.getHeight();
        }

        FireObY += 10;
        if (FireOb.getY() > screenHeight){
            FireObX = (float)Math.floor(Math.random() * (screenWidth - FireOb.getWidth()));
            FireObY = -FireOb.getHeight();
        }


        //if (hitBoxX > RollY && hitBox.getX + hitBox.getWidth() / 2 < RollY
        //)
        if (hit){
            loseLife();
        }

        Rock.setX(RockX);
        Rock.setY(RockY);
        Roll.setX(RollX);
        Roll.setY(RollY);
        FireOb.setX(FireObX);
        FireOb.setY(FireObY);
    }

    private void loseLife(){
        lives -= 1;

        if(lives == 2){
            Life1.setVisibility(View.INVISIBLE);
        } else if ( lives == 1){
            Life2.setVisibility(View.INVISIBLE);
        } else {
            timer.cancel();
            Life3.setVisibility(View.INVISIBLE);
            //Do Dalton
        }

    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    /*
    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDown(MotionEvent event) {
            Log.d("TAG","onDown: ");
            holding_item = true;

            // don't return false here or else none of the other
            // gestures will work
            return true;
        }


        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("TAG", "onSingleTapConfirmed: ");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("TAG", "onLongPress: ");
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("TAG", "onDoubleTap: ");
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            Log.i("TAG", "onScroll: ");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {
            Log.d("TAG", "onFling: ");
            return true;
        }

    }

     */
}