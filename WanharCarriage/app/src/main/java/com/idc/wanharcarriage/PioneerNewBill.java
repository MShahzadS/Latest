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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.idc.wanharcarriage.classes.PioneerBill;
import com.idc.wanharcarriage.classes.PioneerOrder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Random;

public class PioneerNewBill extends AppCompatActivity implements AddOrderDialog.OrderDialogListener {

    EditText edtBillDate ;
    ImageView btnAddOrder,btnCaptureImage ;
    ListView billOrderList ;
    TextView txtTotalBill;
    UploadTask uploadTask;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ArrayList<String> orderInvoices;
    ArrayList<DataModel> ordersArrays ;
    int totalBill=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pioneer_new_bill);


        ordersArrays = new ArrayList<DataModel>();
        orderInvoices = new ArrayList<String>();
        edtBillDate = (EditText) findViewById(R.id.edtbillDate);
        billOrderList = (ListView) findViewById(R.id.billOrderList) ;
        btnAddOrder = (ImageView) findViewById(R.id.btnAddOrder) ;
        txtTotalBill = (TextView) findViewById(R.id.txtTotalBill);
        btnCaptureImage = (ImageView) findViewById(R.id.btnCaptureImage) ;


        MyDatePicker mydate = new MyDatePicker(PioneerNewBill.this, edtBillDate);

        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOrderDialog myDialog = new AddOrderDialog();
                myDialog.show(getSupportFragmentManager(),"Order Dialog");
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

    void uploadFile(final String orderid) {
        StorageReference mStorageRef;
        String name = "PioneerBill/"+ orderid + ".jpg" ;
        mStorageRef = FirebaseStorage.getInstance().getReference(name);

        try {
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
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "There was an error uploading Image");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    //Functions.MyFunctions.displayMessage(getApplicationContext(),"Image uploaded Successfully");
                }
            });
        }catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Order Saved Successfully") ;
            Intent i = new Intent(PioneerNewBill.this, PioneerBillActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }

    }

    @Override
    public void saveOrder(PioneerOrder orderData, String orderid) {
            int commission = orderData.getCommission();
            DataModel myModel = new DataModel(orderData.getInvoiceNumber(), String.valueOf(commission)) ;
            ordersArrays.add(myModel) ;
            totalBill += commission ;
            orderInvoices.add(orderData.getInvoiceNumber());

            txtTotalBill.setText(String.valueOf(totalBill));
            MyCustomListAdapter myAdapter = new MyCustomListAdapter(getApplicationContext(),ordersArrays);
            billOrderList.setAdapter(myAdapter);

    }

    public void saveBill(View view) {

        String orderDate = edtBillDate.getText().toString();
        PioneerBill newBill = new PioneerBill(orderDate,orderInvoices,totalBill);

        DatabaseReference billDB = FirebaseDatabase.getInstance().getReference("PioneerBill");

        final String newkey = billDB.push().getKey();

        billDB.child(newkey).setValue(newBill, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    uploadFile(newkey);
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Bill Saved Successfully");
                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Bill could not be saved. Try again...");
                }
            }
        });

    }
}
