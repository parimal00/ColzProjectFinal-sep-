package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.something.arfurnitureapp.ui.home.HomeFragment;

import java.io.File;
import java.io.IOException;

public class AllProducts extends AppCompatActivity {

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);
        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");
        mfireStoreList=findViewById(R.id.allProducts_id);
        firebaseFirestore = FirebaseFirestore.getInstance();




        currentUserID = firebaseAuth.getCurrentUser().getUid();

        Query query = firebaseFirestore.collection("products");

        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(query,ProductModel.class).build();

        adapter = new FirestoreRecyclerAdapter<ProductModel, AllProducts.ProductViewHolder>(options) {



            @NonNull
            @Override
            public AllProducts.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_products_res,parent,false);
                return new AllProducts.ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllProducts.ProductViewHolder holder, int position, @NonNull ProductModel model) {
                holder.Price.setText("Price :"+model.getPrice());
                holder.List_name.setText("Title : "+model.getTitle());
                holder.Address.setText("Address :"+model.getAddresss());
                holder.ContactNumber.setText("Contact_number: "+model.getPhone_no());
                holder.Quantity.setText("Available Quantity :"+model.getQuantity());




                String image_name = model.getImage_name();
                String userID = model.getUserID();
                String price = model.getPrice();
                String itemsName = model.getTitle();
                String modelID = model.getmodel_id();
                String product_doc_ref = model.getProduct_doc_ref();


                //   String orderedQuantity = "1";

                Log.d("sexy",model.getQuantity());


                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    mStorageRef.child(image_name).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
                            holder.ProductImage.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);


                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mfireStoreList.setAdapter(adapter);
    }

    private  class ProductViewHolder extends  RecyclerView.ViewHolder {

        public TextView List_name,ContactNumber,Address;
        EditText OrderedQuantity;
        public TextView Price,Quantity;
        public  ImageView ProductImage;
        Button ShowAR;
        Button BuyBtn;
        Button Delete;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            List_name = itemView.findViewById(R.id.product_name_id);
            Price = itemView.findViewById(R.id.product_price_id);
            ContactNumber = itemView.findViewById(R.id.contact_number_id);
            Address = itemView.findViewById(R.id.address_idzzz);
            Quantity = itemView.findViewById(R.id.list_quantity);
            // ProductImage =  itemView.findViewById(R.id.productImage_id);
            // downloadImage(itemView,"uploads");
            ProductImage = itemView.findViewById(R.id.productImage_id);
            ShowAR=itemView.findViewById(R.id.showARBtn_id);
            BuyBtn = itemView.findViewById(R.id.buyBtn_id);
            OrderedQuantity = itemView.findViewById(R.id.orderedQuantity_id);







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
