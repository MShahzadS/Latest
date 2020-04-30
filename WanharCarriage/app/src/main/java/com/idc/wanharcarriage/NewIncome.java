package com.idc.wanharcarriage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class NewIncome extends AppCompatActivity {


    EditText edtIncomedate ;
    SearchableSpinner customerSpinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_income);

        edtIncomedate = (EditText) findViewById(R.id.edtIncomeDate);


        customerSpinner = (SearchableSpinner) findViewById(R.id.customerspinner) ;
        customerSpinner.setTitle("Select a Customer");

        MyDatePicker mydate = new MyDatePicker(NewIncome.this, edtIncomedate) ;


    }


}
