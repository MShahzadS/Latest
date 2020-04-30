package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Admin;
import com.idc.wanharcarriage.classes.Employee;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;


public class Login extends AppCompatActivity {

    public static final String USERPREFERENCES = "USerPrefs";
    public static final String Name = "nameKey";
    public static final String Company = "companyKey";
    public static final String ID = "idKey";
    private static SharedPreferences sharedpreferences;
    EditText edtCNIC, edtPassword;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        edtCNIC = (EditText) findViewById(R.id.edtCNIC);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        try {
            if (isConnected()) {

                btnLogin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
                        isAdmin();
                    }
                });
            } else {
                Snackbar.make(btnLogin, "You are not connect to Internet. Connect and restart app", Snackbar.LENGTH_LONG).show();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    void isAdmin() {

        Query adminDB = FirebaseDatabase.getInstance().getReference("Admin");

        final String cnic = edtCNIC.getText().toString();

        adminDB.orderByChild("cnic")
                .equalTo(cnic);

        adminDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isFound = false;
                if (dataSnapshot.exists()) {
                    for (DataSnapshot adminData : dataSnapshot.getChildren()) {
                        Admin newAdmin = adminData.getValue(Admin.class);

                        if (newAdmin.getName().equals(cnic)) {
                            setUserpreferences(newAdmin.getName(), "Admin", adminData.getKey());
                            String pass = edtPassword.getText().toString();
                            isFound = true;
                            if (newAdmin.getStatus()) {
                                if (newAdmin.getPassword().equals(pass)) {

                                    Functions.MyFunctions.dismissDialog();
                                    Intent admin = new Intent(Login.this, AdminDashboard.class);
                                    startActivity(admin);
                                } else {
                                    Functions.MyFunctions.displayMessage(getApplicationContext(), "CNIC or Password is incorrect");
                                }
                            } else {
                                Functions.MyFunctions.displayMessage(getApplicationContext(), "Your Account is banned. Please contact Administrator");
                            }
                        }
                    }
                }
                if(!isFound){
                    isEmployee();
                }else{
                    Functions.MyFunctions.dismissDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void isEmployee() {

        final Query adminDB = FirebaseDatabase.getInstance().getReference("Employee");

        final String cnic = edtCNIC.getText().toString();

        adminDB.orderByChild("cnic")
                .equalTo(cnic);

        adminDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Boolean isFound = false;
                if (dataSnapshot.exists()) {

                    for (DataSnapshot empData : dataSnapshot.getChildren()) {
                        Employee newEmp = empData.getValue(Employee.class);
                        isFound = false;
                        String pass = edtPassword.getText().toString();
                        if (newEmp.getPassword().equals(pass)) {
                            isFound = true;
                            setUserpreferences(newEmp.getName(), newEmp.getUsertype(), empData.getKey());
                            if (newEmp.getUsertype().equals("Mapple")) {
                                Intent emp = new Intent(Login.this, MappleEmployeeDashboard.class);
                                startActivity(emp);
                            } else if (newEmp.getUsertype().equals("Pioneer")) {
                                Intent emp = new Intent(Login.this, PioneerEmployeeDashboard.class);
                                startActivity(emp);
                            } else if (newEmp.getUsertype().equals("Flying")) {
                                Intent emp = new Intent(Login.this, FlyingEmployeeDashboard.class);
                                startActivity(emp);
                            } else {
                                Functions.MyFunctions.displayMessage(getApplicationContext(), "You are not Registered Please contact your Administrator");
                            }

                        }
                    }
                    if(!isFound)
                        Functions.MyFunctions.displayMessage(getApplicationContext(), "CNIC or Password is incorrect");
                    Functions.MyFunctions.dismissDialog();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void setUserpreferences(String name, String company, String id) {
        sharedpreferences = getSharedPreferences(USERPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        editor.putString(Name, name);
        editor.putString(Company, Company);
        editor.putString(ID, id);
        editor.commit();
    }

    public boolean isConnected() throws InterruptedException, IOException {
        final String command = "ping -c 1 google.com";
        return Runtime.getRuntime().exec(command).waitFor() == 0;
    }

}
