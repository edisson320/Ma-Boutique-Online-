package com.example.ma_boutique_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoard extends AppCompatActivity {

    private Button btnSalir;

    private FirebaseAuth mAuth;
    private Bundle savedInstanceState;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        btnSalir = findViewById(R.id.btnSalir);

        mAuth = FirebaseAuth.getInstance();

        btnSalir.setOnClickListener(view -> {
            mAuth.signOut();
            startActivity(new Intent(this, AddProductActivity.class));

        });

    }//End onCreate



}