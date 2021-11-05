package com.example.ma_boutique_online.Product_adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ma_boutique_online.EditProductActivity;
import com.example.ma_boutique_online.databinding.ProductItemBinding;
import com.example.ma_boutique_online.entities.Product;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private Context context;
    private ProductItemBinding productItemBinding;
    private ArrayList<Product> productArrayList;
    private FirebaseFirestore db;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, FirebaseFirestore db) {
        this.context = context;
        this.productArrayList = productArrayList;
        this.db = db;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        productItemBinding = productItemBinding.inflate(LayoutInflater.from(context));
        return new ProductViewHolder(productItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductViewHolder holder, int position) {
        Product product = productArrayList.get(position);
        holder.itemBinding.tvName.setText(product.getName());
        holder.itemBinding.tvDescription.setText(product.getDescription());
        holder.itemBinding.tvStock.setText(String.valueOf(product.getStock()));
        holder.itemBinding.tvPrice.setText(String.valueOf(product.getPrice()));
        holder.itemBinding.tvCategory.setText(product.getCategory());

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setPositiveButton("Aceptar", (dialog, which) ->
                db.collection("products").document(product.getId()).delete()
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(context, "Data delete", Toast.LENGTH_SHORT).show();
                            productArrayList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to delete item", Toast.LENGTH_SHORT).show();
                            }
                        }));
        builder.setNegativeButton("Cancelar", (dialog, which) -> {

        });
        holder.itemBinding.btnDelete.setOnClickListener(v -> {
            builder.setMessage("Esta seguro que quiere eliminar el producto?");
            builder.create().show();
        });
        holder.itemBinding.btnEdit.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditProductActivity.class);
            intent.putExtra("product", product);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productArrayList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductItemBinding itemBinding;

        public ProductViewHolder(@NonNull ProductItemBinding itemBinding) {
            super(itemBinding.getRoot());
            this.itemBinding = itemBinding;
        }
    }
}