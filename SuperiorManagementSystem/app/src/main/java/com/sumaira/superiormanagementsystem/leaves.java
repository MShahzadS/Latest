package com.sumaira.superiormanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class leaves extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtlevteach, edtlevreason;
    CalendarView levcalender;
    RadioButton levshort, levfull;
    Button btnsubmit;
    String date,teach, status,reason, shrt, full, teacher_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaves);
        btnsubmit = (Button) findViewById(R.id.btnsubmit);
        edtlevteach = (EditText) findViewById(R.id.edtlevteach);
        edtlevreason = (EditText) findViewById(R.id.edtlevreason);
        levcalender = (CalendarView) findViewById(R.id.levcalendar);
        levcalender.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int day) {
                date = day + " / " + month + " / " + year;

                RadioButton levshort = (RadioButton) findViewById(R.id.fulllv);
                RadioButton levfull = (RadioButton) findViewById(R.id.shrtlv);
            }
        });
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                teach = edtlevteach.getText().toString();
                reason = edtlevreason.getText().toString();
                shrt = levshort.getText().toString();
                full = levfull.getText().toString();


                Toast.makeText(getApplicationContext(),"Data Saved Successfully" , Toast.LENGTH_SHORT).show();
                mDatabase = FirebaseDatabase.getInstance().getReference("leaves");
                String id = mDatabase.push().getKey();

                Leave myleave = new Leave(reason, date, status,teacher_id);
                mDatabase.child("leaves").child(id).setValue(myleave, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Sorry! Data cannot be saved", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
