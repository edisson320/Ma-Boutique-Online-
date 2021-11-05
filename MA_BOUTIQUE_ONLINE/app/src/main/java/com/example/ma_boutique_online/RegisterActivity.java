package com.example.ma_boutique_online;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText txtUser;
    private EditText txtMail;
    private EditText txtPais;
    private EditText txtCiudad;
    private TextInputLayout txtPassword;
    private Button btnRegister;
    private TextView lblLogin;
    private CheckBox cbvendedor;
    private CheckBox cbusuario;
    private EditText etnombretienda;

    String userID;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    DatabaseReference mDatabase;

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        cbvendedor = findViewById(R.id.cbvendedor);
        cbusuario = findViewById(R.id.cbusuario);
        etnombretienda = findViewById(R.id.etnombreTienda);
        txtUser = findViewById(R.id.txtUser);
        txtMail = findViewById(R.id.txtMail);
        txtPais = findViewById(R.id.txtPais);
        txtCiudad = findViewById(R.id.txtCiudad);
        txtPassword = findViewById(R.id.txtPassword);
        lblLogin = findViewById(R.id.lblLogin);
        btnRegister = findViewById(R.id.btnRegister);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnRegister.setOnClickListener(view -> {
            createuser();
        });

        lblLogin.setOnClickListener(view -> openLoginActivity());


        cbvendedor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etnombretienda.setVisibility(View.VISIBLE);
            } else {
                etnombretienda.setVisibility(View.GONE);
            }
        });

    }//End onCreate

    //private fun validatePassword() : Boolean
    public  void openLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }// End openLoginActivity

    public boolean createuser() {

        boolean retorno = true;

        String name = txtUser.getText().toString();
        String mail = txtMail.getText().toString();
        String pais = txtPais.getText().toString();
        String usuario = cbusuario.getText().toString();
        String vendedor = cbvendedor.getText().toString();
        String tienda = etnombretienda.getText().toString();
        String ciudad = txtCiudad.getText().toString();
        String password = txtPassword.getEditText().getText().toString();
        Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.[0-9])(?=.[a-z])(?=.[A-Z])(?=.[@#$%^&+=*])(?=\\S+$).{8,}$");


        if (TextUtils.isEmpty(name)) {
            txtUser.setError("Ingrese un Nombre");
            txtUser.requestFocus();

        } else if (TextUtils.isEmpty(mail)) {
            txtMail.setError("Ingrese un Correo");
            txtMail.requestFocus();

        } else if (TextUtils.isEmpty(pais)) {
            txtPais.setError("Ingrese pais");
            txtPais.requestFocus();

        } if (TextUtils.isEmpty(ciudad)) {
            txtCiudad.setError("Ingrese Ciudad");
            txtCiudad.requestFocus();

        }
        else if (password.length()<8){
            txtPassword.setError("La Contraseña debe tener al menos 8 " +
                    "caracteres especiales, una mayuscula un numero y un caracter [@#$%^&+=*]");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(password).matches()) {
            txtPassword.setError("La contraseña debe contener una mayuscula, un caracter especial y debe ser igual o mas de 8 digitos");
            txtPassword.requestFocus();
            retorno = false;
        } else {
            txtPassword.setError(null);
            txtMail.setError(null);
            Toast.makeText(this, "Ingreso correctamente", Toast.LENGTH_SHORT).show();

        }
        registerUser();
        return retorno;
    }

    public void registerUser() {
        String name = txtUser.getText().toString();
        String email = txtMail.getText().toString();
        String pais = txtPais.getText().toString();
        String tienda = etnombretienda.getText().toString();
        String ciudad = txtCiudad.getText().toString();
        String password = txtPassword.getEditText().getText().toString();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                userID = mAuth.getCurrentUser().getUid();
                DocumentReference documentReference = db.collection("users").document(userID);

                Map<String, Object> user = new HashMap<>();
                user.put("Nombre", name);
                user.put("Correo", email);
                user.put("Pais", pais);
                user.put("Ciuda", ciudad);
                user.put("Contraseña", password);
                user.put("Tienda", tienda);

                documentReference.set(user).addOnSuccessListener(unused ->
                        Log.d("TAG", "onSuccess: Datos registrados" + userID));
                Toast.makeText(RegisterActivity.this, "Usuario Registrado", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, RegisterActivity.class));
            } else {
                Toast.makeText(RegisterActivity.this,
                        "No se pudo registrar este usuario" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }

        });

    }

}// End RegisterActivity