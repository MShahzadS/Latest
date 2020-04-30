package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewExams extends AppCompatActivity {

    Button addExam;
    ListView examlist ;
    ArrayList<String> exams ;
    ArrayList<String> examkeys ;
    ArrayList<Exam> examObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exams);

        addExam = (Button) findViewById(R.id.btnaddexam);
        examlist = (ListView) findViewById(R.id.examsList) ;
        exams = new ArrayList<String>();
        examkeys = new ArrayList<String>();
        examObjects = new ArrayList<Exam>();


        addExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent answerintent = new Intent(ViewExams.this, AddExam.class);
                startActivity(answerintent);
            }
        });

        DatabaseReference examDb = FirebaseDatabase.getInstance().getReference("exam");
        examDb.addValueEventListener(examEventListener);

        examlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent addReslt = new Intent(ViewExams.this, AddResult.class);
                addReslt.putExtra("class", examObjects.get(position).getClassid()) ;
                addReslt.putExtra("exam",examkeys.get(position));
                startActivity(addReslt);
            }
        });
    }

    ValueEventListener examEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                examkeys.clear();
                exams.clear();
                examObjects.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Exam newExam = snapshot.getValue(Exam.class);
                    String ex = newExam.getExamid() + " " + newExam.getClassid() + " (" + newExam.getExamStart() +")" ;
                   exams.add(ex) ;
                   examObjects.add(newExam) ;
                   examkeys.add(snapshot.getKey()) ;

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter examAdapter = new ArrayAdapter(getApplicationContext(), R.layout.simplelistitem, exams);
                examlist.setAdapter(examAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
