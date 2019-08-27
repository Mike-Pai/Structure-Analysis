package com.example.Eric_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


public class TrussPaintView extends View implements Serializable {

    public static final int DOT_COLOR = Color.BLUE;
    public static final int SELECT_COLOR = Color.GREEN;
    public static final int TRUSS_COLOR = Color.BLACK;
    public static final int TRUSS_COLOR2 = Color.WHITE;
    public static final int FORCE_COLOR = Color.RED;
    public static final int SUPPORT_COLOR = Color.rgb(0,0,128);
    public static final int BG_COLOR = Color.WHITE;
    public static int DOT_RADIUS = 18;
    public static int DOT_ERROR = 20;

    private int backgroundcolor;
    public static int height, width;
    public static Paint nodepen, trusspen, forcepen,supportpen,momentpen;
    public static Paint painter_BG1, painter_BG2, nodenumpen , trussnumpen;
    public static Paint BitmapPaint = new Paint(Paint.DITHER_FLAG);
    public static Paint clearpen,cleartextpen;
    public static Path Node_p, Truss_p, Force_p,Pin_p,Roller_p,Fix_p,Support_p;
    public static Region globalRegion;
    public static Bitmap bitmap;
    public static Canvas canvas;
    public static Matrix Mapmatrix;

    public static ArrayList<ArrayList> nodearray1 = new ArrayList<>();
    public static ArrayList<ArrayList> nodearray2 = new ArrayList<>();

    public static ArrayList<ArrayList> oriNodearray = new ArrayList<>();
    public static ArrayList<ArrayList> oriTruss = new ArrayList<>();
    public static ArrayList<ArrayList> oriForce = new ArrayList<>();
    public static ArrayList<ArrayList> oriMoment = new ArrayList<>();
    public static ArrayList<ArrayList> oriSupport = new ArrayList<>();

    public static ArrayList<Region> noderegionarray = new ArrayList<>();
    public static ArrayList<Region> trussrgionarray = new ArrayList<>();
    public static ArrayList<Region> forceregionarray = new ArrayList<>();
    public static ArrayList<Region> supportregionarray = new ArrayList<>();
    public static ArrayList<Region> momentregionarray = new ArrayList<>();

    public static ArrayList<Path> nodepatharray = new ArrayList<>();
    public static ArrayList<Path> nodetextarray = new ArrayList<>();
    public static ArrayList<Path> trusspatharray = new ArrayList<>();
    public static ArrayList<Path> forcepatharray = new ArrayList<>();
    public static ArrayList<Path> supportpatharray = new ArrayList<>();
    public static ArrayList<Path> momentpatharray = new ArrayList<>();

    public static ArrayList<ArrayList> element = new ArrayList<>();
    public static ArrayList<ArrayList> checkelement = new ArrayList<>();
    public static ArrayList<ArrayList> attritrussarray = new ArrayList<>();
    public static ArrayList<Integer> forcekey = new ArrayList<>();
    public static ArrayList<Integer> momentkey = new ArrayList<>();
    public static ArrayList<ArrayList> support = new ArrayList<>();
    public static ArrayList<Integer> checksupport = new ArrayList<>();

    public static float fwidth, fheight;
    private float oriNodeX, oriNodeY, DotX, DotY;
    private float startX, startY;
    private float endX, endY;

    private int horgrid = 100, vergrid = 100;
    private int trusscount = 0, forcecount = 0,supportcount = 0,momentcount = 0;
    private int selectnodecount = 0,selecttrusscount = 0,selectforcecount = 0,selectsupportcount = 0;
    public static int nodeindex = -1, trussindex = -1, forceindex = -1;
    private float downX = 1, downY = -1, n = -1, j = 0,k=0;

    public TrussPaintView(Context context) {
        this(context, null);
    }

    public TrussPaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //設定畫點的筆
        nodepen = new Paint();
        nodepen.setStyle(Paint.Style.FILL);
        nodepen.setStrokeWidth(2);
        nodepen.setColor(DOT_COLOR);
        nodepen.setStrokeJoin(Paint.Join.ROUND);
        nodepen.setStrokeCap(Paint.Cap.ROUND);
        nodepen.setAntiAlias(true);

        //設定畫桿件的筆
        trusspen = new Paint();
        trusspen.setStyle(Paint.Style.FILL_AND_STROKE);
        trusspen.setStrokeWidth(10);
        trusspen.setColor(TRUSS_COLOR);
        trusspen.setStrokeJoin(Paint.Join.ROUND);
        trusspen.setStrokeCap(Paint.Cap.ROUND);
        trusspen.setAntiAlias(true);

        //設定畫力的筆
        forcepen = new Paint();
        forcepen.setStyle(Paint.Style.FILL);
        forcepen.setStrokeWidth(10);
        forcepen.setColor(FORCE_COLOR);
        forcepen.setTextSize(36);
        forcepen.setStrokeJoin(Paint.Join.ROUND);
        forcepen.setStrokeCap(Paint.Cap.ROUND);
        forcepen.setAntiAlias(true);

