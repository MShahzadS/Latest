package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.DealerOrder;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.Income;
import com.idc.wanharcarriage.classes.PioneerOrder;
import com.idc.wanharcarriage.classes.Vehicle;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class PioneerOrderActivity extends AppCompatActivity implements NewDealerDialog.MyDialogListener,
        NewDriverDialog.DriverDialogListener,
        NewVehicleDialog.VehicleDialogListener,
        NewCustomerDialog.MyCustomerDialogListener{


    EditText edtOrderDate, edtweightTons, edtWeightBags, edtTotalCommission,
            edtCommissionPaid, edtInvoiceNumber, edtBuiltyNumber, edtFinalDestination;
    ImageView btnAddDriver, btnAddVehicle, btnAddDealer,btnCaptureImage ;
    SearchableSpinner vehicleSpinner, driverSpinner;
    LinearLayout deliverExtras;
    String vehicle, driver;
    Spinner spinnerOrderType;
    NewDealerDialog myDialog;

    ArrayList<String> drivers, driverIDs;
    ArrayList<String> vehicles, vehicleIDs;
    ListView dealerOrderList ;
    TextView txtGrossFreight ;
    ArrayList<DealerOrder> delaerOrdersArray ;
    int totalfreight = 0, dbDealers = 0;;
    Boolean saved ;
    UploadTask uploadTask;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    DatabaseReference pioneerDB;
    String caller="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pioneer_order);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        readViews();
        addActions();
        loadVehicles();

        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");

    }

    public void readViews() {
        edtOrderDate = (EditText) findViewById(R.id.edtOrderDate) ;
        edtweightTons = (EditText) findViewById(R.id.edttonweight) ;
        edtWeightBags = (EditText) findViewById(R.id.edtbagweight) ;
        edtCommissionPaid = (EditText) findViewById(R.id.edtCommisionPaid);
        edtTotalCommission = (EditText) findViewById(R.id.edtTotalCommission);
        edtInvoiceNumber = (EditText) findViewById(R.id.edtInvoiceNumber);
        edtBuiltyNumber = (EditText) findViewById(R.id.edtBuiltyNumber);
        edtFinalDestination = (EditText) findViewById(R.id.edtFinalDestination);

        btnAddDriver = (ImageView) findViewById(R.id.btnAddDriver);
        btnAddVehicle = (ImageView) findViewById(R.id.btnAddVehicle);
        btnAddDealer = (ImageView) findViewById(R.id.btnAddDealer);
        btnCaptureImage = (ImageView) findViewById(R.id.btnCaptureImage) ;


        vehicleSpinner = (SearchableSpinner) findViewById(R.id.vehicleSpinner) ;
        driverSpinner = (SearchableSpinner) findViewById(R.id.driverSpinner);
        spinnerOrderType = (Spinner) findViewById(R.id.spinnerOrderType);

        deliverExtras = (LinearLayout) findViewById(R.id.deliverExtas);

        dealerOrderList = (ListView) findViewById(R.id.dealerOrderList);
        txtGrossFreight = (TextView) findViewById(R.id.txtgrossfreight);

        drivers = new ArrayList<String>();
        driverIDs = new ArrayList<String>();
        vehicles = new ArrayList<String>();
        vehicleIDs = new ArrayList<String>();
        delaerOrdersArray = new ArrayList<DealerOrder>();

        pioneerDB = FirebaseDatabase.getInstance().getReference("PioneerOrder");

        spinnerOrderType.setSelection(0);
    }

    public void addActions() {
        btnAddVehicle.setOnClickListener(AddVehicle);
        btnAddDriver.setOnClickListener(AddDriver);
        edtweightTons.addTextChangedListener(covertWeight);
        MyDatePicker mydatepicker = new MyDatePicker(PioneerOrderActivity.this,edtOrderDate);

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

        spinnerOrderType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    deliverExtras.setVisibility(View.GONE);
                }else {
                    deliverExtras.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        btnAddDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new NewDealerDialog();
                myDialog.show(getSupportFragmentManager(),"Add Dealer");
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (uploadTask != null && uploadTask.isInProgress()) {
                        Toast.makeText(getApplicationContext(), "Image is being uploaded. Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    dispatchTakePictureIntent() ;
                }

            }
        });
    }

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
    public void saveDriver(Driver d, String driverid, String message) {

        driverSpinner.setSelection(drivers.size()-1);
        driver = driverid;
        //Functions.MyFunctions.displayMessage(btnAddDriver,message);
    }

    @Override
    public void saveVehicle(Vehicle v, String vehicleid, String message) {

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

            ArrayAdapter myAdapter = new ArrayAdapter(PioneerOrderActivity.this,android.R.layout.simple_list_item_1,vehicles);
            vehicleSpinner.setAdapter(myAdapter);
            loadDrivers();
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

            ArrayAdapter myAdapter = new ArrayAdapter(PioneerOrderActivity.this,android.R.layout.simple_list_item_1,drivers);
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

    @Override
    public void saveDelaer(String delaerid, String weight, String freight, String destination) {

        int wt = Integer.valueOf(weight) ;
        int frt = Integer.valueOf(freight);

        int totlfreight = wt * frt ;

        totalfreight += totlfreight;

        txtGrossFreight.setText(String.valueOf(totalfreight));
        DealerOrder neworder = new DealerOrder(delaerid,weight,destination,freight,"P");
        delaerOrdersArray.add(neworder);

        DealerAdapter myAdapter = new DealerAdapter(getApplicationContext(),delaerOrdersArray);
        dealerOrderList.setAdapter(myAdapter);
    }

    @Override
    public void saveCustomer(Customer newCust, String customerID, String message) {

        int position = myDialog.customers.size() - 1 ;
        myDialog.spinnerCustomer.setSelection(position);
        Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

    void uploadFile(final String orderid) {

        try{
            StorageReference mStorageRef;
            String name = "Pioneer/"+ orderid + ".jpg" ;
            mStorageRef = FirebaseStorage.getInstance().getReference(name);

            btnCaptureImage.setDrawingCacheEnabled(true);
            btnCaptureImage.buildDrawingCache();
            Bitmap bitmap = ((BitmapDrawable) btnCaptureImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            uploadTask = mStorageRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"There was an error uploading file") ;
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Functions.MyFunctions.displayMessage(getApplicationContext(),"Image uploaded Successfully");
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void saveDealers(String orderid) {
        saved = true ;
        DatabaseReference dealerOrderDB = FirebaseDatabase.getInstance().getReference("DealerOrder");

        for(int i=0;i<delaerOrdersArray.size();i++){

            delaerOrdersArray.get(i).setOrderid(orderid);
            String dealerid = dealerOrderDB.push().getKey();
            dealerOrderDB.child(dealerid).setValue(delaerOrdersArray.get(i), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference){
                    if(databaseError==null){
                        saved &= true ;
                    }else{
                        saved &= false ;
                    }
                }
            });
        }

        if(saved) {
           // Functions.MyFunctions.displayMessage(getApplicationContext(),"Dealers saved Sucessfully");
        }else {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Dealers could not be Saved");
        }

        saveIncome(orderid);
    }

    public void savePioneerOrder(View view) {

        DatabaseReference pioneerDB = FirebaseDatabase.getInstance().getReference("PioneerOrder");
        final String newPioneer = pioneerDB.push().getKey();

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        String orderDate = edtOrderDate.getText().toString();
        String invoicenumber = edtInvoiceNumber.getText().toString();
        String builtynumber = edtBuiltyNumber.getText().toString();
        int position = driverSpinner.getSelectedItemPosition() ;
        String driver = driverIDs.get(position);
        position = vehicleSpinner.getSelectedItemPosition() ;
        String vehicleselected = vehicleIDs.get(position);
        int weight = Integer.valueOf(edtweightTons.getText().toString());
        String finaldestination = edtFinalDestination.getText().toString();
        int commission = Integer.valueOf(edtTotalCommission.getText().toString());
        String orderType = spinnerOrderType.getSelectedItem().toString();
        int amountpaid=0;
        if(spinnerOrderType.getSelectedItemPosition() == 1)
            amountpaid = Integer.valueOf(edtCommissionPaid.getText().toString());

        PioneerOrder newOrder = new PioneerOrder(orderDate,builtynumber,invoicenumber,vehicleselected,
                                    driver,weight,finaldestination,orderType,commission,amountpaid,false);

        pioneerDB.child(newPioneer).setValue(newOrder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                  //  Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Created");

                    uploadFile(newPioneer);
                    saveDealers(newPioneer);
                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"There was a problem creating order. Try Again...");
                }
            }
        });
    }

    public void saveIncome(String orderid){

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        String formattedDate = df.format(c);

        String commission  = edtTotalCommission.getText().toString() ;

        Income newincome = new Income();
        newincome.setOrderid(orderid);
        newincome.setIncomeDate(formattedDate);
        newincome.setIncomeAbout("Commission");
        newincome.setCompany("Pioneer");
        newincome.setAmount(Integer.valueOf(commission));

        DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
        String incomeid = incomeDB.push().getKey();

        incomeDB.child(incomeid).setValue(newincome, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){

                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Income could not be saved");
                }
                Functions.MyFunctions.dismissDialog();
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Order saved Successfully");

                Intent i ;
                i = new Intent(PioneerOrderActivity.this,Pioneer.class);
                if(caller.equals("Employee"))
                    i = new Intent(PioneerOrderActivity.this,PioneerEmployeeDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }
        });
    }
}
