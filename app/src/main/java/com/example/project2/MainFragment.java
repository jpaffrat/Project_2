package com.example.project2;

import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.concurrent.ExecutionException;

/**
 * fragment for the main screen to start the game and select difficulty level
 */

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    //private int char_sel = 1;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * handles the on click for starting the game
     * each onClick will start the game activity
     * passes by intent the character selected to play the game and the difficulty level
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){

        Button button_easy = (Button) getView().findViewById(R.id.Start_game_easy);
        Button button_medium = (Button) getView().findViewById(R.id.Start_game_medium);
        Button button_hard = (Button) getView().findViewById(R.id.Start_game_hard);

        button_easy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GameActivity.class);
                intent.putExtra("char_sel", IconViewModel.charSelect);
                intent.putExtra("difficulty", 1);
                startActivity(intent);
            }
        });
        button_medium.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GameActivity.class);
                intent.putExtra("char_sel", IconViewModel.charSelect);
                intent.putExtra("difficulty", 2);
                startActivity(intent);
            }
        });
        button_hard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),GameActivity.class);
                intent.putExtra("char_sel", IconViewModel.charSelect);
                intent.putExtra("difficulty", 2);
                startActivity(intent);
            }
        });

    }

}