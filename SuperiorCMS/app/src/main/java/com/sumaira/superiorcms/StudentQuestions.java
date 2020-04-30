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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentQuestions extends AppCompatActivity {

    ListView questionsList ;
    Button btnAskQuest ;
    ArrayList<String> questions ;
    ArrayList<String> keys ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_questions);

        questionsList = (ListView) findViewById(R.id.questionslist);
        btnAskQuest = (Button) findViewById(R.id.btnAddQuestion);
        questions = new ArrayList<String>();
        keys = new ArrayList<String>() ;
        SharedPreferences sp = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
        String studentid = sp.getString(StudentLogin.regNum,"") ;
        Query questDB = FirebaseDatabase.getInstance().getReference("questions")
                .orderByChild("studendid")
                .equalTo(studentid) ;
        questDB.addValueEventListener(questEventListener);

        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent answerintent = new Intent(StudentQuestions.this, ViewAnswer.class);
                answerintent.putExtra("question", questions.get(position));
                answerintent.putExtra("questionid", keys.get(position));
                startActivity(answerintent);
            }
        });

        btnAskQuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addQuestion = new Intent(StudentQuestions.this, AskQuestion.class);
                startActivity(addQuestion);
            }
        });
    }

    ValueEventListener questEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                questions.clear();
                keys.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String newQues = snapshot.getValue(Question.class).getQuestion();
                    questions.add(newQues) ;
                    keys.add(snapshot.getKey());

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter classAdapter = new ArrayAdapter(getApplicationContext(),R.layout.simplelistitem,questions);
                questionsList.setAdapter(classAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
