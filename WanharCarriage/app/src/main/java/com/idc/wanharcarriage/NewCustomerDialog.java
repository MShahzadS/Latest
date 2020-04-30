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
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.idc.wanharcarriage.classes.Customer;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static android.content.ContentValues.TAG;

public class NewCustomerDialog  extends DialogFragment {

    MyCustomerDialogListener myListener ;
    EditText edtCustomerName, edtCustomerContact, edtCustomerAddress, edtCustomerCompany ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.activity_new_customer, null);

        edtCustomerName = (EditText) view.findViewById(R.id.edtCustomerName);
        edtCustomerContact = (EditText) view.findViewById(R.id.edtCustomerContact);
        edtCustomerAddress = (EditText) view.findViewById(R.id.edtCustomerAddress);
        edtCustomerCompany = (EditText) view.findViewById(R.id.edtCustomerCompany);

                builder.setView(view)
                .setTitle("Add Customer")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewCustomerDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String name = edtCustomerName.getText().toString();
                        String contact = edtCustomerContact.getText().toString();
                        String address = edtCustomerAddress.getText().toString();
                        String company = edtCustomerCompany.getText().toString();


                        DatabaseReference custDB = FirebaseDatabase.getInstance().getReference("Customer");
                        final String newid = custDB.push().getKey() ;
                        final Customer newCust = new Customer(name,contact,address,company) ;
                        custDB.child(newid).setValue(newCust, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                if(databaseError == null){
                                    myListener.saveCustomer(newCust, newid, "Customer Added Successfully");
                                } else {
                                    myListener.saveCustomer(newCust,newid, "Customer could not be added. Try again!") ;
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
            myListener = (MyCustomerDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement MyCustomerDialogListener") ;
        }
    }

    public interface MyCustomerDialogListener {

        void saveCustomer(Customer newCust, String customerID, String message);
    }
}
