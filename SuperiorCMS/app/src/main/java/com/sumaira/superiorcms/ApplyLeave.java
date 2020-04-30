package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ApplyLeave extends AppCompatActivity {

    DatePicker edtDate ;
    EditText edtReason;
    EditText edtDays ;
    Button btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);

        edtDate = (DatePicker) findViewById(R.id.edtlvDate) ;
        edtReason = (EditText) findViewById(R.id.edtlvReason) ;
        edtDays = (EditText) findViewById(R.id.edtlvDays) ;
        btnApply = (Button) findViewById(R.id.btnApplyLeave) ;

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String day = Integer.toString(edtDate.getDayOfMonth());
                String month = Integer.toString((edtDate.getMonth() + 1));
                String year = Integer.toString(edtDate.getYear());

                String date= year + "-" + month + "-" + day ;
                String reason = edtReason.getText().toString();
                String days = edtReason.getText().toString();

                SharedPreferences shared = getSharedPreferences(Login.MyPREFERENCES, MODE_PRIVATE);
                String teacherid = (shared.getString(Login.Email, ""));
                TeacherLeave tcleave = new TeacherLeave(teacherid,date,reason,days) ;

                DatabaseReference teachDB = FirebaseDatabase.getInstance().getReference("teachersleave");

                String newkey =  teachDB.push().getKey() ;

                teachDB.child(newkey).setValue(tcleave).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Leave Applied", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Leave cannot be Applied. Try Again!", Toast.LENGTH_LONG).show();
                    }
                });



            }
        });
    }
}
