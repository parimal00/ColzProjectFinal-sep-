package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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
    Boolean validation_error;
    TextView ErrorMessage;

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
        ErrorMessage= findViewById(R.id.error_message_id);

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
                String name = Name.getEditText().getText().toString().replace(" ", "");
                String username = Username.getEditText().getText().toString().replace(" ", "");
                String email = Email.getEditText().getText().toString().replace(" ", "");
                String password1 = Password1.getEditText().getText().toString().replace(" ", "");
                String password2 = Password2.getEditText().getText().toString().replace(" ", "");
                String address = Address.getEditText().getText().toString().replace(" ", "");
                String phone_no = Phone_no.getEditText().getText().toString().replace(" ", "");
                 validation_error=false;

                Log.d("here it comes",""+name+username+email+password1+password2+address+phone_no);

                if (TextUtils.isEmpty(name)) {
                    validation_error=true;
                    ErrorMessage.setText("name is required");
                }

              else if (TextUtils.isEmpty(username)) {
                    validation_error=true;
                    ErrorMessage.setText("username is required");

                }
                else if (TextUtils.isEmpty(email)) {
                    validation_error=true;
                    ErrorMessage.setText("email is required");
                }
                else if (TextUtils.isEmpty(password1)) {
                    validation_error=true;
                    ErrorMessage.setText("password is required");

                }
                else if(password1.length() < 6){

                        validation_error=true;
                        ErrorMessage.setText("password should be greater than 5 characters");

                }
                else if (TextUtils.isEmpty(password2)) {
                    validation_error=true;
                    ErrorMessage.setText("password confirmation field is required");
                }
                else if (TextUtils.isEmpty(address)) {
                    validation_error=true;
                    ErrorMessage.setText("address is required");
                }
                else if (TextUtils.isEmpty(phone_no)) {
                    validation_error=true;
                    ErrorMessage.setText("phone number is required");
                }
                else if(!password1.equals(password2)){
                    validation_error=true;
                    ErrorMessage.setText("passwords did not match !");
                }
                else if(emailValidator(email)==false){
                    validation_error=true;
                    ErrorMessage.setText("email is invalid");
                }
                else{
                    ErrorMessage.setText("");
                    validation_error=false;
                }

        Log.d("validation_error",""+validation_error);
                if (password1.equals(password2) && validation_error==false) {
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

                            else{
                                new AlertDialog.Builder(view.getContext())
                                        .setTitle("Email is already taken!")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {


                                            }
                                        })
                                        .show();
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

    public Boolean emailValidator(String emailToText) {

        // extract the entered data from the EditText
       // String emailToText = etMail.getText().toString();

        // Android offers the inbuilt patterns which the entered
        // data from the EditText field needs to be compared with
        // In this case the entered data needs to compared with
        // the EMAIL_ADDRESS, which is implemented same below
        if (!emailToText.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailToText).matches()) {
            return true;
        } else {
            return false;
        }
    }
}
