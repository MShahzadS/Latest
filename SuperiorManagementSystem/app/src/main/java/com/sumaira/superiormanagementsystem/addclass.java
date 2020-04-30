package com.sumaira.superiormanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.jar.Attributes;

public class addclass extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtClass, edtsection, edtName;
    String class_id, section, name;
    Button btnclassadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addclass);

        edtClass = (EditText) findViewById(R.id.edtClass);
        edtsection = (EditText) findViewById(R.id.edtsection);
        edtName = (EditText) findViewById(R.id.edtName);
        btnclassadd = (Button) findViewById(R.id.btnclassadd);


        btnclassadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edtName.getText().toString();
                class_id = edtClass.getText().toString();
                section = edtsection.getText().toString();

                btnclassadd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent attendance = new Intent(addclass.this, attendance.class) ;
                        startActivity(attendance);
                        finish();
                    }
                });

                ClassAdd a =new ClassAdd(class_id,section,name);
                mDatabase = FirebaseDatabase.getInstance().getReference("classes") ;

                String newid = mDatabase.push().getKey() ;

                mDatabase.child(newid).setValue(a).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Data Saved Successfully" , Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                }) ;



            }

        });
    }
}