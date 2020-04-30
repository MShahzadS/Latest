package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeDetails extends AppCompatActivity {

    TextView txtEmpName, txtEmpCNIC, txtEmpPassword, txtEmpContact, txtEmpAddress, txtEmpLocation;
    String empid ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_details);

        readViews();
        empid = getIntent().getStringExtra("employee") ;

        Query empDB = FirebaseDatabase.getInstance().getReference("Employee")
                .orderByKey()
                .equalTo(empid);

        empDB.addListenerForSingleValueEvent(getEmployeeData); ;
    }

    void readViews(){
        txtEmpName = (TextView) findViewById(R.id.txtEmployeeName) ;
        txtEmpCNIC = (TextView) findViewById(R.id.txtEmployeeCNIC) ;
        txtEmpPassword = (TextView) findViewById(R.id.txtEmployeePassword) ;
        txtEmpContact = (TextView) findViewById(R.id.txtEmployeeContact) ;
        txtEmpAddress = (TextView) findViewById(R.id.txtEmployeeAddress) ;
        txtEmpLocation = (TextView) findViewById(R.id.txtEmployeeLocation) ;
    }

    public void deleteEmployee(View view){
        FirebaseDatabase.getInstance().getReference("Employee")
                .child(empid).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if(databaseError == null){
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Employee Deleted Successfully");
                    Intent i = new Intent(EmployeeDetails.this, EmployeeActivity.class);
                    startActivity(i);
                    finish();
                }else {
                    Functions.MyFunctions.displayMessage(getApplicationContext(),"Employee could not be deleted successfully");
                }
            }
        });
    }

    ValueEventListener getEmployeeData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if(dataSnapshot.exists()){
                for(DataSnapshot empData: dataSnapshot.getChildren()) {

                    com.idc.wanharcarriage.classes.Employee newEmp = empData.getValue(com.idc.wanharcarriage.classes.Employee.class);

                    txtEmpName.setText(newEmp.getName());
                    txtEmpCNIC.setText(newEmp.getCnic());
                    txtEmpPassword.setText(newEmp.getPassword());
                    txtEmpContact.setText(newEmp.getContact());
                    txtEmpAddress.setText(newEmp.getAddress());
                    txtEmpLocation.setText(newEmp.getUsertype());

                }
            } else {
                Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading Failed Try Again");
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Data Loading cancelled");
        }
    };

}
