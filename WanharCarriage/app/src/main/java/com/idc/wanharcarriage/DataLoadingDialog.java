package com.idc.wanharcarriage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class DataLoadingDialog extends DialogFragment {

    Boolean isConnected;

    public  DataLoadingDialog(Boolean isConnected) {
        this.isConnected = isConnected;
    }

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.fragment_loading_data, null);

        builder.setView(view)
                .setTitle("Please wait! Loading Data...") ;

        if(!isConnected){
            builder.setView(view)
                    .setTitle("You are not connected to internet. Connect and try again...") ;
        }
        return builder.create();
    }
}
