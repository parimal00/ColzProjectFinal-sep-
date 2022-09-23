package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportUser extends AppCompatActivity {
    FirebaseFirestore fStore;
    EditText Report;
    Button SendReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        Intent intent = getIntent();
     String product_id= intent.getStringExtra("product_id");
        Report=findViewById(R.id.report_text_id);
//
        SendReport=findViewById(R.id.sendReport);
        SendReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String report_text=Report.getText().toString();
//


            }
        });


    }
}
