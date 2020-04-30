package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.MappleOrder;

import java.util.ArrayList;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MappleOvpsDetails extends AppCompatActivity{

    TextView txtOrderDate, txtBuiltyNumber, txtVehicle, txtDriverName, txtDriverContact,
            txtFinalDestination, txtBagsWeight, txtTonsWeight;
    Spinner vehicleStatus;
    MappleOrder mappleOrder;
    String vehiclestatus,  vehicle, orderid ;
    String caller = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapple_ovps_details);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        txtBuiltyNumber = (TextView) findViewById(R.id.txtBuiltyNumber);
        txtVehicle = (TextView) findViewById(R.id.txtVehicleNumber);
        txtDriverName = (TextView) findViewById(R.id.txtDriverName);
        txtDriverContact = (TextView) findViewById(R.id.txtDriverContact);
        txtFinalDestination = (TextView) findViewById(R.id.txtFinalDestination);
        txtBagsWeight = (TextView) findViewById(R.id.txtBagsWeight);
        txtTonsWeight = (TextView) findViewById(R.id.txtTonsWeight);
        vehicleStatus = (Spinner) findViewById(R.id.vehicleStatus);

        vehicleStatus.setSelection(0);

        vehicle = getIntent().getStringExtra("vehicle");
        orderid = getIntent().getStringExtra("order");
        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");


        loadOrder(orderid);

    }

    void loadOrder(String orderid){
        Query orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder")
                        .orderByKey()
                        .equalTo(orderid);
        orderDB.addListenerForSingleValueEvent(getOrderData);
    }

    void loadDriver(String driverID){
        Query driverDB = FirebaseDatabase.getInstance().getReference("Driver")
                .orderByKey()
                .equalTo(driverID);
        driverDB.addListenerForSingleValueEvent(getDriverData);
    }

    public void saveOrder(View view) {

        vehiclestatus = vehicleStatus.getSelectedItem().toString();
        mappleOrder.setVehicleStatus(vehiclestatus);

        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");

        orderDB.child(orderid).setValue(mappleOrder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Vehicle Status Updated Successfully");
                    Intent i = new Intent(MappleOvpsDetails.this, MappleLeafActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(MappleOvpsDetails.this, MappleEmployeeDashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Vehicle Status could not be updated. Try Again!");
                }

            }
        });


    }

    public void deleteOrder(View view) {

        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");

        orderDB.child(orderid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Deleted Successfully");
                    Intent i = new Intent(MappleOvpsDetails.this, MappleLeafActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(MappleOvpsDetails.this, MappleEmployeeDashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order could not be updated. Try Again!");
                }
            }
        });
    }

    ValueEventListener getOrderData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            String driver = "";

            if(dataSnapshot.exists()){
                for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                    MappleOrder newOrder = orderData.getValue(MappleOrder.class);

                        driver = newOrder.getDriver();

                        mappleOrder = new MappleOrder(newOrder) ;
                        txtOrderDate.setText(newOrder.getOrderDate());
                        txtBuiltyNumber.setText(newOrder.getBuiltyNumber());
                        txtFinalDestination.setText(newOrder.getFinalDestination());
                        txtTonsWeight.setText(newOrder.getWeight());
                        int bags = Integer.valueOf(newOrder.getWeight()) * 20;
                        txtBagsWeight.setText(String.valueOf(bags));
                        txtVehicle.setText(vehicle);

                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            } else {

                Functions.MyFunctions.displayMessage(getApplicationContext(),"No Vehicle is Outside VPS");
            }

            loadDriver(driver);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    ValueEventListener getDriverData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {

                    Driver newEmp = empData.getValue(Driver.class);

                    txtDriverName.setText(newEmp.getName());
                    txtDriverContact.setText(newEmp.getContact());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Driver not Found");
            }

            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };
}

