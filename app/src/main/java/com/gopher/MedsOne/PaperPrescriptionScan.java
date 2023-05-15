package com.gopher.MedsOne;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PaperPrescriptionScan extends AppCompatActivity {


    private MaterialButton inputImageBtn;
    private MaterialButton recognizeTextBtn;
    private ShapeableImageView showImgView;
    private TextView recognizedTextView;
    private TextView medicineTextView;

    private static final String TAG = "MAIN_TAG";
    private Uri imageUri = null;

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 101;

    private String[] cameraPermission;
    private String[] storagePermission;

    private ProgressDialog progressDialog;

    private TextRecognizer textRecognizer;

    // Extract Text
    private String extractText;
    private String[] words;
    List<String> desiredWords = Arrays.asList("paracetamol", "anti-biotic", "expectorant", "vitamin", "imipramine");
    List<String> extractedWords = new ArrayList<>();
    String wordsAsString;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paper_prescription_scan);

        inputImageBtn = findViewById(R.id.inputImageBtn);
        recognizeTextBtn = findViewById(R.id.recognizeTextBtn);
        showImgView = findViewById(R.id.showImgView);
        recognizedTextView = findViewById(R.id.recognizedTextView);
        medicineTextView = findViewById(R.id.medicineTextView);

        // Init array of permission for camera, gallery
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE};

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCanceledOnTouchOutside(false);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        inputImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInputImageDialog();

            }
        });

        recognizeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri == null) {
                    Toast.makeText(PaperPrescriptionScan.this, "Pick Image First!", Toast.LENGTH_SHORT).show();
                }
                else {
                    recognizedTextFromImage();
                }
            }
        });
    }

    private void recognizedTextFromImage() {
        Log.d(TAG, "recognizedTextFromImage:");

        progressDialog.setMessage("Preparing Image ...");
        progressDialog.show();

        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            progressDialog.setMessage("Recognizing Text ...");

            Task<Text> textTaskResult = textRecognizer.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<Text>() {
                        @Override
                        public void onSuccess(Text text) {
                            progressDialog.dismiss();

                            String recognizedText = text.getText();

                            Log.d(TAG, "onSuccess: recognizedText:" + recognizedText);

                            // Extract Text part
                            extractText = recognizedText;
                            words = extractText.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
                            wordsAsString = Arrays.toString(words);

                            for (String word : words) {
                                if (desiredWords.contains(word.toLowerCase())) {
                                    extractedWords.add(word);
                                }
//                                else {
//                                    Toast.makeText(MainActivity.this, "recognizedText: "+recognizedText.toString(), Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(MainActivity.this, "No words added", Toast.LENGTH_SHORT).show();
//                                }
                            }
                            Toast.makeText(PaperPrescriptionScan.this, "Extracted Words: "+extractedWords.toString(), Toast.LENGTH_SHORT).show();
                            // Extract Text part end

                            recognizedTextView.setText(recognizedText);
                            medicineTextView.setText(extractedWords.toString());
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Log.d(TAG, "onFailure: ", e);
                            Toast.makeText(PaperPrescriptionScan.this, "Failed recognizing text due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } catch (Exception e) {
            progressDialog.dismiss();

            Log.d(TAG, "onFailure: recognizedTextFromImage ", e);
            Toast.makeText(this, "Failed preparing image due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    // Show Input Image when click on Take Image
    private void showInputImageDialog() {
        PopupMenu popupMenu = new PopupMenu(this, inputImageBtn);

        popupMenu.getMenu().add(Menu.NONE, 1, 1, "CAMERA");
        popupMenu.getMenu().add(Menu.NONE, 2, 2, "GALLERY");
        popupMenu.show();
        System.out.println("showInputImageDialog");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id = menuItem.getItemId();
                if(id == 1) {
                    Log.d(TAG, "onMenuItemClick: Camera Clicked...");
                    if (checkCameraPermission()) { pickImageCamera();}
                    else { requestCameraPermission();}
                }
                else if (id == 2) {
                    Log.d(TAG, "onMenuItemClick: Gallery Clicked...");

                    if(checkStoragePermission()) {pickImageGallery();}
                    else {requestStoragePermission();}
                }
                return true;
            }
        });
    }

    //    Gallery
    private void pickImageGallery() {
        Log.d(TAG, "pickImageGallery");

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");

        galleryActivityResultLauncher.launch(galleryIntent);
    }

    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK) {
                        imageUri = result.getData().getData();

                        Log.d(TAG, "onActivityResult: gallery imageUri" + imageUri);

                        showImgView.setImageURI(imageUri);
                    }
                    else {
                        Log.d(TAG, "onActivityResult: gallery cancelled");
                        Toast.makeText(PaperPrescriptionScan.this, "Image not found from Gallery!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    //

    // Camera
    private void pickImageCamera() {
        Log.d(TAG, "pickImageCamera:");
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Sample Title");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Description");

        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        cameraActivityResultLauncher.launch(cameraIntent);
    }

    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {

                        Log.d(TAG, "onActivityResult: camera imageUri"+ imageUri);

                        showImgView.setImageURI(imageUri);
                    }
                    else {
                        Log.d(TAG, "onActivityResult: camera cancelled");
                        Toast.makeText(PaperPrescriptionScan.this, "Image not found from Camera!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    //

    // Storage Permission
    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission
                (this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermission, STORAGE_REQUEST_CODE);
    }
    //

    // Camera Permission
    private boolean checkCameraPermission() {
        boolean cameraResult = ContextCompat.checkSelfPermission
                (this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        //boolean storageResult = ContextCompat.checkSelfPermission
//                (this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return cameraResult;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermission, CAMERA_REQUEST_CODE);
    }
    //

    // Handle Permission Results
    public void onRequestPermissionResult(int requestCode, @NonNull String[] permission, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permission, grantResults);

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted) {
                        pickImageCamera();
                    }
                    else {
                        Toast.makeText(PaperPrescriptionScan.this, "Camera and Storage Permissions are required!",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PaperPrescriptionScan.this, "Camera Request Cancelled",Toast.LENGTH_SHORT).show();
                }
            }
            break;
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted) {
                        pickImageGallery();
                    }
                    else {
                        Toast.makeText(PaperPrescriptionScan.this, "Gallery Permissions are required!",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(PaperPrescriptionScan.this, "Gallery Request Cancelled!",Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }
}