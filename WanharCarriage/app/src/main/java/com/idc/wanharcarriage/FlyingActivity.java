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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.FlyingOrder;
import com.idc.wanharcarriage.classes.PioneerOrder;
import com.idc.wanharcarriage.classes.Vehicle;
import com.idc.wanharcarriage.pioneer.DeliveryFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class FlyingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    ListView flyingOrderList ;
    FloatingActionButton btnAddOrder ;
    ArrayList<String> orderIDs;
    EditText edtSearchOrder;
    ArrayList<DataModel> orderArray;
    HashMap<String, String> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flying);

        btnAddOrder = (FloatingActionButton) findViewById(R.id.btnAddOrder) ;
        edtSearchOrder = (EditText) findViewById(R.id.edtSearchOrder);

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FlyingActivity.this, FlyingOrderActivity.class) ;
                startActivity(i);
            }
        });

        flyingOrderList = (ListView) findViewById(R.id.flyingOrderList) ;

        orderArray = new ArrayList<DataModel>();
        orderIDs = new ArrayList<String>();
        vehicles = new HashMap<>();

        loadVehicles();

        edtSearchOrder.addTextChangedListener(searchOrder);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getApplicationContext(), FlyingOrderDetails.class);
        i.putExtra("order", orderIDs.get(position));
        i.putExtra("vehicle", orderArray.get(position).getItemmain());
        startActivity(i);
        /*
        Snackbar.make(parent, orderArray.get(position).getItemmain(),Snackbar.LENGTH_LONG).show();
         */
    }

    public void loadVehicles(){
        Query vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");
        vehicleDB.addValueEventListener(getVehicleData);
    }

    public String getVehicle(String vehicleid){
        return vehicles.get(vehicleid);
    }

    ValueEventListener getVehicleData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot vehData : dataSnapshot.getChildren()) {
                    Vehicle newVeh = vehData.getValue(Vehicle.class);
                    vehicles.put(vehData.getKey(),newVeh.getRegistrationNumber());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(), "Failed to Load Vehicle Info. Try Again!");

            }

            loadOrderData();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    public void loadOrderData(){
        Query orderDB = FirebaseDatabase.getInstance().getReference("FlyingOrder");
        orderDB.addValueEventListener(getOrders);
    }

    ValueEventListener getOrders = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            orderArray.clear();
            orderIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                    FlyingOrder newOrder = orderData.getValue(FlyingOrder.class);
                    String sname = edtSearchOrder.getText().toString().toLowerCase() ;

                    String vehicle = getVehicle(newOrder.getVehicle());
                    if(!sname.isEmpty()) {
                        if(vehicle.toLowerCase().startsWith(sname)) {
                            orderIDs.add(orderData.getKey());
                            DataModel orderModel = new DataModel(vehicle, newOrder.getFinalDestination());
                            orderArray.add(orderModel);
                        }
                    }else
                    {
                        orderIDs.add(orderData.getKey());
                        DataModel orderModel = new DataModel(vehicle, newOrder.getFinalDestination());
                        orderArray.add(orderModel);
                    }

                }
            } else {

                //  Functions.MyFunctions.displayMessage(mappleOrderList,"No Vehicle is "+ orderStatus);
            }

            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(),orderArray);
            flyingOrderList.setOnItemClickListener(FlyingActivity.this);
            flyingOrderList.setAdapter(myCustomListAdapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    TextWatcher searchOrder = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loadOrderData();


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
