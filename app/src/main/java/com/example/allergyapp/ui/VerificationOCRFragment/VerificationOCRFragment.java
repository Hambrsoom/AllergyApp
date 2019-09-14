package com.example.allergyapp.ui.VerificationOCRFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.allergyapp.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public class VerificationOCRFragment extends Fragment {

    private VerificationOcrViewModel mViewModel;

    public static VerificationOCRFragment newInstance() {
        return new VerificationOCRFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.verification_ocr_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VerificationOcrViewModel.class);
        // TODO: Use the ViewModel
    }

    // Camera option clicked
    public void onClickCamera() {
        if(!checkCameraPermission()) { // Request camera permission
            requestCameraPermission();
        }
        else { // Permission allowed
            pickCamera();
        }

    }

    private void requestCameraPermission() {
    }

    private void pickCamera() {
    }

    private boolean checkCameraPermission() {
        boolean resultCam  = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean resultStorage  = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resultCam && resultStorage;
    }

    public void onClickGallery() {
        if(!checkStoragePermission()) { // Request storage permission
            requestStoragePermission();
        }
        else {  // Permission allowed
            pickGallery();
        }
    }

    private boolean checkStoragePermission() {
        return false;
    }

    private void requestStoragePermission() {
    }

    private void pickGallery() {
    }

}
