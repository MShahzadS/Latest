package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddExam extends AppCompatActivity {

    EditText edtExamid, edtExamStart, edtExamEnd ;
    Button btnSaveExam ;
    Spinner classlist ;
    ArrayList<String> classes ;
    ArrayList<StdClass> classObjects ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exam);


        edtExamid = (EditText) findViewById(R.id.edtexamName) ;
        edtExamStart = (EditText)findViewById(R.id.edtStartDate) ;
        edtExamEnd = (EditText) findViewById(R.id.edtEndDate) ;
        btnSaveExam = (Button) findViewById(R.id.btnSaveExam) ;

        classlist = (Spinner)  findViewById(R.id.spnrclasslist) ;
        classes = new ArrayList<String>() ;
        classObjects = new ArrayList<StdClass>();

        final DatabaseReference classDb = FirebaseDatabase.getInstance().getReference("class") ;
        classDb.addValueEventListener(classEventListener);

        btnSaveExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edtExamid.getText().toString() ;
                String startdate = edtExamStart.getText().toString();
                String examend = edtExamEnd.getText().toString() ;
                String classid = classlist.getSelectedItem().toString().split(" ")[0] ;

                Exam newex = new Exam(name,classid,startdate,examend);

                DatabaseReference examDb = FirebaseDatabase.getInstance().getReference("exam");

                String newkey = examDb.push().getKey() ;

                examDb.child(newkey).setValue(newex).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Exam Saved Successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Exam not Saved", Toast.LENGTH_LONG).show();
                    }
                }) ;
            }
        });
    }

    ValueEventListener classEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                classObjects.clear();
                classes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StdClass newClass = snapshot.getValue(StdClass.class);
                    String name= newClass.getClassid() + " " + newClass.getClassname() + " " + newClass.getClasssection();
                    classes.add(name) ;
                    classObjects.add(newClass) ;

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, classes);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down vieww
                classlist.setAdapter(spinnerArrayAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
