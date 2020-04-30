package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.idc.wanharcarriage.classes.Employee;
import com.idc.wanharcarriage.classes.Expense;
import com.idc.wanharcarriage.classes.Income;

public class ExpenseDetails extends AppCompatActivity {

    TextView txtExpenseDate, txtExpenseAmount, txtExpenseType, txtEmployeeName ;
    Expense expense;
    String expenseid;
    String caller = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_details);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        txtExpenseDate = (TextView) findViewById(R.id.txtExpenseDate) ;
        txtExpenseAmount = (TextView) findViewById(R.id.txtExpenseAmount) ;
        txtExpenseType = (TextView) findViewById(R.id.txtExpenseType) ;
        txtEmployeeName = (TextView) findViewById(R.id.txtEmployeeName) ;

        expenseid = getIntent().getStringExtra("expense");
        if(getIntent().hasExtra("caller"))
            caller = getIntent().getStringExtra("caller");

        loadExpense();
    }

    void loadExpense(){
        Query incomeDB = FirebaseDatabase.getInstance().getReference("Expense")
                .orderByKey()
                .equalTo(expenseid);
        incomeDB.addListenerForSingleValueEvent(getExpense);
    }

    void loadEmployee(String empid){

        Query empDB = FirebaseDatabase.getInstance().getReference("Employee")
                .orderByKey()
                .equalTo(empid);
        empDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot empData: dataSnapshot.getChildren()){
                        Employee newEmp = empData.getValue(Employee.class);

                        txtEmployeeName.setText(newEmp.getName());
                    }
                }else {
                    txtEmployeeName.setText("Admin");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    ValueEventListener getExpense = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot expenseData: dataSnapshot.getChildren()){

                    expense = expenseData.getValue(Expense.class);

                    txtExpenseDate.setText(expense.getExpenseDate());
                    txtExpenseAmount.setText(String.valueOf(expense.getExpenseAmount()));
                    txtExpenseType.setText(expense.getExpenseType());
                    loadEmployee(expense.getExpenseEmployee());
                }

                Functions.MyFunctions.dismissDialog();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    public void deleteExpense(View view) {

        DatabaseReference expenseDB = FirebaseDatabase.getInstance().getReference("Expense");
        expenseDB.child(expenseid).removeValue(new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Expense deleted Successfully");
                    Intent i = new Intent(ExpenseDetails.this, ExpenseActivity.class);
                    if(caller.equals("Employee"))
                        i = new Intent(ExpenseDetails.this, EmployeeProfile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }else
                {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Expense could not be deleted. Try again...");
                }
            }
        });
    }
}
