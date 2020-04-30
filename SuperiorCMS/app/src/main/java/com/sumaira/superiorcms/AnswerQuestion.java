package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class AnswerQuestion extends AppCompatActivity {

    EditText edtAnswer ;
    TextView txtQuestion ;
    Button btnSubmit ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer_question);

        btnSubmit = (Button) findViewById(R.id.btnSubmitAnswer) ;
        txtQuestion = (TextView) findViewById(R.id.txtQuestion) ;
        edtAnswer = (EditText) findViewById(R.id.edtAnswer);

        final String qst = getIntent().getExtras().getString("question").toString() ;

        txtQuestion.setText(qst);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query answerDB = FirebaseDatabase.getInstance().getReference("questions")
                                .orderByChild("question")
                                .equalTo(qst) ;

                answerDB.addValueEventListener(questEventListener) ;


            }
        });
    }

    ValueEventListener questEventListener = new ValueEventListener() {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                String qstkey = "" ;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    qstkey  = snapshot.getKey();
                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }
                if(qstkey.equals("") == false) {
                    DatabaseReference qstDb = FirebaseDatabase.getInstance().getReference("questions");

                    qstDb.child(qstkey).child("answer").setValue(edtAnswer.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Answer Submitted Successfully", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Answer cannot be Submitted", Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
