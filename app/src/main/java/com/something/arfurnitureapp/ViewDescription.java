package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_description);

        Intent intent=getIntent();
        String specifications= intent.getStringExtra("specifications");
        String description=intent.getStringExtra("description");

        TextView Specifications=findViewById(R.id.product_specification_id);
        TextView Description=findViewById(R.id.product_description_id);

        Specifications.setText(specifications);
        Description.setText(description);

    }
}
