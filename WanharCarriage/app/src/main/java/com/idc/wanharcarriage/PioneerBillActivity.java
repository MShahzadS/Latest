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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.PioneerBill;

import java.util.ArrayList;

public class PioneerBillActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView orderList ;
    ArrayList<DataModel> billsArray ;
    ArrayList<String> billIDS;
    FloatingActionButton btnAddBill ;
    EditText edtStartDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pioneer_bills);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        btnAddBill = (FloatingActionButton) findViewById(R.id.btnAddBill) ;
        edtStartDate = (EditText) findViewById(R.id.edtbillDate );
        orderList = (ListView) findViewById(R.id.pioneerBillList) ;
        billsArray = new ArrayList<DataModel>();
        billIDS = new ArrayList<>();

        MyDatePicker mStart = new MyDatePicker(PioneerBillActivity.this, edtStartDate) ;

        edtStartDate.addTextChangedListener(findOrder);

        btnAddBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(PioneerBillActivity.this, PioneerNewBill.class) ;
                startActivity(i);
            }
        });

        orderList.setOnItemClickListener(this);

        loadBills();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(PioneerBillActivity.this, PioneerBillDetails.class) ;
        i.putExtra("bill", billIDS.get(position));
        startActivity(i);
    }

    void loadBills(){

        DatabaseReference billDB = FirebaseDatabase.getInstance().getReference("PioneerBill");

        billDB.addValueEventListener(loadBillData);
    }

    ValueEventListener loadBillData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            billsArray.clear();
            billIDS.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot billData: dataSnapshot.getChildren()){

                    PioneerBill newBill = billData.getValue(PioneerBill.class);

                    DataModel billModel =  new DataModel(newBill.getBillDate(),String.valueOf(newBill.getTotalBill()));
                    billsArray.add(billModel);
                    billIDS.add(billData.getKey());
                }
            }

            if(getApplicationContext() != null) {
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(), billsArray);
                orderList.setAdapter(myCustomListAdapter);
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }

    };

    TextWatcher findOrder = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            Query billDB = FirebaseDatabase.getInstance().getReference("PioneerBill")
                                           .orderByChild("billDate")
                                            .equalTo(edtStartDate.getText().toString());
            billDB.addValueEventListener(loadBillData);

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

}
