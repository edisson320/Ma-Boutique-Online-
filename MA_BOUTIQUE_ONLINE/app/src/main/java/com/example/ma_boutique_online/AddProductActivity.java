package com.example.ma_boutique_online;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ma_boutique_online.databinding.ActivityAddProductBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class AddProductActivity extends AppCompatActivity {

   // private EditText etnombre, etdescripcion, etUnidad, etPrecio, etCategoria;
   // private Button btnRegistrar, btProducto;

    private ActivityAddProductBinding addProductBinding;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;
    private ActivityAddProductBinding mainBinding;


    FirebaseFirestore db;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        addProductBinding = ActivityAddProductBinding.inflate(getLayoutInflater());
        View v = addProductBinding.getRoot();

        setContentView(v);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // con esta linea hacemos el view binding asi la llamamos los datos
        mainBinding = ActivityAddProductBinding.inflate(getLayoutInflater());
        View view =mainBinding.getRoot();
        setContentView(view);
        createProduct();

    }



    public void selectImageFromGallery(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galleryLauncher =  registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public
                void onActivityResult(ActivityResult result) {

                    //Obtener el resultado de Seleccionar La imagen
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        Uri uri = data.getData();
                        if (uri != null) {
                            addProductBinding.ivProducto.setImageURI(uri);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Cancelar", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    //metodo para agregar informacion a firebase
    public  void createProduct(){
        Map<String, Object> productData = new HashMap<>();
        String producto = mainBinding.etnombre.getText().toString();//trae el valor de campo
        String descripcion = mainBinding.etdescripcion.getText().toString();
        String stock = mainBinding.etUnidad.getText().toString();//trae el valor de campo
        String precio = mainBinding.etPrecio.getText().toString();
        String categoria = mainBinding.etCategoria.getText().toString();//trae el valor de campo email
       // String imagen = mainBinding.ivProducto.getImageAlpha();

        productData.put("Producto",producto); // se envia la informacion
        productData.put("Descripcion",descripcion);
        productData.put("stock",stock);
        productData.put("Precio",precio);
        productData.put("Categoria",categoria);


        db.collection("producto").add(productData)
                .addOnSuccessListener(documentReference -> {

                    //Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    Toast.makeText(getApplicationContext(), "Producto agregado",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    //Log.w(TAG, "Error adding document", e);
                    Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                });

    }
    /*public void  realtimeData(){
        final DocumentReference docRef = db.collection("Productos")
                .document("Productos");
        docRef.addSnapshotListener((value, error) -> {
            if (error != null) {
                Log.w("Error data", "Listen failed.", error);
                return;
            }

            if (value != null && value.exists()) {
                Map<String, Object> data = value.getData();

                //Log.d("Snapshop", "Current data: " + data.getOrDefault("email","").toString());
                Log.d("Snapshop", "Current data: " + value.getData());
            } else {
                Log.d("Snapshop", "Current data: null");
            }
        });
    }*/
}