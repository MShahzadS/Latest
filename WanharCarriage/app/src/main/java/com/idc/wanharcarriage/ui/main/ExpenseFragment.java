package com.idc.wanharcarriage.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.DataModel;
import com.idc.wanharcarriage.ExpenseActivity;
import com.idc.wanharcarriage.ExpenseDetails;
import com.idc.wanharcarriage.Functions;
import com.idc.wanharcarriage.Login;
import com.idc.wanharcarriage.MyCustomListAdapter;
import com.idc.wanharcarriage.MyDatePicker;
import com.idc.wanharcarriage.NewExpenseDialog;
import com.idc.wanharcarriage.R;
import com.idc.wanharcarriage.classes.Expense;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView expenseList ;
    ArrayList<DataModel> expenseArray ;
    ArrayList<String> expenseIDs;
    EditText edtExpenseDate ;
    Boolean dateChanged =false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_expense,container,false);

        Functions.MyFunctions.displayDialog(getParentFragmentManager());
        expenseArray = new ArrayList<DataModel>();
        expenseIDs = new ArrayList<String>();
        edtExpenseDate = (EditText) root.findViewById(R.id.edtExpenseDate );
        expenseList = (ListView) root.findViewById(R.id.expenseList) ;
        expenseArray = new ArrayList<DataModel>();
        expenseIDs = new ArrayList<String>();


        MyDatePicker mStart = new MyDatePicker(getActivity(), edtExpenseDate) ;



        edtExpenseDate.addTextChangedListener(searchExpense);
        loadExpenses();



        return root ;
    }

    void loadExpenses(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.USERPREFERENCES, getActivity().MODE_PRIVATE);
        String empid =sharedPreferences.getString(Login.ID,"");
        Query expenseDB = FirebaseDatabase.getInstance().getReference("Expense")
                                        .orderByChild("expenseEmployee")
                                        .equalTo(empid);
        expenseDB.addValueEventListener(getExpenses);
    }

    ValueEventListener getExpenses = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            expenseArray.clear();
            expenseIDs.clear();

            if (dataSnapshot.exists()) {
                for (DataSnapshot expenseData : dataSnapshot.getChildren()) {

                    Expense newExpense = expenseData.getValue(Expense.class);;
                    DataModel expenseModel = new DataModel(newExpense.getExpenseDate(), newExpense.getExpenseAmount());
                    expenseIDs.add(expenseData.getKey());
                    expenseArray.add(expenseModel);
                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            }

            if(getActivity() != null) {
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getActivity(), expenseArray);
                expenseList.setOnItemClickListener(ExpenseFragment.this);
                expenseList.setAdapter(myCustomListAdapter);
            }
            //if(dateChanged) dateChanged = false;
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getActivity(), "Process Cancelled");
        }
    };

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(getActivity(), ExpenseDetails.class) ;
        i.putExtra("expense", expenseIDs.get(position));
        i.putExtra("caller", "Employee");
        startActivity(i);

    }

    public void addExpense(View view) {
        NewExpenseDialog newDialog = new NewExpenseDialog();
        newDialog.show(getParentFragmentManager(),"Add Expense");
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
            Query expenseDB = FirebaseDatabase.getInstance().getReference("Expense")
                                                .orderByChild("expenseDate")
                                                .equalTo(edtExpenseDate.getText().toString());
            expenseDB.addValueEventListener(getDateExpenses);

        }
    };

    ValueEventListener getDateExpenses = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            expenseArray.clear();
            expenseIDs.clear();

            if (dataSnapshot.exists()) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.USERPREFERENCES, getActivity().MODE_PRIVATE);
                String empid =sharedPreferences.getString(Login.ID,"");
                for (DataSnapshot expenseData : dataSnapshot.getChildren()) {

                    Expense newExpense = expenseData.getValue(Expense.class);;

                    if(newExpense.getExpenseEmployee().equals(empid)) {
                        DataModel expenseModel = new DataModel(newExpense.getExpenseDate(), newExpense.getExpenseAmount());
                        expenseIDs.add(expenseData.getKey());
                        expenseArray.add(expenseModel);
                    }
                }
                // Functions.MyFunctions.displayMessage(customerList,"Data Loaded Successfully");
            }

            if(getActivity() != null) {
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getActivity(), expenseArray);
                expenseList.setOnItemClickListener(ExpenseFragment.this);
                expenseList.setAdapter(myCustomListAdapter);
            }
            //if(dateChanged) dateChanged = false;
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getActivity(), "Process Cancelled");
        }
    };
}
