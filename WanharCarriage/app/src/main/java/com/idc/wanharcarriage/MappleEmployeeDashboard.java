package com.idc.wanharcarriage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.Vehicle;
import com.idc.wanharcarriage.mapple.SectionsPagerAdapter;

public class MappleEmployeeDashboard extends AppCompatActivity implements View.OnClickListener
        , NewCustomerDialog.MyCustomerDialogListener
        , NewDriverDialog.DriverDialogListener
        , NewVehicleDialog.VehicleDialogListener {

    ImageView btnProfile, btnAddCustomer, btnAddDriver, btnAddbus ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);


        btnProfile = (ImageView) findViewById(R.id.btnProfile) ;
        btnAddCustomer = (ImageView) findViewById(R.id.btnAddCustomer) ;
        btnAddDriver = (ImageView) findViewById(R.id.btnAddDriver) ;
        btnAddbus = (ImageView) findViewById(R.id.btnAddBus) ;


        btnProfile.setOnClickListener(this);
        btnAddCustomer.setOnClickListener(this);
        btnAddDriver.setOnClickListener(this);
        btnAddbus.setOnClickListener(this);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MappleEmployeeDashboard.this, MappleOrderActivity.class) ;
                startActivity(i);

            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences(Login.USERPREFERENCES, MODE_PRIVATE);
        String name =sharedPreferences.getString(Login.Name,"");
        this.setTitle("Welcome " + name);

    }

    @Override
    public void onClick(View v) {

        Intent i ;
        switch (v.getId()) {

            case R.id.btnProfile:
                i = new Intent(MappleEmployeeDashboard.this, EmployeeProfile.class);
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
            logout = new Intent(MappleEmployeeDashboard.this, Login.class);
            startActivity(logout);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void saveCustomer(Customer newCust, String customerID, String message) {
        Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

    @Override
    public void saveDriver(Driver driver, String driverid, String message) {
        Functions.MyFunctions.displayMessage(getApplicationContext(), message);
    }

    @Override
    public void saveVehicle(Vehicle vehicle, String vehicleid, String message) {
        Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

    /*
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            btnCaptureImage.setImageBitmap(imageBitmap);
        }
    }
     */
}
