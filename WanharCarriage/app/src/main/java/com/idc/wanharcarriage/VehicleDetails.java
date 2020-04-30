package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Vehicle;

public class VehicleDetails extends AppCompatActivity {

    TextView txtVehicleNumber, txtCityName, txtVehicleDetail;
    String vehicleid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);

        txtVehicleNumber = (TextView) findViewById(R.id.txtRegistrationNumber) ;
        txtCityName = (TextView) findViewById(R.id.txtCityName) ;
        txtVehicleDetail = (TextView) findViewById(R.id.txtVehicleModel);

        vehicleid = getIntent().getStringExtra("vehicle") ;

        Query empDB = FirebaseDatabase.getInstance().getReference("Vehicle")
                .orderByKey()
                .equalTo(vehicleid);

        empDB.addListenerForSingleValueEvent(getVehicleData); ;
    }

    public void deleteVehicle(View view){
        FirebaseDatabase.getInstance().getReference("Vehicle")
                .child(vehicleid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Vehicle Deleted Successfully");
                    Intent i = new Intent(VehicleDetails.this, VehicleActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Vehicle could not be deleted successfully");
                }
            }
        });
    }

    ValueEventListener getVehicleData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot vehData: dataSnapshot.getChildren()) {

                    Vehicle newVeh = vehData.getValue(Vehicle.class);

                    txtVehicleNumber.setText(newVeh.getRegistrationNumber());
                    txtCityName.setText(newVeh.getCityname());
                    txtVehicleDetail.setText(newVeh.getModel());

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
