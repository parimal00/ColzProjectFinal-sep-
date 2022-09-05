package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

public class DigitalSignature extends AppCompatActivity {

    Button SaveSignature;
    ImageView ImageView;
    FirebaseFirestore firebaseFirestore;
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
        String updatedQuantity=intent.getStringExtra("updatedQuantity");

        SaveSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap signature = signaturePad.getSignatureBitmap();
                ImageView.setImageBitmap(signature);

                firebaseFirestore.collection("products").document(product_dec_ref)
                        .update("quantity",String.valueOf(updatedQuantity));

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


                firebaseFirestore.collection("users").document(buying_user_id).collection("itemsBought").document(documentRef).update("item_bought",true)
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

            }
        });
    }
}
