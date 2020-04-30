package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.DealerOrder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UnloadingNew extends AppCompatActivity {

    LinearLayout linearLayout;
    String orderid, vehicle;
    HashMap<String, Customer> customers ;
    HashMap<String, Boolean> unloadIssues;
    String  orderDate;
    EditText edtIssueDate, edtComplaintNumber, edtComplaintDetail ;
    String caller="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unloading_actity);

        linearLayout = (LinearLayout) findViewById(R.id.dealersLayout);
        edtIssueDate = (EditText) findViewById(R.id.edtIssueDate);
        edtComplaintNumber = (EditText) findViewById(R.id.edtComplaintNumber);
        edtComplaintDetail = (EditText) findViewById(R.id.edtComplaintDetails);

        MyDatePicker myDatePicker = new MyDatePicker(UnloadingNew.this,edtIssueDate);


        orderid = getIntent().getStringExtra("order");
        vehicle = getIntent().getStringExtra("vehicle");
        orderDate = getIntent().getStringExtra("orderDate");

        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");

        customers = new HashMap<>() ;
        unloadIssues = new HashMap<>() ;

        loadCustomers();

    }

    void loadCustomers(){
        final DatabaseReference custDB = FirebaseDatabase.getInstance().getReference("Customer");

        custDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                customers.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot custData: dataSnapshot.getChildren()){
                        Customer newCust = custData.getValue(Customer.class);
                        String key = custData.getKey();

                        customers.put(key,newCust);
                    }
                }
                loadDealers();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadDealers() {

        final Query delaerDB = FirebaseDatabase.getInstance().getReference("DealerOrder")
                                        .orderByChild("orderid")
                                        .equalTo(orderid);

        delaerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dealerData: dataSnapshot.getChildren()){
                        DealerOrder newOrder =  dealerData.getValue(DealerOrder.class);
                        String custName  = customers.get(newOrder.getCustomerid()).getName() ;
                        String display = custName + "\t" + newOrder.getTotalweight() + " Tons";
                        AddCheckBox(display);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void AddCheckBox(final String name){
        // Create Checkbox Dynamically
        CheckBox checkBox = new CheckBox(this);
        checkBox.setText(name);
        checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                unloadIssues.put(name,isChecked);

            }
        });

        // Add Checkbox to LinearLayout
        if (linearLayout != null) {
            linearLayout.addView(checkBox);
        }

    }

    public void shareMessage(View view) throws ParseException {

        String message = "Complaint No: " + edtComplaintNumber.getText();
        message += "\nVehicle No: " + vehicle ;
        for(String key:unloadIssues.keySet()){
            if(unloadIssues.get(key)){
                message += "\n" + key;
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
        Date orderdat = sdf.parse(orderDate);
        Date issuedat = sdf.parse(edtIssueDate.getText().toString());

        long diff = issuedat.getTime() - orderdat.getTime() ;
        int daysDiff = (int) (diff / (24 * 60 * 60 * 1000)) ;


        message += "\nWaiting Time: " + daysDiff + " days";
        message += "\nProblem:  " + edtComplaintDetail.getText() ;

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

        Intent i = new Intent(UnloadingNew.this, MappleLeafActivity.class);
        if(caller.equals("Employee"))
            i = new Intent(UnloadingNew.this, MappleEmployeeDashboard.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        //Functions.MyFunctions.displayMessage(getApplicationContext(),message);

    }
}
