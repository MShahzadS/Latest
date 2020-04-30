package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewQuestions extends AppCompatActivity {

    ListView questionsList ;
    ArrayList<String> questions ;
    ArrayList<String> keys;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        questionsList = (ListView) findViewById(R.id.questionslist);
        questions = new ArrayList<String>();
        keys = new ArrayList<String>();
        DatabaseReference questDB = FirebaseDatabase.getInstance().getReference("questions");
        questDB.addValueEventListener(questEventListener);

        questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent answerintent = new Intent(ViewQuestions.this, AnswerQuestion.class);
                    answerintent.putExtra("question", questions.get(position));
                    answerintent.putExtra("questionid", keys.get(position));
                    startActivity(answerintent);
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
                    Question newQues = snapshot.getValue(Question.class);
                    keys.add(snapshot.getKey()) ;
                    questions.add(newQues.getQuestion()) ;

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
