package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class AttendanceList extends AppCompatActivity {

    Button btnAddStudent;
    ListView stdList ;
    ArrayList<String> students ;
    String classid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);



        classid= getIntent().getExtras().getString("classid");
        stdList = (ListView) findViewById(R.id.atstudentsList);

        students = new ArrayList<String>();

        Query stdDB = FirebaseDatabase.getInstance().getReference("students")
                .orderByChild("classid")
                .equalTo(classid) ;

        stdDB.addValueEventListener(stdEventListener) ;

        //Toast.makeText(getApplicationContext(), Integer.toString(students.size()),Toast.LENGTH_SHORT).show();



    }

    ValueEventListener stdEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                students.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Student newStudent = snapshot.getValue(Student.class);

                    students.add(newStudent.getRegNum()) ;

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                StudentListAdapater stdAdapter = new StudentListAdapater(getApplicationContext(),students, classid);
                stdList.setAdapter(stdAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
