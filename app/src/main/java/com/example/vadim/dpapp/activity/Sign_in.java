package com.example.vadim.dpapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.vadim.dpapp.R;
import com.example.vadim.dpapp.application.AppConfig;
import com.example.vadim.dpapp.application.DBHelper;
import com.example.vadim.dpapp.application.RESTController;
import com.example.vadim.dpapp.containers.UserContainer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sign_in extends Activity {
    public static Intent intent;
     Intent settingIntent;
    public static Intent registrationIntent;
    DBHelper dbHelper;
    public static  ArrayList<UserContainer> listUser = new ArrayList<UserContainer>();
    RESTController restController;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        dbHelper = new DBHelper(this);
        dbHelper.create_db();
        dbHelper.open();
        context = this;
        settingIntent = new Intent(this, Setting.class);
        registrationIntent = new Intent(this, registration.class);
        intent = new Intent(this, MainActivity.class);
        AppConfig.flagEnter = false;
        WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            AppConfig.uid =  getAdressMacByInterface();
        }
        else {
            AppConfig.uid = wifiManager.getConnectionInfo().getMacAddress();
        }
        restController = new RESTController(this, Sign_in.class.getSimpleName());
        restController.getUser();
        }

        @Override
    protected void onResume() {
        super.onResume();

        if(AppConfig.flagEnter) {
            finish();
        }
        else {
            //restController.getUser();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(AppConfig.flagEnter) {
            //finish();
        }
    }
    private static final String marshmallowMacAddress = "02:00:00:00:00:00";
    private static final String fileAddressMac = "/sys/class/net/wlan0/address";

    public static String recupAdresseMAC(WifiManager wifiMan) {
        WifiInfo wifiInf = wifiMan.getConnectionInfo();

        if(wifiInf.getMacAddress().equals(marshmallowMacAddress)){
            String ret = null;
            try {
                ret= getAdressMacByInterface();
                if (ret != null){
                    return ret;
                } else {
                    ret = getAddressMacByFile(wifiMan);
                    return ret;
                }
            } catch (IOException e) {
                Log.e("MobileAccess", "Erreur lecture propriete Adresse MAC");
            } catch (Exception e) {
                Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
            }
        } else{
            return wifiInf.getMacAddress();
        }
        return marshmallowMacAddress;
    }

    private static String getAdressMacByInterface(){
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (nif.getName().equalsIgnoreCase("wlan0")) {
                    byte[] macBytes = nif.getHardwareAddress();
                    if (macBytes == null) {
                        return "";
                    }

                    StringBuilder res1 = new StringBuilder();
                    for (byte b : macBytes) {
                        res1.append(String.format("%02X:",b));
                    }

                    if (res1.length() > 0) {
                        res1.deleteCharAt(res1.length() - 1);
                    }
                    return res1.toString();
                }
            }

        } catch (Exception e) {
            Log.e("MobileAcces", "Erreur lecture propriete Adresse MAC ");
        }
        return null;
    }

    private static String getAddressMacByFile(WifiManager wifiMan) throws Exception {
        String ret;
        int wifiState = wifiMan.getWifiState();

        wifiMan.setWifiEnabled(true);
        File fl = new File(fileAddressMac);
        FileInputStream fin = new FileInputStream(fl);
        StringBuilder builder = new StringBuilder();
        int ch;
        while((ch = fin.read()) != -1){
            builder.append((char)ch);
        }

        ret = builder.toString();
        fin.close();

        boolean enabled = WifiManager.WIFI_STATE_ENABLED == wifiState;
        wifiMan.setWifiEnabled(enabled);
        return ret;
    }


}

