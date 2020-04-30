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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Employee;


import java.util.ArrayList;

public class EmployeeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView userList ;
    ArrayList<String> employeeIDs;
    ArrayList<DataModel> userArray ;
    EditText edtSearchEmployee ;
    DatabaseReference empDB ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());

        readViews();
        edtSearchEmployee.addTextChangedListener(searchEmployee);
        if(edtSearchEmployee.getText().toString().isEmpty()){
            empDB = FirebaseDatabase.getInstance().getReference("Employee");
            empDB.addValueEventListener(getEmployeeData);
        }
    }

    void readViews(){
        edtSearchEmployee = (EditText) findViewById(R.id.edtSearchEmployee) ;
        userList = (ListView) findViewById(R.id.userList) ;
        userArray = new ArrayList<DataModel>();
        employeeIDs = new ArrayList<String>();
    }

    public void addEmployeeBtnClick(View view){
        Intent i = new Intent(EmployeeActivity.this, NewEmployeeActivity.class);
        startActivity(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent empDetails = new Intent(EmployeeActivity.this, EmployeeDetails.class) ;
        empDetails.putExtra("employee", employeeIDs.get(position)) ;
        startActivity(empDetails);
    }

    ValueEventListener getEmployeeData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            userArray.clear();
            employeeIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {

                    Employee newEmp = empData.getValue(Employee.class);
                    String ename = edtSearchEmployee.getText().toString().toLowerCase();
                    if(ename.isEmpty()) {
                        employeeIDs.add(empData.getKey());
                        DataModel empModel = new DataModel(newEmp.getName(), newEmp.getContact());
                        userArray.add(empModel);
                    }else {
                        if(newEmp.getName().toLowerCase().startsWith(ename)) {
                            employeeIDs.add(empData.getKey());
                            DataModel empModel = new DataModel(newEmp.getName(), newEmp.getContact());
                            userArray.add(empModel);
                        }
                    }
                }
               // Functions.MyFunctions.displayMessage(edtSearchEmployee,"Data Loaded Successfully");
            } else {

                Functions.MyFunctions.displayMessage(getApplicationContext(),"No Employee Registered");
            }

            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(),userArray);
            userList.setOnItemClickListener(EmployeeActivity.this);
            userList.setAdapter(myCustomListAdapter);

            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

    TextWatcher searchEmployee = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            DatabaseReference empQuery = FirebaseDatabase.getInstance().getReference("Employee");
            empQuery.addValueEventListener(getEmployeeData);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    } ;

}
