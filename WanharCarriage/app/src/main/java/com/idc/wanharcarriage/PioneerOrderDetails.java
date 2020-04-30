package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.idc.wanharcarriage.classes.DealerOrder;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.MappleOrder;
import com.idc.wanharcarriage.classes.PioneerOrder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PioneerOrderDetails extends AppCompatActivity implements ImageViewDialog.ImagePreviewListener {

    ListView dealerOrderList ;
    ArrayList<DealerOrder> delaerOrdersArray ;
    TextView txtOrderDate, txtBuiltyNumber, txtVehicle, txtDriverName, txtInvoiceNumber, txtOrderType,
            txtDriverContact, txtGrossFreight,txtFinalDestination,txtTotalCommission, txtAmountPaid;
    EditText edtPayAmount;
    Button btnSaveOrder ;
    PioneerOrder pioneerOrder;
    LinearLayout deliverExtras;
    ImageView builtyImage ;
    CheckBox chkBillStatus;
    String vehiclestatus,  vehicle, orderid ;
    int dbDealers  = 0;
    int totalfreight = 0;
    Bitmap myBitmap ;
    String caller = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pioneer_order_details);


        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        txtInvoiceNumber = (TextView) findViewById(R.id.txtInvoiceNumber);
        txtBuiltyNumber = (TextView) findViewById(R.id.txtBuiltyNumber);
        txtVehicle = (TextView) findViewById(R.id.txtVehicleNumber);
        txtDriverName = (TextView) findViewById(R.id.txtDriverName);
        txtDriverContact = (TextView) findViewById(R.id.txtDriverContact);
        txtFinalDestination = (TextView) findViewById(R.id.txtFinalDestination);
        txtTotalCommission = (TextView) findViewById(R.id.txtTotalCommission);
        txtOrderType = (TextView) findViewById(R.id.txtOrderType);
        txtGrossFreight = (TextView) findViewById(R.id.txtgrossfreight) ;
        dealerOrderList = (ListView) findViewById(R.id.dealerOrderList) ;
        deliverExtras = (LinearLayout) findViewById(R.id.deliverExtas);
        txtAmountPaid = (TextView) findViewById(R.id.txtAmountPaid);
        edtPayAmount = (EditText) findViewById(R.id.edtPayAmount);
        chkBillStatus = (CheckBox) findViewById(R.id.chkBillStatus);
        builtyImage = (ImageView) findViewById(R.id.builtyImage) ;
        btnSaveOrder = (Button) findViewById(R.id.btnSaveOrder);
        delaerOrdersArray = new ArrayList<DealerOrder>();
        deliverExtras.setVisibility(View.GONE);
        btnSaveOrder.setVisibility(View.GONE);



        vehicle = getIntent().getStringExtra("vehicle");
        orderid = getIntent().getStringExtra("order");

        builtyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewDialog imageViewDialog = new ImageViewDialog(myBitmap);
                imageViewDialog.show(getSupportFragmentManager(),"Preview");
            }
        });

        try {
            downloadFile();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        if(getIntent().hasExtra("caller")){
            caller = getIntent().getStringExtra("caller");
        }
        loadDelaers();


    }

    void downloadFile() {
        StorageReference mStorageRef;
        String name = "Pioneer/"+ orderid + ".jpg" ;
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
                Toast.makeText(getApplicationContext(),"Image could not be Dowloaded", Toast.LENGTH_LONG).show();
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

                DealerAdapter myAdapter = new DealerAdapter(getApplicationContext(),delaerOrdersArray);
                dealerOrderList.setAdapter(myAdapter);
                txtGrossFreight.setText(String.valueOf(totalfreight));

                loadOrder(orderid);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void loadOrder(String orderid){
        Query orderDB = FirebaseDatabase.getInstance().getReference("PioneerOrder")
                .orderByKey()
                .equalTo(orderid);
        orderDB.addValueEventListener(getOrderData);
    }

    ValueEventListener getOrderData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            String driver = "";

            if(dataSnapshot.exists()){
                for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                    PioneerOrder newOrder = orderData.getValue(PioneerOrder.class);

                    driver = newOrder.getDriver();

                    pioneerOrder = new PioneerOrder(newOrder) ;
                    txtOrderDate.setText(newOrder.getOrderDate());
                    txtInvoiceNumber.setText(newOrder.getInvoiceNumber());
                    txtBuiltyNumber.setText(newOrder.getBuiltyNumber());
                    txtFinalDestination.setText(newOrder.getFinalDestination());
                    txtVehicle.setText(vehicle);
                    txtTotalCommission.setText(String.valueOf(newOrder.getCommission()));
                    txtOrderType.setText(newOrder.getorderType());
                    if(newOrder.getorderType().equals("Deliver")) {
                        deliverExtras.setVisibility(View.VISIBLE);
                        btnSaveOrder.setVisibility(View.VISIBLE);
                        txtAmountPaid.setText(String.valueOf(newOrder.getPaidAmount()));
                        chkBillStatus.setChecked(newOrder.getBillStatus());
                    }

                    loadDriver(driver);

                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            } else {
               // Functions.MyFunctions.displayMessage(getApplicationContext(),"Order not Found");
            }

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

    public void deleteOrder(View view) {

            try {
                deleteImage();
            }catch (Exception ex){
                ex.printStackTrace();
            }finally {
                deleteIncome();
            }
    }

    void deleteOrderData() {
        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("PioneerOrder");

        orderDB.child(orderid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Deleted Successfully");
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Order could not be updated. Try Again!");
                }

                Functions.MyFunctions.dismissDialog();
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Deleted Successfully");
                Intent i  = new Intent(PioneerOrderDetails.this, Pioneer.class);
                if(caller.equals("Employee"))
                    i = new Intent(PioneerOrderDetails.this, PioneerEmployeeDashboard.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }

        });
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
                deleteDealerOrders();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

                deleteOrderData();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }

    void deleteImage() {
        StorageReference mStorageRef;
        String name = "Pioneer/"+ orderid + ".jpg" ;
        mStorageRef = FirebaseStorage.getInstance().getReference(name);

        mStorageRef.delete();
    }

    @Override
    public void updateImage() {

    }

    public void updateOrder(View view) {

        Boolean billStatus = chkBillStatus.isChecked();
        int amountpaid = Integer.valueOf(edtPayAmount.getText().toString());

        pioneerOrder.setBillStatus(billStatus);
        amountpaid += pioneerOrder.getPaidAmount();

        pioneerOrder.setPaidAmount(amountpaid);
        DatabaseReference pioneerDB = FirebaseDatabase.getInstance().getReference("PioneerOrder");

        pioneerDB.child(orderid).setValue(pioneerOrder, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    edtPayAmount.setText("");
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Order updated Successfully");
                }else
                {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Order could not be updated");
                }
            }
        });



    }
}


