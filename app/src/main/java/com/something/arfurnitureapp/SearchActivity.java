package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
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
    String id;
    ArrayList<String> ids=new ArrayList<String>();//Creating arraylist

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
                holder.Email.setText(""+model.getEmail());
                holder.Address.setText(""+model.getAddress());
                if(model.getDisable().equals("true")){
                    holder.Status.setText("disabled");
                    holder.Status.setTextColor(Color.RED);
                }else{
                    holder.Status.setText("enabled");
                    holder.Status.setTextColor(Color.GREEN);
                }
                holder.Contact_no.setText(""+model.getPhone_no());


                holder.EnableUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                                                firebaseFirestore.collection("products")
                                                        .document(id)
                                                        .update("disable","false")
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

                        firebaseFirestore.collection("users")
                                .document(model.getUser_id())
                                .update("disable","false")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hello there","success");
                                    }
                                });

                    }


                });
                holder.DisableUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                                                firebaseFirestore.collection("products")
                                                        .document(id)
                                                        .update("disable","true")
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

                        firebaseFirestore.collection("users")
                                .document(model.getUser_id())
                                .update("disable","true")
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hello there","success");
                                    }
                                });

                    }


                });








            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);
    }
    private  class UserViewHolder extends  RecyclerView.ViewHolder {

        public TextView Name,Address,Contact_no,Phone_no,Email,Status;
        EditText OrderedQuantity;
        public TextView Price,Quantity;
        public ImageView ProductImage;
        Button ShowAR;
        Button BuyBtn;
        Button Delete,DisableUser,EnableUser;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);



            Name = itemView.findViewById(R.id.name_id);
            Address=itemView.findViewById(R.id.address_id);
            Contact_no=itemView.findViewById(R.id.contact_no_id);
            Email=itemView.findViewById(R.id.email_id);
            DisableUser=itemView.findViewById(R.id.disableUser_id);
            Status=itemView.findViewById(R.id.status_id);
            EnableUser=itemView.findViewById(R.id.enable_user_id);



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
