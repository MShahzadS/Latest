package com.sumaira.superiorcms;

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

public class AddClass extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtClass, edtsection, edtName;
    String class_id, section, name;
    Button btnclassadd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

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

                StdClass newclass= new StdClass(class_id,name,section);

                mDatabase = FirebaseDatabase.getInstance().getReference("class") ;

                String newid = mDatabase.push().getKey() ;

                mDatabase.child(newid).setValue(newclass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Data Saved Successfully" , Toast.LENGTH_SHORT).show();
                        Intent classList =  new Intent(AddClass.this, ViewClasses.class);
                        startActivity(classList);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Data cannot be saved" , Toast.LENGTH_SHORT).show();
                    }
                }) ;
            }
        });
    }
}
