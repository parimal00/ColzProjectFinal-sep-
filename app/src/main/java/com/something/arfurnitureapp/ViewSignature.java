package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewSignature extends AppCompatActivity {
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_signature);

        ImageView SignatureView=findViewById(R.id.signature_image_view_id);

        Intent intent=getIntent();
       String signature_path= intent.getStringExtra("signature_path");


        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        try {
            final File localFile = File.createTempFile("images", "jpg");
            mStorageRef.child(signature_path).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                  Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
                    SignatureView.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);


                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
