package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ReportUser extends AppCompatActivity {
    FirebaseFirestore fStore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_user);

        fStore = FirebaseFirestore.getInstance();


        DocumentReference documentReference = fStore.collection("reports").document("user_id");

        Map<Object,String> report = new HashMap<>();
        report.put("report_detail","detail");


        documentReference.set(report).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d("succceess!!! ","User addedd waaaaaaaaaa");
            }
        });
    }
}
