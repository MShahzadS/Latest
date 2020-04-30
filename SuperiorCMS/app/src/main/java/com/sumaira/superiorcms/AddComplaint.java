package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddComplaint extends AppCompatActivity {

    Button btnAddComplaint;
    EditText edtComplaintAgainst, edtComplaintDetails ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complaint);

        btnAddComplaint = (Button) findViewById(R.id.btnAddComplaint) ;
        edtComplaintAgainst = (EditText) findViewById(R.id.edtComplaintAgainst) ;
        edtComplaintDetails = (EditText) findViewById(R.id.edtComplaintDetails);

        btnAddComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences myprefs = getSharedPreferences(StudentLogin.MyPREFERENCES, Context.MODE_PRIVATE);
                String studentid = myprefs.getString(StudentLogin.Name,"");
                String defaultername = edtComplaintAgainst.getText().toString() ;
                String details = edtComplaintDetails.getText().toString() ;

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                String formattedDate = df.format(c);

                Complaint newcomp = new Complaint(studentid,formattedDate,defaultername,details);

                DatabaseReference complaintDb = FirebaseDatabase.getInstance().getReference("complaints");

                String newkey = complaintDb.getKey() ;

                complaintDb.child(newkey).setValue(newcomp).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Complaint Submitted Successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Problem in Submitting Complant", Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }
}
