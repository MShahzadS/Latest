package com.sumaira.superiorcms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class StudentListAdapater extends ArrayAdapter<StudentModel> {

    ArrayList<String> students ;
    Context context;
    String classid ;
    public StudentListAdapater(Context context, ArrayList<String> students, String classid) {
        super(context,R.layout.studentattitem);

        this.students = students ;
        this.context = context ;
        this.classid = classid ;
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
    public StudentModel getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View root  = null;

        if (convertView == null) {

            viewHolder = new AtStdViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.studentattitem, parent, false);
            viewHolder.txtStd = (TextView) convertView.findViewById(R.id.txtAtdStudent);
            viewHolder.rdStatus = (RadioGroup) convertView.findViewById(R.id.rdiostatus);

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
                final RadioButton btnSelected = (RadioButton) finalRoot.findViewById(i) ;

                String date = new SimpleDateFormat("yyyy-MM-dd",
                                                    Locale.getDefault()).format(new Date());
                String studentid = students.get(position) ;
                String status = btnSelected.getText().toString() ;

                final Attendance atd = new Attendance(date,classid,studentid,status) ;

                DatabaseReference atdDB = FirebaseDatabase.getInstance().getReference("attendance");
                String newkey = atdDB.push().getKey() ;
                atdDB.child(newkey).setValue(atd).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        viewHolder.rdStatus.setEnabled(false);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),  atd.getStdid() + " Attendance Not Saved "   ,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return root ;
    }
}

class StudentModel{
    String studentID ;
    String status  ;

    public StudentModel() {
        this.studentID = "";
        this.status = "";
    }

    public StudentModel(String studentID, String status) {
        this.studentID = studentID;
        this.status = status;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
