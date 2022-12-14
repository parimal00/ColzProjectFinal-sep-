package com.something.arfurnitureapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAsAdmin extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    TextInputLayout Email,Password;
    Button Login_btn;
    TextView ErrorMessage;
    FirebaseFirestore fStore;

    private  boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo()!=null&&connectivityManager.getActiveNetworkInfo().isConnectedOrConnecting();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_as_admin);



        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Email = findViewById(R.id.email_id);
        Password = findViewById(R.id.password_id);
        Login_btn = findViewById(R.id.loginBtn_id);
        ErrorMessage=findViewById(R.id.error_message_id);


        if(isConnected()==false){
            ErrorMessage.setText("Internet Connection failed");
            return;
        }
        if(firebaseAuth.getCurrentUser()!=null){
            Log.d("getUid",""+firebaseAuth.getUid());
            DocumentReference df= fStore.collection("admins").document(firebaseAuth.getUid());

            df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                            finish();
                        } else {

                        }
                    } else {

                    }
                }
            });



        }

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(firebaseAuth.getCurrentUser()!=null){
                  firebaseAuth.signOut();
                }
                String email = Email.getEditText().getText().toString().replace(" ", "");
                String password = Password.getEditText().getText().toString().replace(" ", "");
                Log.d("checkk","checkk"+email+password);


                if(TextUtils.isEmpty(email)){
                   ErrorMessage.setText("email is empty");
                  return;
                }

                if(TextUtils.isEmpty(password)){
                    ErrorMessage.setText(" password is empty");
                }

                else {
                    ProgressDialog pd = new ProgressDialog(view.getContext());
                    pd.setTitle("Loggin in ...");
                    pd.show();

                    Log.d("email",email+password);

                    firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //pd.dismiss();
                                Log.d("uid_reg",""+firebaseAuth.getUid());
                                DocumentReference df= fStore.collection("admins").document(firebaseAuth.getUid());
                                df.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d("jack",""+String.valueOf(document));
                                                Log.d("waaaaa","jack is sexy");
                                                startActivity(new Intent(getApplicationContext(), AdminHomeActivity.class));
                                                finish();
                                            } else {
                                                pd.dismiss();
                                                ErrorMessage.setText("email or password is incorrect");
                                            }
                                        } else {

                                        }
                                    }
                                });
                            } else {
                                pd.dismiss();
                                ErrorMessage.setText("email or password is incorrect");
                            }
                        }
                    });
                }
            }
        });
    }
}
