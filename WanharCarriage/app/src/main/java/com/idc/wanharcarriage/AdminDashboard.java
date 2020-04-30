package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Expense;
import com.idc.wanharcarriage.classes.Income;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AdminDashboard extends AppCompatActivity implements View.OnClickListener {

    LinearLayout btnIncome, btnExpense, btnPayments, btnCustomers, btnDrivers, btnBuses, btnUsers;
    TextView txtTotalIncome, txtDailyIncome, txtTotalExpense, txtDailyExpense, txtProfit;
    Button btnMappleLeaf, btnPioneer, btnFlying;
    long income, expense, dailyExpense, dailyIncome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        btnMappleLeaf = (Button) findViewById(R.id.btnMappleLeaf);
        btnPioneer = (Button) findViewById(R.id.btnPioneer);
        btnFlying = (Button) findViewById(R.id.btnFlying);

        btnIncome = (LinearLayout) findViewById(R.id.btnIncome);
        btnExpense = (LinearLayout) findViewById(R.id.btnExpense);
        btnPayments = (LinearLayout) findViewById(R.id.btnPayemnts);
        btnCustomers = (LinearLayout) findViewById(R.id.btnCustomer);
        btnDrivers = (LinearLayout) findViewById(R.id.btnDrivers);
        btnBuses = (LinearLayout) findViewById(R.id.btnBuses);
        btnUsers = (LinearLayout) findViewById(R.id.btnUsers);

        txtDailyIncome = (TextView) findViewById(R.id.txtDailyIncome);
        txtTotalIncome = (TextView) findViewById(R.id.txtTotalIncome);
        txtDailyExpense = (TextView) findViewById(R.id.txtDailyExpense);
        txtTotalExpense = (TextView) findViewById(R.id.txtTotalExpense);
        txtProfit = (TextView) findViewById(R.id.txtTotalProfit);

        btnMappleLeaf.setOnClickListener(this);
        btnPioneer.setOnClickListener(this);
        btnFlying.setOnClickListener(this);
        btnIncome.setOnClickListener(this);
        btnExpense.setOnClickListener(this);
        btnPayments.setOnClickListener(this);
        btnCustomers.setOnClickListener(this);
        btnDrivers.setOnClickListener(this);
        btnBuses.setOnClickListener(this);
        btnUsers.setOnClickListener(this);

        getIncome();
        SharedPreferences sharedPreferences = getSharedPreferences(Login.USERPREFERENCES, MODE_PRIVATE);
        String name =sharedPreferences.getString(Login.Name,"");
        this.setTitle("Welcome " + name);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.btnMappleLeaf:
                i = new Intent(AdminDashboard.this, MappleLeafActivity.class);
                startActivity(i);
                break;
            case R.id.btnPioneer:
                i = new Intent(AdminDashboard.this, Pioneer.class);
                startActivity(i);
                break;
            case R.id.btnFlying:
                i = new Intent(AdminDashboard.this, FlyingActivity.class);
                startActivity(i);
                break;
            case R.id.btnIncome:
                i = new Intent(AdminDashboard.this, IncomeActivity.class);
                startActivity(i);
                break;
            case R.id.btnExpense:
                i = new Intent(AdminDashboard.this, ExpenseActivity.class);
                startActivity(i);
                break;
            case R.id.btnPayemnts:
                i = new Intent(AdminDashboard.this, PaymentActivity.class);
                startActivity(i);
                break;
            case R.id.btnCustomer:
                i = new Intent(AdminDashboard.this, CustomerActivity.class);
                startActivity(i);
                break;
            case R.id.btnDrivers:
                i = new Intent(AdminDashboard.this, DriverActivity.class);
                startActivity(i);
                break;
            case R.id.btnBuses:
                i = new Intent(AdminDashboard.this, VehicleActivity.class);
                startActivity(i);
                break;
            case R.id.btnUsers:
                i = new Intent(AdminDashboard.this, EmployeeActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent addprofile = null;
        if (item.getItemId() == R.id.action_logout) {
            addprofile = new Intent(AdminDashboard.this, Login.class);
            startActivity(addprofile);
        }
        return super.onOptionsItemSelected(item);
    }

    void getIncome(){

        DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
        incomeDB.addValueEventListener(
        new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                income = 0;
                if(dataSnapshot.exists()){
                    for(DataSnapshot incomeData: dataSnapshot.getChildren()){

                        Income newincome = incomeData.getValue(Income.class);
                        income += newincome.getAmount();
                    }
                }
                txtTotalIncome.setText(String.valueOf(income));
                getDailyIncome();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void getDailyIncome(){

        Query incomeDB = FirebaseDatabase.getInstance().getReference("Income")
                                    .orderByChild("incomeDate")
                                    .equalTo(getDate());
        incomeDB.addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        dailyIncome = 0;
                        if(dataSnapshot.exists()){
                            for(DataSnapshot incomeData: dataSnapshot.getChildren()){

                                Income newincome = incomeData.getValue(Income.class);
                                dailyIncome += newincome.getAmount();
                            }
                        }
                        txtDailyIncome.setText(String.valueOf(dailyIncome));
                        getExpense();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    void getExpense(){
        DatabaseReference expenseDB = FirebaseDatabase.getInstance().getReference("Expense");
        expenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                expense = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseData : dataSnapshot.getChildren()) {
                        Expense newExpense = expenseData.getValue(Expense.class);
                        expense += Long.valueOf(newExpense.getExpenseAmount());
                    }
                    // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
                }
                txtTotalExpense.setText(String.valueOf(expense));
                getDailyExpense();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Functions.MyFunctions.displayMessage(getApplicationContext(), "Process Cancelled");
            }
        });

    }

    void getDailyExpense(){
        Query expenseDB = FirebaseDatabase.getInstance().getReference("Expense")
                .orderByChild("expenseDate")
                .equalTo(getDate());
        expenseDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                dailyExpense = 0;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot expenseData : dataSnapshot.getChildren()) {
                        Expense newExpense = expenseData.getValue(Expense.class);
                        dailyExpense += Long.valueOf(newExpense.getExpenseAmount());
                    }
                    // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
                }
                txtDailyExpense.setText(String.valueOf(dailyExpense));
                txtProfit.setText(String.valueOf(income - expense));
                Functions.MyFunctions.dismissDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Functions.MyFunctions.displayMessage(getApplicationContext(), "Process Cancelled");
            }
        });
    }

    String getDate(){
        Date c  = Calendar.getInstance().getTime() ;
        SimpleDateFormat df = new SimpleDateFormat("dd/mm/yyyy") ;
        return df.format(c);
    }
}
