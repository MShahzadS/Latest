package com.sumaira.superiormanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class dashboardteach extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardteach);

        Button btnteachattend = (Button) findViewById(R.id.btnteachattend) ;
        Button btnteachtime = (Button) findViewById(R.id.btnteachtime) ;
        Button btnteachans = (Button) findViewById(R.id.btnteachans) ;
        Button btnteachlev = (Button) findViewById(R.id.btnteachlev) ;

        btnteachattend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent attendance = new Intent(dashboardteach.this, addclass.class) ;
                startActivity(attendance);
                finish();
            }
        });


        btnteachans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent answer = new Intent(dashboardteach.this, showteacherquestion.class) ;
                startActivity(answer);
                finish();
            }
        });

        btnteachtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timetable = new Intent(dashboardteach.this, timetable.class) ;
                startActivity(timetable);
                finish();
            }
        });

        btnteachlev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leave = new Intent(dashboardteach.this, leaves.class) ;
                startActivity(leave);
                finish();
            }
        });
    }
}
