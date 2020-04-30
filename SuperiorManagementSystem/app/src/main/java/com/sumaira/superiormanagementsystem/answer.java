package com.sumaira.superiormanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class answer extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtanswer;
    TextView txtanswer;
    Button btnanswer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        edtanswer = (EditText) findViewById(R.id.edtanswer);
        txtanswer = (TextView) findViewById(R.id.txtanswer);
        btnanswer = (Button) findViewById(R.id.btnanswer);

        btnanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String answer = edtanswer.getText().toString();
                String txtans = txtanswer.getText().toString();

                Toast.makeText(getApplicationContext(),"Data Saved Successfully" , Toast.LENGTH_SHORT).show();
                mDatabase = FirebaseDatabase.getInstance().getReference("answer");
                String id = mDatabase.push().getKey();
            }
        });
    }
}