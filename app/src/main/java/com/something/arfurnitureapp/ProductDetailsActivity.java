package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.common.util.ProcessUtils;

public class ProductDetailsActivity extends AppCompatActivity {
TextView ProductName,Description, Specification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Intent intent = getIntent();
        String productName = intent.getStringExtra("productName");
        String description= intent.getStringExtra("description");
        String specification=  intent.getStringExtra("specification");
        Log.d("testSpects",productName+description+specification);

        Specification=findViewById(R.id.product_specification_id);
        Description=findViewById(R.id.product_description_id);
        ProductName=findViewById(R.id.product_name_id);

          Specification.setText(specification);
          Description.setText(description);
        ProductName.setText(productName);
   }
}
