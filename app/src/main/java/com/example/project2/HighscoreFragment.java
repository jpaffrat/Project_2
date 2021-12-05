package com.example.project2;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.lang.Object;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.project2.adapter.FirestoreAdapter;
import com.example.project2.model.HighScore;
import com.example.project2.util.FirebaseUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import static android.content.ContentValues.TAG;

public class HighscoreFragment extends Fragment {

    private HighscoreViewModel mViewModel;
    private TextView mtextView;
    private List<HighScore> highScores = new ArrayList<HighScore>();
    public FirebaseFirestore mFirestore;
    private Query mQuery;
    private String highScoreString = "";
    int hold;




    public static HighscoreFragment newInstance() {
        return new HighscoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.highscore_fragment, container, false);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HighscoreViewModel.class);

    }

    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        mtextView = (TextView) getView().findViewById(R.id.textView);

        FirebaseApp.initializeApp(new MainActivity());
        FirebaseFirestore.setLoggingEnabled(true);
        mFirestore = FirebaseUtil.getFirestore();

        CollectionReference HighScores = mFirestore.collection("HighScores");
        hold = 0;
        highScoreString = "";
        HighScores.orderBy("score", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //Log.d(TAG, document.getId() + " => " + document.getData());

                        highScores.add(document.toObject(HighScore.class));
                        Log.d(TAG, highScores.toString());
                        highScoreString = String.format("%s %s           %d \n", highScoreString, highScores.get(hold).user, highScores.get(hold).score);
                        if(hold == 9) {
                            mtextView.setText(highScoreString);
                            mtextView.bringToFront();
                        }
                        hold++;

                    }
                }
                else {
                    Log.w(TAG, "ERROR:", task.getException());
                }
            }
        });

        System.out.println("hope\n" + highScoreString);
    }

    public void addHighScore(String user, int score){
        CollectionReference HighScores = mFirestore.collection("HighScores");

        HighScore highScore = new HighScore();
        highScore.setUser(user);
        highScore.setScore(score);
        HighScores.add(highScore);

    }





}