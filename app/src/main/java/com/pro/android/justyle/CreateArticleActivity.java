package com.pro.android.justyle;


import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import static com.pro.android.justyle.CameraActivity.newImageUri;
import static com.pro.android.justyle.FrontPageActivity.isWifiConn;
import static com.pro.android.justyle.WardrobeFragment.mImageUri;


public class CreateArticleActivity extends AppCompatActivity {

    private Button mCreateButton;

    private EditText mArticleText;
    private EditText mArticleName;
    private ImageView mArticleImg;
    private ProgressBar mProgressBar;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask mUploadTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_article);


        mArticleText = findViewById(R.id.ArticleTextId);
        mArticleName = findViewById(R.id.ArticleNameId);
        mArticleImg = findViewById(R.id.articleImageId);

        mArticleImg.setImageURI(mImageUri);


        mCreateButton = findViewById(R.id.buttonCreateArticleId);
        mProgressBar = findViewById(R.id.progressBarId);


        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");


        mCreateButton.setOnClickListener(   new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isWifiConn == true) {// if connected to wifi

                    if (mUploadTask != null && mUploadTask.isInProgress()) {
                        // if there is already an upload task on progress we don't want to upload same pictures
                        Toast.makeText(CreateArticleActivity.this, "Upload on progress", Toast.LENGTH_SHORT).show();
                    } else {
                        uploadFile();

                    }

                }
                else {
                    Toast.makeText(CreateArticleActivity.this, "You should be connected to wifi", Toast.LENGTH_SHORT).show();

                }
            }

        });
    }

    private String getFileExtension(Uri uri) {
        //to get file extension from the image

        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 100);

                            Toast.makeText(CreateArticleActivity.this, "Upload successful", Toast.LENGTH_LONG).show();



                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful());
                            Uri downloadUrl = urlTask.getResult();


                            Upload upload = new Upload(mArticleName.getText().toString().trim(), downloadUrl.toString(),mArticleText.getText().toString().trim());


                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            openMyWardrobeActivity(); // start new activity

                        }


                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateArticleActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int) progress);
                        }
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }
   private void openMyWardrobeActivity(){
        Intent intent = new Intent(this, MyWardrobeActivity.class);
        startActivity(intent);
   }


}




