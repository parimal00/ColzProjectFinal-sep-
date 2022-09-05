package com.something.arfurnitureapp.ui.tools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.something.arfurnitureapp.ARActivity;
import com.something.arfurnitureapp.DigitalSignature;
import com.something.arfurnitureapp.OrderedUsersModel;
import com.something.arfurnitureapp.ProductModel;
import com.something.arfurnitureapp.R;
import com.something.arfurnitureapp.ui.home.HomeFragment;

import java.io.File;
import java.io.IOException;

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;
    int quantity;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);


        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mfireStoreList=root.findViewById(R.id.orderList);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa",userID);

        Query query = firebaseFirestore.collection("users").document(userID).collection("buyingUsers").whereEqualTo("item_delivered",false);


        FirestoreRecyclerOptions<OrderedUsersModel> options = new FirestoreRecyclerOptions.Builder<OrderedUsersModel>().setQuery(query,OrderedUsersModel.class).build();




        adapter = new FirestoreRecyclerAdapter<OrderedUsersModel,OrderUsersListHolder>(options) {


            @Override
            protected void onBindViewHolder(@NonNull OrderUsersListHolder holder, int position, @NonNull OrderedUsersModel model) {
                holder.OrderedUserName.setText("Username: "+model.getUsername());
                holder.Address.setText("Address: "+model.getAddress());
                holder.Phone_no.setText("phone no"+model.getPhoneNo());
                holder.OrderedUserQuantity.setText("Quantity :"+model.getOrdered_quantity());

                String  image_name = model.getImage_name();



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



                holder.Delivered_Item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        Log.d("documentRef",model.getDocumentRef());

                        Log.d("delivered",model.getProduct_doc_ref());
                            int updatedQuantity = quantity -Integer.parseInt( model.getOrdered_quantity());

                            Log.d("updated ", String.valueOf(updatedQuantity));

                            String product_dec_ref=model.getProduct_doc_ref();
                            String  documentRef=model.getDocumentRef();
                            String buying_user_id=model.getBuying_user_id();


                            Intent intent=new Intent(getContext(),DigitalSignature.class);
                            intent.putExtra("product_doc_ref",product_dec_ref);
                            intent.putExtra("documentRef",documentRef);
                            intent.putExtra("buying_user_id",buying_user_id);
                            intent.putExtra("userID",userID);
                            intent.putExtra("updatedQuantity",updatedQuantity);
                            startActivity(intent);


//////
//                        firebaseFirestore.collection("products").document(model.getProduct_doc_ref())
//                                .update("quantity",String.valueOf(updatedQuantity));

                        String quantity=model.getOrdered_quantity();


                        //add the buying user in completed orders field

//                        DocumentReference docRef = firebaseFirestore.collection("users").document(userID).collection("buyingUsers").document(model.getDocumentRef());
//                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    DocumentSnapshot document = task.getResult();
//                                    if (document.exists()) {
//                                        Log.d("waaa", "DocumentSnapshot data: " + document.getData());
//
//                                        firebaseFirestore.collection("users").document(userID).collection("orders_delivered")
//                                                .document(model.getDocumentRef()).set(document.getData())
//                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                                    @Override
//                                                    public void onSuccess(Void aVoid) {
//
//                                                    }
//                                                }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//
//                                            }
//                                        });
//
//                                    } else {
//                                        Log.d("waaa", "No such document");
//                                    }
//                                } else {
//                                    Log.d("waaaa", "get failed with ", task.getException());
//                                }
//                            }
//                        });


//                        firebaseFirestore.collection("users").document(userID).collection("buyingUsers").document(model.getDocumentRef()).update("item_delivered",true)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Log.d("sexy","waaaaaaaa");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("sexy","bitch");
//                            }
//                        });
//
//
//                        firebaseFirestore.collection("users").document(buying_user_id).collection("itemsBought").document(model.getDocumentRef()).update("item_bought",true)
//                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<Void> task) {
//                                        Log.d("sexy","waaaaaaaa");
//                                    }
//                                }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.d("sexy","bitch");
//                            }
//                        });


                    }
                });
               // Log.d("waaaaaaaaaaaaaaaa","waaaaaaaaaaaaaaaaaaaaaa");



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
            public OrderUsersListHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_layout,parent,false);
                return new OrderUsersListHolder(view);
            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mfireStoreList.setAdapter(adapter);


        return root;
    }

    private class OrderUsersListHolder extends  RecyclerView.ViewHolder {

        TextView OrderedUserName,Phone_no,Address,OrderedUserQuantity;
        ImageView productImage;
        Button Delivered_Item;


        public OrderUsersListHolder(@NonNull View itemView) {
            super(itemView);

                OrderedUserName=itemView.findViewById(R.id.orderedUSerName_id);
                productImage = itemView.findViewById(R.id.productImagezzzz_id);
                Phone_no = itemView.findViewById(R.id.orderedUser_phone_no_id);
                Address = itemView.findViewById(R.id.orderedUser_address_id);
                OrderedUserQuantity = itemView.findViewById(R.id.orderedUser_quantity_id);
                Delivered_Item = itemView.findViewById(R.id.item_delivered_id);






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