        //設定背景粗網格畫筆
        painter_BG1 = new Paint();
        painter_BG1.setStyle(Paint.Style.FILL);
        painter_BG1.setStrokeWidth(10);
        painter_BG1.setColor(Color.DKGRAY);
        painter_BG1.setAlpha(200);
        painter_BG1.setAntiAlias(true);

        //設定背景細網格畫筆
        painter_BG2 = new Paint();
        painter_BG2.setStyle(Paint.Style.FILL);
        painter_BG2.setStrokeWidth(2);
        painter_BG2.setColor(Color.LTGRAY);
        painter_BG2.setAlpha(100);
        painter_BG2.setAntiAlias(true);

        //設定輸出文字畫筆
        nodenumpen = new Paint();
        nodenumpen.setColor(Color.BLUE);
        nodenumpen.setStyle(Paint.Style.FILL);
        nodenumpen.setTextSize(36);
        nodenumpen.setAntiAlias(true);

        trussnumpen = new Paint();
        trussnumpen.setColor(TRUSS_COLOR);
        trussnumpen.setStyle(Paint.Style.FILL);
        trussnumpen.setTextSize(48);
        trussnumpen.setFakeBoldText(true);
        trussnumpen.setAntiAlias(true);

        //設定支點畫筆
        supportpen = new Paint();
        supportpen.setStyle(Paint.Style.STROKE);
        supportpen.setStrokeWidth(5);
        supportpen.setColor(SUPPORT_COLOR);
        supportpen.setStrokeJoin(Paint.Join.ROUND);
        supportpen.setStrokeCap(Paint.Cap.ROUND);
        supportpen.setAntiAlias(true);

        clearpen = new Paint();
        clearpen.setStrokeWidth(2);
        clearpen.setAntiAlias(true);
        clearpen.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        cleartextpen = new Paint();
        cleartextpen.setTextSize(48);
        cleartextpen.setFakeBoldText(true);
        cleartextpen.setAntiAlias(true);
        cleartextpen.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        momentpen = new Paint();
        momentpen.setStyle(Paint.Style.STROKE);
        momentpen.setStrokeWidth(15);
        momentpen.setColor(FORCE_COLOR);
        momentpen.setTextSize(36);
        momentpen.setStrokeJoin(Paint.Join.ROUND);
        momentpen.setStrokeCap(Paint.Cap.ROUND);
        momentpen.setAntiAlias(true);


