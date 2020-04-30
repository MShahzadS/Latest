package com.shaheen.myapplication;

public class ProfileSettings {

    private boolean notificationSwitch, wifiSwitch, loctionSwitch, mediaSwitch, bluetoothSwitch,
            planeSwitch, dataSwitch, rotationSwitch, alarmswitch ;
    private int notificationsound, mediaSound, alarmSound, brigntnessLevel ;
    String profileid ;

    public ProfileSettings() {
        this.alarmswitch = false ;
        this.rotationSwitch = false ;
        this.notificationSwitch = false;
        this.wifiSwitch = false;
        this.loctionSwitch = false;
        this.mediaSwitch = false;
        this.bluetoothSwitch = false;
        this.planeSwitch = false;
        this.dataSwitch = false;
        this.notificationsound = 7;
        this.mediaSound = 7;
        this.brigntnessLevel = 4;
        this.alarmSound = 7 ;
        profileid = "" ;
    }

    public ProfileSettings(boolean notificationSwitch, boolean wifiSwitch, boolean loctionSwitch,
                           boolean mediaSwitch, boolean bluetoothSwitch, boolean planeSwitch,
                           boolean dataSwitch, boolean rotationSwitch, boolean alarmswitch,
                           int notificationsound, int mediaSound, int alarmSound, int brigntnessLevel,
                           String profileid) {
        this.notificationSwitch = notificationSwitch;
        this.wifiSwitch = wifiSwitch;
        this.loctionSwitch = loctionSwitch;
        this.mediaSwitch = mediaSwitch;
        this.bluetoothSwitch = bluetoothSwitch;
        this.planeSwitch = planeSwitch;
        this.dataSwitch = dataSwitch;
        this.rotationSwitch = rotationSwitch;
        this.alarmswitch = alarmswitch;
        this.notificationsound = notificationsound;
        this.mediaSound = mediaSound;
        this.alarmSound = alarmSound;
        this.brigntnessLevel = brigntnessLevel;
        this.profileid = profileid;
    }

    public String getProfileid() {
        return profileid;
    }

    public void setProfileid(String profileid) {
        this.profileid = profileid;
    }

    public boolean isNotificationSwitch() {
        return notificationSwitch;
    }

    public void setNotificationSwitch(boolean notificationSwitch) {
        this.notificationSwitch = notificationSwitch;
    }

    public boolean isRotationSwitch() {
        return rotationSwitch;
    }

    public void setRotationSwitch(boolean rotationSwitch) {
        this.rotationSwitch = rotationSwitch;
    }

    public boolean isAlarmswitch() {
        return alarmswitch;
    }

    public void setAlarmswitch(boolean alarmswitch) {
        this.alarmswitch = alarmswitch;
    }

    public int getAlarmSound() {
        return alarmSound;
    }

    public void setAlarmSound(int alarmSound) {
        this.alarmSound = alarmSound;
    }

    public boolean isWifiSwitch() {
        return wifiSwitch;
    }

    public void setWifiSwitch(boolean wifiSwitch) {
        this.wifiSwitch = wifiSwitch;
    }

    public boolean isLoctionSwitch() {
        return loctionSwitch;
    }

    public void setLoctionSwitch(boolean loctionSwitch) {
        this.loctionSwitch = loctionSwitch;
    }

    public boolean isMediaSwitch() {
        return mediaSwitch;
    }

    public void setMediaSwitch(boolean mediaSwitch) {
        this.mediaSwitch = mediaSwitch;
    }

    public boolean isBluetoothSwitch() {
        return bluetoothSwitch;
    }

    public void setBluetoothSwitch(boolean bluetoothSwitch) {
        this.bluetoothSwitch = bluetoothSwitch;
    }

    public boolean isPlaneSwitch() {
        return planeSwitch;
    }

    public void setPlaneSwitch(boolean planeSwitch) {
        this.planeSwitch = planeSwitch;
    }

    public boolean isDataSwitch() {
        return dataSwitch;
    }

    public void setDataSwitch(boolean dataSwitch) {
        this.dataSwitch = dataSwitch;
    }

    public int getNotificationsound() {
        return notificationsound;
    }

    public void setNotificationsound(int notificationsound) {
        this.notificationsound = notificationsound;
    }

    public int getMediaSound() {
        return mediaSound;
    }

    public void setMediaSound(int mediaSound) {
        this.mediaSound = mediaSound;
    }

    public int getBrigntnessLevel() {
        return brigntnessLevel;
    }

    public void setBrigntnessLevel(int brigntnessLevel) {
        this.brigntnessLevel = brigntnessLevel;
    }


}
