package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Driver;
import com.idc.wanharcarriage.classes.Expense;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, NewExpenseDialog.ExpenseDialogListener {

    ListView expenseList ;
    ArrayList<DataModel> expenseArray ;
    ArrayList<String> expenseIDs;
    EditText edtExpenseDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        expenseArray = new ArrayList<DataModel>();
        expenseIDs = new ArrayList<String>();
        edtExpenseDate = (EditText) findViewById(R.id.edtExpenseDate );
        expenseList = (ListView) findViewById(R.id.expenseList) ;
        expenseArray = new ArrayList<DataModel>();
        expenseIDs = new ArrayList<String>();

        MyDatePicker mStart = new MyDatePicker(ExpenseActivity.this, edtExpenseDate) ;

        edtExpenseDate.addTextChangedListener(searchExpense);
        loadExpenses();


    }

    void loadExpenses(){
        DatabaseReference expenseDB = FirebaseDatabase.getInstance().getReference("Expense");
        expenseDB.addValueEventListener(getExpenses);
    }

    ValueEventListener getExpenses = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            expenseArray.clear();
            expenseIDs.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot expenseData : dataSnapshot.getChildren()) {
                    Expense newExpense = expenseData.getValue(Expense.class);
                            expenseIDs.add(expenseData.getKey());
                            DataModel expenseModel = new DataModel(newExpense.getExpenseDate(), newExpense.getExpenseAmount());
                            expenseArray.add(expenseModel);
                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            }

            if(getApplicationContext() != null) {
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(), expenseArray);
                expenseList.setOnItemClickListener(ExpenseActivity.this);
                expenseList.setAdapter(myCustomListAdapter);
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(), "Process Cancelled");
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(ExpenseActivity.this, ExpenseDetails.class) ;
        i.putExtra("expense", expenseIDs.get(position));
        startActivity(i);

    }

    public void addExpense(View view) {
        NewExpenseDialog newDialog = new NewExpenseDialog();
        newDialog.show(getSupportFragmentManager(),"Add Expense");
    }

    @Override
    public void saveExpense(String date, String type, String amount) {

        SharedPreferences sharedPreferences = getSharedPreferences(Login.USERPREFERENCES, MODE_PRIVATE);
        String id =sharedPreferences.getString(Login.ID,"");
        Expense newExpense = new Expense(date,id,type,amount);

        DatabaseReference expenseDB = FirebaseDatabase.getInstance().getReference("Expense");

        String newid = expenseDB.push().getKey();

        expenseDB.child(newid).setValue(newExpense, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Expense Created Successfully");
                }
                else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(), "Expense could not be create. Try Again!");
                }
            }
        });
    }

    TextWatcher searchExpense = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            Query incomeDB = FirebaseDatabase.getInstance().getReference("Expense")
                    .orderByChild("expenseDate")
                    .equalTo(edtExpenseDate.getText().toString());
            incomeDB.addValueEventListener(getExpenses);

        }
    };
}
