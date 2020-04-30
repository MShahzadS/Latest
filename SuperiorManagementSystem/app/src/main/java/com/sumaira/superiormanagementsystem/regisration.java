package com.sumaira.superiormanagementsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class regisration extends AppCompatActivity {

    private DatabaseReference mDatabase;
    EditText edtname, edtemail, edtpassword, edtconfirm;
    RadioGroup rdUSerType;
    RadioButton rdSelected ;
    Button btnsignup;
    TextView txtnewlogin ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtname = (EditText) findViewById(R.id.edtname);
        edtemail = (EditText) findViewById(R.id.edtemail);
        edtpassword = (EditText) findViewById(R.id.edtpassword);
        edtconfirm = (EditText) findViewById(R.id.edtconfirmpassword);
        btnsignup = (Button) findViewById(R.id.btnsignup);
        rdUSerType = (RadioGroup) findViewById(R.id.rdUserType) ;
        txtnewlogin = (TextView) findViewById(R.id.txtnewlogin) ;




        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

             //   Toast.makeText(getApplicationContext(),"Hello",Toast.LENGTH_LONG).show() ;

                String name = edtname.getText().toString();
                String email = edtemail.getText().toString();
                String password = edtpassword.getText().toString();
                String confirmpassword = edtpassword.getText().toString();

                int selectedId = rdUSerType.getCheckedRadioButtonId();


                // find the radiobutton by returned id
                rdSelected = (RadioButton) findViewById(selectedId);

                int usertype = 0 ;
                String rdSelectedtext = rdSelected.getText().toString() ;

                if(rdSelectedtext.equals("Teacher"))
                    usertype = 1 ;

                if (password.equals(confirmpassword) != false) {

                    mDatabase = FirebaseDatabase.getInstance().getReference("users");
                    String id = mDatabase.push().getKey();



                    User myuser = new User(name, email, password, usertype );
                    mDatabase.child(id).setValue(myuser, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                            if (databaseError == null) {
                                Toast.makeText(getApplicationContext(), "Data Saved Successfully", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Sorry! Data cannot be saved", Toast.LENGTH_LONG).show();
                            }

                        }
                    });


                } else {
                    Toast.makeText(getApplicationContext(), "Password must match Confirm Password", Toast.LENGTH_LONG).show();
                }

            }
        });


        txtnewlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registr = new Intent(regisration.this, login.class);
                startActivity(registr);

            }
        });
    }
}
