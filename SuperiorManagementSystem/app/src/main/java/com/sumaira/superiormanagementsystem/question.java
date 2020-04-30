package com.sumaira.superiormanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class question extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtqueclass, edtquesection, edtqueteacher, wrtquestions;
    String class_id,section,questions,teacher;
    Button btnquestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

         edtqueclass = (EditText) findViewById(R.id.edtqueclass) ;
         edtquesection = (EditText)  findViewById(R.id.edtquesection);
         edtqueteacher = (EditText) findViewById(R.id.edtqueteacher);
         wrtquestions = (EditText) findViewById(R.id.wrtquestions);

        btnquestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                class_id = edtqueclass.getText().toString();
                section = edtquesection.getText().toString();
                teacher = edtqueteacher.getText().toString();
                questions = wrtquestions.getText().toString();

            }
        });




       // Toast.makeText(getApplicationContext(),edtqueclass + " " +  edtquesection + " " +edtqueteacher + " " + wrtquestions,Toast.LENGTH_SHORT).show();
    //mDatabase = FirebaseDatabase.getInstance().getReference("question");
    //String id = mDatabase.push().getKey();
    }



}

