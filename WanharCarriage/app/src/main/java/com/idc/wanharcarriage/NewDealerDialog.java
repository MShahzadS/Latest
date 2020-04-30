package com.idc.wanharcarriage;

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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.DealerOrder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class NewDealerDialog extends DialogFragment  {

    EditText edtweightTons, edtWeightBags, edtFreight, edtDestination ;
    SearchableSpinner spinnerCustomer ;
    MyDialogListener myListener ;
    ArrayList<String> customers, customerIDs;
    ImageView btnAddCutomer ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.fragment_delerdialog, null);

        spinnerCustomer = (SearchableSpinner) view.findViewById(R.id.customerspinner);
        edtweightTons = (EditText) view.findViewById(R.id.edttonweight) ;
        edtWeightBags = (EditText) view.findViewById(R.id.edtbagweight) ;
        edtFreight = (EditText) view.findViewById(R.id.edtdealerfreight) ;
        edtDestination = (EditText) view.findViewById(R.id.edtdestination) ;
        btnAddCutomer = (ImageView) view.findViewById(R.id.btnAddCustomer);



        customerIDs =  new ArrayList<String>();
        customers = new ArrayList<String>();


        edtweightTons.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                try {
                    int val = Integer.valueOf(s.toString()) * 20;
                    edtWeightBags.setText(String.valueOf(val));
                }catch (Exception e){
                    edtWeightBags.setText(String.valueOf("0")) ;
                    Log.d(TAG, e.getMessage());
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        btnAddCutomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCustomerDialog newCustomerDialog = new NewCustomerDialog();
                newCustomerDialog.show(getFragmentManager(),"Add Customer");
            }
        });

        builder.setView(view)
                .setTitle("Add Dealer")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            NewDealerDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int selectedcustomer = spinnerCustomer.getSelectedItemPosition();

                        String dealerid = customerIDs.get(selectedcustomer);
                        String weighttons = edtweightTons.getText().toString();
                        String freight = edtFreight.getText().toString();
                        String destination = edtDestination.getText().toString();


                        myListener.saveDelaer(dealerid,weighttons,freight,destination);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            myListener = (MyDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement MyDialogListener") ;
        }
        loadCustomers();
    }

    public void loadCustomers(){
        DatabaseReference customerDB = FirebaseDatabase.getInstance().getReference("Customer");
        customerDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    customers.clear();
                    customerIDs.clear();

                    for(DataSnapshot customerData: dataSnapshot.getChildren()){
                        Customer customer = customerData.getValue(Customer.class);

                        customers.add(customer.getName());
                        customerIDs.add(customerData.getKey());
                    }
                }

                ArrayAdapter customerAdapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,customers);
                spinnerCustomer.setAdapter(customerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public interface MyDialogListener {
        void saveDelaer(String dealerid, String weight, String freight, String Destination);
    }
}
