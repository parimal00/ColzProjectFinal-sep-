package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.time.Clock;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class SendReviewActivity extends AppCompatActivity {

    EditText ReviewText;
    Button SendReview;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_review);


        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        ReviewText=findViewById(R.id.review_text_id);
        SendReview=findViewById(R.id.send_review_id);
        String userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = fStore.collection("users").document(userID);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                // Log.d("check profile mann",documentSnapshot.getString("name"));
                username =documentSnapshot.getString("username");


            }
        });

        Intent intent=getIntent();
        String product_id=intent.getStringExtra("product_id");


            SendReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reviewText=ReviewText.getText().toString();
                    if(reviewText.length()>0){
                        ProgressDialog pd = new ProgressDialog(view.getContext());
                        pd.setTitle("sending ...");
                        pd.show();


                        String reviewRef = UUID.randomUUID().toString();

                      DocumentReference documentReference=fStore.collection("products").document(product_id).collection("reviews").document(reviewRef);

                        Map<Object,String> review = new HashMap<>();
                        review.put("review",reviewText);
                        review.put("user_id",username);





                        documentReference.set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("succceess!!! ","User addedd waaaaaaaaaa");
                            }
                        });
                        startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
                        pd.dismiss();

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"review cannot be empty",Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
