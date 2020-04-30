package com.sumaira.superiorcms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btnAbout, btnTeacher, btnStudent ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAbout = (Button) findViewById(R.id.btnAboutus) ;
        btnTeacher = (Button) findViewById(R.id.btnTeacherLogin) ;
        btnStudent = (Button) findViewById(R.id.btnStudentLogin) ;

        btnAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendance = new Intent(MainActivity.this, Aboutus.class) ;
                startActivity(attendance);

            }
        });

        btnTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendance = new Intent(MainActivity.this, Login.class) ;
                startActivity(attendance);

            }
        });

        btnStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent attendance = new Intent(MainActivity.this, StudentLogin.class) ;
                startActivity(attendance);

            }
        });
    }
}
