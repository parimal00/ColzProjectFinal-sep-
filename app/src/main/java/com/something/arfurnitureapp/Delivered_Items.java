package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.something.arfurnitureapp.ui.tools.ToolsFragment;

import java.io.File;
import java.io.IOException;

public class Delivered_Items extends AppCompatActivity {


    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;
    int quantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivered__items);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mfireStoreList=findViewById(R.id.orderList);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa",userID);

        Query query = firebaseFirestore.collection("users").document(userID).collection("buyingUsers").whereEqualTo("item_delivered",true);


        FirestoreRecyclerOptions<OrderedUsersModel> options = new FirestoreRecyclerOptions.Builder<OrderedUsersModel>().setQuery(query,OrderedUsersModel.class).build();




        adapter = new FirestoreRecyclerAdapter<OrderedUsersModel, Delivered_Items.OrderUsersListHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull Delivered_Items.OrderUsersListHolder holder, int position, @NonNull OrderedUsersModel model) {
                holder.OrderedUserName.setText("Username: "+model.getUsername());
                holder.Address.setText("Address: "+model.getAddress());
                holder.Phone_no.setText("phone no"+model.getPhoneNo());
                holder.OrderedUserQuantity.setText("Quantity :"+model.getOrdered_quantity());

                String  image_name = model.getImage_name();
                String buying_user_id=model.getBuying_user_id();


                firebaseFirestore.collection("products").document(model.getProduct_doc_ref())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if(documentSnapshot.exists()){
                                    Log.d("quantityzzzzz",documentSnapshot.getString("quantity"));
                                    quantity = Integer.parseInt(documentSnapshot.getString("quantity"));


                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });




                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    mStorageRef.child(image_name).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
                            holder.productImage.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);


                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }



            @NonNull
            @Override
            public Delivered_Items.OrderUsersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_delivered_layout,parent,false);
                return new Delivered_Items.OrderUsersListHolder(view);
            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);

    }
    private class OrderUsersListHolder extends  RecyclerView.ViewHolder {

        TextView OrderedUserName,Phone_no,Address,OrderedUserQuantity;
        ImageView productImage;



        public OrderUsersListHolder(@NonNull View itemView) {
            super(itemView);

            OrderedUserName=itemView.findViewById(R.id.orderedUSerName_id);
            productImage = itemView.findViewById(R.id.productImagezzzz_id);
            Phone_no = itemView.findViewById(R.id.orderedUser_phone_no_id);
            Address = itemView.findViewById(R.id.orderedUser_address_id);
            OrderedUserQuantity = itemView.findViewById(R.id.orderedUser_quantity_id);







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
