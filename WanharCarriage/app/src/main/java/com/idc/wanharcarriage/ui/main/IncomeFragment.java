package com.idc.wanharcarriage.ui.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.DataModel;
import com.idc.wanharcarriage.Functions;
import com.idc.wanharcarriage.IncomeActivity;
import com.idc.wanharcarriage.IncomeDetails;
import com.idc.wanharcarriage.Login;
import com.idc.wanharcarriage.MyCustomListAdapter;
import com.idc.wanharcarriage.MyDatePicker;
import com.idc.wanharcarriage.NewIncome;
import com.idc.wanharcarriage.R;
import com.idc.wanharcarriage.classes.Income;

import java.util.ArrayList;

public class IncomeFragment extends Fragment implements AdapterView.OnItemClickListener{


    ListView incomeList ;
    ArrayList<DataModel> incomearray ;
    ArrayList<String> incomeIDs;
    EditText edtIncomeDate ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_income,container,false);


        Functions.MyFunctions.displayDialog(getParentFragmentManager());
        edtIncomeDate = (EditText) root.findViewById(R.id.edtIncomeDate );

        MyDatePicker mStart = new MyDatePicker(getActivity(), edtIncomeDate) ;

        incomearray = new ArrayList<DataModel>();
        incomeIDs = new ArrayList<String>();
        incomeList = (ListView) root.findViewById(R.id.incomeList) ;
        incomeList.setOnItemClickListener(this);

        edtIncomeDate.addTextChangedListener(searchIncome);
        loadIncome();
        return root ;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), IncomeDetails.class) ;
        i.putExtra("income", incomeIDs.get(position)) ;
        i.putExtra("caller","Employee");
        startActivity(i);
    }

    void loadIncome(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.USERPREFERENCES, getActivity().MODE_PRIVATE);
        String empid =sharedPreferences.getString(Login.ID,"");
        Query incomeDB = FirebaseDatabase.getInstance().getReference("Income")
                                            .orderByKey()
                                            .equalTo(empid);
        incomeDB.addValueEventListener(getIncome);
    }

    ValueEventListener getIncome = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            incomearray.clear();
            incomeIDs.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot incomeData: dataSnapshot.getChildren()){

                    Income newincome = incomeData.getValue(Income.class);
                    incomeIDs.add(incomeData.getKey());
                    DataModel incomeModel = new DataModel(newincome.getIncomeDate(), newincome.getCompany());
                    incomearray.add(incomeModel);
                }
            }
            if(getActivity() != null){
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getActivity(),incomearray);
                incomeList.setAdapter(myCustomListAdapter);
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    TextWatcher searchIncome = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            Query incomeDB = FirebaseDatabase.getInstance().getReference("Income")
                    .orderByChild("incomeDate")
                    .equalTo(edtIncomeDate.getText().toString());
            incomeDB.addValueEventListener(getDateIncome);

        }
    };

    ValueEventListener getDateIncome = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            incomearray.clear();
            incomeIDs.clear();
            if(dataSnapshot.exists()){
                for(DataSnapshot incomeData: dataSnapshot.getChildren()){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences(Login.USERPREFERENCES, getActivity().MODE_PRIVATE);
                    String company =sharedPreferences.getString(Login.Company,"");

                    Income newincome = incomeData.getValue(Income.class);
                    if(company.equals(newincome.getCompany())) {
                        incomeIDs.add(incomeData.getKey());
                        DataModel incomeModel = new DataModel(newincome.getIncomeDate(), newincome.getCompany());
                        incomearray.add(incomeModel);
                    }
                }
            }
            if(getActivity() != null){
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getActivity(),incomearray);
                incomeList.setAdapter(myCustomListAdapter);
            }
            Functions.MyFunctions.dismissDialog();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
