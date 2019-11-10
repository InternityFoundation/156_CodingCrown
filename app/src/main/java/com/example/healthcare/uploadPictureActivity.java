package com.example.healthcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;


public class uploadPictureActivity extends AppCompatActivity {
    //ImageView imageDisplay;
    Button btnCaptureImage;
    //Uri filePath;
    //Button btnUploadImage;
    StorageReference mStorageRef;
    private static final int CAMERA_REQUEST_CODE = 1;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);

        btnCaptureImage = (Button) findViewById(R.id.button12);
        mProgress = new ProgressDialog(this);
        //imageDisplay = (ImageView) findViewById(R.id.imageView2);
        //btnUploadImage = (Button) findViewById(R.id.button4);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });

        /*btnUploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fileuploader();
            }
        });*/
    }
    /*
    private String getExtension(Uri uri) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void Fileuploader(){
        StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(filePath));

        Ref.putFile(filePath)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Toast.makeText(uploadPictureActivity.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }*/

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
                    super.onActivityResult(requestCode, resultCode, data);
                    /*filePath = data.getData();
                    Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                    imageDisplay.setImageBitmap(bitmap);*/

                    if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){

                        mProgress.setMessage("Uploading Image ...");
                        mProgress.show();

                        Uri uri = data.getData();
                        StorageReference filePath = mStorageRef.child("Photos").child(uri.getLastPathSegment());
                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                mProgress.dismiss();
                                Toast.makeText(uploadPictureActivity.this, "Uploading Finished ...", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }


    }
}
