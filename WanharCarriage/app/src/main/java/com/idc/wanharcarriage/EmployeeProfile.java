package com.idc.wanharcarriage;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idc.wanharcarriage.classes.Expense;
import com.idc.wanharcarriage.ui.main.SectionsPagerAdapter;

public class EmployeeProfile extends AppCompatActivity implements NewExpenseDialog.ExpenseDialogListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


        /*
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */

    }

    public void addExpense(View view) {
        NewExpenseDialog newDialog = new NewExpenseDialog();
        newDialog.show(getSupportFragmentManager(),"Add Expense");
    }

    @Override
    public void saveExpense(String date, String type, String amount) {
        SharedPreferences sharedPreferences = getSharedPreferences(Login.USERPREFERENCES,MODE_PRIVATE);
        String empid =sharedPreferences.getString(Login.ID,"");
        Expense newExpense = new Expense(date,empid,type,amount);

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
}