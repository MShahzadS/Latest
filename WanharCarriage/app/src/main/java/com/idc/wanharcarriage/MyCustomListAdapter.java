package com.idc.wanharcarriage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class MyCustomListAdapter extends ArrayAdapter<DataModel> {

    private Context context ;
    private ArrayList<DataModel> dataModels;
    private int lastPosition = -1;

    private  static class ViewHolder{
        TextView txtDate ;
        TextView txtAmount ;
    }

    public MyCustomListAdapter(@NonNull Context context, ArrayList<DataModel> data) {
        super(context,R.layout.row_item, data);
        this.context = context ;
        this.dataModels = data ;
    }

    public DataModel getItem(int positon)
    {
        return dataModels.get(positon) ;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        DataModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.txtDate = (TextView) convertView.findViewById(R.id.txtincomedate);
            viewHolder.txtAmount = (TextView) convertView.findViewById(R.id.txtincomeamuont);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }


        lastPosition = position;

        viewHolder.txtDate.setText(dataModel.getItemmain());
        viewHolder.txtAmount.setText(dataModel.getItemsecondary());


        // Return the completed view to render on screen
        return convertView;
    }
}
