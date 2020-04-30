package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Payment;

public class PaymentDetails extends AppCompatActivity {

    Payment payment;
    TextView txtPaymentDate, txtPaidBy, txtPaidTo, txtPaymentDetail, txtAmount;
    CheckBox chkPaymentStatus;
    String paymentid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_details);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        txtPaymentDate = (TextView) findViewById(R.id.txtPaymentDate);
        txtPaidBy = (TextView) findViewById(R.id.txtPaidBy);
        txtPaidTo = (TextView) findViewById(R.id.txtPaidTo);
        txtPaymentDetail = (TextView) findViewById(R.id.txtPaymentDetail);
        txtAmount = (TextView) findViewById(R.id.txtPaidAmount);

        paymentid = getIntent().getStringExtra("payment");
        chkPaymentStatus = (CheckBox) findViewById(R.id.chkPaymentStatus);

        chkPaymentStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference paymentDB = FirebaseDatabase.getInstance().getReference("Payment");
                payment.setPaymentStatus(isChecked);
                paymentDB.child(paymentid).setValue(payment);
            }
        });

        loadPayment();


    }

    void loadPayment(){
        Query paymentDB = FirebaseDatabase.getInstance().getReference("Payment")
                                        .orderByKey()
                                        .equalTo(paymentid);
        paymentDB.addListenerForSingleValueEvent(getPayment);
    }
    ValueEventListener getPayment = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot paymentData: dataSnapshot.getChildren()){
                    payment = paymentData.getValue(Payment.class);

                    txtPaymentDate.setText(payment.getPaymentDate());
                    txtPaidBy.setText(payment.getPaidBy());
                    txtPaidTo.setText(payment.getPaidTo());
                    txtPaymentDetail.setText(payment.getPaymentReason());
                    txtAmount.setText(String.valueOf(payment.getAmount()));
                    chkPaymentStatus.setChecked(payment.getPaymentStatus());

                }
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void deletePayment(View view) {
        DatabaseReference paymentDB = FirebaseDatabase.getInstance().getReference("Payment");
        paymentDB.child(paymentid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Payment Deleted Successfully");
                    Intent i = new Intent(PaymentDetails.this, PaymentActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Payment could not be deleted. Try Again...");
                }
            }
        });
    }
}
