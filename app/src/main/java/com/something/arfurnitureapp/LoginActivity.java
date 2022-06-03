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
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

  //  EditText Email,Password;
    Button Login_btn;
    FirebaseAuth firebaseAuth;
    TextInputLayout Email,Password;
    TextView SignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            startActivity(new Intent(getApplicationContext(),NewsFeedActivity.class));
            finish();
        }




        Email = findViewById(R.id.email_id);
        Password = findViewById(R.id.password_id);
        SignupBtn=findViewById(R.id.signupBtn_id);

        Login_btn = findViewById(R.id.loginBtn_id);

        SignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(intent);
                Log.d("buttotn pressed","fucccckckkkkeee");
            }
        });

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getEditText().getText().toString();
                String password = Password.getEditText().getText().toString();
                Log.d("checkk","checkk"+email+password);

                if(TextUtils.isEmpty(email)){
                    Email.setError("email required");
                }

                if(TextUtils.isEmpty(password)){
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
                                pd.dismiss();
                                startActivity(new Intent(getApplicationContext(), NewsFeedActivity.class));
                            } else {
                                pd.dismiss();
                                Email.setError("email or password incorrect");
                            }
                        }
                    });
                }
            }
        });
    }
}
