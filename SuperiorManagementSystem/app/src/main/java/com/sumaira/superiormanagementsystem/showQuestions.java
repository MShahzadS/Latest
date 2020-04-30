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

public class showQuestions extends AppCompatActivity {

    ArrayList<String> questionArray;
    ListView questionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_questions);

        questionArray = new ArrayList<String>();
        questionList = (ListView) findViewById(R.id.questionlist);

        DatabaseReference questiondB = FirebaseDatabase.getInstance().getReference("question");
        questiondB.addValueEventListener(questionListener);

    }

    ValueEventListener questionListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            questionArray.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot Question : dataSnapshot.getChildren()) {
                    QuestionS newQ = Question.getValue(QuestionS.class);

                    String Questions = newQ.getquestion() + "( " + newQ.getAnswer() + " ( " + newQ.getStudent() + "(" + newQ.getTeacher() + ")";
                    Toast.makeText(getApplicationContext(), Questions, Toast.LENGTH_SHORT).show();
                    questionArray.add(Questions);
                }

                final ArrayAdapter studentAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple, questionArray);
                questionList.setAdapter(studentAdapter);

                questionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent resultDetails = new Intent(showQuestions.this, question.class);
                        startActivity(resultDetails);
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