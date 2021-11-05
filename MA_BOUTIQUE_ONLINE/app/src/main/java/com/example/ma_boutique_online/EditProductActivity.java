package com.example.ma_boutique_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.ma_boutique_online.databinding.ActivityEditProductBinding;
import com.example.ma_boutique_online.entities.Product;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProductActivity extends AppCompatActivity implements View.OnClickListener{

    private ActivityEditProductBinding editProductBinding;
    private Product product;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editProductBinding = ActivityEditProductBinding.inflate(getLayoutInflater());
        View view = editProductBinding.getRoot();
        setContentView(view);

        editProductBinding.btnUpdate.setOnClickListener(this);

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");
        db = FirebaseFirestore.getInstance();
        editProductBinding.etName.setText(product.getName());
        editProductBinding.etDescription.setText(product.getDescription());
        editProductBinding.etStock.setText(String.valueOf(product.getStock()));
        editProductBinding.etPrice.setText(String.valueOf(product.getPrice()));
        editProductBinding.etCategory.setText(product.getCategory());
    }

    @Override
    public void onClick(View v) {
        Map<String, Object> dataProduct = new HashMap<>();
        dataProduct.put("name", editProductBinding.etName.getText().toString());
        dataProduct.put("description", editProductBinding.etDescription.getText().toString());
        dataProduct.put("stock", Integer.parseInt(editProductBinding.etStock.getText().toString()));
        dataProduct.put("price", Double.parseDouble(editProductBinding.etPrice.getText().toString()));
        dataProduct.put("category", editProductBinding.etCategory.getText().toString());
        if(v.getId() == editProductBinding.btnUpdate.getId()){
            db.collection("products")
                    .document(product.getId())
                    .update(dataProduct)

                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getApplicationContext(),
                                "Producto se Actulizo", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getApplicationContext(),
                                "Error al Actualizar el Producto", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}