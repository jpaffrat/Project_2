package com.example.project2;

import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.view.PreviewView;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class IconFragment extends Fragment {
    public ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private IconViewModel mViewModel;
    public PreviewView mPreviewView;
    public Button openCamera;
    public Executor executor = Executors.newSingleThreadExecutor();
    public static IconFragment newInstance() {
        return new IconFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {






        return inflater.inflate(R.layout.icon_fragment, container, false);
    }

    /*public void onCameraClicked(View view){

    }*/
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
        mPreviewView = (PreviewView) getView().findViewById(R.id.viewFinder);
        openCamera = (Button) getView().findViewById(R.id.cameraButton);
        openCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(ActivityCompat.checkSelfPermission(getContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
                    cameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
                    cameraProviderFuture.addListener(() -> {
                        try {
                            ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                            bindPreview(cameraProvider);
                        }catch (ExecutionException | InterruptedException e){

                        }
                    },ContextCompat.getMainExecutor(getContext()));
                }
                else{
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA} ,100);
                }

            }
        });
    }

}