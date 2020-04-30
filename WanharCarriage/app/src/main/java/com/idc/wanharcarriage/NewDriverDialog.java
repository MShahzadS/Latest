package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idc.wanharcarriage.classes.Driver;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static android.content.ContentValues.TAG;

public class NewDriverDialog  extends DialogFragment {

    EditText edtDriverName, edtDriverContact, edtDriverAddress ;
    DriverDialogListener myListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.activity_new_driver, null);

        edtDriverName  = (EditText) view.findViewById(R.id.edtDriverName) ;
        edtDriverContact  = (EditText) view.findViewById(R.id.edtDriverConact) ;
        edtDriverAddress  = (EditText) view.findViewById(R.id.edtDriverAddress) ;

        builder.setView(view)
                .setTitle("Add Driver")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewDriverDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = edtDriverName.getText().toString();
                        String contact = edtDriverContact.getText().toString();
                        String address = edtDriverAddress.getText().toString();

                        final Driver newDriver = new Driver(name, contact, address);

                        DatabaseReference driverDB = FirebaseDatabase.getInstance().getReference("Driver");

                        final String newID = driverDB.push().getKey() ;

                        driverDB.child(newID).setValue(newDriver, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if(databaseError == null){

                                    myListener.saveDriver(newDriver,newID, "Driver Added Successfully");
                                }else{
                                    myListener.saveDriver(newDriver,newID, "Driver could not be saved. Try Again!");
                                }
                            }
                        });
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            myListener = (DriverDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement DriverDialogListener") ;
        }
    }

    public interface DriverDialogListener {

        void saveDriver(Driver driver,String driverid, String message);
    }
}
