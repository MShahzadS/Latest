package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idc.wanharcarriage.classes.Payment;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewPayment extends AppCompatActivity {


    EditText edtPaymentDate, edtPaidBy, edtPaidTo, edtPaymentDetail, edtPaymentAmount ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_payment);

        edtPaymentDate = (EditText) findViewById(R.id.edtPaymentDate);
        edtPaidBy = (EditText) findViewById(R.id.edtPaidBy);
        edtPaidTo = (EditText) findViewById(R.id.edtPaidTo);
        edtPaymentDetail = (EditText) findViewById(R.id.edtPaymentDetail);
        edtPaymentAmount = (EditText) findViewById(R.id.edtPaymentAmount);

        MyDatePicker mydate = new MyDatePicker(NewPayment.this, edtPaymentDate) ;

    }


    public void savePayment(View view) {

        String paymentDate = edtPaymentDate.getText().toString();
        String paidBy = edtPaidBy.getText().toString();
        String paidto = edtPaidTo.getText().toString();
        String paymentDetail =  edtPaymentDetail.getText().toString();
        int amount = Integer.valueOf(edtPaymentAmount.getText().toString());

        Payment newPayment = new Payment(paymentDate,paidBy,paidto,paymentDetail,amount);

        DatabaseReference paymentDB = FirebaseDatabase.getInstance().getReference("Payment");
        String newKey = paymentDB.push().getKey();
        paymentDB.child(newKey).setValue(newPayment,new DatabaseReference.CompletionListener(){

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Payment Saved Successfully");
                    Intent i = new Intent(NewPayment.this, PaymentActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else{
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Payment could not be saved. Try Again.....");
                }
            }
        });

    }
}
