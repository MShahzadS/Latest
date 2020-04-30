package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.Employee;

import java.util.ArrayList;
import java.util.Random;

public class CustomerActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , NewCustomerDialog.MyCustomerDialogListener {

    ListView customerList ;
    ArrayList<String> customerIDs;
    EditText edtSearchCustomer ;
    ArrayList<DataModel> customerArray ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        readView() ;
        edtSearchCustomer.addTextChangedListener(searchCustomer);
        if(edtSearchCustomer.getText().toString().isEmpty()){
            DatabaseReference custDB = FirebaseDatabase.getInstance().getReference("Customer");
            custDB.addValueEventListener(getCustomerData) ;
        }

    }

    void readView() {
        customerArray = new ArrayList<DataModel>();
        customerIDs = new ArrayList<String>();
        customerList = (ListView) findViewById(R.id.customerList) ;
        edtSearchCustomer = (EditText) findViewById(R.id.edtSearchCustomer) ;
    }
    public void openCustomerDialog (View view){
        NewCustomerDialog myDialog = new NewCustomerDialog();
        myDialog.show(getSupportFragmentManager(), "Add New Customer");
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(CustomerActivity.this, CustomerDetails.class) ;
        i.putExtra("customer", customerIDs.get(position));
        startActivity(i);
    }

    @Override
    public void saveCustomer(Customer cust, String customerID, String message) {

        Functions.MyFunctions.displayMessage(getApplicationContext(),message);
    }

    ValueEventListener getCustomerData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            customerArray.clear();
            customerIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {
                    Customer newCust = empData.getValue(Customer.class);
                    String sname = edtSearchCustomer.getText().toString().toLowerCase() ;
                    if(!sname.isEmpty()) {

                        if(newCust.getName().toLowerCase().startsWith(sname)) {
                            customerIDs.add(empData.getKey());
                            DataModel custModel = new DataModel(newCust.getName(), newCust.getContact());
                            customerArray.add(custModel);
                        }
                    }else
                    {
                        customerIDs.add(empData.getKey());
                        DataModel custModel = new DataModel(newCust.getName(), newCust.getContact());
                        customerArray.add(custModel);
                    }
                }
               // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            } else {

                Functions.MyFunctions.displayMessage(getApplicationContext(),"No Customer Registered");
            }

            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(),customerArray);
            customerList.setOnItemClickListener(CustomerActivity.this);
            customerList.setAdapter(myCustomListAdapter);
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    TextWatcher searchCustomer = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            DatabaseReference custDB = FirebaseDatabase.getInstance().getReference("Customer");
            custDB.addValueEventListener(getCustomerData);


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
