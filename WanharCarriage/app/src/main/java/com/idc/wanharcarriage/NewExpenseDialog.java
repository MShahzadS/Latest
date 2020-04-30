package com.idc.wanharcarriage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.idc.wanharcarriage.classes.Expense;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import static android.content.ContentValues.TAG;

public class NewExpenseDialog  extends DialogFragment {

    ExpenseDialogListener myExpenseListener;
    EditText edtExpenseDate, edtExpenseAmount;
    Spinner spinnerExpenseType ;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.activity_new_expense, null);

        edtExpenseDate = (EditText) view.findViewById(R.id.edtexpenseDate);
        edtExpenseAmount = (EditText) view.findViewById(R.id.edtExpenseAmount) ;
        spinnerExpenseType = (Spinner) view.findViewById(R.id.spinnerExpenseType);

        MyDatePicker expensedate = new MyDatePicker(view.getContext(),edtExpenseDate);

        builder.setView(view)
                .setTitle("Add Dealer")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        NewExpenseDialog.this.dismiss();
                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String date = edtExpenseDate.getText().toString();
                        String amount = edtExpenseAmount.getText().toString();
                        String expenseType = spinnerExpenseType.getSelectedItem().toString();

                        myExpenseListener.saveExpense(date, expenseType, amount);

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
             myExpenseListener = (ExpenseDialogListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement ExpenseDialogListener") ;
        }
    }

    public interface ExpenseDialogListener {

        void saveExpense(String date, String type, String amount);
    }
}
