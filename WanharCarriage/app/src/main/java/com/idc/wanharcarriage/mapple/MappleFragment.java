package com.idc.wanharcarriage.mapple;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.CustomerActivity;
import com.idc.wanharcarriage.DataModel;
import com.idc.wanharcarriage.ExpenseDetails;
import com.idc.wanharcarriage.Functions;
import com.idc.wanharcarriage.MappleClearDetails;
import com.idc.wanharcarriage.MappleCompleteDetails;
import com.idc.wanharcarriage.MappleEmployeeDashboard;
import com.idc.wanharcarriage.MappleOvpsDetails;
import com.idc.wanharcarriage.MappleVpsDetails;
import com.idc.wanharcarriage.MyCustomListAdapter;
import com.idc.wanharcarriage.PioneerEmployeeDashboard;
import com.idc.wanharcarriage.R;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.MappleOrder;
import com.idc.wanharcarriage.classes.Vehicle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class MappleFragment extends Fragment implements AdapterView.OnItemClickListener {

    ListView mappleOrderList ;
    ArrayList<DataModel> orderArray;
    ArrayList<String> orderIDs;
    EditText edtSearchOrder;
    HashMap<String, String> vehicles;
    String orderStatus;
    int fragmentID ;
    String caller = "";

    private  String[] TAB_TITLES  = {"Outside VPS", "Inside VPS", "Cleared", "Completed"};

    public MappleFragment(int fragmentID) {

      //  this.TAB_TITLES = getActivity().getResources().getStringArray(R.array.mapplestatus) ;
        this.orderStatus = TAB_TITLES[fragmentID];
        this.fragmentID = fragmentID;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_mapple, container, false);

        orderArray = new ArrayList<DataModel>();
        orderIDs = new ArrayList<String>();
        vehicles = new HashMap<>();
        mappleOrderList = (ListView) root.findViewById(R.id.mappleOrderList) ;
        edtSearchOrder = (EditText) root.findViewById(R.id.edtSearchOrder) ;
        edtSearchOrder.addTextChangedListener(searchOrder);


        mappleOrderList.setOnItemClickListener(MappleFragment.this);

        if(getActivity().getClass().toString().equals(MappleEmployeeDashboard.class.toString())) {
            caller = "Employee";
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(orderStatus.equals("Outside VPS")){
            Functions.MyFunctions.displayDialog(getParentFragmentManager());
        }
        loadVehicles();
    }

    public void loadVehicles(){
        Query vehicleDB = FirebaseDatabase.getInstance().getReference("Vehicle");
        vehicleDB.addValueEventListener(getVehicleData);
    }

    public void loadOrderData(){
        Query orderDB = FirebaseDatabase.getInstance().getReference("MappleOrder")
                .orderByChild("vehicleStatus")
                .equalTo(orderStatus);
        orderDB.addValueEventListener(getStatusOrders);
    }

    public String getVehicle(String vehicleid){
        return vehicles.get(vehicleid);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = null ;

        switch (fragmentID){
            case 0:
                i = new Intent(getContext(), MappleOvpsDetails.class);
                break;
            case 1:
                i = new Intent(getContext(), MappleVpsDetails.class);
                break;
            case 2:
                i = new Intent(getContext(), MappleClearDetails.class);
                break;
            case 3:
                i = new Intent(getContext(), MappleCompleteDetails.class);
                break;
        }

        i.putExtra("vehicle", orderArray.get(position).getItemmain());
        i.putExtra("order",orderIDs.get(position));
        i.putExtra("caller", caller);
        startActivity(i);
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
                Functions.MyFunctions.displayMessage(getActivity(), "Failed to Load Vehicle Info. Try Again!");

            }
            loadOrderData();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getActivity(),"Data Loading cancelled");
        }
    };

    ValueEventListener getStatusOrders = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            orderArray.clear();
            orderIDs.clear();

            if(dataSnapshot.exists()){
                for(DataSnapshot orderData: dataSnapshot.getChildren()) {
                    MappleOrder newOrder = orderData.getValue(MappleOrder.class);
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
                    }

                }
            } else {

              //  Functions.MyFunctions.displayMessage(mappleOrderList,"No Vehicle is "+ orderStatus);
            }

            if(getActivity() != null) {
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getActivity(), orderArray);
                mappleOrderList.setAdapter(myCustomListAdapter);
            }
            if(orderStatus.equals("Outside VPS")){
                Functions.MyFunctions.dismissDialog();
            }

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Functions.MyFunctions.displayMessage(getActivity(),"Data Loading cancelled");
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
