package com.sumaira.superiormanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ImageView logoimg =(ImageView) findViewById(R.id.logoimg);


        logoimg.setImageResource(R.drawable.superior);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent login = new Intent(splashscreen.this, login.class) ;
                startActivity(login);
                finish();
            }
        },5000);
    }
}
