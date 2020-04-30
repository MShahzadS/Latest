package com.shaheen.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.style.BulletSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddProfile extends AppCompatActivity implements View.OnClickListener {

    EditText edtprofileName ;
    Button btnSaveProfile ;
    ImageView btnWifi, btnBluetooth, btnplane;
    ImageView btnsSchool, btnTheater, btnRoom;
    ImageView btnCar, btnBus, btnSubway ;
    int iconid = R.id.school ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_profile);

        edtprofileName = (EditText) findViewById(R.id.edtprofileName) ;
        btnSaveProfile = (Button) findViewById(R.id.btnSavePofile) ;
        edtprofileName = (EditText) findViewById(R.id.edtprofileName) ;
        btnWifi = (ImageView) findViewById(R.id.wifi) ;
        btnBluetooth = (ImageView) findViewById(R.id.bluetooth) ;
        btnplane = (ImageView) findViewById(R.id.plane) ;
        btnsSchool = (ImageView) findViewById(R.id.school) ;
        btnTheater = (ImageView) findViewById(R.id.theater) ;
        btnRoom = (ImageView) findViewById(R.id.room) ;
        btnCar = (ImageView) findViewById(R.id.car) ;
        btnBus = (ImageView) findViewById(R.id.bus) ;
        btnSubway = (ImageView) findViewById(R.id.subway) ;


        btnWifi.setOnClickListener(this);
        btnBluetooth.setOnClickListener(this);
        btnplane.setOnClickListener(this);
        btnsSchool.setOnClickListener(this);
        btnTheater.setOnClickListener(this);
        btnRoom.setOnClickListener(this);
        btnCar.setOnClickListener(this);
        btnBus.setOnClickListener(this);
        btnSubway.setOnClickListener(this);
        btnSaveProfile.setOnClickListener(this);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSavePofile:
                String name = edtprofileName.getText().toString() ;

                SharedPreferences sharedprefs = getSharedPreferences(LoginActivity.MyPREFERENCES,MODE_PRIVATE);

                String userid = sharedprefs.getString(LoginActivity.userid,"");


                DatabaseReference profileDB = FirebaseDatabase.getInstance().getReference("Profile");

                String newkey = profileDB.push().getKey();

                //Toast.makeText(getApplicationContext(),"Icon: " + iconid, Toast.LENGTH_SHORT).show();
                Profile newProfile = new Profile(newkey, name, userid, Integer.toString(iconid));

                profileDB.child(newkey).setValue(newProfile).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(),"Profile Added Successfully", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Profile could not be Added. Try Again", Toast.LENGTH_LONG).show();
                    }
                });



                break;
            case R.id.wifi:
                iconid = R.drawable.ic_network_wifi_ ;
                removeColor();
                btnWifi.setBackgroundColor(R.color.colorPrimaryDark);
                break;
            case R.id.plane:
                removeColor();
                Toast.makeText(getApplicationContext(),"Plane: " + R.id.plane, Toast.LENGTH_SHORT).show();
                btnplane.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_noconnection ;
                break;
            case R.id.bluetooth:
                removeColor();
                btnBluetooth.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_bluetooth_on;
                break;
            case R.id.school:
                removeColor();
                btnsSchool.setBackgroundColor(R.color.colorPrimaryDark);
                iconid= R.drawable.ic_school;
                break;
            case R.id.theater:
                removeColor();
                btnTheater.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_theaters;
                break;
            case R.id.room:
                removeColor();
                btnRoom.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_room ;
                break;
            case R.id.car:
                removeColor();
                btnCar.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_car ;
                break;
            case R.id.bus:
                removeColor();
                btnBus.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_bus;
                break;
            case R.id.subway:
                removeColor();
                btnSubway.setBackgroundColor(R.color.colorPrimaryDark);
                iconid = R.drawable.ic_subway;
                break;
        }

    }

    @SuppressLint("ResourceAsColor")
    private void removeColor()
    {
        int colorid = 0 ;
        btnWifi.setBackgroundColor(colorid);
        btnBluetooth.setBackgroundColor(colorid);
        btnplane.setBackgroundColor(colorid);
        btnsSchool.setBackgroundColor(colorid);
        btnTheater.setBackgroundColor(colorid);
        btnRoom.setBackgroundColor(colorid);
        btnCar.setBackgroundColor(colorid);
        btnBus.setBackgroundColor(colorid);
        btnSubway.setBackgroundColor(colorid);

    }
}
