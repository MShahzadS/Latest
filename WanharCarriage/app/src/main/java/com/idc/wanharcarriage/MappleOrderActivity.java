package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.MappleOrder;
import com.idc.wanharcarriage.classes.Vehicle;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;


public class MappleOrderActivity extends AppCompatActivity implements NewDriverDialog.DriverDialogListener,
        NewVehicleDialog.VehicleDialogListener {

    EditText edtOrderDate, edtweightTons, edtWeightBags;
    ImageView btnAddDriver, btnAddVehicle ;
    SearchableSpinner vehicleSpinner, driverSpinner;
    ArrayList<String> drivers, driverIDs;
    ArrayList<String> vehicles, vehicleIDs;
    String vehicle, driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapple_order);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        readViews();
        addActions();
        loadVehicles();
        loadDrivers();
    }

    public void readViews() {
        edtOrderDate = (EditText) findViewById(R.id.edtOrderDate) ;
        edtweightTons = (EditText) findViewById(R.id.edttonweight) ;
        edtWeightBags = (EditText) findViewById(R.id.edtbagweight) ;
        btnAddDriver = (ImageView) findViewById(R.id.btnAddDriver);
        btnAddVehicle = (ImageView) findViewById(R.id.btnAddVehicle);
        vehicleSpinner = (SearchableSpinner) findViewById(R.id.vehicleSpinner) ;
        driverSpinner = (SearchableSpinner) findViewById(R.id.driverSpinner);
        drivers = new ArrayList<String>();
        driverIDs = new ArrayList<String>();
        vehicles = new ArrayList<String>();
        vehicleIDs = new ArrayList<String>();
    }

    public void addActions() {
        btnAddVehicle.setOnClickListener(AddVehicle);
        btnAddDriver.setOnClickListener(AddDriver);
        edtweightTons.addTextChangedListener(covertWeight);
        MyDatePicker mydatepicker = new MyDatePicker(MappleOrderActivity.this,edtOrderDate);
        driverSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              driver = driverIDs.get(position) ;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        vehicleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                vehicle = vehicleIDs.get(position) ;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void loadDrivers() {
        DatabaseReference driverDB = FirebaseDatabase.getInstance().getReference("Driver");
        driverDB.addValueEventListener(getDriverData);
    }

    public void loadVehicles() {
        DatabaseReference vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");
        vehicleDB.addValueEventListener(getVehicleData);
    }

    View.OnClickListener AddVehicle =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            NewVehicleDialog newVehicleDialog = new NewVehicleDialog();
            newVehicleDialog.show(getSupportFragmentManager(), "Add Vehicle");
        }
    };

    View.OnClickListener AddDriver = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
                NewDriverDialog newDriverDialog = new NewDriverDialog();
                newDriverDialog.show(getSupportFragmentManager(), "Add Driver");
        }
    };

    @Override
    public void saveDriver(Driver d,String driverid, String message) {

        driverSpinner.setSelection(drivers.size()-1);
        driver = driverid;
        //Functions.MyFunctions.displayMessage(btnAddDriver,message);
    }

    @Override
    public void saveVehicle(Vehicle v,String vehicleid, String message) {

        vehicleSpinner.setSelection(vehicles.size()-1);
        vehicle = vehicleid;

    }

    ValueEventListener getVehicleData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            vehicles.clear();
            vehicleIDs.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot vehicleData : dataSnapshot.getChildren()) {
                    Vehicle newVehicle = vehicleData.getValue(Vehicle.class);
                    vehicles.add(newVehicle.getRegistrationNumber());
                    vehicleIDs.add(vehicleData.getKey());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"There was Problem Loading Vehicles. Try Again");
            }

            ArrayAdapter myAdapter = new ArrayAdapter(MappleOrderActivity.this,android.R.layout.simple_list_item_1,vehicles);
            vehicleSpinner.setAdapter(myAdapter);
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Loading Vehicles has been cancelled. Try Again");
        }
    };

    ValueEventListener getDriverData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            drivers.clear();
            driverIDs.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot driverData : dataSnapshot.getChildren()) {
                    Driver newDriver = driverData.getValue(Driver.class);
                    drivers.add(newDriver.getName());
                    driverIDs.add(driverData.getKey());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"There was Problem Loading Drivers. Try Again");
            }

            ArrayAdapter myAdapter = new ArrayAdapter(MappleOrderActivity.this,android.R.layout.simple_list_item_1,drivers);
            driverSpinner.setAdapter(myAdapter);
            Functions.MyFunctions.dismissDialog();
        }
        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Loading Drivers has been cancelled. Try Again");
        }
    };

    TextWatcher covertWeight = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                int val = Integer.valueOf(s.toString()) * 20;
                edtWeightBags.setText(String.valueOf(val));
            } catch (Exception e) {
                edtWeightBags.setText(String.valueOf("0"));
                e.printStackTrace();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    } ;

    public void createMappleOrder(View view) {

        final EditText edtBuiltyNumber = (EditText) findViewById(R.id.edtBuiltyNumber) ;
        EditText edtFinalDestination = (EditText) findViewById(R.id.edtFinalDestination) ;
        Spinner vehicleStatusSpinner = (Spinner) findViewById(R.id.vehicleStatus) ;

        String orderDate = edtOrderDate.getText().toString();
        String builtyNumber = edtBuiltyNumber.getText().toString();
        String weight = edtweightTons.getText().toString();
        String finaldestination = edtFinalDestination.getText().toString();
        String vehicleStatus = vehicleStatusSpinner.getSelectedItem().toString();

        MappleOrder newOrder = new MappleOrder(orderDate,builtyNumber,vehicle,driver,
                                                weight,finaldestination,vehicleStatus);

        DatabaseReference mappleDB = FirebaseDatabase.getInstance().getReference("MappleOrder");

        String newID = mappleDB.push().getKey();

        mappleDB.child(newID).setValue(newOrder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Created Successfully");
                    Intent i = new Intent(MappleOrderActivity.this, MappleLeafActivity.class) ;
                    startActivity(i);
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order could not be created");
                }
            }
        });
    }
}

