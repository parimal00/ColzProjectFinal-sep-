package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ReportUser extends AppCompatActivity {
    FirebaseFirestore fStore;
    EditText Report;
    Button SendReport;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
     String user_id=intent.getStringExtra("user_id");
        Report=findViewById(R.id.report_text_id);
//
        SendReport=findViewById(R.id.sendReport);
        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String report_text=Report.getText().toString();
                String report_ref = UUID.randomUUID().toString();


                String my_id = firebaseAuth.getCurrentUser().getUid();

        if(report_text.length()>0) {
            DocumentReference documentReference = fStore.collection("users").document(user_id).collection("reports").document(report_ref);

            Map<Object, String> report = new HashMap<>();
            report.put("reported_by", my_id);
            report.put("report_text", report_text);


            documentReference.set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("succceess!!! ", "User addedd waaaaaaaaaa");
                }
            });
        }
        else{
            Toast.makeText(getApplicationContext(),"report cannot be empty",Toast.LENGTH_SHORT).show();
        }
            }
        });


    }
}
