package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idc.wanharcarriage.classes.DealerOrder;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.Income;
import com.idc.wanharcarriage.classes.MappleOrder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class MappleClearDetails extends AppCompatActivity implements  ImageViewDialog.ImagePreviewListener {

    ListView dealerOrderList ;
    ArrayList<DealerOrder> delaerOrdersArray ;
    TextView txtOrderDate, txtBuiltyNumber, txtVehicle,
            txtDriverName, txtDriverContact, txtGrossFreight,txtFinalDestination;
    EditText edtCommissionRecieved;
    Spinner vehicleStatus;
    MappleOrder mappleOrder;
    ImageView builtyImage ;
    String vehicle, orderid ;
    int dbDealers  = 0;
    Bitmap myBitmap ;
    String caller ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapple_clear_details);


        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        txtBuiltyNumber = (TextView) findViewById(R.id.txtBuiltyNumber);
        txtVehicle = (TextView) findViewById(R.id.txtVehicleNumber);
        txtDriverName = (TextView) findViewById(R.id.txtDriverName);
        txtDriverContact = (TextView) findViewById(R.id.txtDriverContact);
        txtFinalDestination = (TextView) findViewById(R.id.txtFinalDestination);
        edtCommissionRecieved = (EditText) findViewById(R.id.edtCommissionRecieved);
        vehicleStatus = (Spinner) findViewById(R.id.vehicleStatus);
        txtGrossFreight = (TextView) findViewById(R.id.txtgrossfreight) ;
        dealerOrderList = (ListView) findViewById(R.id.dealerOrderList) ;
        builtyImage = (ImageView) findViewById(R.id.builtyImage) ;
        delaerOrdersArray = new ArrayList<DealerOrder>();

        vehicleStatus.setSelection(2);
        vehicle = getIntent().getStringExtra("vehicle");
        orderid = getIntent().getStringExtra("order");

        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");

        builtyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageViewDialog imageViewDialog = new ImageViewDialog(myBitmap);
                imageViewDialog.show(getSupportFragmentManager(),"Preview");
            }
        });

        downloadFile();
        loadDelaers();


    }

    void downloadFile() {
        StorageReference mStorageRef;
        String name = "Mapple/"+ orderid + ".jpg" ;
        mStorageRef = FirebaseStorage.getInstance().getReference(name);

        File localFile = null;
        try {
            localFile = File.createTempFile("images", "jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        final File finalLocalFile = localFile;

        mStorageRef.getFile(localFile)
                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        // Successfully downloaded data to local file
                        // ...

                        myBitmap = BitmapFactory.decodeFile(finalLocalFile.getAbsolutePath());
                        builtyImage.setImageBitmap(myBitmap);

                       // Toast.makeText(getApplicationContext(),"Image Dowloaded Successfully", Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle failed download
                // ...
                Toast.makeText(getApplicationContext(),"Image not Found", Toast.LENGTH_LONG).show();
            }
        });

    }

    public void loadDelaers() {

        Query dealerOrderDB = FirebaseDatabase.getInstance().getReference("DealerOrder")
                .orderByChild("orderid")
                .equalTo(orderid);

        dealerOrderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalfreight = 0 ;
                delaerOrdersArray.clear();
                if(dataSnapshot.exists()){
                    for (DataSnapshot dealerData: dataSnapshot.getChildren()){
                        DealerOrder newOrder = dealerData.getValue(DealerOrder.class);
                        totalfreight += Integer.valueOf(newOrder.getTotalweight()) * Integer.valueOf(newOrder.getFreightperTon());
                        delaerOrdersArray.add(newOrder) ;
                        dbDealers += 1;
                    }
                }

                if(getApplicationContext() != null) {
                    DealerAdapter myAdapter = new DealerAdapter(getApplicationContext(), delaerOrdersArray);
                    dealerOrderList.setAdapter(myAdapter);
                }
                txtGrossFreight.setText(String.valueOf(totalfreight));

                loadOrder(orderid);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadOrder(String orderid){
        Query orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder")
                .orderByKey()
                .equalTo(orderid);
        orderDB.addListenerForSingleValueEvent(getOrderData);
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
                    txtVehicle.setText(vehicle);
                    edtCommissionRecieved.setText(newOrder.getCommission());



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

    void loadDriver(String driverID){
        Query driverDB = FirebaseDatabase.getInstance().getReference("Driver")
                .orderByKey()
                .equalTo(driverID);
        driverDB.addListenerForSingleValueEvent(getDriverData);
    }

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

    public void saveOrder(View view) {

        String commission  = edtCommissionRecieved.getText().toString() ;
        mappleOrder.setVehicleStatus(vehicleStatus.getSelectedItem().toString());
        mappleOrder.setCommission(commission);

        if(!commission.isEmpty()) {
            saveIncome(commission);
        }

        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");
        orderDB.child(orderid).setValue(mappleOrder,new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Data Saved Successfully");
                    Intent i = new Intent(MappleClearDetails.this, MappleLeafActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(MappleClearDetails.this, MappleEmployeeDashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Data could not be saved. Try Again!");
                }
            }
        });

    }

    private void saveIncome(String commission) {

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy");
        String formattedDate = df.format(c);

        Income newincome = new Income();
        newincome.setOrderid(orderid);
        newincome.setIncomeDate(formattedDate);
        newincome.setIncomeAbout("Commission");
        newincome.setCompany("Mapple");
        newincome.setAmount(Integer.valueOf(commission));

        DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
        String incomeid = incomeDB.push().getKey();

        incomeDB.child(incomeid).setValue(newincome);
    }

    public void deleteIncome() {
        final DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
        incomeDB.orderByChild("orderid").equalTo(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    String key = postsnapshot.getKey();
                    incomeDB.child(key).removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void deleteOrder(View view) {
        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");

        orderDB.child(orderid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){

                    try{
                        deleteImage();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }finally {
                        deleteIncome();
                        deleteDealerOrders();
                    }

                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order could not be updated. Try Again!");
                }
            }
        });
    }

    public void deleteImage() {

        StorageReference mStorageRef;
        String name = "Mapple/"+ orderid + ".jpg" ;
        mStorageRef = FirebaseStorage.getInstance().getReference(name);

        mStorageRef.delete() ;

    }

    public void deleteDealerOrders() {

        final DatabaseReference dealerOrderDB = FirebaseDatabase.getInstance().getReference("DealerOrder");
        dealerOrderDB.orderByChild("orderid").equalTo(orderid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postsnapshot : dataSnapshot.getChildren()) {

                    String key = postsnapshot.getKey();
                    dealerOrderDB.child(key).removeValue();
                }
                Functions.MyFunctions.dismissDialog();
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Deleted Successfully");
                Intent i = new Intent(MappleClearDetails.this, MappleLeafActivity.class);
                if(caller.equals("Employee"))
                    i = new Intent(MappleClearDetails.this, MappleEmployeeDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) ;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    @Override
    public void updateImage() {

    }

}


