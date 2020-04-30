package com.idc.wanharcarriage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.DealerOrder;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DealerAdapter extends ArrayAdapter<DealerOrder> {

    private Context context ;
    private ArrayList<DealerOrder> orders;
    private int lastPosition = -1;




    private  static class ViewHolder{
        TextView txtName ;
        TextView txtDestination ;
        TextView txtFreight ;
        TextView txtTotalFreight;
    }

    public DealerAdapter(@NonNull Context context, ArrayList<DealerOrder> data) {
        super(context,R.layout.dealer_item, data);
        this.context = context ;
        this.orders = data ;
    }

    public DealerOrder getItem(int positon)
    {
        return orders.get(positon) ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final DealerOrder order = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        final ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.dealer_item, parent, false);
            viewHolder.txtName = (TextView) convertView.findViewById(R.id.txtdelaerName);
            viewHolder.txtDestination = (TextView) convertView.findViewById(R.id.txtdelaerDestination);
            viewHolder.txtFreight = (TextView) convertView.findViewById(R.id.txtfreightperton);
            viewHolder.txtTotalFreight = (TextView) convertView.findViewById(R.id.txtTotalFreight);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        Query customerDB = FirebaseDatabase.getInstance().getReference("Customer")
                .orderByKey()
                .equalTo(order.getCustomerid());

        customerDB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot customerData: dataSnapshot.getChildren()){
                        Customer customer = customerData.getValue(Customer.class);
                        viewHolder.txtName.setText(customer.getName());
                        viewHolder.txtDestination.setText("Destination: "+order.getDestination());
                        viewHolder.txtFreight.setText("Per Ton: " + order.getFreightperTon());
                        int totalfreight = Integer.valueOf(order.getFreightperTon()) * Integer.valueOf(order.getTotalweight());
                        viewHolder.txtTotalFreight.setText("Total Freight: " + totalfreight);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        // Return the completed view to render on screen
        return convertView;



    }
}
