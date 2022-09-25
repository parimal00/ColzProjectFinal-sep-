package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;

public class ViewReviewsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;
    int quantity;
    Button Search;
    EditText Email;
    TextView Error;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reviews);

        Intent intent=getIntent();
       String product_id =intent.getStringExtra("product_id");
        Log.d("product_id_test",product_id);



        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mfireStoreList=findViewById(R.id.orderList);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");
        Error=findViewById(R.id.error_id);

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa",userID);

        Query query = firebaseFirestore.collection("products").document(product_id).collection("reviews");

        firebaseFirestore.collection("products").document(product_id).collection("reviews")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count=count+1;

                            }
                            if(count==0){
                                Error.setText("this item has no reviews");
                            }

                        } else {

                        }
                    }
                });


        FirestoreRecyclerOptions<ReviewModel> options = new FirestoreRecyclerOptions.Builder<ReviewModel>().setQuery(query,ReviewModel.class).build();




        adapter = new FirestoreRecyclerAdapter<ReviewModel, ViewReviewsActivity.OrderUsersListHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull ViewReviewsActivity.OrderUsersListHolder holder, int position, @NonNull ReviewModel model) {

                String user_id=model.getUser_id();
                String review=model.getReview();

                holder.Review.setText(review);
                holder.UserId.setText(user_id);
            }



            @NonNull
            @Override
            public ViewReviewsActivity.OrderUsersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review_layout,parent,false);
                return new ViewReviewsActivity.OrderUsersListHolder(view);
            }
        };


        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);

}
private class OrderUsersListHolder extends  RecyclerView.ViewHolder {


    TextView UserId,Review;


    public OrderUsersListHolder(@NonNull View itemView) {
        super(itemView);

        UserId=itemView.findViewById(R.id.username_id);
        Review=itemView.findViewById(R.id.review_id);







    }
}
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

}
