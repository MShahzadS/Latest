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
import com.idc.wanharcarriage.classes.Vehicle;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static android.content.ContentValues.TAG;

public class NewVehicleDialog  extends DialogFragment {

    VehicleDialogListener myListener;
    EditText edtRegNum, edtCity, edtModel ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.activity_new_bus, null);

        edtRegNum = (EditText) view.findViewById(R.id.edtVehicleRegNum) ;
        edtCity = (EditText) view.findViewById(R.id.edtVehicleCity) ;
        edtModel = (EditText) view.findViewById(R.id.edtVehicleModel) ;

        builder.setView(view)
                .setTitle("Add Vehicle")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewVehicleDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String reg =  edtRegNum.getText().toString();
                        String city =  edtCity.getText().toString();
                        String model =  edtModel.getText().toString();

                        final Vehicle newVehicle = new Vehicle(reg,city,model);

                        DatabaseReference vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");

                        final String newID = vehicleDB.push().getKey();

                        vehicleDB.child(newID).setValue(newVehicle, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    myListener.saveVehicle(newVehicle,newID, "Vehicle Added Successfully");
                                }else {
                                    myListener.saveVehicle(newVehicle,newID, "Vehicle could not be added. Try Again");
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
            myListener = (VehicleDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement VehicleDialogListener") ;
        }
    }

    public interface VehicleDialogListener {
        void saveVehicle(Vehicle vehicle,String vehicleid, String message);
    }
}
