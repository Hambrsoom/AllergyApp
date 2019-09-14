package com.example.allergyapp.ui.VerificationOCRFragment;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Button;

import com.example.allergyapp.R;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import static android.app.Activity.RESULT_OK;

public class VerificationOCRFragment extends Fragment {

    private Button cameraBtn;
    private Button galleryBtn;
    private Button verifyAllergiesBtn;
    private ImageView mPreviewIv;
    private Uri image_uri;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    // camera permission
    String[] cameraPermission;

    // storage permission
    String[] storagePermission;


    private VerificationOcrViewModel mViewModel;

    public static VerificationOCRFragment newInstance() {
        return new VerificationOCRFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.verification_ocr_fragment, container, false);
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cameraBtn = rootView.findViewById(R.id.cameraButton);
        cameraBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onCameraBtnClicked();
            }
        });

        galleryBtn = rootView.findViewById(R.id.galleryButton);
        galleryBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onGalleryBtnClicked();
            }
        });

        verifyAllergiesBtn = rootView.findViewById(R.id.viewAllergiesBtn);
        verifyAllergiesBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                onVerifyAllergiesBtnClicked();
            }
        });

        mPreviewIv = rootView.findViewById(R.id.imageIv);

        return rootView;
    }

    // Camera option clicked
    public void onCameraBtnClicked(){
        if(!checkCameraPermission()) { // Request camera permission
            requestCameraPermission();
        }
        else
            pickCamera();
    }

    // Gallery option clicked
    public void onGalleryBtnClicked(){
        if(!checkStoragePermission()) { // Request storage permission
            requestStoragePermission();
        }
        else
            pickGallery();
    }

    public void onVerifyAllergiesBtnClicked(){

    }

    private boolean checkCameraPermission() {
        boolean resultCam  = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean resultStorage  = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resultCam && resultStorage;
    }

    private boolean checkStoragePermission() {
        boolean resultStorage  = ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resultStorage;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(getActivity(), cameraPermission, CAMERA_REQUEST_CODE);
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(getActivity(), storagePermission, STORAGE_REQUEST_CODE);
    }

    private void pickCamera() {
        // Take a picture with the camera and store it to storage
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Description");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        getActivity().startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void pickGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                // got image from gallery, now crop it
                CropImage.activity(data.getData()).setGuidelines(CropImageView.Guidelines.ON).start(getActivity());

            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                // got image from camera, now crop it
                CropImage.activity(image_uri).setGuidelines(CropImageView.Guidelines.ON).start(getActivity());
            }
        }

        // get cropped image
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                // set image to image view
                mPreviewIv.setImageURI(resultUri);

                // get drawable bitmap for text recognition
                BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();

                TextRecognizer recognizer = new TextRecognizer.Builder(getActivity().getApplicationContext()).build();

                if (!recognizer.isOperational()) {
                    Toast.makeText(getActivity().getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                } else {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<TextBlock> items = recognizer.detect(frame);
                    StringBuilder sb = new StringBuilder();

                    // Get text from sb until there is no text
                    for (int i = 0; i < items.size(); i++) {
                        TextBlock myItem = items.valueAt(i);
                        sb.append(myItem.getValue());
                        sb.append("\n");
                    }
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                // if there is any error
                Exception error = result.getError();
                Toast.makeText(getActivity().getApplicationContext(), "" + error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(VerificationOcrViewModel.class);
        // TODO: Use the ViewModel
    }
}
