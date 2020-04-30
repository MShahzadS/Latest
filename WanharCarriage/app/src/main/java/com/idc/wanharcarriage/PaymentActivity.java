package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Payment;

import java.util.ArrayList;

public class PaymentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView paymentList ;
    ArrayList<DataModel> paymentArray ;
    ArrayList<String> paymentIDs ;
    FloatingActionButton btnAddPayment ;
    EditText edtPaymentDate ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);


        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        edtPaymentDate = (EditText) findViewById(R.id.edtPaymentsDate );
        btnAddPayment = (FloatingActionButton) findViewById(R.id.btnAddPayment);
        paymentList = (ListView) findViewById(R.id.paymentList) ;

        paymentArray = new ArrayList<DataModel>();
        paymentIDs = new ArrayList<String>();


        MyDatePicker mStart = new MyDatePicker(PaymentActivity.this, edtPaymentDate) ;
        edtPaymentDate.addTextChangedListener(searchPayments);
        paymentList.setOnItemClickListener(this);

        btnAddPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PaymentActivity.this, NewPayment.class) ;
                startActivity(i);
            }
        });

        loadPayments();
    }

    void loadPayments(){
        DatabaseReference paymentDB = FirebaseDatabase.getInstance().getReference("Payment");
        paymentDB.addValueEventListener(getPayments);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(PaymentActivity.this, PaymentDetails.class) ;
        i.putExtra("payment", paymentIDs.get(position));
        startActivity(i);
    }

    ValueEventListener getPayments = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            paymentArray.clear();
            paymentIDs.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot paymentData: dataSnapshot.getChildren()){
                    Payment newPayment = paymentData.getValue(Payment.class);
                    paymentIDs.add(paymentData.getKey());

                    DataModel paymentModel = new DataModel(newPayment.getPaymentDate(), newPayment.getPaidBy());
                    paymentArray.add(paymentModel);
                }
            }

            if(getApplicationContext() != null){
                MyCustomListAdapter myAdapter = new MyCustomListAdapter(getApplicationContext(),paymentArray);
                paymentList.setAdapter(myAdapter);
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    TextWatcher searchPayments = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Query paymentDB = FirebaseDatabase.getInstance().getReference("Payment")
                                            .orderByChild("paymentDate")
                                            .equalTo(edtPaymentDate.getText().toString());
            paymentDB.addValueEventListener(getPayments);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
