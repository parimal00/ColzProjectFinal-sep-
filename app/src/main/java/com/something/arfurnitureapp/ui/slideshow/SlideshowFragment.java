package com.something.arfurnitureapp.ui.slideshow;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.something.arfurnitureapp.NewsFeedActivity;
import com.something.arfurnitureapp.R;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    TextInputLayout Title, Price, Address, Phone_no, Quantity,Specification,Description;
    Button PostProduct, ChooseFile, UploadGlb,UploadModel;
    ImageView ProductImage;
    TextView ImageCheck,ModelCheck;
   public Uri filepath,filepath2;
   String uuID;
   String modelID;


    Boolean imageUploaded=false;
    Boolean modelUploaded=false;


    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    private StorageReference mStorageRef;

    Boolean validate(String imageCheck,String title,String price,String address,String phone_no,String quantity,String specification,String description){

        if (imageCheck.length()>0) {
            return true;
        }
        if (title.length()>0) {
            return true;
        }
        if (price.length()>0) {
            return true;
        }
        if (address.length()>0) {
            return true;
        }
        if (phone_no.length()>0) {
            return true;
        }
        if (quantity.length()>0) {
            return true;
        }
        if (specification.length()>0) {
            return true;
        }
        if (description.length()>0) {
            return true;
        }

        return false;

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && data != null && resultCode == getActivity().RESULT_OK && data.getData() != null) {
            filepath = data.getData();



            ProductImage.setImageURI(filepath);
            ImageCheck.setText("checked");
            this.imageUploaded=true;
            Log.d("filepath",":waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+filepath);
        }

        if (requestCode == 2 && data != null && resultCode == getActivity().RESULT_OK && data.getData() != null) {
            filepath2 = data.getData();
            Log.d("filepath",":waaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"+filepath2);
            ModelCheck.setText("checked");
            this.modelUploaded=true;
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
//        final TextView textView = root.findViewById(R.id.text_slideshow);
//        slideshowViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference();

        uuID = UUID.randomUUID().toString();
        modelID = UUID.randomUUID().toString();
        String userID = firebaseAuth.getCurrentUser().getUid();


        Title = root.findViewById(R.id.title_id);
        Price = root.findViewById(R.id.price_id);
        Address = root.findViewById(R.id.address_id);
        Phone_no = root.findViewById(R.id.mobile_no_id);
        Quantity = root.findViewById(R.id.quantity_id);
        Description=root.findViewById(R.id.description_id);
        Specification=root.findViewById(R.id.specification_id);
        ImageCheck=root.findViewById(R.id.image_check_id);
        ModelCheck=root.findViewById(R.id.model_check_id);

        ProductImage = root.findViewById(R.id.productImage_id);


        ChooseFile = root.findViewById(R.id.chooseFileBtn_id);

        ChooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select and image"), 1);

            }
        });


        PostProduct = root.findViewById(R.id.postProduct_id);

        PostProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String documentRef = UUID.randomUUID().toString();

                String title = Title.getEditText().getText().toString();
                String price = Price.getEditText().getText().toString();
                String address = Address.getEditText().getText().toString();
                String phone_no = Phone_no.getEditText().getText().toString();
                String quantity = Quantity.getEditText().getText().toString();
                String specification = Specification.getEditText().getText().toString();
                String description=Description.getEditText().getText().toString();
                String imageCheck=ImageCheck.getText().toString();


                String userID = firebaseAuth.getCurrentUser().getUid();
                Log.d("idd bitchh", userID);
                String postRef = UUID.randomUUID().toString();

                Map<Object, String> productInfo = new HashMap<>();
                productInfo.put("userID", userID);
                productInfo.put("title", title);
                productInfo.put("price", price);
                productInfo.put("addresss", address);
                productInfo.put("phone_no", phone_no);
                productInfo.put("quantity",quantity);
                productInfo.put("image_name",uuID);
                productInfo.put("model_id",modelID);
                productInfo.put("product_doc_ref",documentRef);
                productInfo.put("postRef",postRef);
                productInfo.put("specification",specification);
                productInfo.put("description",description);


                if(validate(imageCheck,title,price,address,phone_no,quantity,specification,description)==false){
                    Toast.makeText(getContext(), "Fill all the forms", Toast.LENGTH_SHORT).show();
                }
                else{




                firebaseFirestore.collection("users").document(userID).collection("myPosts")
                        .document(postRef)
                        .set(productInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

//
                firebaseFirestore.collection("products").document(documentRef)
                        .set(productInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("waaaaa","succeessss");
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("waaaaa","failure bro");
                    }
                });

                uploadImage();
                postGLb();
                }
            }
        });


        UploadGlb = root.findViewById(R.id.upload_glb_id);
        UploadGlb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("hey","clicked");
                Intent intent = new Intent();
                intent.setType("*/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "select 3d model"), 2);


            }
        });

//        UploadModel = root.findViewById(R.id.upload_model_id);
//        UploadModel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                postGLb();
//            }
//        });

        return root;
    }

    private void postGLb() {

        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("uploading...");
        pd.show();

        Log.d("filepath","zzzzzzzzzzzz"+filepath2);

        String userID = firebaseAuth.getCurrentUser().getUid();



        mStorageRef.child(modelID).putFile(filepath2).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getContext(), "Successfully  uploaded", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), NewsFeedActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("failue","bllla:"+e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progressPerct = (100.00* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                pd.setMessage("Percentage :"+(int)progressPerct + "%");
            }
        });
    }

    private void uploadImage() {
//
        ProgressDialog pd = new ProgressDialog(getContext());
        pd.setTitle("uploading...");
        pd.show();

        Log.d("filepath","zzzzzzzzzzzz"+filepath);

        String userID = firebaseAuth.getCurrentUser().getUid();



        mStorageRef.child(uuID).putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Toast.makeText(getContext(), "Successfully  uploaded", Toast.LENGTH_SHORT).show();
                //startActivity(new Intent(getContext(), NewsFeedActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               Log.d("failue","bllla:"+e);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progressPerct = (100.00* taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                pd.setMessage("Percentage :"+(int)progressPerct + "%");
            }
        });

//        riversRef.putFile(filepath)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        // Get a URL to the uploaded content
//                        Log.d("success Bitch", "suuuuccccccccccccesss");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        // Handle unsuccessful uploads
//                        // ...
//                    }
//                });
    }
}