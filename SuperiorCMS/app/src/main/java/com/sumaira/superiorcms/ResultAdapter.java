package com.sumaira.superiorcms;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ResultAdapter extends ArrayAdapter<ResultModel> {

    ArrayList<String> students ;
    Context context;
    public ArrayList<String> marks ;
    int lastposition = -1 ;
    ResultViewHolder viewHolder ;

    public ResultAdapter(@NonNull Context context, ArrayList<String> students) {
        super(context, R.layout.resultitem);

        this.context = context ;
        this.students = students ;
        marks = new ArrayList<String>() ;

        for(String student: students)
            marks.add("0") ;

    }

    @Override
    public int getCount() {
        return students.size();
    }

    @Override
    public ResultModel getItem(int position) {
        return super.getItem(position);
    }

    private class ResultViewHolder{
        TextView txtStd ;
        EditText edtMark ;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View root  = null;

        if (convertView == null) {

            viewHolder = new ResultViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.resultitem, parent, false);
            viewHolder.txtStd = (TextView) convertView.findViewById(R.id.txtstdregnum);
            viewHolder.edtMark = (EditText) convertView.findViewById(R.id.edtObtainedMarks);

            root = convertView;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ResultViewHolder) convertView.getTag();
            root=convertView;
        }

        viewHolder.txtStd.setText(students.get(position)) ;
        final View finalRoot = root;
        viewHolder.edtMark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                marks.set(position,s.toString()) ;
                //Toast.makeText(context,marks.get(position),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });
        return root ;
    }

}

class ResultModel{
    String studentID ;
    String marks  ;

    public ResultModel() {
        this.studentID = "";
        this.marks = "";
    }

    public ResultModel(String studentID, String marks) {
        this.studentID = studentID;
        this.marks = marks;
    }
}
