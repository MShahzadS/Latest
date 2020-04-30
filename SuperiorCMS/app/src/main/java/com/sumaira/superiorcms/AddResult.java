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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddResult extends AppCompatActivity {

    ListView stdmarksList ;
    EditText subjectName ;
    EditText edtTotalMarks;
    Button btnSaveResult ;
    ResultAdapter examAdapter ;
    ArrayList<String> subjects ;
    ArrayList<String> students;
    ArrayList<String> marks ;
    String myclassid ;
    String totalmarks;
    String subject ;
    String exam ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_result);

        stdmarksList = (ListView) findViewById(R.id.stdmarksList) ;
        subjectName = (EditText) findViewById(R.id.edtSubjectName) ;
        edtTotalMarks = (EditText) findViewById(R.id.edtTotalMarks) ;
        btnSaveResult = (Button) findViewById(R.id.btnSaveResult) ;

        subjects = new ArrayList<String>();
        students = new ArrayList<String>();
        marks = new ArrayList<String>();

        myclassid = getIntent().getStringExtra("class") ;
        exam = getIntent().getStringExtra("exam");

        Query getStudents = FirebaseDatabase.getInstance().getReference("students")
                .orderByChild("classid")
                .equalTo(myclassid);

        getStudents.addValueEventListener(stdEventListener) ;

    }

    ValueEventListener stdEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                students.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student newstd = snapshot.getValue(Student.class);
                    String reg = newstd.getRegNum() ;
                    students.add(reg) ;

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                examAdapter = new ResultAdapter(getApplicationContext(), students);
                stdmarksList.setAdapter(examAdapter);

                btnSaveResult.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        totalmarks = edtTotalMarks.getText().toString() ;
                        subject = subjectName.getText().toString() ;
                        marks = examAdapter.marks ;

                        SharedPreferences sharedPreferences = getSharedPreferences(Login.MyPREFERENCES, Context.MODE_PRIVATE);
                        String teacherid = sharedPreferences.getString(Login.Email,"");
                        for(int i = 0;i<marks.size();i++)
                        {
                        Result newres =  new Result(teacherid, students.get(i),
                                subject,exam,marks.get(i),totalmarks) ;

                            DatabaseReference resultDB = FirebaseDatabase.getInstance().getReference("results");
                            String newkey = resultDB.push().getKey();

                            resultDB.child(newkey).setValue(newres).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                   // Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "There was a problem saving ", Toast.LENGTH_LONG).show();
                                }
                            });

                        }



                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
