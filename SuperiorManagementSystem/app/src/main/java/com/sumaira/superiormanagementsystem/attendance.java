package com.sumaira.superiormanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class attendance extends AppCompatActivity {

    ArrayList<String> students ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        students = new ArrayList<String>();

        for(int i=1;i<10;i++)
            students.add("Student " + i ) ;

        ListView atdStudentList = (ListView) findViewById(R.id.atStudentList) ;

         AtStdAdapter myadapter = new AtStdAdapter(this, students) ;
        //ArrayAdapter<String> myadapter = new ArrayAdapter<String>(this, R.layout.simple, students) ;
        atdStudentList.setAdapter(myadapter);

    }


}
