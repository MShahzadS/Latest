package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idc.wanharcarriage.classes.Employee;

public class NewEmployeeActivity extends AppCompatActivity {

    EditText edtEmployeeName, edtEmployeeCNIC, edtEmployeePassword, edtEmployeeContact, edtEmployeeAddress ;
    Spinner compinesSpiner;
    Button btnSaveEmployee ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        readViews();
    }


    public void saveEmployeeData(View view){

            if(isValid()){

                String name = edtEmployeeName.getText().toString() ;
                String cnic = edtEmployeeCNIC.getText().toString() ;
                String password = edtEmployeePassword.getText().toString() ;
                String contact = edtEmployeeContact.getText().toString() ;
                String address = edtEmployeeAddress.getText().toString() ;
                String company = compinesSpiner.getSelectedItem().toString() ;

                Employee newEmp = new Employee(name,cnic,password,contact,address,company) ;

                DatabaseReference employeeDB = FirebaseDatabase.getInstance().getReference("Employee");

                String newid = employeeDB.push().getKey() ;

                employeeDB.child(newid).setValue(newEmp, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if(databaseError == null){

                                Functions.MyFunctions.displayMessage(getApplicationContext(),"Employee saved Successfully");
                                Intent i = new Intent(NewEmployeeActivity.this, EmployeeActivity.class);
                                startActivity(i);
                                clearData();
                            }else {
                                Functions.MyFunctions.displayMessage(getApplicationContext(),"There was a problem creating Employee. Try Again!");
                            }
                    }
                });

                }

        }

    void readViews() {
        edtEmployeeName = (EditText) findViewById(R.id.edtEmployeeName) ;
        edtEmployeeCNIC = (EditText) findViewById(R.id.edtEmployeeCNIC) ;
        edtEmployeePassword = (EditText) findViewById(R.id.edtEmployeePassword) ;
        edtEmployeeContact = (EditText) findViewById(R.id.edtEmployeeContact) ;
        edtEmployeeAddress = (EditText) findViewById(R.id.edtEmployeeAddress) ;
        compinesSpiner = (Spinner) findViewById(R.id.companiesSpinner) ;
        btnSaveEmployee = (Button) findViewById(R.id.btnSaveEmployee) ;
    }

    void clearData(){
        edtEmployeeName.setText("");
        edtEmployeePassword.setText("");
        edtEmployeeCNIC.setText("");
        edtEmployeeContact.setText("");
        edtEmployeeAddress.setText("");
        compinesSpiner.setSelection(0);
    }

    Boolean isValid() {
        if(isEmpty(edtEmployeeName) || isEmpty(edtEmployeeCNIC) ||
                isEmpty(edtEmployeePassword) || isEmpty(edtEmployeeContact))
        {
            Functions.MyFunctions.displayMessage(getApplicationContext(),"Please fill out required fields");
            return false ;
        }

        return true ;
    }

    Boolean isEmpty(EditText edt)
    {
        if(TextUtils.isEmpty(edt.getText().toString()))
        {
            edt.setBackgroundColor(getResources().getColor(R.color.colorRed));
            return true;
        }
        return false ;
    }

}
