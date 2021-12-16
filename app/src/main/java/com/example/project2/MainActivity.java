package com.example.project2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;

import com.example.project2.model.HighScore;
import com.example.project2.util.FirebaseUtil;
import com.example.project2.HighscoreFragment;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;

/**
 * The main activity for the app
 */

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onResume(){
        super.onResume();
        loadFragment(new MainFragment());
    }

    /**
     * handles the fragment navigation
     * all fragments are used through this activity in a fragment holder
     * loads a new fragment into the fragment holder when the user selects this
     * @param view
     */
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


    @Override
    protected void onStop(){
        super.onStop();


    }

}

