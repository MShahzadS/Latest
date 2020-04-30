package com.sumaira.superiorcms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText edtname;
    EditText edtpassword;
    DatabaseReference userRecord;
    String pass ;

    public static final String MyPREFERENCES = "TeacherPrefs" ;
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        TextView txtnewaccount = (TextView) findViewById(R.id.txtnewaccount);
        Button btnlogin = (Button) findViewById(R.id.btnlogin);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtname = (EditText) findViewById(R.id.edtusername);
                edtpassword = (EditText) findViewById(R.id.edtpassword);
                pass = edtpassword.getText().toString() ;

                userRecord = FirebaseDatabase.getInstance().getReference("users") ;

                Query getUser = FirebaseDatabase.getInstance().getReference("users")
                        .orderByChild("email")
                        .equalTo(edtname.getText().toString().trim());
                getUser.addValueEventListener(valueEventListener);

            }
        });

        txtnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registrationForm = new Intent(Login.this, Registration.class);
                startActivity(registrationForm);
            }
        });
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            boolean isfound = false;
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User myuser = snapshot.getValue(User.class);

                    if (myuser.getPassword().equals(pass)) {

                        isfound = true;

                            SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();

                            editor.putString(Name, myuser.getName());
                            editor.putString(Email, myuser.getEmail());
                            editor.commit();
                           // Toast.makeText(getApplicationContext(),"Teacher Login", Toast.LENGTH_LONG).show();
                            Intent teacherDashboard = new Intent(Login.this, TeacherDashboard.class);
                            startActivity(teacherDashboard);

                        break;
                    }
                }
                if (isfound == false) {
                    Toast.makeText(getApplicationContext(), "Incorrect Password", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "User not found", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a problem", Toast.LENGTH_LONG).show();
        }
    };
}
