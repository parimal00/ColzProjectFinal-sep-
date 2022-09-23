package com.something.arfurnitureapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminHomeActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        Button AllUsers,AllOrders,AllItems,Logout;

        firebaseAuth = FirebaseAuth.getInstance();

        AllUsers=findViewById(R.id.all_users_id);
//        AllOrders=findViewById(R.id.all_orders_id);
//        AllItems=findViewById(R.id.all_items_d);
        Logout=findViewById(R.id.admin_logout_id);

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.signOut();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            }
        });

        AllUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AllUsersActivity.class);
                startActivity(intent);
            }
        });

//        AllOrders.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        AllItems.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(),AllProducts.class);
//                startActivity(intent);
//            }
//        });
    }
}
