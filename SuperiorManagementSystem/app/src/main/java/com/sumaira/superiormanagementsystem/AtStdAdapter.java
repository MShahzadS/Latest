package com.sumaira.superiormanagementsystem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AtStdAdapter extends ArrayAdapter<AtdStudentModel> {

    ArrayList<String> students ;
    Context context;
    public AtStdAdapter(Context context, ArrayList<String> students) {
        super(context,R.layout.atstudentitem);

        this.students = students ;
        this.context = context ;
    }


    private class AtStdViewHolder{
        TextView txtStd ;
        RadioGroup rdStatus ;
    }

    int lastposition = -1 ;

    AtStdViewHolder viewHolder ;

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public AtdStudentModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View root  = null;

        if (convertView == null) {

            viewHolder = new AtStdViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.atstudentitem, parent, false);
            viewHolder.txtStd = (TextView) convertView.findViewById(R.id.txtAtdStudent);
            viewHolder.rdStatus = (RadioGroup) convertView.findViewById(R.id.rdatdstatus);


            root = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (AtStdViewHolder) convertView.getTag();
            root=convertView;
        }

        viewHolder.txtStd.setText(students.get(position)) ;
        final View finalRoot = root;
        viewHolder.rdStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton btnSelected ;
                int selectedid  = (int) viewHolder.rdStatus.getCheckedRadioButtonId() ;

                btnSelected = (RadioButton) finalRoot.findViewById(selectedid) ;

                Toast.makeText(getContext(), students.get(position) + " " +btnSelected.getText(),Toast.LENGTH_LONG).show();

            }
        });
        return root ;
    }
}
