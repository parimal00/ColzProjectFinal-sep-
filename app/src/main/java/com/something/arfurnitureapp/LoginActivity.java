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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

  //  EditText Email,Password;
    Button Login_btn;
    FirebaseAuth firebaseAuth;
    TextInputLayout Email,Password;
    TextView SignupBtn;
    TextView LoginAsAdmin,Error;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

      // startActivity(new Intent(getApplicationContext(), DigitalSignature.class));



        fStore = FirebaseFirestore.getInstance();

        Error=findViewById(R.id.login_error_id);

        firebaseAuth = FirebaseAuth.getInstance();


        if(firebaseAuth.getCurrentUser()!=null){



            DocumentReference df= fStore.collection("admins").document(firebaseAuth.getUid());
            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                        } else {

                        }
                    } else {

                    }
                }
            });




//
            DocumentReference dfu= fStore.collection("users").document(firebaseAuth.getUid());
            dfu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
                        } else {

                        }
                    } else {

                    }
                }
            });




        }




        Email = findViewById(R.id.email_id);
        Password = findViewById(R.id.password_id);
        SignupBtn=findViewById(R.id.signupBtn_id);

        Login_btn = findViewById(R.id.loginBtn_id);
        LoginAsAdmin=findViewById(R.id.loginAsAdmin_id);
        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                Log.d("buttotn pressed","hello");
            }
        });


        LoginAsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginAsAdmin.class);
                startActivity(intent);
            }
        });

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getEditText().getText().toString().replace(" ", "");
                String password = Password.getEditText().getText().toString().replace(" ", "");
                Log.d("checkk","checkk"+email+password);

                if(TextUtils.isEmpty(email)){
                    Error.setText("email required");
                    Email.setError("email required");
                }

                else if(TextUtils.isEmpty(password)){
                    Error.setText("password required");
                    Password.setError("password required");
                }

                else {
                    ProgressDialog pd = new ProgressDialog(view.getContext());
                    pd.setTitle("Loggin in ...");
                    pd.show();

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Log.d("user_id",firebaseAuth.getUid());
                                DocumentReference dfu= fStore.collection("users").document(firebaseAuth.getUid());
                                dfu.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                pd.dismiss();
                                                startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
                                            } else {
                                                pd.dismiss();
                                                Error.setText("incorrect email or password");
                                                firebaseAuth.signOut();
                                            }
                                        } else {

                                        }
                                    }
                                });

//                                startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
                            } else {
                                pd.dismiss();
                                Error.setText("incorrect email or password");
                                Email.setError("email or password incorrect");
                            }
                        }
                    });
                }
            }
        });
    }
}
