package com.shaheen.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, Switch.OnCheckedChangeListener {


    SeekBar seekRinger, seekMedia, seekBrightness, seekAlarm ;
    Switch swtRinger, swtMedia, swtWifi, swtBluetooth, swtPlane, swtLocation, swtData, swtAlarm,
            swtRoation;
    int sekRinger=0, sekMedia = 0, sekBrightness = 0, sekAlarm = 0 ;
    boolean swRinger=false, swMedia=false, swWifi=false, swBluetooth=false, swPlane=false,
            swlocation = false, swData=false , swAlaram = false, swRotation = false ;
    Button btnSave ;
    String profileid ;
    String settingskey = "" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        seekRinger = (SeekBar) findViewById(R.id.seekRinger) ;
        seekMedia = (SeekBar) findViewById(R.id.seekMedia) ;
        seekAlarm = (SeekBar) findViewById(R.id.seekAlarm) ;
        seekBrightness = (SeekBar) findViewById(R.id.seekBrightness) ;
        swtBluetooth = (Switch) findViewById(R.id.swtBluetooth);
        swtWifi = (Switch) findViewById(R.id.swtWifi);
        swtLocation = (Switch) findViewById(R.id.swtGPS);
        swtData = (Switch) findViewById(R.id.swtDataConnection);
        swtRinger = (Switch) findViewById(R.id.swtRinger);
        swtMedia = (Switch) findViewById(R.id.swtMedia);
        swtPlane = (Switch) findViewById(R.id.swtplane);
        swtAlarm = (Switch) findViewById(R.id.swtAlarm);
        swtRoation = (Switch) findViewById(R.id.swtRotate);
        btnSave = (Button) findViewById(R.id.btnSaveSettings) ;
        profileid = getIntent().getStringExtra("profile");

        Query profileQuery = FirebaseDatabase.getInstance().getReference("ProfileSettings")
                .orderByChild("profileid")
                .equalTo(profileid);

        profileQuery.addListenerForSingleValueEvent(settingsEvent); ;

        seekRinger.setOnSeekBarChangeListener(this);
        seekMedia.setOnSeekBarChangeListener(this);
        seekBrightness.setOnSeekBarChangeListener(this);
        seekAlarm.setOnSeekBarChangeListener(this);

        swtBluetooth.setOnCheckedChangeListener(this);
        swtWifi.setOnCheckedChangeListener(this);
        swtRinger.setOnCheckedChangeListener(this);
        swtMedia.setOnCheckedChangeListener(this);
        swtLocation.setOnCheckedChangeListener(this);
        swtData.setOnCheckedChangeListener(this);
        swtPlane.setOnCheckedChangeListener(this);
        swtAlarm.setOnCheckedChangeListener(this);
        swtRoation.setOnCheckedChangeListener(this);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProfileSettings newsettings = new ProfileSettings();

                newsettings.setProfileid(profileid);
                newsettings.setNotificationSwitch(swRinger);
                newsettings.setNotificationsound(sekRinger);
                newsettings.setMediaSwitch(swMedia);
                newsettings.setMediaSound(sekMedia);
                newsettings.setWifiSwitch(swWifi);
                newsettings.setBluetoothSwitch(swBluetooth);
                newsettings.setLoctionSwitch(swlocation);
                newsettings.setPlaneSwitch(swPlane);
                newsettings.setDataSwitch(swData);
                newsettings.setBrigntnessLevel(sekBrightness);
                newsettings.setAlarmswitch(swAlaram);
                newsettings.setAlarmSound(sekAlarm);
                newsettings.setRotationSwitch(swRotation);

                DatabaseReference settingsDB = FirebaseDatabase.getInstance().getReference("ProfileSettings");


               // Toast.makeText(getApplicationContext(), "Setting " + settingskey, Toast.LENGTH_SHORT).show();


                Map settingmap = new HashMap();


                settingmap.put("alarmswitch", newsettings.isAlarmswitch());
                settingmap.put("rotationSwitch", newsettings.isRotationSwitch());
                settingmap.put("notificationSwitch", newsettings.isNotificationSwitch());
                settingmap.put("wifiSwitch", newsettings.isWifiSwitch());
                settingmap.put("dataSwitch", newsettings.isDataSwitch());
                settingmap.put("loctionSwitch", newsettings.isLoctionSwitch());
                settingmap.put("mediaSwitch", newsettings.isMediaSwitch());
                settingmap.put("bluetoothSwitch", newsettings.isBluetoothSwitch());
                settingmap.put("planeSwitch", newsettings.isPlaneSwitch());
                settingmap.put("notificationsound", newsettings.getNotificationsound());
                settingmap.put("mediaSound", newsettings.getMediaSound());
                settingmap.put("brigntnessLevel", newsettings.getBrigntnessLevel());
                settingmap.put("alarmSound", newsettings.getAlarmSound());


                if (settingskey != "") {
                    settingsDB.child(settingskey).updateChildren(settingmap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Settings Updated Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Settings not updated", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    String newsettingskey = settingsDB.push().getKey();
                    settingsDB.child(newsettingskey).setValue(newsettings).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(), "Settings Saved Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Settings not saved", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        switch (seekBar.getId())
        {
            case R.id.seekRinger:
                sekRinger = progress ;
                //Toast.makeText(getApplicationContext(), Integer.toString(progress), Toast.LENGTH_SHORT).show();
                break;
            case R.id.seekMedia:
                sekMedia = progress ;
                //Toast.makeText(getApplicationContext(), Integer.toString(progress), Toast.LENGTH_SHORT).show();
                break;
            case R.id.seekBrightness:
                sekBrightness = progress ;
                //Toast.makeText(getApplicationContext(), Integer.toString(progress), Toast.LENGTH_SHORT).show();
                break;
            case R.id.seekAlarm:
                sekAlarm = progress ;
                //Toast.makeText(getApplicationContext(), Integer.toString(progress), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        switch (buttonView.getId())
        {
            case R.id.swtRinger:
                swRinger = isChecked ;
                if(!isChecked)
                    seekRinger.setEnabled(false);
                else
                    seekRinger.setEnabled(true);
                break ;
            case R.id.swtMedia:
                swMedia = isChecked ;
                if(!isChecked)
                    seekMedia.setEnabled(false);
                else
                    seekMedia.setEnabled(true);
                break;
            case R.id.swtAlarm:
                swAlaram = isChecked ;
                if(!isChecked)
                    seekAlarm.setEnabled(false);
                else
                    seekAlarm.setEnabled(true);
                break;
            case R.id.swtBluetooth:
                swBluetooth = isChecked ;
                break ;
            case R.id.swtWifi:
                swWifi = isChecked ;
                break;
            case R.id.swtDataConnection:
                swData = isChecked ;
                break;
            case R.id.swtplane:
                swPlane = isChecked ;
                break ;
            case R.id.swtGPS:
                swlocation = isChecked ;
                break;
            case R.id.swtRotate:
                swRotation = isChecked ;
                break;
        }
    }

    ValueEventListener settingsEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                for(DataSnapshot ds: dataSnapshot.getChildren())
                {
                    settingskey = ds.getKey() ;

                    ProfileSettings newSettings = ds.getValue(ProfileSettings.class);

                    seekRinger.setProgress(newSettings.getNotificationsound());
                    seekMedia.setProgress(newSettings.getMediaSound());
                    seekBrightness.setProgress(newSettings.getBrigntnessLevel());
                    seekAlarm.setProgress(newSettings.getAlarmSound());

                    if(!newSettings.isAlarmswitch())
                        seekAlarm.setEnabled(false);
                    else
                        seekAlarm.setEnabled(true);

                    if(!newSettings.isMediaSwitch())
                        seekMedia.setEnabled(false);
                    else
                        seekMedia.setEnabled(true);

                    if(!newSettings.isNotificationSwitch())
                        seekRinger.setEnabled(false);
                    else
                        seekRinger.setEnabled(true);

                    swtBluetooth.setChecked(newSettings.isBluetoothSwitch());
                    swtWifi.setChecked(newSettings.isWifiSwitch());
                    swtMedia.setChecked(newSettings.isMediaSwitch());
                    swtRinger.setChecked(newSettings.isNotificationSwitch());
                    swtData.setChecked(newSettings.isDataSwitch());
                    swtLocation.setChecked(newSettings.isLoctionSwitch());
                    swtPlane.setChecked(newSettings.isPlaneSwitch());
                    swtAlarm.setChecked(newSettings.isAlarmswitch());
                    swtRoation.setChecked(newSettings.isRotationSwitch());

                }
            }else
            {
                //Toast.makeText(getApplicationContext(),"No Settings Found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Data Could not be laoded", Toast.LENGTH_SHORT).show();
        }
    };


}
