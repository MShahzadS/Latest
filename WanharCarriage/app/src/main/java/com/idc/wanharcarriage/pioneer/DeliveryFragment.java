package com.idc.wanharcarriage.pioneer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.DataModel;
import com.idc.wanharcarriage.ExpenseDetails;
import com.idc.wanharcarriage.Functions;
import com.idc.wanharcarriage.MyCustomListAdapter;
import com.idc.wanharcarriage.Pioneer;
import com.idc.wanharcarriage.PioneerBillActivity;
import com.idc.wanharcarriage.PioneerEmployeeDashboard;
import com.idc.wanharcarriage.PioneerOrderDetails;
import com.idc.wanharcarriage.R;
import com.idc.wanharcarriage.classes.MappleOrder;
import com.idc.wanharcarriage.classes.PioneerOrder;
import com.idc.wanharcarriage.classes.Vehicle;
import com.idc.wanharcarriage.mapple.MappleFragment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class DeliveryFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView pioneerOrderList ;
    ArrayList<DataModel> orderArray;
    ArrayList<String> orderIDs;
    EditText edtSearchOrder;
    Button btnManageBills;
    HashMap<String, String> vehicles;
    Context mContext;
    String caller="" ;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_delivery, container, false);

        pioneerOrderList = (ListView) root.findViewById(R.id.pioneerOrderList) ;
        pioneerOrderList.setOnItemClickListener(DeliveryFragment.this);

        orderArray = new ArrayList<DataModel>();
        orderArray = new ArrayList<DataModel>();
        orderIDs = new ArrayList<String>();
        vehicles = new HashMap<>();
        edtSearchOrder = (EditText) root.findViewById(R.id.edtSearchOrder) ;
        btnManageBills = (Button) root.findViewById(R.id.btnManageBills);
        edtSearchOrder.addTextChangedListener(searchOrder);



       if(getActivity().getClass().toString().equals(PioneerEmployeeDashboard.class.toString())) {
           btnManageBills.setVisibility(View.GONE);
           caller = "Employee";
       } else {
           btnManageBills.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   Intent i = new Intent(getActivity(), PioneerBillActivity.class);
                   startActivity(i);
               }
           });
       }


        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        Functions.MyFunctions.displayDialog(getParentFragmentManager());
        loadVehicles();

    }

    public void loadOrderData(){
        Query orderDB = FirebaseDatabase.getInstance().getReference("PioneerOrder")
                .orderByChild("orderType")
                .equalTo("Deliver");
        orderDB.addValueEventListener(getOrders);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Intent i = new Intent(mContext, PioneerOrderDetails.class);
        i.putExtra("order", orderIDs.get(position));
        i.putExtra("vehicle", orderArray.get(position).getItemmain());
        i.putExtra("caller", caller);
        startActivity(i);
    }

    public void loadVehicles(){
        Query vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");
        vehicleDB.addValueEventListener(getVehicleData);
    }

    public String getVehicle(String vehicleid){
        return vehicles.get(vehicleid);
    }

    ValueEventListener getVehicleData = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            if (dataSnapshot.exists()) {
                for (DataSnapshot vehData : dataSnapshot.getChildren()) {
                    Vehicle newVeh = vehData.getValue(Vehicle.class);
                    vehicles.put(vehData.getKey(),newVeh.getRegistrationNumber());
                }
            } else {
                Functions.MyFunctions.displayMessage(mContext, "Failed to Load Vehicle Info. Try Again!");

            }

            loadOrderData();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(mContext,"Data Loading cancelled");
        }
    };

    ValueEventListener getOrders = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            orderArray.clear();
            orderIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                    PioneerOrder newOrder = orderData.getValue(PioneerOrder.class);
                    String sname = edtSearchOrder.getText().toString().toLowerCase() ;

                    String vehicle = getVehicle(newOrder.getVehicle());
                    if(!sname.isEmpty()) {
                        if(vehicle.toLowerCase().startsWith(sname)) {
                            orderIDs.add(orderData.getKey());
                            DataModel orderModel = new DataModel(vehicle, newOrder.getFinalDestination());
                            orderArray.add(orderModel);
                        }
                    }else
                    {
                        orderIDs.add(orderData.getKey());
                        DataModel orderModel = new DataModel(vehicle, newOrder.getFinalDestination());
                        orderArray.add(orderModel);
                        if(mContext != null) {
                            MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(mContext, orderArray);
                            pioneerOrderList.setAdapter(myCustomListAdapter);
                        }
                    }

                }
            } else {

                //  Functions.MyFunctions.displayMessage(mappleOrderList,"No Vehicle is "+ orderStatus);
            }



                Functions.MyFunctions.dismissDialog();

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(mContext,"Data Loading cancelled");
        }
    };

    TextWatcher searchOrder = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            loadOrderData();


        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
