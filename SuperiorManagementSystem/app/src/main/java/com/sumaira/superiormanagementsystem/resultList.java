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
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class resultList extends AppCompatActivity {

    ArrayList<String> termsArray ;
    ListView termsList ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_list);

        termsArray = new ArrayList<String>() ;
        termsList = (ListView) findViewById(R.id.termsList) ;

        DatabaseReference resultTermdB = FirebaseDatabase.getInstance().getReference("resultTerm");
        resultTermdB.addValueEventListener(termListener) ;

    }

    ValueEventListener termListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            termsArray.clear() ;
            if(dataSnapshot.exists())
            {
                for(DataSnapshot terms: dataSnapshot.getChildren())
                {
                    ResultTerm newTerm = terms.getValue(ResultTerm.class);

                    String term = newTerm.getTermName() + "( " + newTerm.getTermMonth() + ")";
                    Toast.makeText(getApplicationContext(), term , Toast.LENGTH_SHORT).show();
                    termsArray.add(term) ;
                }

                final ArrayAdapter studentAdapter = new ArrayAdapter<String>(getApplicationContext(),R.layout.simple,termsArray);
                termsList.setAdapter(studentAdapter);

                termsList.setOnItemClickListener(new  AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent resultDetails = new Intent(resultList.this, viewresult.class) ;
                        startActivity(resultDetails);
                    }
                });
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "Problem in Loading Data", Toast.LENGTH_LONG).show();
        }
    };


}