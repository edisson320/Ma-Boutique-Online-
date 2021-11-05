package com.example.ma_boutique_online;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public
class SplashActivity extends AppCompatActivity {

    @Override
    protected
    void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_splash);

        Animation animacion1 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_arriba);
        Animation animacion2 = AnimationUtils.loadAnimation(this,R.anim.desplazamiento_abajo);

        ImageView logo = findViewById(R.id.ivLogo);
        TextView titulo = findViewById(R.id.txtTitulo);

        logo.setAnimation(animacion1);
        titulo.setAnimation(animacion2);

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);

            startActivity(intent);
            finish();
        },5000);


    }
}