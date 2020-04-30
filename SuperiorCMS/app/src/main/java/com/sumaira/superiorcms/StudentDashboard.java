package com.sumaira.superiorcms;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StudentDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dashboard);


        Button btnstdResult = (Button) findViewById(R.id.btnstdResult) ;
        Button btnstdtime = (Button) findViewById(R.id.btnstdtime) ;
        Button btnstdq = (Button) findViewById(R.id.btnstdq) ;
        Button btnstdattendance = (Button) findViewById(R.id.btnstdAttendance) ;
        Button btnComplaint = (Button) findViewById(R.id.btnComplaint) ;
        Button btnLogout = (Button) findViewById(R.id.btnLogout) ;

        btnstdResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewResult = new Intent(StudentDashboard.this, StudentExams.class) ;
                startActivity(viewResult);
            }
        });

        btnstdtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timetable = new Intent(StudentDashboard.this, StudentTimetable.class) ;
                startActivity(timetable);
            }
        });

        btnstdq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent question = new Intent(StudentDashboard.this, StudentQuestions.class) ;
                startActivity(question);
            }
        });

        btnstdattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent events = new Intent(StudentDashboard.this, StudentAttendance.class) ;
                startActivity(events);
            }
        });

        btnComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent events = new Intent(StudentDashboard.this, AddComplaint.class) ;
                startActivity(events);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPreferences settings = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent events = new Intent(StudentDashboard.this, StudentLogin.class) ;
                startActivity(events);
            }
        });
    }
}
