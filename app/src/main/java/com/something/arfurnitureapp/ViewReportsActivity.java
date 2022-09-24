package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.core.Repo;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import javax.annotation.Nullable;

public class ViewReportsActivity extends AppCompatActivity {

    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;
    int quantity;
    Button Search;
    EditText Email;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reports);

        Intent intent=getIntent();
        String user_id =intent.getStringExtra("user_id");




        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        mfireStoreList=findViewById(R.id.orderList);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");


        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa",userID);

        Query query = firebaseFirestore.collection("users").document(user_id).collection("reports");


        FirestoreRecyclerOptions<ReportModel> options = new FirestoreRecyclerOptions.Builder<ReportModel>().setQuery(query,ReportModel.class).build();




        adapter = new FirestoreRecyclerAdapter<ReportModel, ViewReportsActivity.OrderUsersListHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull ViewReportsActivity.OrderUsersListHolder holder, int position, @NonNull ReportModel model) {

                String user_id=model.getReported_by();
                String review=model.getReport_text();

                DocumentReference documentReference = firebaseFirestore.collection("users").document(user_id);


                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        // Log.d("check profile mann",documentSnapshot.getString("name"));
                        username =documentSnapshot.getString("email");
                        holder.UserId.setText(username);
                    }
                });


                holder.Review.setText(review);

            }



            @NonNull
            @Override
            public ViewReportsActivity.OrderUsersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_review_layout,parent,false);
                return new ViewReportsActivity.OrderUsersListHolder(view);
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
