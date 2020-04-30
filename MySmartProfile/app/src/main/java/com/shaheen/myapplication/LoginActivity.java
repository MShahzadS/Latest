package com.shaheen.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {


    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Name = "nameKey";
    public static final String userid = "idkey";

    EditText edtEmail, edtPassword ;
    Button btnLogin ;
    TextView txtRegister ;
    String email, password ;

    SharedPreferences sharedpreferences  ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edtEmail = (EditText) findViewById(R.id.edtEmail) ;
        edtPassword = (EditText) findViewById(R.id.edtPassword) ;
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtRegister = (TextView) findViewById(R.id.txtRegister) ;

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = edtEmail.getText().toString() ;
                password = edtPassword.getText().toString();

                Query UserDb = FirebaseDatabase.getInstance().getReference("User").
                        orderByChild("email")
                        .equalTo(email);

                UserDb.addValueEventListener(userListener) ;
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registration = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registration);
            }
        });
    }

    ValueEventListener userListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    User newuser = ds.getValue(User.class);

                    if(newuser.getPassword().equals(password))
                    {


                        SharedPreferences.Editor editor = sharedpreferences.edit();

                        editor.putString(Name, newuser.getName());
                        editor.putString(userid, ds.getKey());
                        editor.commit();
                       // Toast.makeText(getApplicationContext(),"Login Successfull", Toast.LENGTH_SHORT).show();
                        Intent dashboard = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(dashboard);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),"Password incorrect", Toast.LENGTH_LONG).show();
                    }
                }
            }
            else
            {
                Toast.makeText(getApplicationContext(),"User not Found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(),"Connection in Problem", Toast.LENGTH_LONG).show();
        }
    };
}
