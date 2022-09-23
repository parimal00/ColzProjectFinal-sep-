package com.something.arfurnitureapp.ui.share;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.something.arfurnitureapp.ItemsBoughtModel;
import com.something.arfurnitureapp.OrderedUsersModel;
import com.something.arfurnitureapp.ProductDetailsActivity;
import com.something.arfurnitureapp.R;
import com.something.arfurnitureapp.ui.tools.ToolsFragment;

import java.io.File;
import java.io.IOException;

import javax.annotation.Nullable;

public class ShareFragment extends Fragment {

    private ShareViewModel shareViewModel;

    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        shareViewModel =
//                ViewModelProviders.of(this).get(ShareViewModel.class);
        View root = inflater.inflate(R.layout.fragment_share, container, false);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mfireStoreList = root.findViewById(R.id.items_bought_id);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa", userID);

        Query query = firebaseFirestore.collection("users").document(userID).collection("itemsBought").whereEqualTo("item_bought",false);


        FirestoreRecyclerOptions<ItemsBoughtModel> options = new FirestoreRecyclerOptions.Builder<ItemsBoughtModel>().setQuery(query, ItemsBoughtModel.class).build();
//


        adapter = new FirestoreRecyclerAdapter<ItemsBoughtModel, ItemsBoughtHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ItemsBoughtHolder holder, int position, @NonNull ItemsBoughtModel model) {
                //holder.textView.setText(model.getImage_name());

                String image_name =model.getImage_name();

                holder.Quantity.setText(""+model.getOrdered_quantity());
                holder.Price.setText(""+ model.getPrice());
                holder.Name.setText("" +model.getProductName());
                holder.ProductID.setText(""+model.getProduct_doc_ref());

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

                DocumentReference productRef = firebaseFirestore.collection("products").document(model.getProduct_doc_ref());


                productRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        // Log.d("check profile mann",documentSnapshot.getString("name"));
//                        Name.setText(documentSnapshot.getString("name"));
//                        Address.setText(documentSnapshot.getString("address"));
//                        Phone_no.setText(documentSnapshot.getString("phone_no"));
                        Log.d("is_delted_let",documentSnapshot.getString("is_deleted"));
                        String status=documentSnapshot.getString("is_deleted");
                        Log.d("stausCheck",status);
                        if(status.equals("true")){
                            Log.d("checkMan",documentSnapshot.getString("is_deleted"));
                            holder.ErrorView.setText("this product is unavailable");
                        }


                    }
                });



                Log.d("product_doc_ref_let",model.getProduct_doc_ref());

                holder.CancelOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("bitch",model.getDocumentRef());
                        String documentRef = model.getDocumentRef();
                        String postedUserId = model.getPostedUserId();

                       firebaseFirestore.collection("users").document(userID).collection("itemsBought").document(documentRef).delete()
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {
                                        Log.d("sexy","waaaaaaaa");
                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {
                               Log.d("sexy","bitch");
                           }
                       });

                       firebaseFirestore.collection("users").document(postedUserId).collection("buyingUsers").document(documentRef).delete()
                               .addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> task) {

                                   }
                               }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });


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
            public ItemsBoughtHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_bought_layout, parent, false);
                return new ItemsBoughtHolder(view);
            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mfireStoreList.setAdapter(adapter);


        return root;
    }

    private class ItemsBoughtHolder extends RecyclerView.ViewHolder {

        TextView textView, Name, Price, Quantity,SellerName,SellerNo,ErrorView,ProductID;
        ImageView imageView;
        Button CancelOrder;


        public ItemsBoughtHolder(@NonNull View itemView) {
            super(itemView);

           // textView = itemView.findViewById(R.id.items_bought_holder_id);
            imageView = itemView.findViewById(R.id.items_bought_image_id);
            CancelOrder = itemView.findViewById(R.id.cancel_order_id);
            SellerName=itemView.findViewById(R.id.seller_name_id);
            SellerNo=itemView.findViewById(R.id.seller_contact_no_id);

            Name= itemView.findViewById(R.id.itemsBought_itemName_id);
            Price = itemView.findViewById(R.id.itemsBought_price_id);
            Quantity = itemView.findViewById(R.id.itemsBought_quantity_id);
            ErrorView=itemView.findViewById(R.id.errorView_id);
            ProductID=itemView.findViewById(R.id.product_id_id);
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