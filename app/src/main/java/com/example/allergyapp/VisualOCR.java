package com.example.allergyapp;

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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class VisualOCR extends AppCompatActivity {

    ImageView mPreviewIv;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String UID = user.getUid();
    private Button cameraBtn;
    private Button galleryBtn;
    private Button verifyAllergiesBtn;
    private Uri image_uri;
    private TextView resultTv;
    private TextView txtViewTop;
    private TextView verdictTv;

    private static final int CAMERA_REQUEST_CODE = 200;
    private static final int STORAGE_REQUEST_CODE = 400;
    private static final int IMAGE_PICK_GALLERY_CODE = 1000;
    private static final int IMAGE_PICK_CAMERA_CODE = 1001;

    String cameraPermission[];
    String storagePermission[];

    StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        setContentView(R.layout.activity_visual_ocr);
        cameraBtn = findViewById(R.id.cameraButton);
        galleryBtn = findViewById(R.id.galleryButton);
        txtViewTop = findViewById(R.id.textView);
        verdictTv = findViewById(R.id.verdictTv);
        verifyAllergiesBtn = findViewById(R.id.viewAllergiesBtn);
        verifyAllergiesBtn.setVisibility(View.GONE);
        mPreviewIv = findViewById(R.id.imageIv);
        resultTv = findViewById(R.id.resultTv);
        resultTv.setVisibility(View.GONE);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }


    // Camera option clicked
    public void onCameraBtnClicked(View view){
        if(!checkCameraPermission()) { // Request camera permission
            requestCameraPermission();
        }
        else
            pickCamera();
    }

    // Gallery option clicked
    public void onGalleryBtnClicked(View view){
        if(!checkStoragePermission()) { // Request storage permission
            requestStoragePermission();
        }
        else
            pickGallery();
    }

    public void onVerifyAllergiesBtnClicked(View view){
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mPreviewIv.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!recognizer.isOperational()){

        }
        else { // Display text from image
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = recognizer.detect(frame);
            sb = new StringBuilder();

            for (int i = 0; i < items.size(); i ++){
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }
        }

        (FirebaseDatabase.getInstance().getReference()).child("users").child(UID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot == null || snapshot.getValue() == null)
                    return;
                String stringOfAllergies = snapshot.getValue().toString();
                List<String> allergies = convertStringOfAllergiesIntoArray(stringOfAllergies);
                String allergy = compareWithIngredients(allergies);
                if(allergy.isEmpty()) {
                    getVerdictSynonyms(allergies);
                    return;
                }

                // Can conclude it is not safe
                verdictTv.setText("Oh helllll nah, put it back! This contains " + allergy.toLowerCase() + ".");
                verdictTv.setVisibility(View.VISIBLE);
                verifyAllergiesBtn.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public String compareWithIngredients(List<String> allergies) {
        String allergy = "";
        for(String a: allergies) {
            if(sb.toString().contains(a.toLowerCase()) || sb.toString().contains(a) || sb.toString().contains(a.toUpperCase()))
                allergy = a;
        }

        return allergy;
    }

    private void getVerdictSynonyms(List<String> allergies) {

        for(String a: allergies) {
            (FirebaseDatabase.getInstance().getReference()).child("synonym").child(a.toLowerCase()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    if(snapshot == null || snapshot.getValue() == null)
                        return;
                    String stringOfSynonyms = snapshot.getValue().toString();
                    List<String> synonyms = convertStringOfAllergiesIntoArray(stringOfSynonyms);

                    String allergy = "";
                    for(String s: synonyms) {
                        if(sb.toString().contains(s.toLowerCase()) || sb.toString().contains(s) || sb.toString().contains(s.toUpperCase()))
                            allergy = s;
                    }

                    if(allergy.isEmpty()) {
                        verdictTv.setText("No worries, you're good to go!");
                    }
                    else {
                        verdictTv.setText("Oh helllll nah, put it back! This contains "  + allergy.toLowerCase() + ".");
                    }

                    mPreviewIv.setVisibility(View.GONE);
                    verdictTv.setVisibility(View.VISIBLE);
                    verifyAllergiesBtn.setVisibility(View.GONE);

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }

    }

    private List<String> convertStringOfAllergiesIntoArray(String stringOfAllergies) {
        String[] allergiesArr = stringOfAllergies.split(",");
        List<String> allergies = new ArrayList<>(Arrays.asList(allergiesArr));

        return allergies;
    }

    private boolean checkCameraPermission() {
        boolean resultCam  = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        boolean resultStorage  = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resultCam && resultStorage;
    }

    private boolean checkStoragePermission() {
        boolean resultStorage  = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        return resultStorage;
    }
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }
    private void pickCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "NewPic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Image to text");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);
    }

    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }
    private void pickGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_GALLERY_CODE);
    }


    // handle permission result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case CAMERA_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted){
                        pickCamera();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted){
                        pickGallery();
                    }
                    else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }


    }

    //handleImageResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //got the image from camera

        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE){

                //got image from gallery now crop it
                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON).start(this); // enable image guidelines
            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                // got image from camera now crop it
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON).start(this); // enable image guidelines
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK){
                Uri resultUri = result.getUri();
                mPreviewIv.setImageURI(resultUri);

                verifyAllergiesBtn.setVisibility(View.VISIBLE);
                txtViewTop.setVisibility(View.GONE);
                cameraBtn.setVisibility(View.GONE);
                galleryBtn.setVisibility(View.GONE);

            }
            else if(resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}
