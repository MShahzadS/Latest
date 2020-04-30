package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.idc.wanharcarriage.classes.PioneerBill;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PioneerBillDetails extends AppCompatActivity implements ImageViewDialog.ImagePreviewListener {

    ListView billOrderList ;
    TextView txtTotalBill, txtBillDate;
    CheckBox chkBillStatus;
    ArrayList<DataModel> ordersArrays ;
    int totalBill=0;
    PioneerBill bill;
    ImageView billImage ;
    Bitmap myBitmap;
    String billid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pioneer_bill_details);

        ordersArrays = new ArrayList<DataModel>();
        billOrderList = (ListView) findViewById(R.id.billOrderList) ;
        txtTotalBill = (TextView) findViewById(R.id.txtTotalBill) ;
        txtBillDate = (TextView) findViewById(R.id.txtBillDate) ;
        billImage = (ImageView) findViewById(R.id.billImage);
        chkBillStatus = (CheckBox) findViewById(R.id.chkBillStatus);

        billImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImageViewDialog imageViewDialog = new ImageViewDialog(myBitmap);
                imageViewDialog.show(getSupportFragmentManager(),"Preview");
            }
        });

        chkBillStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference billDB = FirebaseDatabase.getInstance().getReference("PioneerBill");

                bill.setBillStatus(isChecked);
                billDB.child(billid).setValue(bill);
            }
        });

        billid = getIntent().getStringExtra("bill");
        downloadFile(billid);
        loadBill(billid);

    }

    void downloadFile(String billid) {
        StorageReference mStorageRef;
        String name = "PioneerBill/"+ billid + ".jpg" ;
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
                        billImage.setImageBitmap(myBitmap);

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

    void loadBill(String billid){

        Query billDB = FirebaseDatabase.getInstance().getReference("PioneerBill")
                                        .orderByKey()
                                        .equalTo(billid);
        billDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot billData : dataSnapshot.getChildren()){
                        bill = billData.getValue(PioneerBill.class);

                        txtBillDate.setText(bill.getBillDate());
                        txtTotalBill.setText(String.valueOf(bill.getTotalBill()));
                        chkBillStatus.setChecked(bill.getBillStatus());

                        ArrayAdapter myadapter = new ArrayAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,bill.getOrderInvoices());
                        billOrderList.setAdapter(myadapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void updateImage() {

    }

    public void deleteImage() {

        StorageReference mStorageRef;
        String name = "PioneerBill/"+ billid + ".jpg" ;
        mStorageRef = FirebaseStorage.getInstance().getReference(name);

        mStorageRef.delete() ;

    }

    public void deleteBill(View view) {

        DatabaseReference billDB = FirebaseDatabase.getInstance().getReference("PioneerBill");

        billDB.child(billid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){

                    try{
                        deleteImage();
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }finally {
                        Functions.MyFunctions.displayMessage(getApplicationContext(),"Bill Deleted Successfully");
                        Intent i = new Intent(PioneerBillDetails.this, PioneerBillActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                    }

                }
            }
        });
    }
}
