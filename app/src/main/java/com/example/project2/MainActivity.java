package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;


import android.os.Bundle;
import android.view.View;

import com.example.project2.util.FirebaseUtil;
import com.example.project2.HighscoreFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;


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

    @Override
    protected void onResume(){
        super.onResume();
        loadFragment(new MainFragment());
    }

    public void onIconClicked(View view){
        loadFragment(new IconFragment());
    }
    public void onHighscoresClicked(View view){
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
}

