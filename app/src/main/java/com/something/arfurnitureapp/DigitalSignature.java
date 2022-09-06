package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DigitalSignature extends AppCompatActivity {

    Button SaveSignature;
    ImageView ImageView;
    FirebaseFirestore firebaseFirestore;
    String uuID,signature_path;

    FirebaseAuth firebaseAuth;
     StorageReference mStorageRef;

String updatedQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_signature);

        firebaseFirestore = FirebaseFirestore.getInstance();
        SaveSignature = findViewById(R.id.save_signature_id);
        ImageView=findViewById(R.id.display_signature_id);
        SignaturePad signaturePad=findViewById(R.id.signature_id);

        Intent intent=getIntent();
        String product_dec_ref=intent.getStringExtra("product_doc_ref");
        String  documentRef=intent.getStringExtra("documentRef");
        String buying_user_id=intent.getStringExtra("buying_user_id");
        String userID=intent.getStringExtra("userID");
         updatedQuantity=intent.getStringExtra("updatedQuantity");
        uuID = UUID.randomUUID().toString();
        signature_path = UUID.randomUUID().toString();

        Log.d("signature_updated_quantity",updatedQuantity);
        SaveSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ProgressDialog pd = new ProgressDialog(view.getContext());
                pd.setTitle("uploading...");
                pd.show();
//
                Bitmap signature = signaturePad.getSignatureBitmap();
               // ImageView.setImageBitmap(signature);

                uploadImage(signature,signature_path);

                Log.d("signature_updated_quantity_hey","waaa"+updatedQuantity);

                firebaseFirestore.collection("products").document(product_dec_ref)
                        .update("quantity",updatedQuantity);

                firebaseFirestore.collection("users").document(userID).collection("buyingUsers").document(documentRef).update("item_delivered",true)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("sexy","waaaaaaaa");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sexy","bitch");
                    }
                });

                //update signature path to buyingUser collection

                firebaseFirestore.collection("users").document(userID).collection("buyingUsers").document(documentRef).update("signature_path",signature_path)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("sexy","waaaaaaaa");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sexy","bitch");
                    }
                });

                firebaseFirestore.collection("users").document(buying_user_id).collection("itemsBought").document(documentRef).update("item_bought",true)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("sexy","waaaaaaaa");
                                pd.setTitle("success");
                                startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("sexy","bitch");
                    }
                });

            }
        });

    }
    private void uploadImage(Bitmap signature,String uuID) {
//
//        ProgressDialog pd = new ProgressDialog(getApplicationContext());
//        pd.setTitle("uploading...");
//        pd.show();
//
//


        Bitmap bmp = signature;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        mStorageRef = FirebaseStorage.getInstance().getReference();
        UploadTask uploadTask = mStorageRef.child(uuID).putBytes(data);


       // String userID = firebaseAuth.getCurrentUser().getUid();

//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
//                // ...
//            }
//        });

//        riversRef.putFile(filepath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                        Log.d("success Bitch", "suuuuccccccccccccesss");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
    }
}
