package com.shaheen.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword ;
    Button btnRegister ;
    TextView txtLogin ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        edtName = (EditText) findViewById(R.id.edtName) ;
        edtEmail = (EditText) findViewById(R.id.edtEmail) ;
        edtPassword = (EditText) findViewById(R.id.edtPassword) ;
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtLogin = (TextView) findViewById(R.id.txtLogin);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edtName.getText().toString() ;
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                User newuser = new User("",name,email,password);

                DatabaseReference UserDb =  FirebaseDatabase.getInstance().getReference("User");

                String newkey = UserDb.push().getKey();

                UserDb.child(newkey).setValue(newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Registration Successfull! Login Now...", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "There was a prolem in Registration! Try Again...", Toast.LENGTH_LONG).show();
                    }
                });


            }
        });

        txtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(login);
            }
        });
    }

}
