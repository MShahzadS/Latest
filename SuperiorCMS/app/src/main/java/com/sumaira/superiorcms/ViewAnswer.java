package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class ViewAnswer extends AppCompatActivity {

    TextView txtQuestion , txtAnswer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_answer);

        txtQuestion = (TextView) findViewById(R.id.txtQuestion) ;
        txtAnswer = (TextView) findViewById(R.id.txtAnswer) ;

        final String qst = getIntent().getExtras().getString("question").toString() ;
        final String qstid = getIntent().getExtras().getString("questionid").toString() ;
        txtQuestion.setText(qst);


        Query answerDB = FirebaseDatabase.getInstance().getReference("questions")
                .orderByKey()
                .equalTo(qstid) ;
        answerDB.addValueEventListener(questEventListener) ;

    }

    ValueEventListener questEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String answer = "" ;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    answer  = snapshot.getValue(Question.class).getAnswer();
                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }
                txtAnswer.setText(answer);
            }else
            {
                txtAnswer.setText("Teacher has not yet replied");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
