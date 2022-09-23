package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class EditProfile extends AppCompatActivity {
   // TextInputLayout Name,Username,Email,Password1,Password2,Phone_no,Address;
    TextInputEditText  Name,Username,Email,Password1,Password2,Phone_no,Address;
    Button EditProfile;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        Name=findViewById(R.id.name_id);
        Phone_no=findViewById(R.id.contact_no_id);
        Address=findViewById(R.id.address_id);
        EditProfile=findViewById(R.id.edit_profile_id);

        Intent intent =getIntent();
        String userID=intent.getStringExtra("userID");

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
        EditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String name=Name.getText().toString().replace(" ", "");
                String address=Address.getText().toString().replace(" ", "");
                String phone_no=Phone_no.getText().toString().replace(" ", "");

                if(name.length()>0&&address.length()>0&&phone_no.length()>0) {
                    ProgressDialog pd = new ProgressDialog(view.getContext());
                    pd.setTitle("Editing ...");
                    pd.show();
                    firebaseFirestore.collection("users")
                            .document(userID)
                            .update("name", name, "address", address)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("hello there", "success");
                                    pd.setTitle("Success");
                                    startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
                                }
                            });
                }
                else{
                    Toast.makeText(getApplicationContext(),"fill all the forms",Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}
