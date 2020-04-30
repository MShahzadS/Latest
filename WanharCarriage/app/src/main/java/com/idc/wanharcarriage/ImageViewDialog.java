package com.idc.wanharcarriage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.idc.wanharcarriage.classes.Customer;
import com.idc.wanharcarriage.classes.DealerOrder;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;


public class ImageViewDialog extends DialogFragment  {


    ImageView imagePreview ;
    ImagePreviewListener myListener;
    Bitmap bmp ;

    public  ImageViewDialog(Bitmap bmp)
    {
        this.bmp = bmp;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder  builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater() ;
        View view = inflater.inflate(R.layout.imagepreview, null);


        imagePreview = (ImageView) view.findViewById(R.id.imgImagePreview);
        imagePreview.setImageBitmap(bmp);

        builder.setView(view)
                .setTitle("Add Dealer")
                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ImageViewDialog.this.dismiss();
                    }
                });

        myListener.updateImage();
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            myListener = (ImagePreviewListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement MyDialogListener") ;
        }
    }

    public interface ImagePreviewListener {
        void updateImage();
    }
}
