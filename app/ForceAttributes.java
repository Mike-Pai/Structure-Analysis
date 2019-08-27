package com.example.Eric_app;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatDialogFragment;

public class ForceAttributes extends AppCompatDialogFragment {
    private EditText editText1;
    private EditText editText2;
    private float oldF,oldAngle;
    private float setF,setAngle;
    private float setFx,setFy,setradians;

    public ForceAttributes(Float F,Float Angle) {
        oldF = F;
        oldAngle = Angle;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_force,null);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText1.setText(String.valueOf(oldF));
        editText2.setText(String.valueOf(oldAngle));

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
                        setF = Float.valueOf(str1);
                        setAngle = Float.valueOf(str2);
                        setradians = (float)(Math.toRadians(setAngle));
                        setFx = (float)(setF*Math.cos(setradians));
                        setFy = (float)(setF*Math.sin(setradians));
                        TrussPaintView.forcemap.get(String.valueOf(TrussPaintView.forcekey.get(TrussPaintView.forceindex))).set(0,setFx);
                        TrussPaintView.forcemap.get(String.valueOf(TrussPaintView.forcekey.get(TrussPaintView.forceindex))).set(1,setFy);
                        TrussPaintView.forcemap.get(String.valueOf(TrussPaintView.forcekey.get(TrussPaintView.forceindex))).set(2,setAngle);
                        dismiss();
                    }
                });

        return builder.create();

    }

}