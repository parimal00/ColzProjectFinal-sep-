package com.something.arfurnitureapp.ui.gallery;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.something.arfurnitureapp.EditProfile;
import com.something.arfurnitureapp.MyDialogueFragment;
import com.something.arfurnitureapp.R;

import javax.annotation.Nullable;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;

    TextView Name,Address,Phone_no;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    Button Test,EditProfile;
    FragmentManager fragmentManager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
//        final TextView textView = root.findViewById(R.id.text_gallery);
//        galleryViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        String userID = firebaseAuth.getCurrentUser().getUid();
        EditProfile=root.findViewById(R.id.editProfile_id);
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),EditProfile.class);
                intent.putExtra("userID",userID);
                startActivity(intent);
            }
        });





        Name = root.findViewById(R.id.profileName_id);
        Address = root.findViewById(R.id.profileAddress_id);
        Phone_no = root.findViewById(R.id.profilePhoneNo_id);



        DocumentReference documentReference = firebaseFirestore.collection("users").document(userID);


        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
               // Log.d("check profile mann",documentSnapshot.getString("name"));
                Name.setText(documentSnapshot.getString("name"));
                Address.setText(documentSnapshot.getString("address"));
                Phone_no.setText(documentSnapshot.getString("phone_no"));

            }
        });

        Name.setText("jack is sexy");
        Address.setText("jack is sexy");
        Phone_no.setText("jack is sexy");





        fragmentManager = getActivity().getSupportFragmentManager();
//        Test.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Log.d("waaaaaaaaaaaaa","waaaaaaaaaaaaaa");
//                new AlertDialog.Builder(view.getContext())
//                        .setNegativeButton("aawee", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//
//                            }
//                        }).show();
//            }
       // });

        return root;
    }
}