package com.sumaira.superiorcms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        ImageView logoimg = (ImageView) findViewById(R.id.logoimg);
        logoimg.setImageResource(R.drawable.superior);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(login);
                finish();
            }
        }, 5000);
    }
}
