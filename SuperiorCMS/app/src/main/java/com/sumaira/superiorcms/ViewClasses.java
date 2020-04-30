package com.sumaira.superiorcms;

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

public class ViewClasses extends AppCompatActivity {

    ArrayList<String> classes ;
    ArrayList<StdClass> classObjects ;
    ListView classList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_classes);

        Button addClass= (Button) findViewById(R.id.addClass) ;
        classList = (ListView) findViewById(R.id.classList) ;
        classes = new ArrayList<String>();
        classObjects = new ArrayList<StdClass>();
        addClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addclass = new Intent(ViewClasses.this, AddClass.class);
                startActivity(addclass);
            }
        });

        DatabaseReference classDb = FirebaseDatabase.getInstance().getReference("class");
        classDb.addValueEventListener(classEventListener);

        classList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(), classes.get(position), Toast.LENGTH_SHORT).show();
                Intent atndnc =  new Intent(ViewClasses.this, AttendanceList.class);
                atndnc.putExtra("classid", classObjects.get(position).getClassid()) ;
                atndnc.putExtra("classname", classObjects.get(position).getClassname()) ;
                startActivity(atndnc);
            }
        });
    }

    ValueEventListener classEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                classObjects.clear();
                classes.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StdClass newClass = snapshot.getValue(StdClass.class);
                    String name= newClass.getClassid() + " " + newClass.getClassname() + " " + newClass.getClasssection();
                    classes.add(name) ;
                    classObjects.add(newClass) ;

                  //  Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();
                }

                ArrayAdapter classAdapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,classes);
                classList.setAdapter(classAdapter);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };


}