        Mapmatrix = new Matrix();
        Node_p = new Path();
        Truss_p = new Path();
        Force_p = new Path();
        Pin_p = new Path();
        Roller_p = new Path();
        Fix_p = new Path();
        Support_p = new Path();
    }

    //獲取螢幕的長寬
    public void init(DisplayMetrics metrics) {
        height = metrics.heightPixels;
        width = metrics.widthPixels;

        fheight = height;
        fwidth = width;

        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
    }

    //確認螢幕的長寬
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Mapmatrix.reset();

        globalRegion = new Region(-w, -h, w, h);

        bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    /**
    *
    *
    *
    * */



    //畫點
    public void setDot() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        downX = motionEvent.getX();
                        downY = motionEvent.getY();
                        DrawNode(downX, downY);
                        invalidate();
                        break;
                }
                return true;
            }
        });
    }

    //選擇點
    boolean checkN = false;
    public boolean selectnode() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();

                int x = (int) pts[0];
                int y = (int) pts[1];

                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {
                                nodepen.setColor(SELECT_COLOR);
                                Node_p = nodepatharray.get(nodeindex);
                                canvas.drawPath(Node_p, nodepen);
                                selectnodecount++;
                                if (selectnodecount > 1) {
                                    DotTurnBack();
                                    checkN = false;
                                } else {
                                    checkN = true;
                                }
                            } else {
                                DotTurnBack();
                                checkN = false;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
        return checkN;
    }

    //丟出現在點選之點座標
    public float ThrowNodeX() {
        float tnodeX;
        tnodeX = (float)nodearray1.get(nodeindex).get(1);
        return tnodeX;
    }
    public float ThrowNodeY() {
        float tnodeY;
        tnodeY = (float)nodearray1.get(nodeindex).get(2);
        return tnodeY;
    }

    /*public void ResetNode() {
        float[] pts = new float[2];
        pts[0] = (float)nodearray1.get(nodeindex).get(1);
        pts[1] = (float)nodearray1.get(nodeindex).get(2);
        Log.d("reset",pts[0]+","+pts[1]);
        Path nnode = new Path();
        Path trans_nnode = new Path();
        Region nregion = new Region();

        Mapmatrix.mapPoints(pts);

        nnode.addCircle(pts[0],pts[1],DOT_RADIUS, Path.Direction.CW);
        nodepatharray.get(nodeindex).set(nnode);
        canvas.drawPath(nodepatharray.get(nodeindex),nodepen);
        trans_nnode.addCircle(pts[0],pts[1],2*DOT_RADIUS, Path.Direction.CW);
        nregion.setPath(trans_nnode, globalRegion);
        noderegionarray.get(nodeindex).set(nregion);
        invalidate();
    }*/

    //建立桿件
    int a = 0;
    int b = 0;
    public void setTruss() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();


                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {
                                nodepen.setColor(SELECT_COLOR);
                                Node_p = nodepatharray.get(nodeindex);
                                canvas.drawPath(Node_p, nodepen);

                                oriTruss.add(oriNodearray.get(nodeindex));

                                float[] tps1 = new float[2];
                                tps1[0] = (float) oriTruss.get(0).get(0);
                                tps1[1] = (float) oriTruss.get(0).get(1);

                                trusscount++;
                                if (trusscount == 1) {
                                    a = nodeindex;
                                } else if (trusscount == 2) {
                                    float[] tps2 = new float[2];
                                    tps2[0] = (float) oriTruss.get(1).get(0);
                                    tps2[1] = (float) oriTruss.get(1).get(1);

                                    b = nodeindex;
                                    if (a == b || Checktruss(a, b) == false) {
                                        DotTurnBack();
                                    } else {
                                        DrawTruss(tps1[0],tps1[1],tps2[0],tps2[1]);
                                        ArrayList<Integer> truss = new ArrayList<>();
                                        ArrayList<Integer> checktruss = new ArrayList<>();
                                        ArrayList<Float> attritruss = new ArrayList<>();
                                        truss.add(a);
                                        truss.add(b);
                                        Collections.sort(truss);
                                        checktruss.add(b);
                                        checktruss.add(a);
                                        attritruss.add((float) 200);
                                        attritruss.add((float) 10);
                                        attritruss.add((float) 10);
                                        element.add(truss);
                                        checkelement.add(checktruss);
                                        attritrussarray.add(attritruss);
                                        Log.w("truss", truss.toString());
                                        Log.w("element", element.toString());
                                        Log.w("num1", String.valueOf(trusspatharray.size()));
                                        Log.w("num2", String.valueOf(trussrgionarray.size()));
                                        DotTurnBack();
                                    }
                                }
                            } else {
                                DotTurnBack();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    //把點顏色回歸預設
    void DotTurnBack() {
        trusscount = 0;
        selectnodecount = 0;
        oriTruss.clear();
        nodepen.setColor(DOT_COLOR);
        for (int i = 0; i < nodepatharray.size(); i++) {
            Node_p = nodepatharray.get(i);
            canvas.drawPath(Node_p, nodepen);
        }
    }

    boolean trusscheck = true;
    boolean Checktruss(int a, int b) {
        for (int i = 0; i < element.size(); i++) {
            if (element.get(i).contains(a) && element.get(i).contains(b) ||
                    checkelement.get(i).contains(a) && checkelement.get(i).contains(b)) {
                trusscheck = false;
                break;
            } else {
                trusscheck = true;
            }
        }
        return trusscheck;
    }

    boolean checkT = false;
    public boolean selecttruss() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();

                int x = (int) pts[0];
                int y = (int) pts[1];

                trussindex = FindTrussRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (trussrgionarray.get(trussindex).contains(x, y)) {
                                trusspen.setColor(SELECT_COLOR);
                                Truss_p = trusspatharray.get(trussindex);
                                canvas.drawPath(Truss_p, trusspen);
                                nodepen.setColor(DOT_COLOR);
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                selecttrusscount++;
                                if (selecttrusscount > 1) {
                                    TrussTurnBack();
                                    for (int i = 0; i < nodepatharray.size(); i++) {
                                        Node_p = nodepatharray.get(i);
                                        canvas.drawPath(Node_p, nodepen);
                                    }
                                    checkT = false;
                                } else {
                                    checkT = true;
                                }
                            } else {
                                TrussTurnBack();
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                checkT = false;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
        return checkT;
    }

    void TrussTurnBack() {
        selecttrusscount = 0;
        trusspen.setColor(TRUSS_COLOR);
        for (int i = 0; i < trusspatharray.size(); i++) {
            Truss_p = trusspatharray.get(i);
            canvas.drawPath(Truss_p, trusspen);
        }
    }

    //丟出現在點選之點座標
    public float ThrowTrussE() {
        float ttrussE;
        ttrussE = (float)attritrussarray.get(trussindex).get(0);
        return ttrussE;
    }
    public float ThrowTrussA() {
        float ttrussA;
        ttrussA = (float)attritrussarray.get(trussindex).get(1);
        return ttrussA;
    }
    public  float ThrowTrussI(){
        float ttrussI;
        ttrussI = (float)attritrussarray.get(trussindex).get(2);
        return ttrussI;
    }



    //設置外力
    public void setForce() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriForce.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriForce.get(0).get(0);
                                tps[1] = (float) oriForce.get(0).get(1);

                                DrawForce(tps[0], tps[1]);
                                forcecount++;
                                if (forcecount == 1) {
                                    forcecount = 0;
                                    oriForce.clear();
                                    return true;
                                }
                            } else {
                                forcecount = 0;
                                oriForce.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    boolean checkF = false;
    public boolean selectforce() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();

                int x = (int) pts[0];
                int y = (int) pts[1];

                forceindex = FindForceRegionIndex(x, y);

                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (forceregionarray.get(forceindex).contains(x, y)) {
                                forcepen.setColor(SELECT_COLOR);
                                Force_p = forcepatharray.get(forceindex);
                                canvas.drawPath(Force_p, forcepen);
                                nodepen.setColor(DOT_COLOR);
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                selectforcecount++;
                                if (selectforcecount > 1) {
                                    ForceTurnBack();
                                    for (int i = 0; i < nodepatharray.size(); i++) {
                                        Node_p = nodepatharray.get(i);
                                        canvas.drawPath(Node_p, nodepen);
                                    }
                                    checkF = false;
                                } else {
                                    checkF = true;
                                }
                            } else {
                                ForceTurnBack();
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                checkF = false;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
        return checkF;
    }
    void ForceTurnBack() {
        selectforcecount = 0;
        forcepen.setColor(FORCE_COLOR);
        for (int i = 0; i < forcepatharray.size(); i++) {
            Force_p = forcepatharray.get(i);
            canvas.drawPath(Force_p, forcepen);
        }
    }

    public float ThrowForceF() {
        float tforceFx,tforceFy,tforceF;
        Log.d("THROWF",String.valueOf(forcekey.get(forceindex)));
        tforceFx = (float)forcemap.get(String.valueOf(forcekey.get(forceindex))).get(0);
        tforceFy = (float)forcemap.get(String.valueOf(forcekey.get(forceindex))).get(1);
        tforceF = (float)Math.sqrt(Math.pow(tforceFx, 2) + Math.pow(tforceFy, 2));
        return tforceF;
    }
    public float ThrowForceAngle() {
        float tforceAngle;
        tforceAngle = (float)forcemap.get(String.valueOf(forcekey.get(forceindex))).get(2);
        return tforceAngle;
    }

    public void setMoment(){
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriMoment.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriMoment.get(0).get(0);
                                tps[1] = (float) oriMoment.get(0).get(1);

                                DrawMoment(tps[0], tps[1]);
                                momentcount++;
                                if (momentcount == 1) {
                                    momentcount = 0;
                                    oriMoment.clear();
                                    return true;
                                }
                            } else {
                                momentcount = 0;
                                oriMoment.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }



    public void setPin() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriSupport.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriSupport.get(0).get(0);
                                tps[1] = (float) oriSupport.get(0).get(1);

                                if (checksupport.contains(nodeindex)) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                } else {
                                    DrawPin(tps[0], tps[1]);
                                    checksupport.add(nodeindex);
                                    supportcount++;
                                }

                                if (supportcount == 1) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                }
                            } else {
                                supportcount = 0;
                                oriSupport.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    public void setVerRoller() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriSupport.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriSupport.get(0).get(0);
                                tps[1] = (float) oriSupport.get(0).get(1);

                                if (checksupport.contains(nodeindex)) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                } else {
                                    DrawVerticalRoller(tps[0], tps[1]);
                                    checksupport.add(nodeindex);
                                    supportcount++;
                                }

                                if (supportcount == 1) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                }
                            } else {
                                supportcount = 0;
                                oriSupport.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    public void setHorRoller() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriSupport.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriSupport.get(0).get(0);
                                tps[1] = (float) oriSupport.get(0).get(1);

                                if (checksupport.contains(nodeindex)) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                } else {
                                    DrawHorzintalRoller(tps[0], tps[1]);
                                    checksupport.add(nodeindex);
                                    supportcount++;
                                }

                                if (supportcount == 1) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                }
                            } else {
                                supportcount = 0;
                                oriSupport.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    public void setFix() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();
                int x = (int) pts[0];
                int y = (int) pts[1];
                nodeindex = FindNodeRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (noderegionarray.get(nodeindex).contains(x, y)) {

                                oriSupport.add(oriNodearray.get(nodeindex));

                                float[] tps = new float[2];
                                tps[0] = (float) oriSupport.get(0).get(0);
                                tps[1] = (float) oriSupport.get(0).get(1);

                                if (checksupport.contains(nodeindex)) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                } else {
                                    DrawFixWall(tps[0], tps[1]);
                                    checksupport.add(nodeindex);
                                    supportcount++;
                                }

                                if (supportcount == 1) {
                                    supportcount = 0;
                                    oriSupport.clear();
                                    return true;
                                }
                            } else {
                                supportcount = 0;
                                oriSupport.clear();
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
    }

    /*boolean checkS = false;
    public boolean selectsupport() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                float[] pts = new float[2];
                pts[0] = motionEvent.getX();
                pts[1] = motionEvent.getY();

                int x = (int) pts[0];
                int y = (int) pts[1];

                supportindex = FindSupportRegionIndex(x, y);
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        try {
                            if (supportregionarray.get(supportindex).contains(x, y)) {
                                supportpen.setColor(SELECT_COLOR);
                                Support_p = supportpatharray.get(supportindex);
                                canvas.drawPath(Support_p, supportpen);
                                nodepen.setColor(DOT_COLOR);
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                selectsupportcount++;
                                if (selectsupportcount > 1) {
                                    SupportTurnBack();
                                    for (int i = 0; i < nodepatharray.size(); i++) {
                                        Node_p = nodepatharray.get(i);
                                        canvas.drawPath(Node_p, nodepen);
                                    }
                                    checkS = false;
                                } else {
                                    checkS = true;
                                }
                            } else {
                                SupportTurnBack();
                                for (int i = 0; i < nodepatharray.size(); i++) {
                                    Node_p = nodepatharray.get(i);
                                    canvas.drawPath(Node_p, nodepen);
                                }
                                checkS = false;
                            }
                        } catch (IndexOutOfBoundsException e) {
                            return true;
                        }

                }
                invalidate();
                return true;
            }
        });
        return checkS;
    }
    void SupportTurnBack() {
        selectsupportcount = 0;
        supportpen.setColor(SUPPORT_COLOR);
        for (int i = 0; i < supportpatharray.size(); i++) {
            Support_p = supportpatharray.get(i);
            canvas.drawPath(Support_p, supportpen);
        }
    }*/

    @Override
    public void onDraw(Canvas canvas) {
        int vernum = (height / vergrid) + 1;
        int hornum = (width / horgrid) + 1;

        canvas.drawColor(backgroundcolor);

        //canvas座標翻轉
        canvas.translate(fwidth / 2, fheight / 2);
        canvas.scale(1, -1);

        //繪製坐標軸線
        drawTranslateCoordinateSpace(canvas);

        if (Mapmatrix.isIdentity()) {
            canvas.getMatrix().invert(Mapmatrix);
        }

        //繪製網格線 [這裡可嘗試寫個drawGridline(int vergrid,int horgrid)函式庫]//
        for (int i = 0; i < vernum; i++) {
            canvas.drawLine(i * vergrid, fheight / 2, i * vergrid, (-fheight / 2), painter_BG2);
            canvas.drawLine((-i - 1) * vergrid, fheight / 2, (-i - 1) * vergrid, (-fheight / 2), painter_BG2);
        }
        for (int i = 0; i < hornum; i++) {
            canvas.drawLine(fwidth / 2, i * horgrid, (-fwidth / 2), i * horgrid, painter_BG2);
            canvas.drawLine(fwidth / 2, (-i - 1) * horgrid, (-fwidth / 2), (-i - 1) * horgrid, painter_BG2);
        }

        canvas.drawBitmap(bitmap, Mapmatrix, BitmapPaint);
    }

    //繪製x,y軸
    private void drawTranslateCoordinateSpace(Canvas canvas) {
        canvas.save();
        CanvasAidUtils.set2DAxisLength(fwidth / 2, fwidth / 2, fheight / 2, fheight / 2);
        CanvasAidUtils.setLineColor(Color.GRAY);
        CanvasAidUtils.draw2DCoordinateSpace(canvas);
        CanvasAidUtils.draw2DCoordinateSpace(canvas);
        canvas.restore();
    }

    //繪製端點的方法
    public void DrawNode(float downX, float downY) {
        //歸零
        nodepen.setColor(DOT_COLOR);
        for (int i = 0; i < nodepatharray.size(); i++) {
            Node_p = nodepatharray.get(i);
            canvas.drawPath(Node_p, nodepen);
        }

        float[] pts = {downX, downY};
        float[] transpts = new float[2];
        Path dotpath = new Path();
        Path textpath = new Path();
        Path transdot_p = new Path();
        Region dotregion = new Region();
        j++;

        //取得畫面座標反矩陣，讓使用者的觸摸處和畫布座標吻合
        canvas.getMatrix().invert(Mapmatrix);
        Mapmatrix.mapPoints(transpts, pts);

        //儲存碰觸的座標點
        oriNodeX = transpts[0];
        oriNodeY = transpts[1];
        DotX = transpts[0] - fwidth / 2;
        DotY = -transpts[1] + fheight / 2;

        //繪製文字表示點號
        if (j < 10) {
            textpath.addRect(pts[0] - 42, pts[1]+DOT_RADIUS, pts[0]-6, pts[1]+48, Path.Direction.CW);
            canvas.drawText(String.valueOf((int) j), pts[0] - 42, pts[1] + 48, nodenumpen);
        } else if (j < 100) {
            textpath.addRect(pts[0] - 54, pts[1]+DOT_RADIUS, pts[0]-6, pts[1]+48, Path.Direction.CW);
            canvas.drawText(String.valueOf((int) j), pts[0] - 54, pts[1] + 48, nodenumpen);
        } else if (j < 1000) {
            textpath.addRect(pts[0] - 72, pts[1]+DOT_RADIUS, pts[0]-6, pts[1]+48, Path.Direction.CW);
            canvas.drawText(String.valueOf((int) j), pts[0] - 72, pts[1] + 48, nodenumpen);
        }

        //繪製觸碰點和建立區域判斷點擊
        dotpath.addCircle(pts[0], pts[1], DOT_RADIUS, Path.Direction.CW);
        canvas.drawPath(dotpath, nodepen);
        nodepatharray.add(dotpath);
        nodetextarray.add(textpath);
        transdot_p.addCircle(pts[0],pts[1],2*DOT_RADIUS, Path.Direction.CW);
        dotregion.setPath(transdot_p, globalRegion);
        noderegionarray.add(dotregion);



        //儲存資料
        ArrayList<Float> oriDot = new ArrayList<>();
        ArrayList<Float> Dot1 = new ArrayList<>();
        ArrayList<Float> Dot2 = new ArrayList<>();
        ArrayList<Integer> free = new ArrayList<>();
        ArrayList<Float> empty = new ArrayList<>();
        ArrayList<Float> empty1 = new ArrayList<>();
        n++;
        Dot1.add(n);
        oriDot.add(oriNodeX);
        oriDot.add(oriNodeY);

        Dot1.add(DotX);
        Dot1.add(DotY);
        Dot2.add(DotX);
        Dot2.add(DotY);
        free.add(1);
        free.add(1);
        free.add(1);
        empty.add((float)0);
        empty.add((float)0);
        empty.add((float)0);
        empty1.add((float)0);
        Log.d("Dot1", Dot1.toString());

        oriNodearray.add(oriDot);
        nodearray1.add(Dot1);
        nodearray2.add(Dot2);
        forcemap.put(String.valueOf((int)n),empty);
        momentmap.put(String.valueOf((int)n),empty1);
        supportmap.put(String.valueOf((int)n),free);
        Log.d("pointarray2", nodearray1.toString());
    }

    public void DrawTruss(float startX,float startY,float endX,float endY) {
        Path ttruss = new Path();
        Path transtruss_p = new Path();
        Region trussregion = new Region();
        k++;

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
    }

    float fx = 10,fy = 0, forceangle = 0;
    public static Map<String,ArrayList> forcemap = new HashMap<>();
    public static ArrayList<Map<String, ArrayList>> forcelist = new ArrayList<>();
    public void DrawForce(float x, float y) {
        Path forcepath1 = new Path();
        Path forcepath2 = new Path();
        Path finalforcepath = new Path();
        Path transforce_p = new Path();
        Region forceregion = new Region();
        Matrix forcerotateM = new Matrix();
        forcerotateM.preScale(1,-1,x,y);
        forcerotateM.preRotate(forceangle,x,y);


        forcepath1.moveTo(x - DOT_RADIUS, y);
        forcepath1.lineTo(x - DOT_RADIUS - DOT_ERROR, y +DOT_ERROR);
        forcepath1.lineTo(x - DOT_RADIUS - DOT_ERROR, y -DOT_ERROR);
        forcepath1.close();
        forcepath2.addRect(x - 100 - DOT_RADIUS - DOT_ERROR, y + 8,
                x - DOT_RADIUS - DOT_ERROR, y - 8, Path.Direction.CW);

        float force = (float)Math.sqrt(Math.pow(fx, 2) + Math.pow(fy, 2));
        finalforcepath.addPath(forcepath1);
        finalforcepath.addPath(forcepath2);
        finalforcepath.transform(forcerotateM);
        canvas.drawPath(finalforcepath, forcepen);
        forcepatharray.add(finalforcepath);
        canvas.drawText(force+"N", x - 100 - DOT_RADIUS - DOT_ERROR, y - 16, forcepen);
        forceregion.setPath(finalforcepath, globalRegion);
        forceregionarray.add(forceregion);

        ArrayList<Float> attriforce = new ArrayList<>();
        attriforce.add(fx);
        attriforce.add(fy);
        attriforce.add(forceangle);
        forcekey.add(nodeindex);
        Log.w("key", forcekey.toString());
        forcemap.put(String.valueOf(nodeindex),attriforce);
        Log.w("map", forcemap.get(String.valueOf(nodeindex)).toString());
    }

    float moment =10;
    public static Map<String,ArrayList> momentmap = new HashMap<>();
    public static ArrayList<Map<String, ArrayList>> momentlist = new ArrayList<>();
    public void DrawMoment(float x, float y){
        Path momentpath1 = new Path();
        Path momentpath2 = new Path();
        Path finalpath = new Path();
        Region momentregion = new Region();

        momentpath1.moveTo(x-DOT_ERROR-10,y-DOT_RADIUS-30);
        momentpath1.lineTo(x,y-DOT_ERROR-10);
        momentpath1.lineTo(x,y-DOT_ERROR-50);
        momentpath1.close();

        RectF oval =new RectF(x-DOT_ERROR-40,y-DOT_ERROR-30,x+DOT_ERROR+40,y+DOT_ERROR+40);
        momentpath2.addArc(oval,-90,90);

        canvas.drawPath(momentpath1,forcepen);
        canvas.drawPath(momentpath2,momentpen);
        finalpath.addPath(momentpath1);
        finalpath.addPath(momentpath2);

        momentpatharray.add(finalpath);
        canvas.drawText(moment+"N*m", x - 100 - DOT_RADIUS - DOT_ERROR, y - 16, forcepen);
        momentregion.setPath(finalpath, globalRegion);
        momentregionarray.add(momentregion);

        ArrayList<Float> attrimoment = new ArrayList<>();
        attrimoment.add(moment);
        momentkey.add(nodeindex);
        Log.w("key", momentkey.toString());
        momentmap.put(String.valueOf(nodeindex),attrimoment);
        Log.w("map", momentmap.get(String.valueOf(nodeindex)).toString());

    }



    public Map<String,ArrayList> supportmap = new HashMap<>();
    public ArrayList<Map<String, ArrayList>> supportlist = new ArrayList<>();
    public void DrawPin(float x, float y) {
        Path pin = new Path();
        Path ground = new Path();
        Path finalpinpath = new Path();
        Region pinregion = new Region();

        pin.moveTo(x, y);
        pin.lineTo(x+DOT_ERROR,y+DOT_RADIUS+DOT_ERROR);
        pin.lineTo(x-DOT_ERROR,y+DOT_RADIUS+DOT_ERROR);
        pin.close();
        ground.moveTo(x+DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        ground.lineTo(x-DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        for (int i = 0; i <= 10 ; i++) {
            ground.moveTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR);
            ground.lineTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR+DOT_ERROR/2);
        }

        Pin_p.addPath(pin);
        Pin_p.addPath(ground);
        canvas.drawPath(Pin_p,supportpen);
        pinregion.setPath(Pin_p, globalRegion);
        supportregionarray.add(pinregion);

        ArrayList<Integer> attrisupport = new ArrayList<>();
        attrisupport.add(0);
        attrisupport.add(0);
        attrisupport.add(1);
        supportmap.put(String.valueOf(nodeindex),attrisupport);
        Log.w("support",supportmap.toString());

    }

    public void DrawHorzintalRoller(float x, float y) {
        Path verroller = new Path();
        Path verground = new Path();
        Path finalverrollerpath = new Path();
        Region rollerregion = new Region();

        verroller.addCircle(x,y+DOT_RADIUS+DOT_RADIUS/2,DOT_RADIUS/2, Path.Direction.CW);
        verground.moveTo(x+DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        verground.lineTo(x-DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        for (int i = 0; i <= 10 ; i++) {
            verground.moveTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR);
            verground.lineTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR+DOT_ERROR/2);
        }

        finalverrollerpath.addPath(verroller);
        finalverrollerpath.addPath(verground);
        canvas.drawPath(finalverrollerpath,supportpen);
        supportpatharray.add(finalverrollerpath);
        rollerregion.setPath(Roller_p, globalRegion);
        supportregionarray.add(rollerregion);

        ArrayList<Integer> attrisupport = new ArrayList<>();
        attrisupport.add(1);
        attrisupport.add(0);
        attrisupport.add(1);
        supportmap.put(String.valueOf(nodeindex),attrisupport);
        Log.w("support",supportmap.toString());
    }

    float verrollerangle = 270;
    public void DrawVerticalRoller(float x, float y) {
        Path horroller = new Path();
        Path horground = new Path();
        Path finalhorrollerpath = new Path();
        Region rollerregion = new Region();
        Matrix horrollerrotateM = new Matrix();
        horrollerrotateM.preRotate(verrollerangle,x,y);

        horroller.addCircle(x,y+DOT_RADIUS+DOT_RADIUS/2,DOT_RADIUS/2, Path.Direction.CW);
        horground.moveTo(x+DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        horground.lineTo(x-DOT_ERROR*2,y+DOT_RADIUS+DOT_ERROR);
        for (int i = 0; i <= 10 ; i++) {
            horground.moveTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR);
            horground.lineTo(x-DOT_ERROR*2+8*i,y+DOT_RADIUS+DOT_ERROR+DOT_ERROR/2);
        }

        finalhorrollerpath.addPath(horroller);
        finalhorrollerpath.addPath(horground);
        finalhorrollerpath.transform(horrollerrotateM);
        canvas.drawPath(finalhorrollerpath,supportpen);
        supportpatharray.add(finalhorrollerpath);
        rollerregion.setPath(Roller_p, globalRegion);
        supportregionarray.add(rollerregion);

        ArrayList<Integer> attrisupport = new ArrayList<>();
        attrisupport.add(0);
        attrisupport.add(1);
        attrisupport.add(1);
        supportmap.put(String.valueOf(nodeindex),attrisupport);
        Log.w("support",supportmap.toString());
    }

    public void DrawFixWall(float x, float y) {
        Path fixwall = new Path();
        Path fix = new Path();
        Region fixregion = new Region();

        fixwall.moveTo(x+DOT_ERROR*2,y);
        fixwall.lineTo(x-DOT_ERROR*2,y);
        for (int i = 0; i <= 10 ; i++) {
            fixwall.moveTo(x-DOT_ERROR*2+8*i,y);
            fixwall.lineTo(x-DOT_ERROR*2+8*i,y+DOT_ERROR/2);
        }

        fix.addPath(fixwall);
        canvas.drawPath(fix,supportpen);
        supportpatharray.add(fix);
        fixregion.setPath(fix, globalRegion);
        supportregionarray.add(fixregion);

        ArrayList<Integer> attrisuppport = new ArrayList<>();
        attrisuppport.add(0);
        attrisuppport.add(0);
        attrisuppport.add(0);
        supportmap.put(String.valueOf(nodeindex), attrisuppport);
    }

    //搜尋DotRegion目錄
    boolean nodesearch = false;
    int FindNodeRegionIndex(int inputX, int inputY) {
        int ans = 0;
        for (int i = 0; i < noderegionarray.size(); i++) {
            nodesearch = noderegionarray.get(i).contains(inputX, inputY);
            if (nodesearch == true) {
                ans = noderegionarray.indexOf(noderegionarray.get(i));
                break;
            }
        }
        nodesearch = false;
        return ans;
    }

    //搜尋TrussRegion目錄
    boolean trusssearch = false;
    int FindTrussRegionIndex(int inputX, int inputY) {
        int ans = 0;
        for (int i = 0; i < trussrgionarray.size(); i++) {
            trusssearch = trussrgionarray.get(i).contains(inputX, inputY);
            if (trusssearch == true) {
                ans = trussrgionarray.indexOf(trussrgionarray.get(i));
                break;
            }
        }
        trusssearch = false;
        return ans;
    }

    //搜尋ForceRegion目錄
    boolean forcesearch = false;
    int FindForceRegionIndex(int inputX, int inputY) {
        int ans = 0;
        for (int i = 0; i < forceregionarray.size(); i++) {
            forcesearch = forceregionarray.get(i).contains(inputX,inputY);
            if (forcesearch == true) {
                ans = forceregionarray.indexOf(forceregionarray.get(i));
                break;
            }
        }
        forcesearch = false;
        return ans;
    }

    //搜尋SupportRegion目錄
    /*boolean supportsearch = false;
    int FindSupportRegionIndex(int inputX, int inputY) {
        int ans = 0;
        for (int i = 0; i < supportregionarray.size(); i++) {
            supportsearch = supportregionarray.get(i).contains(inputX,inputY);
            if (supportsearch == true) {
                ans = supportregionarray.indexOf(supportregionarray.get(i));
                break;
            }
        }
        supportsearch = false;
        return ans;
    }*/

    public Bundle nodebundle1() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nodes1", nodearray1);
        Log.d("bundle.nodes1", nodearray1.toString());
        return bundle;
    }

    public Bundle elementbundle1() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("elements1", element);
        Log.d("bundle.elements1", element.toString());
        return bundle;
    }

    public Bundle forcebundle1() {
        forcelist.clear();
        forcelist.add(forcemap);
        Log.w("flist1", forcelist.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("forces1", forcelist);
        Log.d("bundle.forces1", forcelist.toString());
        return bundle;
    }


    public Bundle supportbundle1() {
        supportlist.clear();
        supportlist.add(supportmap);

        Log.w("slist1", supportlist.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("supports1", supportlist);
        Log.d("bundle.supports1", supportlist.toString());
        return bundle;
    }

    public Bundle nodebundle2() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("nodes2", nodearray1);
        Log.d("bundle.nodes2", nodearray1.toString());
        return bundle;
    }

    public Bundle elementbundle2() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("elements2", element);
        Log.d("bundle.elements2", element.toString());
        return bundle;
    }

    public Bundle forcebundle2() {
        forcelist.clear();
        forcelist.add(forcemap);
        Log.w("flist2", forcelist.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("forces2", forcelist);
        Log.d("bundle.forces2", forcelist.toString());
        return bundle;
    }

    public Bundle supportbundle2() {
        supportlist.clear();
        supportlist.add(supportmap);
        Log.w("slist2", supportlist.toString());
        Bundle bundle = new Bundle();
        bundle.putSerializable("supports2", supportlist);
        Log.d("bundle.supports2", supportlist.toString());
        return bundle;
    }

    public Bundle attritrussbundle() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("attritruss", attritrussarray);
        Log.d("bundle.attritruss", attritrussarray.toString());
        return bundle;
    }
    public Bundle momentbundle() {
        momentlist.clear();
        momentlist.add(momentmap);
        Bundle bundle = new Bundle();
        bundle.putSerializable("moment", momentlist);
        Log.d("bundle.moment", momentlist.toString());
        return bundle;
    }

    //清除矩陣
    public void removeArray() {
        nodearray1.clear();
        nodearray2.clear();
        nodepatharray.clear();
        noderegionarray.clear();

        trusspatharray.clear();
        trussrgionarray.clear();

        forcepatharray.clear();
        forceregionarray.clear();

        momentpatharray.clear();
        momentregionarray.clear();

        supportpatharray.clear();
        supportregionarray.clear();

        oriNodearray.clear();
        oriTruss.clear();
        oriForce.clear();
        oriMoment.clear();
        oriSupport.clear();

        checkelement.clear();
        checksupport.clear();
        element.clear();
        forcemap.clear();
        momentmap.clear();
        supportmap.clear();
        forcelist.clear();
        momentlist.clear();

        forcekey.clear();
        momentkey.clear();
    }

    public void clear() {
        n = -1;
        j = 0;
        k=0;
        trusscount = 0;
        forcecount = 0;
        removeArray();
        Node_p.reset();
        Truss_p.reset();
        Force_p.reset();
        Pin_p.reset();
        Roller_p.reset();
        Fix_p.reset();
        Support_p.reset();

        backgroundcolor = BG_COLOR;
        canvas.drawColor(0, PorterDuff.Mode.CLEAR);
    }
}