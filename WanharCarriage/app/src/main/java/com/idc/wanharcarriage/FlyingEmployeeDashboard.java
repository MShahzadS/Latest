package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.FlyingOrder;
import com.idc.wanharcarriage.classes.Vehicle;
import com.idc.wanharcarriage.pioneer.SectionsPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class FlyingEmployeeDashboard extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
        {

    ImageView btnProfile, btnAddCustomer, btnAddDriver, btnAddbus ;
    ListView flyingOrderList ;
    FloatingActionButton btnAddOrder ;
    ArrayList<String> orderIDs;
    EditText edtSearchOrder;
    ArrayList<DataModel> orderArray;
    HashMap<String, String> vehicles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flying_employee_dashboard);


        btnProfile = (ImageView) findViewById(R.id.btnProfile) ;
        btnAddCustomer = (ImageView) findViewById(R.id.btnAddCustomer) ;
        btnAddDriver = (ImageView) findViewById(R.id.btnAddDriver) ;
        btnAddbus = (ImageView) findViewById(R.id.btnAddBus) ;


        btnProfile.setOnClickListener(this);
        btnAddCustomer.setOnClickListener(this);
        btnAddDriver.setOnClickListener(this);
        btnAddbus.setOnClickListener(this);

        btnAddOrder = (FloatingActionButton) findViewById(R.id.btnAddOrder) ;
        edtSearchOrder = (EditText) findViewById(R.id.edtSearchOrder);

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(FlyingEmployeeDashboard.this, FlyingOrderActivity.class) ;
                i.putExtra("caller", "Employee");
                startActivity(i);
            }
        });

        flyingOrderList = (ListView) findViewById(R.id.flyingOrderList) ;

        orderArray = new ArrayList<DataModel>();
        orderIDs = new ArrayList<String>();
        vehicles = new HashMap<>();

        loadVehicles();

        edtSearchOrder.addTextChangedListener(searchOrder);

        SharedPreferences sharedPreferences = getSharedPreferences(Login.USERPREFERENCES, MODE_PRIVATE);
        String name =sharedPreferences.getString(Login.Name,"");
        this.setTitle("Welcome " + name);
    }

    @Override
    public void onClick(View v) {

        Intent i ;
        switch (v.getId()) {

            case R.id.btnProfile:
                i = new Intent(FlyingEmployeeDashboard.this, EmployeeProfile.class);
                startActivity(i);
                break;
            case R.id.btnAddCustomer:
                NewCustomerDialog myDialog = new NewCustomerDialog();
                myDialog.show(getSupportFragmentManager(), "Add New Customer");
                break;
            case R.id.btnAddDriver:
                NewDriverDialog driverDialog = new NewDriverDialog();
                driverDialog.show(getSupportFragmentManager(), "Add Driver");
                break;
            case R.id.btnAddBus:
                NewVehicleDialog vehicleDialog = new NewVehicleDialog();
                vehicleDialog.showNow(getSupportFragmentManager(),"Add Vehicle");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu) ;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent logout = null;
        if (item.getItemId() == R.id.action_logout) {
            logout = new Intent(FlyingEmployeeDashboard.this, Login.class);
            startActivity(logout);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getApplicationContext(), FlyingOrderDetails.class);
        i.putExtra("order", orderIDs.get(position));
        i.putExtra("caller", "Employee");
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
            flyingOrderList.setOnItemClickListener(FlyingEmployeeDashboard.this);
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
