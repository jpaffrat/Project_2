package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.graphics.Bitmap;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2.model.HighScore;
import com.example.project2.util.FirebaseUtil;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Timer;
import java.util.TimerTask;

// TODO Speed up guys with time // different difficulty algorithems
// TODO Invincible once hit  - could flash
// TODO Make the object fall at variing speeds and heights

public class GameActivity extends AppCompatActivity {

    //Screen Size
    private int screenWidth;
    private int screenHeight;

    public FirebaseFirestore mFirestore2;

    //objects
    private ImageView mCharSelect;
    private ImageView Rock;
    private ImageView Roll;
    private ImageView FireOb;
    private TextView scoreBoard;
    private TextView prompt;
    private EditText editTextUsername;
    private TextView livesBoard;
    private Button Enter;

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

    private EditText editText;
    private String user;

    // Initialize Class
    private Handler handler = new Handler();
    private Timer timer = new Timer();

    // dummy var / delete later
    private boolean hit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        mCharSelect = (ImageView)findViewById(R.id.char_select);
        Bundle extras = getIntent().getExtras();
        if (extras != null){
            int charSelect = extras.getInt("char_sel");
            Bitmap bitmap = extras.getParcelable("char_icon");
            switch (charSelect){
                case 1: mCharSelect.setImageDrawable(getResources().getDrawable(R.drawable.yoshi));
                        break;
                case 2: mCharSelect.setImageDrawable(getResources().getDrawable(R.drawable.mario));
                        break;
                case 3: mCharSelect.setImageDrawable(IconViewModel.custom);
                        break;
                default: mCharSelect.setImageDrawable(getResources().getDrawable(R.drawable.megaman));
                         break;
            }
        }
        mCharSelect.setX(100000);

        scoreBoard = (TextView)findViewById(R.id.Score_id);
        livesBoard = (TextView)findViewById(R.id.Lives_id);

        Rock = (ImageView)findViewById(R.id.Angry_Rock);
        Roll = (ImageView)findViewById(R.id.Big_boulder);
        FireOb = (ImageView)findViewById(R.id.Fire);

        RockX = (float)Math.floor(Math.random() * (screenWidth - Rock.getWidth()));
        RollX = (float)Math.floor(Math.random() * (screenWidth - Roll.getWidth()));
        FireObX = (float)Math.floor(Math.random() * (screenWidth - FireOb.getWidth()));

        Enter = (Button)findViewById(R.id.Enter);
        Enter.setVisibility(View.INVISIBLE);

        prompt = (TextView)findViewById(R.id.Prompt);
        prompt.setVisibility(View.INVISIBLE);

        editTextUsername = (EditText)findViewById(R.id.editTextUsername);
        editTextUsername.setVisibility(View.INVISIBLE);



        FirebaseApp.initializeApp(this);
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore2 = FirebaseUtil.getFirestore();

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

                mCharSelect.setX(motionEvent.getRawX() - mCharSelect.getWidth() / 2);
                mCharSelect.setY(motionEvent.getRawY() - mCharSelect.getHeight() / 1);
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
                        changePosRock();
                        changePosRoll();
                        changePosFire();
                        time_current = System.currentTimeMillis()/100;
                        time_elapsed = time_current - time_int;
                        scoreBoard.setText(String.format("%d", time_elapsed));
                    }
                });
            }
        }, 0 , 20);
    }

    public void changePosRock() {
        //Speed up as level progress
        speed_increase = 30 - (2000 / (time_elapsed + 1));
        if (speed_increase < 10)
            speed_increase = 10;

        // move rock
        RockY += speed_increase + RockSpeedBonus;
        if (Rock.getY() > screenHeight){
            RockX = (float)Math.floor(Math.random() * (screenWidth - Rock.getWidth()));
            RockSpeedBonus = (float)Math.floor(Math.random() * 10);
            RockY = -Rock.getHeight();
        }
        // move Boulder
        RollY += speed_increase + RollSpeedBonus;
        if (Roll.getY() > screenHeight){
            RollX = (float)Math.floor(Math.random() * (screenWidth - Roll.getWidth()));
            RollSpeedBonus = (float)Math.floor(Math.random() * 10);
            RollY = -Roll.getHeight();
        }

        FireObY += speed_increase + FireObSpeedBonus;
        if (FireOb.getY() > screenHeight){
            FireObX = (float)Math.floor(Math.random() * (screenWidth - FireOb.getWidth()));
            FireObSpeedBonus = (float)Math.floor(Math.random() * 10);
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

    public void changePosRoll() {

    }

    public void changePosFire() {

    }

    private void loseLife(){
        if(!Invincibility_on){
            lives -= 1;
            Invincibility_on = true;
            time_hit = System.currentTimeMillis()/100;
            livesBoard.setText(String.format("%d", lives));
            handler.postDelayed(Invincibility_invisible, 0);
        }

        if(lives <= 1){
            onGameOver();
        }

    }

    public Runnable Invincibility_invisible = new Runnable() {
        @Override
        public void run() {
            player.setVisibility(View.INVISIBLE);
            mCharSelect.setVisibility(View.INVISIBLE);
            time_system = System.currentTimeMillis()/100;
            if(time_system - time_hit > 20){
                handler.removeCallbacks(Invincibility_invisible);
                handler.removeCallbacks(Invincibility_visible);
                player.setVisibility(View.VISIBLE);
                mCharSelect.setVisibility(View.VISIBLE);
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
            mCharSelect.setVisibility(View.VISIBLE);
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

    private void onGameOver(){
        timer.cancel();
        prompt.setVisibility(View.VISIBLE);
        prompt.setText("Game over, Type your name below");
        Enter.setVisibility(View.VISIBLE);
        editTextUsername.setVisibility(View.VISIBLE);


    }

    public void onEditTextUsername(View view){

        editText = (EditText) findViewById(R.id.editTextUsername);
        user = editText.getText().toString();
        if(user.length() != 4){
            prompt.setText("Please enter a 4 character name");
        } else {
            addHighScore(user, (int) time_elapsed);
            recreate();
        }


    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return gestureDetector.onTouchEvent(event);
        }
    };

    public void addHighScore(String user, int score){
        CollectionReference HighScores = mFirestore2.collection("HighScores");

        user = user + "\t\t\t";
        HighScore highScore = new HighScore();
        highScore.setUser(user);
        highScore.setScore(score);
        HighScores.add(highScore);

    }
}