package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Driver;

import org.w3c.dom.Text;

public class DriverDetails extends AppCompatActivity {

    TextView txtDriverName, txtDriverContact, txtDriverAddress ;
    String driverid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_details);

        txtDriverName = (TextView) findViewById(R.id.txtDriverName);
        txtDriverContact = (TextView) findViewById(R.id.txtDriverContact);
        txtDriverAddress = (TextView) findViewById(R.id.txtDriverAddress);

        driverid = getIntent().getStringExtra("driver") ;

        Query driverDB = FirebaseDatabase.getInstance().getReference("Driver")
                .orderByKey()
                .equalTo(driverid);
        driverDB.addListenerForSingleValueEvent(getDriverData);

    }

    public void deleteDriver(View view){
        FirebaseDatabase.getInstance().getReference("Driver")
                .child(driverid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Driver Deleted Successfully");
                    Intent i = new Intent(DriverDetails.this, DriverActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Driver could not be deleted successfully");
                }
            }
        });
    }

    ValueEventListener getDriverData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {

                   Driver newEmp = empData.getValue(Driver.class);

                    txtDriverName.setText(newEmp.getName());
                    txtDriverContact.setText(newEmp.getContact());
                    txtDriverAddress.setText(newEmp.getAddress());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading Failed Try Again");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };
}
