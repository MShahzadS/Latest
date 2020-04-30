package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;

public class CustomerDetails extends AppCompatActivity {

    TextView btnCustomerOrders, txtCustomerName, txtCustomerContact, txtCustomerAddres, txtCustomerCompany ;
    String custid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);

        custid = getIntent().getStringExtra("customer") ;

        
        txtCustomerName = (TextView) findViewById(R.id.txtCustomerName) ;
        txtCustomerContact = (TextView) findViewById(R.id.txtCustomerContac) ;
        txtCustomerAddres = (TextView) findViewById(R.id.txtCustomerAddress) ;
        txtCustomerCompany = (TextView) findViewById(R.id.txtCustomerCompany) ;


        Query custDB = FirebaseDatabase.getInstance().getReference("Customer")
                .orderByKey()
                .equalTo(custid);
        custDB.addListenerForSingleValueEvent(getCustomerDetails); ;
    }

    public void deleteCustomer(View view){

                FirebaseDatabase.getInstance().getReference("Customer")
                        .child(custid).removeValue(new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                        if(databaseError == null){
                            Functions.MyFunctions.displayMessage(getApplicationContext(),"Customer Deleted Successfully");
                            Intent i = new Intent(CustomerDetails.this, CustomerActivity.class);
                            startActivity(i);
                            finish();
                        }else {
                            Functions.MyFunctions.displayMessage(getApplicationContext(),"Customer could not be deleted successfully");
                        }
                    }
                });
            }

    ValueEventListener getCustomerDetails = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {

                    Customer newCust = empData.getValue(Customer.class);

                    txtCustomerName.setText(newCust.getName());
                    txtCustomerContact.setText(newCust.getContact());
                    txtCustomerAddres.setText(newCust.getAddress());
                    txtCustomerCompany.setText(newCust.getCompany());
                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading Failed Try Again");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

}
