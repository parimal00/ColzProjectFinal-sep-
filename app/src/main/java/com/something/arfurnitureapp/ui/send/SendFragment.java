package com.something.arfurnitureapp.ui.send;

import android.content.Intent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.something.arfurnitureapp.DialogBox;
import com.something.arfurnitureapp.ItemsBoughtModel;
import com.something.arfurnitureapp.MyPostsModel;
import com.something.arfurnitureapp.NewsFeedActivity;
import com.something.arfurnitureapp.R;
import com.something.arfurnitureapp.ui.share.ShareFragment;

import java.io.File;
import java.io.IOException;

public class SendFragment extends Fragment {

    private SendViewModel sendViewModel;



    FirebaseFirestore firebaseFirestore;
    FirestoreRecyclerAdapter adapter;
    FirebaseAuth firebaseAuth;
    RecyclerView mfireStoreList;
    StorageReference mStorageRef;
    Bitmap bitmap;
    TextView Error;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sendViewModel =
                ViewModelProviders.of(this).get(SendViewModel.class);
        View root = inflater.inflate(R.layout.fragment_send, container, false);
//


        Error=root.findViewById(R.id.error_id);

        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        mfireStoreList = root.findViewById(R.id.my_post_recycler_id);

        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://fir-aoo-49890.appspot.com");

        String userID = firebaseAuth.getCurrentUser().getUid();

        Log.d("waaaaaaaaaaaaaa", userID);

        firebaseFirestore.collection("users").document(userID).collection("myPosts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            int count=0;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                count=count+1;

                            }
                            if(count==0){
                                Error.setText("you have posted 0 items");
                            }

                        } else {

                        }
                    }
                });

        Query query = firebaseFirestore.collection("users").document(userID).collection("myPosts");


        FirestoreRecyclerOptions<MyPostsModel> options = new FirestoreRecyclerOptions.Builder<MyPostsModel>().setQuery(query, MyPostsModel.class).build();
//


        adapter = new FirestoreRecyclerAdapter<MyPostsModel, MyPostsHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyPostsHolder holder, int position, @NonNull MyPostsModel model) {

            holder.Item_Name.setText(""+model.getTitle());
            holder.Quantity.setText("" +model.getQuantity());
            holder.Phone_no.setText(""+model.getPhone_no());
            holder.ProductPrice.setText(""+model.getPrice());
            holder.ProductId.setText(""+model.getProduct_doc_ref());
            String image_name = model.getImage_name();
                try {
                    final File localFile = File.createTempFile("images", "jpg");
                    mStorageRef.child(image_name).getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
//
                            holder.MyPost_image.setImageBitmap(bitmap);
//                            ImageView2.setImageBitmap(bitmap);


                        }
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }



                holder.DeletePost.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String documentRef = model.getProduct_doc_ref();

                        firebaseFirestore.collection("products").document(documentRef).update("is_deleted","true")

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("hello there","success");


                                    }
                                });
                        firebaseFirestore.collection("users").document(userID).collection("myPosts").document(model.getPostRef()).delete()
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
                    }
                });




            }

            @NonNull
            @Override
            public MyPostsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_layout, parent, false);
                return new MyPostsHolder(view);
            }
        };

        mfireStoreList.setHasFixedSize(true);
        mfireStoreList.setLayoutManager(new LinearLayoutManager(getContext()));
        mfireStoreList.setAdapter(adapter);

        return root;
    }


    private class MyPostsHolder extends RecyclerView.ViewHolder {

        TextView Item_Name,Phone_no, Quantity, ProductPrice,ProductId;
        ImageView MyPost_image;
        Button DeletePost;


        public MyPostsHolder(@NonNull View itemView) {
            super(itemView);

           Item_Name = itemView.findViewById(R.id.item_name_id);
           Phone_no = itemView.findViewById(R.id.myPost_phoneNO_id);
           Quantity = itemView.findViewById(R.id.myPost_quantity_id);

           MyPost_image = itemView.findViewById(R.id.myPost_image_id);

           DeletePost = itemView.findViewById(R.id.deletePost_id);
           ProductPrice=itemView.findViewById(R.id.product_price_id);
           ProductId=itemView.findViewById(R.id.product_id_id);
        }
    }
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