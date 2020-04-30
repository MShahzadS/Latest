package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class StudentAttendance extends AppCompatActivity {

    ArrayList<String> attendacs ;
    ListView attendanceList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        SharedPreferences myprefs = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
        String studentid = myprefs.getString(StudentLogin.regNum, "") ;
        attendacs = new ArrayList<String>();
        attendanceList = (ListView) findViewById(R.id.stdattlist) ;

        Query stdattendDB = FirebaseDatabase.getInstance().getReference("attendance")
        .orderByChild("stdid")
        .equalTo(studentid) ;

        stdattendDB.addValueEventListener(attendanceLsitener) ;
    }

    ValueEventListener attendanceLsitener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                attendacs.clear();
                for(DataSnapshot attendance:dataSnapshot.getChildren())
                {
                    Attendance newatt = attendance.getValue(Attendance.class);

                    attendacs.add(newatt.getaDate() + "            ( " + newatt.getStatus() + " )") ;
                }

                ArrayAdapter attendanceadapter = new ArrayAdapter(getApplicationContext(),R.layout.simplelistitem,attendacs);
                attendanceList.setAdapter(attendanceadapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
