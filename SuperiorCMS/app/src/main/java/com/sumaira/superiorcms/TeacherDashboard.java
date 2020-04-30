package com.sumaira.superiorcms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TeacherDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dashboard);

        Button btnteachattend = (Button) findViewById(R.id.btnteachattend) ;
        Button btnteachtime = (Button) findViewById(R.id.btnteachtime) ;
        Button btnteachans = (Button) findViewById(R.id.btnteachans) ;
        Button btnReults  = (Button) findViewById(R.id.btnViewExams) ;
        Button btnteachlev = (Button) findViewById(R.id.btnteachlev) ;
        Button btnLogout = (Button)  findViewById(R.id.btnLogout) ;

        btnteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent attendance = new Intent(TeacherDashboard.this, ViewClasses.class) ;
                startActivity(attendance);

            }
        });
        btnteachtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent timetable = new Intent(TeacherDashboard.this, ViewTimeTable.class) ;
                startActivity(timetable);
            }
        });

        btnteachans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teachanswer = new Intent(TeacherDashboard.this, ViewQuestions.class) ;
                startActivity(teachanswer);
            }
        });

        btnteachlev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent teachleave = new Intent(TeacherDashboard.this, ApplyLeave.class) ;
                startActivity(teachleave);
            }
        });

        btnReults.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewexam = new Intent(TeacherDashboard.this, ViewExams.class) ;
                startActivity(viewexam);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences settings = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent events = new Intent(TeacherDashboard.this, Login.class) ;
                startActivity(events);
            }
        });
    }
}
