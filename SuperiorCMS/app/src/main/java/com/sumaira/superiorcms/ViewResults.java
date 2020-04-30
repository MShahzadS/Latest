package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewResults extends AppCompatActivity {

    ListView resultsList ;
    ArrayList<String> results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_results);


        results = new ArrayList<String>();
        TextView stdname = (TextView) findViewById(R.id.txtstdname) ;
        TextView examName = (TextView) findViewById(R.id.txtexamname) ;
        resultsList = (ListView) findViewById(R.id.stdsubjectslist)  ;


        String examkey = getIntent().getStringExtra("examkey");
        String examname = getIntent().getStringExtra("exam");

        SharedPreferences myprefs = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
        String studentname = myprefs.getString(StudentLogin.Name,"") ;
        String classid = myprefs.getString(StudentLogin.Classid,"") ;

     //   Toast.makeText(getApplicationContext(),examkey,Toast.LENGTH_SHORT).show();

        stdname.setText(studentname);
        examName.setText(examname) ;

        Query getResult = FirebaseDatabase.getInstance().getReference("results")
                .orderByChild("examid")
                .equalTo(examkey);

        getResult.addValueEventListener(examEventListener);
    }

    ValueEventListener examEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                results.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Result newRes = snapshot.getValue(Result.class);

                    String reslt =  newRes.getSubjectid() +  "                        " +newRes.getObtainedMarks() +
                            " / " + newRes.getTotalMarks() ;

                    results.add(reslt) ;

                    //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter examAdapter = new ArrayAdapter(getApplicationContext(), R.layout.simplelistitem, results);
                resultsList.setAdapter(examAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
