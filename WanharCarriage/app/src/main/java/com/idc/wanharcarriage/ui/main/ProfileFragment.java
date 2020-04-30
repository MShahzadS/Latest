package com.idc.wanharcarriage.ui.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.Functions;
import com.idc.wanharcarriage.Login;
import com.idc.wanharcarriage.R;
import com.idc.wanharcarriage.classes.Employee;

public class ProfileFragment extends Fragment {

    TextView txtEmployeeName, txtEmployeeCNIC, txtEmployeePassword, txtEmployeeContact,
            txtEmployeeAddress, txtEmployeeLocation;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile,container,false);

        txtEmployeeName = (TextView) root.findViewById(R.id.txtEmployeeName);
        txtEmployeeCNIC = (TextView) root.findViewById(R.id.txtEmployeeCNIC);
        txtEmployeePassword = (TextView) root.findViewById(R.id.txtEmployeePassword);
        txtEmployeeContact = (TextView) root.findViewById(R.id.txtEmployeeContact);
        txtEmployeeAddress = (TextView) root.findViewById(R.id.txtEmployeeAddress);
        txtEmployeeLocation = (TextView) root.findViewById(R.id.txtEmployeeLocation);

        loadEmployee();
        return root;
    }

    void loadEmployee(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.USERPREFERENCES, getActivity().MODE_PRIVATE);
        String empid =sharedPreferences.getString(Login.ID,"");
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
                        txtEmployeeCNIC.setText(newEmp.getCnic());
                        txtEmployeeContact.setText(newEmp.getContact());
                        txtEmployeePassword.setText(newEmp.getPassword());
                        txtEmployeeAddress.setText(newEmp.getAddress());
                        txtEmployeeLocation.setText(newEmp.getUsertype());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
