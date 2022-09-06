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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.something.arfurnitureapp.ui.share.ShareFragment;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

public class Items_Bought extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items__bought);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mfireStoreList = findViewById(R.id.items_bought_id);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa", userID);

        Query query = firebaseFirestore.collection("users").document(userID).collection("itemsBought").whereEqualTo("item_bought",true);


        FirestoreRecyclerOptions<ItemsBoughtModel> options = new FirestoreRecyclerOptions.Builder<ItemsBoughtModel>().setQuery(query, ItemsBoughtModel.class).build();
//


        adapter = new FirestoreRecyclerAdapter<ItemsBoughtModel, Items_Bought.ItemsBoughtHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull Items_Bought.ItemsBoughtHolder holder, int position, @NonNull ItemsBoughtModel model) {
                //holder.textView.setText(model.getImage_name());

                String image_name =model.getImage_name();

                holder.Quantity.setText(""+model.getOrdered_quantity());
                holder.Price.setText(" "+ model.getPrice());
                holder.Name.setText("" +model.getProductName());


                DocumentReference documentReference = firebaseFirestore.collection("users").document(model.getPostedUserId());


                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        // Log.d("check profile mann",documentSnapshot.getString("name"));
//                        Name.setText(documentSnapshot.getString("name"));
//                        Address.setText(documentSnapshot.getString("address"));
//                        Phone_no.setText(documentSnapshot.getString("phone_no"));
                        holder.SellerName.setText(documentSnapshot.getString("name"));
                        holder.SellerNo.setText(documentSnapshot.getString("phone_no"));

                    }
                });

                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    mStorageRef.child(image_name).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
                            holder.imageView.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);


                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @NonNull
            @Override
            public Items_Bought.ItemsBoughtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bought, parent, false);
                return new Items_Bought.ItemsBoughtHolder(view);
            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);

    }
    private class ItemsBoughtHolder extends RecyclerView.ViewHolder {

        TextView textView, Name, Price, Quantity,SellerName,SellerNo;
        ImageView imageView;



        public ItemsBoughtHolder(@NonNull View itemView) {
            super(itemView);

           // textView = itemView.findViewById(R.id.items_bought_holder_id);
            SellerName=itemView.findViewById(R.id.seller_name_id);
            SellerNo=itemView.findViewById(R.id.seller_contact_no_id);

            imageView = itemView.findViewById(R.id.items_bought_image_id);

            Name= itemView.findViewById(R.id.itemsBought_itemName_id);
            Price = itemView.findViewById(R.id.itemsBought_price_id);
            Quantity = itemView.findViewById(R.id.itemsBought_quantity_id);
        }
    }

    //
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
