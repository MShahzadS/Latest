package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Income;

import java.util.ArrayList;

public class IncomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView incomeList ;
    ArrayList<DataModel> incomearray ;
    ArrayList<String> incomeIDs;
    EditText edtIncomeDate ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);

        Functions.MyFunctions.displayDialog(getSupportFragmentManager());
        edtIncomeDate = (EditText) findViewById(R.id.edtIncomeDate );

        MyDatePicker mStart = new MyDatePicker(IncomeActivity.this, edtIncomeDate) ;

        incomearray = new ArrayList<DataModel>();
        incomeIDs = new ArrayList<String>();
        incomeList = (ListView) findViewById(R.id.incomeList) ;
        incomeList.setOnItemClickListener(this);

        edtIncomeDate.addTextChangedListener(searchIncome);
        loadIncome();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(IncomeActivity.this, IncomeDetails.class) ;
        i.putExtra("income", incomeIDs.get(position)) ;
        startActivity(i);
    }

    void loadIncome(){
        DatabaseReference incomeDB = FirebaseDatabase.getInstance().getReference("Income");
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
            if(getApplicationContext() != null){
                MyCustomListAdapter myCustomListAdapter = new MyCustomListAdapter(getApplicationContext(),incomearray);
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
            incomeDB.addValueEventListener(getIncome);

        }
    };
}
