package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentExams extends AppCompatActivity {

    ListView examlist ;
    ArrayList<String> exams ;
    ArrayList<String> examkeys ;
    ArrayList<Exam> examObjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_exams);

        examlist = (ListView) findViewById(R.id.examsList) ;
        exams = new ArrayList<String>();
        examkeys = new ArrayList<String>();
        examObjects = new ArrayList<Exam>();


        SharedPreferences myprefs = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
        String classid= myprefs.getString(StudentLogin.Classid,"") ;
        Query examDb = FirebaseDatabase.getInstance().getReference("exam")
                .orderByChild("classid")
                .equalTo(classid) ;
        examDb.addValueEventListener(examEventListener);

        examlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent addReslt = new Intent(StudentExams.this, ViewResults.class);
                addReslt.putExtra("examkey",examkeys.get(position));
                addReslt.putExtra("exam",examObjects.get(position).getExamid());
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
                    String ex = newExam.getExamid() +"(" + newExam.getExamStart() +" to " + newExam.getExamEnd() + " )" ;
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
