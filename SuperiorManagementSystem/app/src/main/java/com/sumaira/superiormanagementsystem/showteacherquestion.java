package com.sumaira.superiormanagementsystem;

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
import java.util.List;

public class showteacherquestion extends AppCompatActivity {

    ArrayList<String> questionArray;
    ListView questionList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showteacherquestion);

        questionArray = new ArrayList<String>();
        questionList = (ListView) findViewById(R.id.teacherQlist);

        DatabaseReference questionteachdB = FirebaseDatabase.getInstance().getReference("question");
        questionteachdB.addValueEventListener(questionListener);

    }

    ValueEventListener questionListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            questionArray.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot Qlist : dataSnapshot.getChildren()) {
                    QuestionT newQ = Qlist.getValue(QuestionT.class);

                    String Questions = newQ.getquestion() + "( " + newQ.getAnswer() + " ( " + newQ.getStudent() + "(" + newQ.getTeacher() + ")";
                    Toast.makeText(getApplicationContext(), Questions, Toast.LENGTH_SHORT).show();
                    questionArray.add(Questions);
                }

                final ArrayAdapter studentAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple, questionArray);
                questionList.setAdapter(studentAdapter);

                questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent Qdetails = new Intent(showteacherquestion.this, answer.class);
                        startActivity(Qdetails);
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Problem in Loading Data", Toast.LENGTH_LONG).show();
        }
    };
}



