package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ServerValue;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    int count;
    ProgressDialog pd;

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
                username =documentSnapshot.getString("email");


            }
        });

        Intent intent=getIntent();
        String product_id=intent.getStringExtra("product_id");


            SendReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String reviewText = ReviewText.getText().toString();
                    if (reviewText.length() > 0) {
                        pd = new ProgressDialog(view.getContext());
                        pd.setTitle("sending ...");
                        pd.show();
                        prepareReview(reviewText,product_id);
                    }
                    else{
                            Toast.makeText(getApplicationContext(), "review cannot be empty", Toast.LENGTH_SHORT).show();
                        }
                    }

            });

    }
     public void prepareReview(String reviewText,String product_id){
            count=0;
         final Boolean[] found = {false};
          fStore.collection("products").document(product_id).collection("reviews").whereEqualTo("user_id",username)
         .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
             @Override
             public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isSuccessful()) {
                     for (QueryDocumentSnapshot document : task.getResult()) {
                         count=count+1;

                     }

            if(count==0){
                sendReview(reviewText,product_id);
                Log.d("count_testss",""+count);
            }else {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"you have already sent the review",Toast.LENGTH_SHORT).show();
            }
                 } else {
                     Log.d("TAG", "Error getting documents: ", task.getException());
                 }
             }
         });


     }

     public void sendReview(String reviewText,String product_id){


         String reviewRef = UUID.randomUUID().toString();

         DocumentReference documentReference = fStore.collection("products").document(product_id).collection("reviews").document(reviewRef);

         Map<Object, String> review = new HashMap<>();
         review.put("review", reviewText);
         review.put("user_id", username);


         documentReference.set(review).addOnSuccessListener(new OnSuccessListener<Void>() {
             @Override
             public void onSuccess(Void aVoid) {
                 Log.d("succceess!!! ", "User addedd waaaaaaaaaa");
             }
         });
         Toast.makeText(getApplicationContext(),"review sent successfully",Toast.LENGTH_SHORT).show();
         startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
         pd.dismiss();
     }
}
