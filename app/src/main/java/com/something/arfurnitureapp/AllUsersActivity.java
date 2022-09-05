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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AllUsersActivity extends AppCompatActivity {


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
    String id;
    ArrayList<String> ids=new ArrayList<String>();//Creating arraylist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");
        mfireStoreList=findViewById(R.id.allUsers_id);
        firebaseFirestore = FirebaseFirestore.getInstance();

        Search_field=findViewById(R.id.search_field_id);
        Search= findViewById(R.id.search_id);
        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email=Search_field.getText().toString().replace(" ", "");
                Intent intent = new Intent(AllUsersActivity.this,SearchActivity.class);
                intent.putExtra("email",email);
                startActivity(intent);
            }
        });

        currentUserID = firebaseAuth.getCurrentUser().getUid();

        Query query = firebaseFirestore.collection("users");

        FirestoreRecyclerOptions<UserModel> options = new FirestoreRecyclerOptions.Builder<UserModel>().setQuery(query,UserModel.class).build();
        Log.d("test2","hello there");

        adapter = new FirestoreRecyclerAdapter<UserModel, AllUsersActivity.UserViewHolder>(options) {




            @NonNull
            @Override
            public AllUsersActivity.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_users_res,parent,false);
                return new AllUsersActivity.UserViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllUsersActivity.UserViewHolder holder, int position, @NonNull UserModel model) {
                holder.Name.setText(model.getName());

                Map<Object,Boolean> userMap = new HashMap<>();
                userMap.put("disbled",false);
                holder.DisableUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("hello_user_id",model.getUser_id());

                    firebaseFirestore.collection("products")
                            .whereEqualTo("userID",model.getUser_id())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            id=document.getId();
                                            ids.add(id);
                                            Log.d("waaaaaa",""+ids.size());

                                            firebaseFirestore.collection("products")
                                                    .document(id)
                                                    .update("disable",true)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void aVoid) {
                                                            Log.d("hello there","success");
                                                        }
                                                    });

                                        }

                                        Log.d("check_me",""+ids.size());
                                    } else {
                                        Log.d("TAG", "Error getting documents: ", task.getException());
                                    }
                                }
                            });


                    Log.d("array_size",""+ids.size());
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
