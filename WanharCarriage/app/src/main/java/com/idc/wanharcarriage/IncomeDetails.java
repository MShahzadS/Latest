package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Income;

public class IncomeDetails extends AppCompatActivity {

    TextView txtincomeDate, txtincomeAmount, txtIncomeCompany, txtEmployeeName, txtIncomeDetail ;
    Income income;
    String incomeid;
    String caller = "" ;
    String recevedBy = "Admin" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_details);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        txtincomeDate = (TextView) findViewById(R.id.txtIncomeDate) ;
        txtincomeAmount = (TextView) findViewById(R.id.txtIncomeAmount) ;
        txtIncomeCompany = (TextView) findViewById(R.id.txtIncomeCompany) ;
        txtIncomeDetail = (TextView) findViewById(R.id.txtIncomeDetail) ;
        txtEmployeeName = (TextView) findViewById(R.id.txtEmployeeName) ;

        incomeid = getIntent().getStringExtra("income");
        if(getIntent().hasExtra("caller")) {
            caller = getIntent().getStringExtra("caller");
            recevedBy = caller ;
        }

        loadIncome();
    }

    void loadIncome(){
        Query incomeDB = FirebaseDatabase.getInstance().getReference("Income")
                                        .orderByKey()
                                        .equalTo(incomeid);
        incomeDB.addListenerForSingleValueEvent(getIncome);
    }

    ValueEventListener getIncome = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot incomeData: dataSnapshot.getChildren()){

                    income = incomeData.getValue(Income.class);

                    txtincomeDate.setText(income.getIncomeDate());
                    txtincomeAmount.setText(String.valueOf(income.getAmount()));
                    txtIncomeCompany.setText(income.getCompany());
                    txtIncomeDetail.setText(income.getCompany());
                    txtEmployeeName.setText(recevedBy);
                }

                Functions.MyFunctions.dismissDialog();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void deleteIncome(View view) {

        DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
        incomeDB.child(incomeid).removeValue(new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Income deleted Successfully");
                    Intent i = new Intent(IncomeDetails.this, IncomeActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(IncomeDetails.this, EmployeeProfile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else
                {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Income could not be deleted. Try again...");
                }
            }
        });
    }
}
