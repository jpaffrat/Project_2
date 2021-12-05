package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.View;

import com.example.project2.model.HighScore;
import com.example.project2.util.FirebaseUtil;
import com.example.project2.HighscoreFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;


    public FirebaseFirestore mFirestore2;
    public int test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firestore
        FirebaseApp.initializeApp(this);
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore2 = FirebaseUtil.getFirestore();
        loadFragment(new HighscoreFragment());
        loadFragment(new MainFragment());
    }

    @Override
    protected void onResume(){
        super.onResume();
        loadFragment(new MainFragment());
    }

    public void onIconClicked(View view){
        loadFragment(new IconFragment());
    }
    public void onHighscoresClicked(View view){
        addHighScore("test", 80);
        loadFragment(new HighscoreFragment());
    }
    public void onMainClicked(View view){
        loadFragment(new MainFragment());
    }
    public void loadFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout,fragment)
                .commit();
    }

    public void addHighScore(String user, int score){
        CollectionReference HighScores = mFirestore2.collection("HighScores");

        user = user + "\t\t\t";
        HighScore highScore = new HighScore();
        highScore.setUser(user);
        highScore.setScore(score);
        HighScores.add(highScore);

    }

}

