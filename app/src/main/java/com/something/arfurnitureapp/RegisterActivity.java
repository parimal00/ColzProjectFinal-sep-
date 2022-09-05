package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    Button Register;
    //EditText Name,Username,Email,Password1,Password2,Phone_no,Address;
    TextInputLayout Name,Username,Email,Password1,Password2,Phone_no,Address;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Name=findViewById(R.id.name_id);
        Username=findViewById(R.id.username_id);
        Email=findViewById(R.id.email_id);
        Password1=findViewById(R.id.password_logout_id);
        Password2=findViewById(R.id.password2_id);
        Phone_no=findViewById(R.id.phone_no_id);
        Address=findViewById(R.id.address_id);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
            finish();
        }


        Register = findViewById(R.id.registerBtn_id);
        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Log.d("waaa","btuttsfjfsff");

                // FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                //  DatabaseReference myRef = firebaseDatabase.getReference().child("Post");

//                HashMap<String,Object> map = new HashMap<>();
//                map.put("title","jack is bitch");
//
//                FirebaseDatabase.getInstance().getReference().child("Post").push().
//                        setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        Log.d("comlllleeete","biiiitttcchh");
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.d("exxxxxxxxxxxccccccceppptionnn","waaaaafuck you"+e);
//                    }
//                });



//
                String name = Name.getEditText().getText().toString();
                String username = Username.getEditText().getText().toString();
                String email = Email.getEditText().getText().toString();
                String password1 = Password1.getEditText().getText().toString();
                String password2 = Password2.getEditText().getText().toString();
                String address = Address.getEditText().getText().toString();
                String phone_no = Phone_no.getEditText().getText().toString();

                Log.d("here it comes",""+name+username+email+password1+password2+address+phone_no);

                if (TextUtils.isEmpty(name)) {
                    Name.setError("Name is required");
                }
                if (TextUtils.isEmpty(username)) {
                    Username.setError("Username is required");
                }
                if (TextUtils.isEmpty(email)) {
                    Email.setError("Email is required");
                }
                if (TextUtils.isEmpty(password1)) {
                    Password1.setError("Password is required");
                    if (password1.length() < 6) {
                        Password1.setError("Password should be at least 6 characters");
                    }
                }
                if (TextUtils.isEmpty(password2)) {
                    Password2.setError("Password is required");
                }
                if (TextUtils.isEmpty(address)) {
                    Address.setError("Address is required");
                }
                if (TextUtils.isEmpty(phone_no)) {
                    Phone_no.setError("Phone number is required");
                }



                if (password1.equals(password2)) {
                    ProgressDialog pd = new ProgressDialog(view.getContext());
                    pd.setTitle("Registering ...");
                    pd.show();
                    Log.d("waa","passwroddd equal");
                    //startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
                    firebaseAuth.createUserWithEmailAndPassword(email, password1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d("Congratsss dude!!!", "users with email and password created");

                                String userID = firebaseAuth.getCurrentUser().getUid();

                                DocumentReference documentReference = fStore.collection("users").document(userID);

                                Map<Object,String> user = new HashMap<>();
                                user.put("name",name);
                                user.put("username",username);
                                user.put("address",address);
                                user.put("phone_no",phone_no);
                                user.put("user_id",userID);
                                user.put("email",email);

                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("succceess!!! ","User addedd waaaaaaaaaa");
                                    }
                                });
                                startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
                                pd.dismiss();
                            }

                        }
                    });
                }
                else {

                    Password1.setError("Both passwords must be same");
                }
           }
     });

    }
}
