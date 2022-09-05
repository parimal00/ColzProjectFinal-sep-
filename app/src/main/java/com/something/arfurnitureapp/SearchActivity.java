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
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    FirebaseFirestore firebaseFirestore;

    FirestoreRecyclerAdapter adapter;

    StorageReference mStorageRef;

    Bitmap bitmap;
    ImageView ProductImage;


    ImageView ImageView2;

    String imageName2;

    String currentUserID;
    String currentUserName;
    String currentUserAddress;
    String currentUserPhoneNo;
    String currentUserUsername;

    Button Search;
    EditText Search_field;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

       String email=getIntent().getStringExtra("email");
       Log.d("test_email",email);


        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");
        mfireStoreList=findViewById(R.id.allUsers_id);
        firebaseFirestore = FirebaseFirestore.getInstance();



        currentUserID = firebaseAuth.getCurrentUser().getUid();

        Query query = firebaseFirestore.collection("users").whereEqualTo("email", email);

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();
        Log.d("test2","hello there");

        adapter = new FirestoreRecyclerAdapter  <UserModel, SearchActivity.UserViewHolder>(options) {




            @NonNull
            @Override
            public SearchActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_res,parent,false);
                return new SearchActivity.UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull SearchActivity.UserViewHolder holder, int position, @NonNull UserModel model) {
                holder.Name.setText(model.getName());

                Map<Object,Boolean> userMap = new HashMap<>();
                userMap.put("disbled",false);
                holder.DisableUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("hello_user_id",model.getUser_id());
                        firebaseFirestore.collection("users")
                                .document(model.getUser_id())
                                .update("disable",true)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hello there","success");
                                    }
                                });


                    }
                });




                //  Log.d("sexy",model.getName());




            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);
    }
    private  class UserViewHolder extends  RecyclerView.ViewHolder {

        public TextView Name;
        EditText OrderedQuantity;
        public TextView Price,Quantity;
        public ImageView ProductImage;
        Button ShowAR;
        Button BuyBtn;
        Button Delete,DisableUser;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            Name = itemView.findViewById(R.id.name_id);
            DisableUser=itemView.findViewById(R.id.disableUser_id);






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
