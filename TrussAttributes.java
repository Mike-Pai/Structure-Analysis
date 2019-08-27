package com.example.Eric_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class TrussAttributes extends AppCompatDialogFragment {
    private EditText editText1;
    private EditText editText2;
    private float oldE,oldArea,oldI;
    private float setE,setArea,setI;
    public static boolean checkingtruss = false;

    public TrussAttributes(Float E,Float A,Float I) {
        oldE = E;
        oldArea = A;
        oldI = I;
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_truss,null);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText1.setText(String.valueOf(oldE));
        editText2.setText(String.valueOf(oldI));

        builder.setView(view)
                //  .setTitle("Add")
                .setNegativeButton("cancel",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        dismiss();
                    }
                })
                .setPositiveButton("Add",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        String str1 = editText1.getText().toString().trim();
                        String str2 = editText2.getText().toString().trim();
                        setE = Float.valueOf(str1);
                        setI = Float.valueOf(str2);
                        if (setE == oldE || setArea == oldArea) {
                            checkingtruss = false;
                        } else {
                            checkingtruss = true;
                            TrussPaintView.attritrussarray.get(TrussPaintView.trussindex).set(0,setE);
                            TrussPaintView.attritrussarray.get(TrussPaintView.trussindex).set(1,setI);
                        }
                        dismiss();
                    }
                });



        return builder.create();

    }

}