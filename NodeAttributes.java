package com.example.Eric_app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Path;
import android.graphics.Region;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;

public class NodeAttributes extends AppCompatDialogFragment {

    static boolean checkingnode = false;
    private EditText editText1;
    private EditText editText2;
    private float oldx,oldy;
    private float setX,setY;
    private int checknodeindex = TrussPaintView.nodeindex;



    public NodeAttributes(Float inputx, Float inputy) {
        oldx = inputx;
        oldy = inputy;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_node,null);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText1.setText(String.valueOf(oldx));
        editText2.setText(String.valueOf(oldy));
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
                    public void onClick(DialogInterface dialogInterface,int i){
                        String str1 = editText1.getText().toString().trim();
                        String str2 = editText2.getText().toString().trim();
                        setX = Float.valueOf(str1);
                        setY = Float.valueOf(str2);
                        float NodeX,NodeY;
                        float oriX,oriY;

                        if (setX == oldx || setY == oldy) {
                            checkingnode = false;
                        } else {
                            checkingnode = true;
                            TrussPaintView.nodearray1.get(TrussPaintView.nodeindex).set(1,setX);
                            TrussPaintView.nodearray1.get(TrussPaintView.nodeindex).set(2,setY);

                            oriX = (float)TrussPaintView.oriNodearray.get(checknodeindex).get(0);
                            oriY = (float)TrussPaintView.oriNodearray.get(checknodeindex).get(1);

                            NodeX = setX + TrussPaintView.fwidth / 2;
                            NodeY = -setY + TrussPaintView.fheight / 2;
                            ArrayList<Float> setarray = new ArrayList<>();
                            setarray.add(NodeX);
                            setarray.add(NodeY);
                            TrussPaintView.oriNodearray.set(TrussPaintView.nodeindex,setarray);
                            Log.d("Node",NodeX +","+ NodeY);

                            DeleteOldNode(oriX,oriY);
                            DrawNewNode(NodeX,NodeY);
                        }
                        dismiss();
                    }
                });

        return builder.create();
    }

    private void DeleteOldNode(float dx,float dy) {
        Path delete_Np = new Path();
        Path delete_Nt = new Path();

        delete_Np.addCircle(dx,dy,TrussPaintView.DOT_ERROR, Path.Direction.CW);
        delete_Nt.addPath(TrussPaintView.nodetextarray.get(checknodeindex));
        TrussPaintView.canvas.drawPath(delete_Np,TrussPaintView.clearpen);
        TrussPaintView.canvas.drawPath(delete_Nt,TrussPaintView.clearpen);
    }

    private void DrawNewNode(float x,float y) {
        Path nnode = new Path();
        Path textnode = new Path();
        Path trans_nnode = new Path();

        nnode.addCircle(x,y,TrussPaintView.DOT_RADIUS, Path.Direction.CW);
        TrussPaintView.nodepatharray.get(TrussPaintView.nodeindex).set(nnode);
        trans_nnode.addCircle(x,y,2*TrussPaintView.DOT_RADIUS, Path.Direction.CW);
        TrussPaintView.noderegionarray.get(TrussPaintView.nodeindex).setPath(trans_nnode,TrussPaintView.globalRegion);
        TrussPaintView.canvas.drawPath(nnode,TrussPaintView.nodepen);
        if (checknodeindex < 9) {
            textnode.addRect(x - 42, y+TrussPaintView.DOT_RADIUS, x-6, y+48, Path.Direction.CW);
            TrussPaintView.nodetextarray.set(checknodeindex,textnode);
            TrussPaintView.canvas.drawText(String.valueOf(checknodeindex+1), x - 42, y + 48, TrussPaintView.nodenumpen);
        } else if (checknodeindex < 99) {
            textnode.addRect(x - 54, y+TrussPaintView.DOT_RADIUS, x-6, y+48, Path.Direction.CW);
            TrussPaintView.nodetextarray.set(checknodeindex,textnode);
            TrussPaintView.canvas.drawText(String.valueOf(checknodeindex+1), x - 54, y + 48, TrussPaintView.nodenumpen);
        } else if (checknodeindex < 999) {
            textnode.addRect(x - 72, y+TrussPaintView.DOT_RADIUS, x-6, y+48, Path.Direction.CW);
            TrussPaintView.nodetextarray.set(checknodeindex,textnode);
            TrussPaintView.canvas.drawText(String.valueOf(checknodeindex+1), x - 72, y + 48, TrussPaintView.nodenumpen);
        }

    }

    /*private void DeleteOldTruss(float startX, float startY, float endX, float endY) {
        Path delete_Tp = new Path();
        Path delete_Tt = new Path();

        delete_Np.addCircle(dx,dy,TrussPaintView.DOT_ERROR, Path.Direction.CW);
        delete_Nt.addPath(TrussPaintView.nodetextarray.get(checknodeindex));
        TrussPaintView.canvas.drawPath(delete_Np,TrussPaintView.clearpen);
        TrussPaintView.canvas.drawPath(delete_Nt,TrussPaintView.clearpen);

    }

    private void DrawNewTruss(float startX,float startY,float endX,float endY){
        Path newtruss = new Path();
        Path newtranstruss_p = new Path();
        Region newtrussregion = new Region();

        ttruss.moveTo(startX,startY);
        ttruss.lineTo(endX,endY);

        canvas.drawPath(ttruss,trusspen);
        trusspatharray.add(ttruss);
        if (startX < endX) {
            for (int i = 0; i <= 2048; i++) {
                transtruss_p.addRoundRect(startX -DOT_RADIUS +(i * (endX - startX) / 2048), startY + DOT_RADIUS + (i * (endY - startY) / 2048)
                        , startX +DOT_RADIUS+ ((i + 1) * (endX - startX) / 2048), startY - DOT_RADIUS +((i + 1) * (endY - startY) / 2048)
                        ,5,5, Path.Direction.CCW
                );
            }
            if (Math.abs((endY-startY)/(endX-startX))<=1 ||(endY-startY)/(endX-startX) ==0) {
                canvas.drawText(String.valueOf((int) k), startX + ((endX - startX) / 2), startY + ((endY - startY) / 2) - DOT_RADIUS, trussnumpen);
            } else {
                canvas.drawText(String.valueOf((int) k), startX + ((endX - startX) / 2) + DOT_RADIUS, startY + ((endY - startY) / 2), trussnumpen);
            }
        }else if (startX > endX) {
            for (int i = 0; i < 2047; i++) {
                transtruss_p.addRoundRect(endX -DOT_RADIUS+ (i * (startX - endX) / 2048), endY + DOT_RADIUS + (i * (startY - endY) / 2048)
                        , endX +DOT_RADIUS+((i + 1) * (startX - endX) / 2048), endY - DOT_RADIUS + ((i + 1) * (startY - endY) / 2048)
                        , 5,5,Path.Direction.CCW
                );
            }
            if (Math.abs((startY - endY) / (startX - endX)) <= 1) {
                canvas.drawText(String.valueOf((int) k), endX + ((startX - endX) / 2), endY + ((startY - endY) / 2) - DOT_RADIUS, trussnumpen);
            } else {
                canvas.drawText(String.valueOf((int) k), endX + ((startX - endX) / 2) + DOT_RADIUS, endY + ((startY - endY) / 2), trussnumpen);
            }
        }

        trussregion.setPath(transtruss_p, globalRegion);
        trussrgionarray.add(trussregion);

    }*/
}
