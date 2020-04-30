package com.sumaira.superiormanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class dashboardstd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboardstd);

        Button btnstdResult = (Button) findViewById(R.id.btnstdResult) ;
        Button btnstdtime = (Button) findViewById(R.id.btnstdtime) ;
        Button btnstdq = (Button) findViewById(R.id.btnstdq) ;
        Button btnstdevents = (Button) findViewById(R.id.btnstdevents) ;

        btnstdResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent viewResult = new Intent(dashboardstd.this, resultList.class) ;
                startActivity(viewResult);
                finish();
            }
        });

        btnstdtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent timetable = new Intent(dashboardstd.this, timetable.class) ;
                startActivity(timetable);
                finish();
            }
        });

        btnstdq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent question = new Intent(dashboardstd.this, showQuestions.class) ;
                startActivity(question);
                finish();
            }
        });

        btnstdevents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent events = new Intent(dashboardstd.this, events.class) ;
                startActivity(events);
                finish();
            }
        });

    }
}
