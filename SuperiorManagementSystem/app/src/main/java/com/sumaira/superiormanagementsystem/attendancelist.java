package com.sumaira.superiormanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class attendancelist extends AppCompatActivity {

    ListView listView;
    ArrayList<String> listviewitems ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendancelist);

        DatabaseReference attendancedB = FirebaseDatabase.getInstance().getReference("attendance");
        attendancedB.addValueEventListener(attendanceListner);


        listView = (ListView) findViewById(R.id.attendanceStdlist);
        listviewitems = new ArrayList<String>() ;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.simple, listviewitems);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String Templistview = listviewitems.toString();

                Intent intent = new Intent(attendancelist.this, attendance.class);

                intent.putExtra("Listviewclickvalue", Templistview);
                startActivity(intent);
            }
        });
    }


    ValueEventListener attendanceListner = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            listviewitems.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot attend : dataSnapshot.getChildren()) {
                    AttendanceS newattendance = attend.getValue(AttendanceS.class);

                    String attendlist = (newattendance).getClass_id() + "( " + newattendance.getStudent_id() + "( " + newattendance.getStatus() + ")";
                    Toast.makeText(getApplicationContext(), attendlist, Toast.LENGTH_SHORT).show();
                    listviewitems.add(attendlist);
                }
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    } ;
}