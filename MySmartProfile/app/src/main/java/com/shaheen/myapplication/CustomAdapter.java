package com.shaheen.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.logging.Logger;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Profile> {

    ArrayList<Profile> profiles;
    Context context;
    Method dataConnSwitchmethod_ON;
    Method dataConnSwitchmethod_OFF;
    Class telephonyManagerClass;
    Object ITelephonyStub;
    Class ITelephonyClass;
    private NotificationManager mNotificationManager;

    private int lastposition = -1;

    public CustomAdapter(@NonNull Context context, ArrayList<Profile> profiles) {
        super(context, R.layout.profileitem, profiles);

        this.profiles = profiles;
        this.context = context;
        mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
    }

    @Override
    public int getCount() {
        return profiles.size();
    }

    @Nullable
    @Override
    public Profile getItem(int position) {
        return profiles.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View root = null;

        final Profile newprofile = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.profileitem, parent, false);

            viewHolder.txtProfilename = (TextView) convertView.findViewById(R.id.txtprofilename);
            viewHolder.imgProfielIcon = (ImageView) convertView.findViewById(R.id.profileicon);
            viewHolder.imgEditProfile = (ImageView) convertView.findViewById(R.id.btneditprofile);
            viewHolder.imgApplyProfile = (ImageView) convertView.findViewById(R.id.btnapplyprofile);

            root = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            root = convertView;
        }

        viewHolder.txtProfilename.setText(newprofile.getProfilename());
        viewHolder.imgEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editProfile = new Intent(context, EditProfile.class);
                editProfile.putExtra("profile", newprofile.getProfileid());
                editProfile.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(editProfile);
                // Toast.makeText(context,Integer.toString(newprofile.getProfilepicture()),Toast.LENGTH_SHORT).show();
            }
        });

        viewHolder.imgApplyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Query profileQuery = FirebaseDatabase.getInstance().getReference("ProfileSettings")
                        .orderByChild("profileid")
                        .equalTo(newprofile.getProfileid());

                profileQuery.addListenerForSingleValueEvent(settingsEvent); ;

            }
        });

        viewHolder.imgProfielIcon.setImageResource(Integer.valueOf(newprofile.getProfilepicture()));


        return convertView;


    }

    private static class ViewHolder {

        TextView txtProfilename;
        ImageView imgProfielIcon;
        ImageView imgEditProfile;
        ImageView imgApplyProfile;
    }

    public void changeWifi(boolean state) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        wifiManager.setWifiEnabled(state);

    }

    public void changeBluetooth(boolean state) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!state) {
            mBluetoothAdapter.disable();
        } else {
            mBluetoothAdapter.enable();
        }
    }

    public void changeRingerVolume(int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        audioManager.setStreamVolume(AudioManager.STREAM_RING, vol, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, vol, 0);
    }

    public void changeMediaVolume(int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);
    }

    public void changeAlarmVolume(int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0);
    }


    public void changeRingerState(boolean state, int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_RING, vol, 0);
        audioManager.setStreamVolume(AudioManager.STREAM_NOTIFICATION, vol, 0);

    }

    public void changeMediaState(boolean state, int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, vol, 0);

    }

    public void changeAlarmState(boolean state, int vol) {
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_ALARM, vol, 0);
    }


    public void setPlaneMode(boolean enabled)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.EXTRA_AIRPLANE_MODE_ENABLED, enabled ? 1:0) ;
    }

    public void changeBrightness(int brightness)
    {
        Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
    }

    public void setOrientationLock(boolean locked)
    {
        if(locked)
        {
            ((MainActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        }else
        {
            ((MainActivity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    private void turnGPSOn(){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(!provider.contains("gps")) { //if gps is disabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    private void turnGPSOff(){
        String provider = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

        if(provider.contains("gps")){ //if gps is enabled
            final Intent poke = new Intent();
            poke.setClassName("com.android.settings", "com.android.settings.widget.SettingsAppWidgetProvider");
            poke.addCategory(Intent.CATEGORY_ALTERNATIVE);
            poke.setData(Uri.parse("3"));
            context.sendBroadcast(poke);
        }
    }

    public void setMobileDataState(boolean mobileDataEnabled) {
        TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        Method setMobileDataEnabledMethod = null;
        try {
            setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);
        if (null != setMobileDataEnabledMethod) {
            setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
        }
            } catch (Exception ex){
            Log.d("Mobile Data",ex.getMessage());
        }
    }

    public boolean getMobileDataState()
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method getMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("getDataEnabled");

            if (null != getMobileDataEnabledMethod)
            {
                boolean mobileDataEnabled = (Boolean) getMobileDataEnabledMethod.invoke(telephonyService);

                return mobileDataEnabled;
            }
        }
        catch (Exception ex)
        {
            Log.d("Mobile Data: ",ex.getMessage());
        }

        return false;
    }

    ValueEventListener settingsEvent = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if (dataSnapshot.exists()) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    ProfileSettings newSettings = ds.getValue(ProfileSettings.class);

                    int ringer = newSettings.getNotificationsound();
                    int media = newSettings.getMediaSound();
                    int alarm = newSettings.getAlarmSound();
                    int brightness = newSettings.getBrigntnessLevel() ;

                    boolean ringerstate = newSettings.isNotificationSwitch();
                    boolean mediastate = newSettings.isMediaSwitch();
                    boolean alaramstate = newSettings.isAlarmswitch();
                    boolean wifi = newSettings.isWifiSwitch();
                    boolean bluetooth = newSettings.isBluetoothSwitch();
                    boolean planemode = newSettings.isPlaneSwitch() ;
                    boolean orientationlock = newSettings.isRotationSwitch();
                    boolean gps = newSettings.isLoctionSwitch() ;
                    boolean data = newSettings.isDataSwitch() ;


                    try {
                        changeRingerVolume(ringer);
                        if (!ringerstate)
                            changeRingerState(ringerstate, 0);
                    }
                    catch (Exception ex) {
                        Log.d("Ringer: ",ex.getMessage());
                    }
                    try{
                    changeMediaVolume(media);
                        if (!mediastate)
                            changeMediaState(mediastate, 0);
                    }
                    catch (Exception ex) {
                        Log.d("Media: ",ex.getMessage());
                    }

                    try {
                    changeAlarmVolume(alarm);
                        if (!alaramstate)
                            changeAlarmState(alaramstate, 0) ;
                    }
                    catch (Exception ex) {
                        Log.d("Alarm: ",ex.getMessage());
                    }
                    try{
                    changeWifi(wifi);
                    }
                    catch (Exception ex) {
                        Log.d("WIFI: ",ex.getMessage());
                    }

                    try{
                    changeBluetooth(bluetooth);
                    }
                    catch (Exception ex) {
                        Log.d("Bluetooth: ",ex.getMessage());
                    }

                    try {
                        setMobileDataState(data);
                    }
                    catch (Exception ex) {
                        Log.d("Mobile Data: ",ex.getMessage());
                    }
                    try{
                    setOrientationLock(orientationlock);
                    }
                    catch (Exception ex) {
                        Log.d("Rotation Lock: ",ex.getMessage());
                    }
                    try{
                    setPlaneMode(planemode);
                    }
                    catch (Exception ex) {
                        Log.d("Aeroplane Mode: ",ex.getMessage());
                    }
                    try{
                    changeBrightness(brightness);
                    }
                    catch (Exception ex) {
                        Log.d("Brightness: ",ex.getMessage());
                    }





                    Toast.makeText(context, "Profile Set Successfully", Toast.LENGTH_SHORT).show();
                }


            } else {
                //Toast.makeText(getApplicationContext(),"No Settings Found", Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Toast.makeText(context, "Data Could not be laoded", Toast.LENGTH_SHORT).show();
        }
    };

}
