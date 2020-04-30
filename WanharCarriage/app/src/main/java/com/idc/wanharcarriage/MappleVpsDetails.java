package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.MappleOrder;
import com.idc.wanharcarriage.classes.DealerOrder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;


public class MappleVpsDetails extends AppCompatActivity implements
        NewDealerDialog.MyDialogListener, NewCustomerDialog.MyCustomerDialogListener {


    TextView txtOrderDate, txtBuiltyNumber, txtVehicle, txtDriverName, txtDriverContact, txtGrossFreight;
    EditText edtFinalDestination;
    Spinner vehicleStatus;
    MappleOrder mappleOrder;
    String vehiclestatus, vehicle, orderid;
    ImageView btnAddDealer;
    ListView dealerOrderList;
    NewDealerDialog myDialog;
    ImageView btnCaptureImage;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String message;
    Boolean saved;
    int dbDealers = 0;
    UploadTask uploadTask;
    ArrayList<DealerOrder> delaerOrdersArray;
    int totalfreight = 0;
    String caller = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapple_vps_details);


        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        delaerOrdersArray = new ArrayList<DealerOrder>();
        txtOrderDate = (TextView) findViewById(R.id.txtOrderDate);
        txtBuiltyNumber = (TextView) findViewById(R.id.txtBuiltyNumber);
        txtVehicle = (TextView) findViewById(R.id.txtVehicleNumber);
        txtDriverName = (TextView) findViewById(R.id.txtDriverName);
        txtDriverContact = (TextView) findViewById(R.id.txtDriverContact);
        edtFinalDestination = (EditText) findViewById(R.id.edtFinalDestination);
        vehicleStatus = (Spinner) findViewById(R.id.vehicleStatus);
        btnAddDealer = (ImageView) findViewById(R.id.btnAddDealer);
        dealerOrderList = (ListView) findViewById(R.id.dealerOrderList);
        txtGrossFreight = (TextView) findViewById(R.id.txtgrossfreight);
        btnCaptureImage = (ImageView) findViewById(R.id.btnCaptureImage);

        vehicleStatus.setSelection(1);
        vehicle = getIntent().getStringExtra("vehicle");
        orderid = getIntent().getStringExtra("order");
        message = "";

        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");

        loadDelaers();
        btnAddDealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDialog = new NewDealerDialog();
                myDialog.show(getSupportFragmentManager(), "Add Dealer");
            }
        });

        btnCaptureImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Image is being uploaded. Please wait...", Toast.LENGTH_SHORT).show();
                } else {
                    dispatchTakePictureIntent();
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

    void uploadFile() {


        StorageReference mStorageRef;
        String name = "Mapple/" + orderid + ".jpg";
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
                Toast.makeText(getApplicationContext(), "There was an error uploading file", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
                Toast.makeText(getApplicationContext(), "Image uploaded Successfully", Toast.LENGTH_LONG).show();


            }
        });


    }

    @Override
    public void saveDelaer(String delaerid, String weight, String freight, String destination) {

        try {
            int wt = Integer.valueOf(weight);
            int frt = Integer.valueOf(freight);

            int totlfreight = wt * frt;

            totalfreight += totlfreight;

            txtGrossFreight.setText(String.valueOf(totalfreight));

            DealerOrder neworder = new DealerOrder(orderid, delaerid, weight, destination, freight, "M");
            delaerOrdersArray.add(neworder);

            if (getApplicationContext() != null) {
                DealerAdapter myAdapter = new DealerAdapter(getApplicationContext(), delaerOrdersArray);
                dealerOrderList.setAdapter(myAdapter);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    void loadOrder(String orderid) {
        Query orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder")
                .orderByKey()
                .equalTo(orderid);
        orderDB.addListenerForSingleValueEvent(getOrderData);
    }

    void loadDriver(String driverID) {
        Query driverDB = FirebaseDatabase.getInstance().getReference("Driver")
                .orderByKey()
                .equalTo(driverID);
        driverDB.addListenerForSingleValueEvent(getDriverData);
    }

    public void saveOrder(View view) {

        vehiclestatus = vehicleStatus.getSelectedItem().toString();
        mappleOrder.setVehicleStatus(vehiclestatus);
        mappleOrder.setFinalDestination(edtFinalDestination.getText().toString());

        try {
            uploadFile();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            saveDealers();
        }
    }

    public void updateOrder() {

        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");
        orderDB.child(orderid).setValue(mappleOrder, new DatabaseReference.CompletionListener() {

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null)
                    saved &= true;
                else
                    saved &= false;

                if (saved) {

                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Data saved Successfully");

                    Intent i = new Intent(MappleVpsDetails.this, MappleLeafActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(MappleVpsDetails.this, MappleEmployeeDashboard.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                } else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "There was a problem saving data. Try Again!");
                }

            }

        });
    }

    public void saveDealers() {
        saved = true;
        DatabaseReference dealerOrderDB = FirebaseDatabase.getInstance().getReference("DealerOrder");

        for (int i = dbDealers; i < delaerOrdersArray.size(); i++) {

            String newid = dealerOrderDB.push().getKey();

            dealerOrderDB.child(newid).setValue(delaerOrdersArray.get(i), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError == null) {
                        saved &= true;
                    } else {
                        saved &= false;
                    }
                }
            });
        }

        updateOrder();
    }

    public void loadDelaers() {

        Query dealerOrderDB = FirebaseDatabase.getInstance().getReference("DealerOrder")
                .orderByChild("orderid")
                .equalTo(orderid);

        dealerOrderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int totalfreight = 0;
                delaerOrdersArray.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dealerData : dataSnapshot.getChildren()) {
                        DealerOrder newOrder = dealerData.getValue(DealerOrder.class);
                        if (newOrder.getCompany().equals("M")) {
                            totalfreight += Integer.valueOf(newOrder.getTotalweight()) * Integer.valueOf(newOrder.getFreightperTon());
                            delaerOrdersArray.add(newOrder);
                            dbDealers += 1;
                        }
                    }
                }

                if (getApplicationContext() != null) {
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

    public void deleteOrder(View view) {

        DatabaseReference orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder");

        orderDB.child(orderid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError == null) {
                    try {
                        deleteImage();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }finally {
                        deleteDealerOrders();
                    }


                } else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Order could not be updated. Try Again!");
                }
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
                Functions.MyFunctions.dismissDialog();
                Functions.MyFunctions.displayMessage(getApplicationContext(), "Order Deleted Successfully");
                Intent i = new Intent(MappleVpsDetails.this, MappleLeafActivity.class);
                if(caller.equals("Employee"))
                    i = new Intent(MappleVpsDetails.this, MappleEmployeeDashboard.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Functions.MyFunctions.displayMessage(getApplicationContext(), "Order could not be deleted. Try Again..");
            }


        });
    }

    public void deleteImage() {

         StorageReference mStorageRef;
         String name = "Mapple/"+ orderid + ".jpg" ;
         mStorageRef = FirebaseStorage.getInstance().getReference(name);

         mStorageRef.delete() ;

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
                    edtFinalDestination.setText(newOrder.getFinalDestination());
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

    @Override
    public void saveCustomer(Customer newCust, String customerID, String message) {


                int position = myDialog.customers.size() - 1 ;
                myDialog.spinnerCustomer.setSelection(position);
                Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

}
