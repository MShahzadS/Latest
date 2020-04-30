package com.idc.wanharcarriage;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;

public class Functions {

    public static class  MyFunctions {

        private static DataLoadingDialog pleasewait;

        public static void displayMessage(Context context, String message) {

            if (context != null)
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }

        public static void displayDialog(FragmentManager manager) {

            try {
                pleasewait = new DataLoadingDialog(isConnected());
                pleasewait.show(manager, "Please wait....");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static void dismissDialog() {
            try {
                pleasewait.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static boolean isConnected() throws InterruptedException, IOException {
            final String command = "ping -c 1 google.com";
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        }

    }
}
