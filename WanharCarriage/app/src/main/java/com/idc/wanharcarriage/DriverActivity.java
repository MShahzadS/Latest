package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.Driver;

import java.util.ArrayList;
import java.util.Random;

public class DriverActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NewDriverDialog.DriverDialogListener {

    ListView driverList;
    ArrayList<DataModel> driverArray;
    ArrayList<String> driverIDs;
    EditText edtSearchDriver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        driverIDs = new ArrayList<String>();
        driverArray = new ArrayList<DataModel>();
        driverList = (ListView) findViewById(R.id.driverList);
        edtSearchDriver = (EditText) findViewById(R.id.edtSearchDriver);

        DatabaseReference driverDB = FirebaseDatabase.getInstance().getReference("Driver");
        driverDB.addValueEventListener(getDriverData);
    }

    public void addDriver(View view) {
        NewDriverDialog driverDialog = new NewDriverDialog();
        driverDialog.show(getSupportFragmentManager(), "Add Driver");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(DriverActivity.this, DriverDetails.class);
        i.putExtra("driver", driverIDs.get(position));
        startActivity(i);
    }

    @Override
    public void saveDriver(Driver driver,String driverid, String message) {
        Functions.MyFunctions.displayMessage(getApplicationContext(), message);
    }

    ValueEventListener getDriverData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            driverArray.clear();
            driverIDs.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot empData : dataSnapshot.getChildren()) {
                    Driver newDriver = empData.getValue(Driver.class);
                    String sname = edtSearchDriver.getText().toString().toLowerCase();
                    if (!sname.isEmpty()) {

                        if (newDriver.getName().toLowerCase().startsWith(sname)) {
                            driverIDs.add(empData.getKey());
                            DataModel custModel = new DataModel(newDriver.getName(), newDriver.getContact());
                            driverArray.add(custModel);
                        }
                    } else {
                        driverIDs.add(empData.getKey());
                        DataModel custModel = new DataModel(newDriver.getName(), newDriver.getContact());
                        driverArray.add(custModel);
                    }
                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(), "No Driver Registered");
            }

            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(), driverArray);
            driverList.setOnItemClickListener(DriverActivity.this);
            driverList.setAdapter(myCustomListAdapter);
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(), "Process Cancelled");
        }
    };
}
