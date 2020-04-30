package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.idc.wanharcarriage.classes.Employee;
import com.idc.wanharcarriage.classes.Vehicle;

import java.util.ArrayList;
import java.util.Random;

public class VehicleActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NewVehicleDialog.VehicleDialogListener {

    ListView vehicleList ;
    ArrayList<DataModel> vehicleArray ;
    ArrayList<String> vehicleIDs;
    EditText edtSearchVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        vehicleIDs = new ArrayList<String>();
        vehicleList = (ListView)  findViewById(R.id.vehicleList) ;
        edtSearchVehicle = (EditText) findViewById(R.id.edtSearchVehicle);
        vehicleArray = new ArrayList<DataModel>();

        DatabaseReference vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");
        vehicleDB.addValueEventListener(getVehicleData);
        edtSearchVehicle.addTextChangedListener(searchVehicle);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(VehicleActivity.this, VehicleDetails.class) ;
        i.putExtra("vehicle", vehicleIDs.get(position)) ;
        startActivity(i);
    }

    public void addVehicle(View view) {
        NewVehicleDialog vehicleDialog = new NewVehicleDialog();
        vehicleDialog.showNow(getSupportFragmentManager(),"Add Vehicle");
    }

    @Override
    public void saveVehicle(Vehicle vehicle, String vehicleid, String message) {
        Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

    ValueEventListener getVehicleData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            vehicleArray.clear();
            vehicleIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot vehData: dataSnapshot.getChildren()) {

                    Vehicle newVehicle = vehData.getValue(Vehicle.class);
                    String evehicle = edtSearchVehicle.getText().toString().toLowerCase();
                    if(evehicle.isEmpty()) {
                        vehicleIDs.add(vehData.getKey());
                        DataModel vehModel = new DataModel(newVehicle.getRegistrationNumber(), newVehicle.getCityname());
                        vehicleArray.add(vehModel);
                    }else {
                        if(newVehicle.getRegistrationNumber().toLowerCase().startsWith(evehicle)) {
                            vehicleIDs.add(vehData.getKey());
                            DataModel vehModel = new DataModel(newVehicle.getRegistrationNumber(), newVehicle.getCityname());
                            vehicleArray.add(vehModel);
                        }
                    }
                }
                // Functions.MyFunctions.displayMessage(edtSearchEmployee,"Data Loaded Successfully");
            } else {

                Functions.MyFunctions.displayMessage(getApplicationContext(),"No Vehicle Registered");
            }

            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(),vehicleArray);
            vehicleList.setOnItemClickListener(VehicleActivity.this);
            vehicleList.setAdapter(myCustomListAdapter);

            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    TextWatcher searchVehicle = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            DatabaseReference vehDB = FirebaseDatabase.getInstance().getReference("Vehicle");
            vehDB.addValueEventListener(getVehicleData);


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
