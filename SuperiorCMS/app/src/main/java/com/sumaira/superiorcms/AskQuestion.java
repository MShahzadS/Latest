package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AskQuestion extends AppCompatActivity {

    EditText edtQuestion ;
    Spinner teacherList ;
    Button btnAsk ;
    ArrayList<String> teachers ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_question);

        btnAsk = (Button) findViewById(R.id.btnAskQuestion) ;
        edtQuestion = (EditText) findViewById(R.id.edtQuestion) ;
        teacherList = (Spinner) findViewById(R.id.spnrTeacherList) ;
        teachers = new ArrayList<String>();
        DatabaseReference teachDB = FirebaseDatabase.getInstance().getReference("users");

        teachDB.addValueEventListener(teachEventListener);

        btnAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);


                String teacherid = teacherList.getSelectedItem().toString() ;
                String studentid = sp.getString(StudentLogin.regNum,"") ;
                String question =  edtQuestion.getText().toString() ;

                Question newquestion = new Question(teacherid,studentid,question,"") ;

                DatabaseReference questDB = FirebaseDatabase.getInstance().getReference("questions");
                String newkey = questDB.push().getKey() ;

                questDB.child(newkey).setValue(newquestion).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Question has been asked", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Question cannot be asked. Try Again!", Toast.LENGTH_LONG).show();
                    }
                }) ;
            }
        });
    }

    ValueEventListener teachEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {

                teachers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User newTeacher = snapshot.getValue(User.class);
                    String name= newTeacher.getEmail();
                    teachers.add(name) ;


                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, teachers);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                teacherList.setAdapter(spinnerArrayAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
