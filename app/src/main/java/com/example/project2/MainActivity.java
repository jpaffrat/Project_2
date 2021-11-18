package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.project2.util.FirebaseUtil;
import com.example.project2.HighscoreFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    public FirebaseFirestore mFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firestore
        FirebaseApp.initializeApp(this);
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();


       // new HighscoreFragment().addHighScore("hello",1 , mFirestore);
    }
}