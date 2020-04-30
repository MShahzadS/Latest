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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.PioneerOrder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


public class AddOrderDialog extends DialogFragment {


    SearchableSpinner spinnerOrder ;
    OrderDialogListener myListener ;
    ArrayList<PioneerOrder> orderArray;
    ArrayList<String> orderInvoices;
    ArrayList<String> orderIDs;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.fragment_add_order_dialog, null);

        orderArray = new ArrayList<>();
        orderIDs = new ArrayList<>();
        orderInvoices = new ArrayList<>();
        spinnerOrder = (SearchableSpinner) view.findViewById(R.id.selectOrderSpinner);

        loadOrderData();
        builder.setView(view)
                .setTitle("Add Dealer")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AddOrderDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int position = spinnerOrder.getSelectedItemPosition();

                        PioneerOrder order = orderArray.get(position);
                        String orderid = orderIDs.get(position);

                        myListener.saveOrder(order, orderid);
                    }
                });

        return builder.create();
    }

    public void loadOrderData(){
        Query orderDB = FirebaseDatabase.getInstance().getReference("PioneerOrder")
                .orderByChild("orderType")
                .equalTo("Deliver");
        orderDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                orderArray.clear();
                orderIDs.clear();

                if(dataSnapshot.exists()){
                    for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                        PioneerOrder newOrder = orderData.getValue(PioneerOrder.class);

                        orderIDs.add(orderData.getKey());
                        orderArray.add(newOrder);
                        orderInvoices.add(newOrder.getInvoiceNumber());
                    }
                    if(getActivity() != null) {
                        ArrayAdapter myadapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1 ,orderInvoices);
                        spinnerOrder.setAdapter(myadapter);
                    }
                } else {

                    //  Functions.MyFunctions.displayMessage(mappleOrderList,"No Vehicle is "+ orderStatus);
                }

                Functions.MyFunctions.dismissDialog();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //Functions.MyFunctions.displayMessage(getActivity(),"Data Loading cancelled");
            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {

            myListener = (OrderDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement MyDialogListener") ;
        }
    }

    public interface OrderDialogListener {

        void saveOrder(PioneerOrder order,String orderid) ;
    }
}
