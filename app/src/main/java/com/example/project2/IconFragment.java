package com.example.project2;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project2.model.HighScore;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * fragment that handles the choosing of which character will appear in the game
 */
public class IconFragment extends Fragment {
    public ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private IconViewModel mViewModel;
    public PreviewView mPreviewView;
    public TextView mTextView;
    public TextView mMegamanTextview;
    public ImageView mMegaman;
    public TextView mYoshiTextView;
    public ImageView mYoshi;
    public TextView mMarioTextView;
    public ImageView mMario;
    public TextView mCustomTextView;
    public ImageView mCustom;
    public Button openCamera;
    public TextView buttonDisplay;
    public boolean cameraOpen;
    public boolean cameraInit;
    public Bitmap test;
    public Executor executor = Executors.newSingleThreadExecutor();
    public static IconFragment newInstance() {
        return new IconFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.icon_fragment, container, false);
    }

    /**
     * handles the picture taking
     * Creates a bitmap from the camera preview, this is easier than using the takePicture() function
     * @param view
     */
    public void onCameraClicked(View view){
        //mPreviewView.setVisibility(View.VISIBLE);
        test=mPreviewView.getBitmap();

    }


    /**
     * sets up the preview and attaches it to the surface view on the fragment,
     * allowing the user to see their camera
     * @param cameraProvider
     */
    public void bindPreview(@NonNull ProcessCameraProvider cameraProvider){
        Preview preview = new Preview.Builder()
                .build();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
                .build();

        preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

        Camera camera = cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(IconViewModel.class);
        // TODO: Use the ViewModel
    }


    @Override
    public void onViewCreated(View view,@Nullable Bundle savedInstanceState){
        cameraOpen = false;
        cameraInit = false;
        mCustom = (ImageView) getView().findViewById(R.id.customView);
        if (IconViewModel.custom != null){
            mCustom.setImageDrawable(IconViewModel.custom);
        }
        mPreviewView = (PreviewView) getView().findViewById(R.id.viewFinder);
        mTextView = (TextView) getView().findViewById(R.id.textView);
        mMegamanTextview = (TextView) getView().findViewById(R.id.megamanText);
        mMegamanTextview.setVisibility(View.INVISIBLE);
        mYoshiTextView = (TextView) getView().findViewById(R.id.yoshiText);
        mYoshiTextView.setVisibility(View.INVISIBLE);
        mMarioTextView = (TextView) getView().findViewById(R.id.marioText);
        mMarioTextView.setVisibility(View.INVISIBLE);
        mCustomTextView = (TextView) getView().findViewById(R.id.customText);
        mCustomTextView.setVisibility(View.INVISIBLE);
        characterSelect();

        buttonDisplay = (TextView) getView().findViewById(R.id.cameraButton);
        buttonDisplay.setText("Take Custom Image");

        /**
         * All of the onClick functions for the four characters
         * Each onClick will:
         * hide the text of the unselected characters
         * Play a sound effect for the character
         * sets the variable for character selected for use in the game
         */
        //megaClick
        mMegaman = (ImageView) getView().findViewById(R.id.megamanView);
        mMegaman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                IconViewModel.charSelect=0;
                MediaPlayer mediaPlayer1 = MediaPlayer.create(getContext(),R.raw.megaman);
                mediaPlayer1.start();
                mMegamanTextview.setVisibility(View.VISIBLE);
                mYoshiTextView.setVisibility(View.INVISIBLE);
                mMarioTextView.setVisibility(View.INVISIBLE);
                mCustomTextView.setVisibility(View.INVISIBLE);
            }
        });
        //megaClick
        //yoshiClick
        mYoshi = (ImageView) getView().findViewById(R.id.yoshiView);
        mYoshi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                IconViewModel.charSelect=1;
                MediaPlayer mediaPlayer2 = MediaPlayer.create(getContext(),R.raw.yoshi);
                mediaPlayer2.start();
                mMegamanTextview.setVisibility(View.INVISIBLE);
                mYoshiTextView.setVisibility(View.VISIBLE);
                mMarioTextView.setVisibility(View.INVISIBLE);
                mCustomTextView.setVisibility(View.INVISIBLE);
            }
        });
        //yoshiClick
        //marioClick
        mMario = (ImageView) getView().findViewById(R.id.marioView);
        mMario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                IconViewModel.charSelect=2;
                MediaPlayer mediaPlayer3 = MediaPlayer.create(getContext(),R.raw.mario);
                mediaPlayer3.start();
                mMegamanTextview.setVisibility(View.INVISIBLE);
                mYoshiTextView.setVisibility(View.INVISIBLE);
                mMarioTextView.setVisibility(View.VISIBLE);
                mCustomTextView.setVisibility(View.INVISIBLE);
            }
        });
        //marioClick

        //customClick
        mCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view2) {
                IconViewModel.charSelect=3;
                MediaPlayer mediaPlayer4 = MediaPlayer.create(getContext(),R.raw.mii);
                mediaPlayer4.start();
                mMegamanTextview.setVisibility(View.INVISIBLE);
                mYoshiTextView.setVisibility(View.INVISIBLE);
                mMarioTextView.setVisibility(View.INVISIBLE);
                mCustomTextView.setVisibility(View.VISIBLE);
            }
        });
        //customClick

        /**
         * handles the camera preview through camerax
         * When the user tries to take a picture for a custom character, preview is created and
         * displayed to the screen, while hiding all of the other UI
         * Keeps one button on the screen allowing the user to take a save a picture for their
         * custom character
         */
        //Camera
        openCamera = (Button) getView().findViewById(R.id.cameraButton);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                    if/*camera is not currently open*/(cameraOpen==false){
                        if(cameraInit==false){
                            if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                                cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
                                cameraProviderFuture.addListener(() -> {
                                    try {
                                        ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                                        bindPreview(cameraProvider);
                                    } catch (ExecutionException | InterruptedException e) {

                                    }
                                }, ContextCompat.getMainExecutor(getContext()));
                                cameraOpen = true;
                                cameraInit = true;
                                buttonDisplay.setText("Take Picture");
                            }
                            else /*Permissions need to be requested*/{
                                ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA} ,100);
                            }
                        }
                        else{
                            mPreviewView.setVisibility(View.VISIBLE);
                            cameraOpen = true;
                            buttonDisplay.setText("Take Picture");
                        }
                        mTextView.setVisibility(View.INVISIBLE);
                        mMegaman.setVisibility(View.INVISIBLE);
                        mMegamanTextview.setVisibility(View.INVISIBLE);
                        mYoshi.setVisibility(View.INVISIBLE);
                        mYoshiTextView.setVisibility(View.INVISIBLE);
                        mMario.setVisibility(View.INVISIBLE);
                        mMarioTextView.setVisibility(View.INVISIBLE);
                        mCustom.setVisibility(View.INVISIBLE);
                        mCustomTextView.setVisibility(View.INVISIBLE);
                    }
                    else {
                        onCameraClicked(v);
                        saveImage(test);
                    }
                }



        });
        //camera
    }

    /**
     * sets the variable for character select in the viewmodel
     * Highlights the text for the character selected so that the user knows which character they have selected
     */
    public void characterSelect(){
        int select=IconViewModel.charSelect;
        switch (select){
            case 0: mMegamanTextview.setVisibility(View.VISIBLE);
                    break;
            case 1: mYoshiTextView.setVisibility(View.VISIBLE);
                    break;
            case 2: mMarioTextView.setVisibility(View.VISIBLE);
                    break;
            case 3: mCustomTextView.setVisibility(View.VISIBLE);
                    break;
        }
    }

    /**
     * saves all image objects to the viewmodel
     * also reinitializes all UI that disappears when the camera preview is activated
     * @param bitmap
     */
    public void saveImage(Bitmap bitmap){
        Drawable drawable = new BitmapDrawable(bitmap);
        IconViewModel.custom = drawable;
        IconViewModel.bitmap = bitmap;
        mCustom.setImageDrawable(drawable);
        mPreviewView.setVisibility(View.INVISIBLE);
        buttonDisplay.setText("Take Custom Image");
        cameraOpen=false;
        mTextView.setVisibility(View.VISIBLE);
        mMegaman.setVisibility(View.VISIBLE);
        mYoshi.setVisibility(View.VISIBLE);
        mMario.setVisibility(View.VISIBLE);
        mCustom.setVisibility(View.VISIBLE);
        characterSelect();
    }




}