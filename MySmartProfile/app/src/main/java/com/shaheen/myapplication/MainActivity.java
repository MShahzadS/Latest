package com.shaheen.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  {

    ArrayList<Profile> profiles ;
    ListView profileslist ;
    Button btnAddprofile ;
    SharedPreferences sharedprefs ;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profileslist = (ListView) findViewById(R.id.listprofiles) ;
        profiles = new ArrayList<Profile>();
        btnAddprofile = (Button) findViewById(R.id.btnAddProfile);


        if(!Settings.System.canWrite(getApplicationContext())) {
            Intent i = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
            startActivity(i);
        }


        sharedprefs = getSharedPreferences(LoginActivity.MyPREFERENCES, MODE_PRIVATE);
        String userid = sharedprefs.getString(LoginActivity.userid,"");

       // Toast.makeText(getApplicationContext(),userid,Toast.LENGTH_SHORT).show();
        Query profileDb = FirebaseDatabase.getInstance().getReference("Profile")
                .orderByChild("userid")
                .equalTo(userid);

        profileDb.addValueEventListener(profileEvent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    ValueEventListener profileEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                profiles.clear();

                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    Profile newprofile = ds.getValue(Profile.class);
                    profiles.add(newprofile) ;

                }
                CustomAdapter profileadapter = new CustomAdapter(getApplicationContext(),profiles);
                profileslist.setAdapter(profileadapter);

                //Toast.makeText(getApplicationContext(), "Data Loaded", Toast.LENGTH_SHORT).show();
            }
            else
            {
               // Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(getApplicationContext(), "There was a Problem in Loading Data", Toast.LENGTH_SHORT).show();
        }
    } ;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent addprofile = null;
        if (item.getItemId() == R.id.btnAddProfile) {
            addprofile = new Intent(MainActivity.this, AddProfile.class);
            startActivity(addprofile);
        }
        if (item.getItemId() == R.id.btnLogout) {
            addprofile = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(addprofile);
        }
        return false;
    }
}
