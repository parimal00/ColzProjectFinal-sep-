package com.something.arfurnitureapp.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.something.arfurnitureapp.ARActivity;
import com.something.arfurnitureapp.LoginActivity;
import com.something.arfurnitureapp.MainActivity;
import com.something.arfurnitureapp.ProductModel;
import com.something.arfurnitureapp.R;
import com.something.arfurnitureapp.ViewDescription;
import com.something.arfurnitureapp.ViewReviewsActivity;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    Button Logout;
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


    Button ShowAR;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });


        firebaseAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");
        mfireStoreList=root.findViewById(R.id.firestore_list);
        firebaseFirestore = FirebaseFirestore.getInstance();


        //ImageView2 = root.findViewById(R.id.imageView2);

      currentUserID = firebaseAuth.getCurrentUser().getUid();







       // downloadImage();


        Logout = root.findViewById(R.id.logout_id);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getContext(), LoginActivity.class));

            }
        });




        Query query = firebaseFirestore.collection("products").whereEqualTo("is_deleted","false").whereEqualTo("disable","false");

        FirestoreRecyclerOptions<ProductModel> options = new FirestoreRecyclerOptions.Builder<ProductModel>().setQuery(query,ProductModel.class).build();

         adapter = new FirestoreRecyclerAdapter<ProductModel, ProductViewHolder>(options) {

            @NonNull
            @Override
            public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_single,parent,false);
                return new ProductViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull ProductModel model) {
                holder.Price.setText(""+model.getPrice());
                holder.List_name.setText(""+model.getTitle());
                holder.Address.setText(""+model.getAddresss());
                holder.ContactNumber.setText(""+model.getPhone_no());
                holder.Quantity.setText(""+model.getQuantity());
                holder.ViewReview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("view_review","pressed");
                        Intent intent=new Intent(getContext(), ViewReviewsActivity.class);
                        intent.putExtra("product_id",model.getProduct_doc_ref());
                        startActivity(intent);
                    }
                });





                String image_name = model.getImage_name();
                String userID = model.getUserID();
                String price = model.getPrice();
                String itemsName = model.getTitle();
                String modelID = model.getmodel_id();
                String product_doc_ref = model.getProduct_doc_ref();
                String description=model.getDescription();
                String specification=model.getSpecification();


                DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);


                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        holder.PostedBy.setText(documentSnapshot.getString("email"));

                    }
                });

                holder.BuyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(view.getContext())
                                .setTitle("Are you sure you want to order this item?")
                                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        String orderedQuantity = holder.OrderedQuantity.getText().toString();
                                        if(!model.getQuantity().equals("null")) {
                                            buyProduct(userID, price, itemsName, image_name, orderedQuantity, product_doc_ref);
                                            Toast.makeText(view.getContext(), "items successfully bought", Toast.LENGTH_SHORT);
                                        }
                                        else{
                                            new AlertDialog.Builder(view.getContext())
                                                    .setTitle("Product not available")
                                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialogInterface, int i) {


                                                        }
                                                    })
                                                   .show();
                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).show();


                    }
                });

                holder.ViewSpecification.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(getContext(),ViewDescription.class);
                        intent.putExtra("specifications",specification);
                        intent.putExtra("description",description);
                        startActivity(intent);

                    }
                });

                holder.ShowAR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                            Intent intent = new Intent(getContext(), ARActivity.class);
                            intent.putExtra("modelId",modelID);

                            startActivity(intent);
                    }
                });

                //holder.ProductImage.setImageBitmap(bitmap);

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
         mfireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
         mfireStoreList.setAdapter(adapter);





        return root;
    }

    private void buyProduct(String userID,String price,String itemsName, String image_name, String orderedQuantity, String product_doc_ref) {

        String documentRef = UUID.randomUUID().toString();

        DocumentReference documentReference = firebaseFirestore.collection("users").document(currentUserID);

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
               currentUserUsername=value.getString("username");
               currentUserName =value.getString("name");
                currentUserAddress=value.getString("address");
                currentUserPhoneNo=value.getString("phone_no");



                Map<String, Object> user = new HashMap<>();
                user.put("username", currentUserName);
                user.put("name", currentUserName);
                user.put("address", currentUserAddress);
                user.put("phoneNo", currentUserPhoneNo);
                user.put("image_name",image_name);
                user.put("documentRef",documentRef);
                user.put("ordered_quantity",orderedQuantity);
                user.put("product_doc_ref",product_doc_ref);
                user.put("item_delivered",false);
                user.put("buying_user_id",firebaseAuth.getCurrentUser().getUid());
                user.put("email",value.get("email"));



                //Add a new document with a generated ID
                firebaseFirestore.collection("users").document(userID).collection("buyingUsers")
                        .document(documentRef).set(user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


            }
        });

//



        //Log.d("username",currentUserName);





        ///get current user ID and insert info about the product



        Map<String, Object> itemsInfo = new HashMap<>();
        itemsInfo.put("price", price);
        itemsInfo.put("productName", itemsName);
        itemsInfo.put("image_name",image_name);
        itemsInfo.put("documentRef",documentRef);
        itemsInfo.put("postedUserId",userID);
        itemsInfo.put("ordered_quantity",orderedQuantity);
        itemsInfo.put("product_doc_ref",product_doc_ref);
        itemsInfo.put("item_bought",false);

        firebaseFirestore.collection("users").document(currentUserID).collection("itemsBought")
                .document(documentRef).set(itemsInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getContext(),"ordered placed successfully",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }


    private class ProductViewHolder extends  RecyclerView.ViewHolder {

        public TextView List_name,ContactNumber,Address,PostedBy;
        EditText OrderedQuantity;
       public TextView Price,Quantity;
       public  ImageView ProductImage;
        Button ShowAR,ViewSpecification;
        Button BuyBtn;
        Button Delete;
        Button ViewReview;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            ViewSpecification=itemView.findViewById(R.id.view_specifications_id);
            List_name = itemView.findViewById(R.id.list_name);
            Price = itemView.findViewById(R.id.list_price);
            ContactNumber = itemView.findViewById(R.id.contact_number_id);
            Address = itemView.findViewById(R.id.address_idzzz);
            Quantity = itemView.findViewById(R.id.list_quantity);
           // ProductImage =  itemView.findViewById(R.id.productImage_id);
           // downloadImage(itemView,"uploads");
            ProductImage = itemView.findViewById(R.id.productImage_id);
            ShowAR=itemView.findViewById(R.id.showARBtn_id);
            BuyBtn = itemView.findViewById(R.id.buyBtn_id);
            OrderedQuantity = itemView.findViewById(R.id.orderedQuantity_id);
            ViewReview=itemView.findViewById(R.id.view_review_id);
            PostedBy=itemView.findViewById(R.id.posted_by_id);







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
    private void downloadImage(View itemView,String imageName) {


//        Log.d("waaaaaaaaaaaaaaaaaaaaaaaa","paaaaaaaaaaahhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
//        try {
//            final File localFile = File.createTempFile("images", "jpg");
//
//            ProductImage = itemView.findViewById(R.id.productImage_id);
//            mStorageRef.child(imageName).getFile(localFile)
//                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//
//                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
//                            ProductImage.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);
//
//
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception exception) {
//                    // Handle failed download
//                    // ...
//                }
//            });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


    }
}