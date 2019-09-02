package com.example.Eric_app;

import android.util.Log;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class BeamCaculation1 {
    public static boolean Toastcheck1 = true;
    public static void Beam() {
        ArrayList<ArrayList> BeamData = new ArrayList<>();
        ArrayList<Integer> checkn = new ArrayList<>();
        for (int i = 0; i < BeamPaintView.element.size(); i++) {
            for (int j = 0; j < 2; j++) {
                if (!checkn.contains(BeamPaintView.element.get(i).get(j))) {
                    checkn.add((int) BeamPaintView.element.get(i).get(j));
                }
            }
        }
        Log.d("NWeWant",checkn.toString());
        Collections.sort(checkn);
        Log.d("NWeWantCh",checkn.toString());
        int n = checkn.size();
        int m = BeamPaintView.element.size();
        float[][] K = new float[2*n][2*n];                               //K矩陣
        float[][] Coordinate = new float[n][2];                                     //點座標(Coordinate)
        for (int i = 0; i < n; i++) {
            Coordinate[i][0] = (float)BeamPaintView.nodearray1.get(checkn.get(i)).get(1);
            Coordinate[i][1] = (float)BeamPaintView.nodearray1.get(checkn.get(i)).get(2);
            Log.d("coordx",String.valueOf(Coordinate[i][0]));
            Log.d("coordy",String.valueOf(Coordinate[i][1]));
        }
        int[][] Information = new int[m][2];                                                        //桿件連接訊息(Information)
        for (int i = 0; i < m; i++) {
            Information[i][0] = (int)BeamPaintView.element.get(i).get(0);
            Information[i][1] = (int)BeamPaintView.element.get(i).get(1);
            Log.d("infstart",String.valueOf(Information[i][0]));
            Log.d("infend",String.valueOf(Information[i][1]));
        }
        int [][] PointFreedom = new int[n][2];                            //接點自由度(不可動0，可動=1，預位移=-1)
        int count = 0;
        for (int i = 0; i < n; i++) {
            PointFreedom[i][0] = (int)BeamPaintView.supportmap.get(String.valueOf(checkn.get(i))).get(1);
            if (PointFreedom[i][0] == 1){
                count++;
            }
            PointFreedom[i][1] = (int)BeamPaintView.supportmap.get(String.valueOf(checkn.get(i))).get(2);
            if (PointFreedom[i][1] == 1){
                count++;
            }
            Log.d("freex",String.valueOf(PointFreedom[i][0]));
            Log.d("freey",String.valueOf(PointFreedom[i][1]));
        }
        Log.d("count",String.valueOf(count));
        float[][] FM = new float[count][1];
        int z=0;
        int z1=0;
        for (int i = 0; i < count; i++) {
            F:for(int j=z1; j < PointFreedom.length;j++){
                for(int k=z;k < 2;k++){
                    if(PointFreedom[j][k] == 1){
                        if(k == 0){
                            FM[i][0] = (float)BeamPaintView.forcemap.get(String.valueOf(j)).get(1);
                            z=z+1;
                        }
                        if(k == 1){
                            FM[i][0] = (float)BeamPaintView.momentmap.get(String.valueOf(j)).get(0);
                            z1=z1+1;
                            z=0;
                        }

                        break F;
                    }
                }
            }
        }
        ArrayList<ArrayList> LengthM =new ArrayList<>();
        int sum = -1;
        for (int k = 0; k < m; k++) {
            ArrayList<Float> Length = new ArrayList<>();

            sum = sum + 1;
            int dot1 = Information[sum][0];        //起點
            int dot2 = Information[sum][1];        //終點
            float x1 = Coordinate[dot1][0];       //起點X座標
            float y1 = Coordinate[dot1][1];       //起點Y座標
            //Log.d("StartX,StartY", x1 + "," + y1);
            float x2 = Coordinate[dot2][0];       //終點X座標
            float y2 = Coordinate[dot2][1];       //終點Y座標
            //Log.d("EndX,EndY", x2 + "," + y2);
            float L = (float) sqrt(pow((x2 - x1), 2) + pow((y2 - y1), 2));     //桿件長度
            float EI =(float)BeamPaintView.attritrussarray.get(k).get(0)*(float)BeamPaintView.attritrussarray.get(k).get(2);
            //Log.w("EI",EI+",");
            /*=====填矩陣=====*/
            K[(2 * dot1)][(2 * dot1)] = K[(2 * dot1)][(2 * dot1)] + EI*(12/(float)pow(L,3));
            K[(2 * dot1)][(2 * dot1) + 1] = K[(2 * dot1)][(2 * dot1) + 1] + EI*(6/(float)pow(L,2));
            K[(2 * dot1)][(2 * dot2)] = K[(2 * dot1)][(2 * dot2)] + EI*(-12/(float)pow(L,3));
            K[(2 * dot1)][(2 * dot2) + 1] = K[(2 * dot1)][(2 * dot2) + 1] + EI*(6/(float)pow(L,2));

            K[(2 * dot1) + 1][(2 * dot1)] = K[(2 * dot1) + 1][(2 * dot1)] + EI*(6/(float)pow(L,2));
            K[(2 * dot1) + 1][(2 * dot1) + 1] = K[(2 * dot1) + 1][(2 * dot1) + 1] + EI*(4/(float)pow(L,1));
            K[(2 * dot1) + 1][(2 * dot2)] = K[(2 * dot1) + 1][(2 * dot2)] + EI*(-6/(float)pow(L,2));
            K[(2 * dot1) + 1][(2 * dot2) + 1] = K[(2 * dot1) + 1][(2 * dot2) + 1] + EI*(2/(float)pow(L,1));

            K[(2 * dot2)][(2 * dot1)] = K[(2 * dot2)][(2 * dot1)] + EI*(-12/(float)pow(L,3));
            K[(2 * dot2)][(2 * dot1) + 1] = K[(2 * dot2)][(2 * dot1) + 1] + EI*(-6/(float)pow(L,2));
            K[(2 * dot2)][(2 * dot2)] = K[(2 * dot2)][(2 * dot2)] + EI*(12/(float)pow(L,3));
            K[(2 * dot2)][(2 * dot2) + 1] = K[(2 * dot2)][(2 * dot2) + 1] + EI*(-6/(float)pow(L,2));

            K[(2 * dot2) + 1][(2 * dot1)] = K[(2 * dot2) + 1][(2 * dot1)] + EI*(6/(float)pow(L,2));
            K[(2 * dot2) + 1][(2 * dot1) + 1] = K[(2 * dot2) + 1][(2 * dot1) + 1] + EI*(2/(float)pow(L,1));
            K[(2 * dot2) + 1][(2 * dot2)] = K[(2 * dot2) + 1][(2 * dot2)] + EI*(-6/(float)pow(L,2));
            K[(2 * dot2) + 1][(2 * dot2) + 1] = K[(2 * dot2) + 1][(2 * dot2) + 1] + EI*(4/(float)pow(L,1));
            /*=====填矩陣=====*/
            Length.add(L);
            LengthM.add(Length);
            Log.d("Length",String.valueOf(LengthM));
        }
        int k2sum = 0;                                  //k2矩陣大小的計數器
        for (int i = 0; i < (n); i++) {
            for (int j = 0; j < 2; j++) {
                if (PointFreedom[i][j] == 1) {
                    k2sum = k2sum + 1;
                }
            }
        }

        float[][] K2 = new float[k2sum][k2sum];        //k2=用來帶入LU矩陣的K矩陣
        int t = 0;
        int PFn[] = new int[k2sum];
        for (int i = 0; i < PointFreedom.length; i++) { //紀錄接點可動的自由度
            for (int j = 0; j < 2; j++) {
                if (PointFreedom[i][j] == 1) {
                    PFn[t] = 2 * i + j;
                    t = t + 1;
                }
            }
        }
        for (int a = 0; a < K2.length; a++) {  //填入所需矩陣
            for (int b = 0; b < K2.length; b++) {
                K2[a][b] = K[PFn[a]][PFn[b]];
            }
        }

            /*for(int i=0;i<K2.length;i++){
                for(int j=0;j<1;j=j+4){
                    Log.w("K2",K2[i][j]+","+K2[i][j+1]+","+K2[i][j+2]+","+K2[i][j+3]+",");
                }
            }
            */



        float[][] d = LU(K2, FM, k2sum, k2sum); //LU計算反矩陣
        float[][] D = new float[2 * n][1];
        int t1 = 0;
        int t2 = 0;
        for (int i = 0; i < D.length; i++) {
            for (int j = t2; j < PFn.length; j++) {
                if (i != PFn[j]) {
                    D[i][0] = 0;
                    break;
                }
                if (i == PFn[j]) {
                    D[i][0] = d[t1][0];
                    t2 = t2 + 1;
                    t1 = t1 + 1;
                    break;
                }
            }

        }
        for (int i = 0; i < D.length; i++) {
            Log.i("D", D[i][0] + ",");
        }

        float[][] Q = MMultiply(K, D, 2 * n, 2 * n, 1); //顯示各點受力

        for (int i = 0; i < PointFreedom.length; i++) {
            for (int j = 0; j < 2; j++) {
                if(PointFreedom[i][j]==0 && j==0){
                    Q[2*i+j][0]=Q[2*i+j][0]-(float)BeamPaintView.forcemap.get(String.valueOf(i)).get(1);
                }
                if(PointFreedom[i][j]==0 && j==1){
                    Q[2*i+j][0]=Q[2*i+j][0]-(float)BeamPaintView.momentmap.get(String.valueOf(i)).get(0);
                }
            }
        }
        for (int i = 0; i < Q.length; i++) {
            if (Float.isNaN(Q[i][0])) {
                Toastcheck1 = false;
                return;
            }
        }
        for (int i = 0; i < PointFreedom.length; i++) {
            ArrayList<Float> addOnNode = new ArrayList<>();
            for (int j = 0; j < 2; j++) {
                addOnNode.add(Q[2*i+j][0]);
            }
            BeamData.add(addOnNode);
        }
        for(int i=0;i<n;i++){
            for(int j=0;j<2;j++){
                if(j==0){
                    BeamPaintView.nodearray1.get(i).set(j+1,(float)BeamPaintView.nodearray1.get(i).get(j+1)-10*D[2*i+j][0]);
                    BeamPaintView.oriNodearray.get(i).set(j,(float)BeamPaintView.oriNodearray.get(i).get(j)-10*D[2*i+j][0]);
                }
            }
        }
        Log.d("BeamData",String.valueOf(BeamData));
            /*ArrayList<ArrayList> InternalForceM = new ArrayList<>();
            for (int elementvalue = 0; elementvalue < m; elementvalue++) {  //計算桿件內力
                ArrayList<Float> InternalF = new ArrayList<>();
                float[][] displacement = new float[4][1];
                int nodefreedom1 = (int) elementS.get(elementvalue).get(0);
                int nodefreedom2 = (int) elementS.get(elementvalue).get(1);
                displacement[0][0] = D[2 * nodefreedom1][0];
                displacement[1][0] = D[2 * nodefreedom1 + 1][0];
                displacement[2][0] = D[2 * nodefreedom2][0];
                displacement[3][0] = D[2 * nodefreedom2 + 1][0];
                float[][] elementTfM = new float[1][4];
                elementTfM[0][0] = -(float) LandaXYMatrix.get(elementvalue).get(0);
                elementTfM[0][1] = -(float) LandaXYMatrix.get(elementvalue).get(1);
                elementTfM[0][2] = (float) LandaXYMatrix.get(elementvalue).get(0);
                elementTfM[0][3] = (float) LandaXYMatrix.get(elementvalue).get(1);
            for(int i=0;i<4;i++) {
                Log.d("elementTfM" + elementvalue, elementTfM[0][i] + ",");
            }
            for(int i=0;i<4;i++) {
                Log.d("displacement" + elementvalue, displacement[i][0] + ",");
            }
                float[][] fM = MMultiply(elementTfM, displacement, 1, 4, 1);
                float f = fM[0][0] / (float) LengthM.get(elementvalue).get(0);
                Log.w("Interforce", String.valueOf(f));
                InternalF.add(f);
                InternalForceM.add(InternalF);
            }
            TextView title1 = new TextView(this); //顯示桿件內力
            title1.setTextSize(18);
            title1.setText("各個桿件之內力");
            calaroot1.addView(title1);
            for (int i = 0; i < InternalForceM.size(); i++) {
                TextView calaview = new TextView(this);
                calaview.setTextSize(18);
                if (Float.isNaN((float) InternalForceM.get(i).get(0))) {
                    title1.setText(" ");
                    calaroot1.addView(calaview);
                    break;
                }
                calaview.setText("Element" + i + 1 + " : " + calaview.getText().toString() + InternalForceM.get(i).get(0) + "\n");
                calaroot1.addView(calaview);
            }
            */

    }
    public static float[][] LU(float [][]K1,float[][]F,int p,int q){
        float[][] A = new float[p][q];
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A.length;j++){
                A[i][j]=K1[i][j];
            }
        }
        float[][] B = new float[p][1];
        for(int i=0;i<B.length;i++){
            B[i][0]=F[i][0];
        }
        float[][] L = new float[p][q];
        float[][] U = new float[p][q];
        for (int i = 0; i < p; i++)//將A矩陣分解成LU矩陣
        {
            for (int j = 0; j < q; j++) {
                float sum1 = 0; //暫存器
                float sum2 = 0;
                if (i == 0) { //算U[0][j]
                    U[i][j] = A[i][j];
                } else {
                    if (j == 0) { //算L[i][0]
                        L[i][0] = A[i][0] / U[0][0];
                    } else {
                        if (i > j) {
                            for (int s = 0; s <= j - 1; s++) {
                                sum1 = sum1 + L[i][s] * U[s][j];
                            }
                            L[i][j] = (A[i][j] - sum1) / U[j][j];
                        }
                    }
                    if (j >= 1) {
                        for (int s = 0; s <= i - 1; s++) {
                            sum2 = sum2 + L[i][s] * U[s][j];
                        }
                        U[i][j] = A[i][j] - sum2;
                    }
                }
                if (i == j) {
                    L[i][j] = 1;
                }
            }
        }
        caculate ca = new caculate(); //將y矩陣計算 並回傳至Y矩陣 以用來計算最終x矩陣
        float[][] Y = ca.ForwardSupstitution(L, B, p);
        float[][] X = ca.BackwardSupstitution(U, Y, p);

        return X;
    }

    private static class caculate{
        private float[][] ForwardSupstitution(float L[][],float B[][],int n){
            float [][]y=new float[n][1];
            float T1=0;
            for(int i=0;i<n;i++) {
                T1=0;
                for(int j=0;j<=i-1;j++) {
                    T1=T1+L[i][j]*y[j][0];
                }
                y[i][0]=(B[i][0]-T1)/L[i][i];
            }
            return y;
        }
        private float[][] BackwardSupstitution(float U[][],float Y[][],int n) {
            float [][]x=new float[n][1];
            float T2=0;
            for(int i=n-1;i>-1;i--) {
                T2=0;
                for(int j=i+1;j<n;j++) {
                    T2=T2+U[i][j]*x[j][0];
                }
                x[i][0]=(Y[i][0]-T2)/U[i][i];
            }
            return x;
        }
    }
    public float[][] Addt(float anglex,float angley){
        float [][] T = new  float[2][4];
        T[0][0]=anglex;
        T[0][1]=angley;
        T[1][2]=anglex;
        T[1][3]=angley;
        return T;
    }
    public float[][] MTransposed(float [][] A, int m,int n){ //轉至矩陣計算
        float[][] tsp= new float[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                tsp[i][j]=A[i][j];
            }
        }
        return  tsp;
    }
    public static float[][] MMultiply(float [][] A,float [][]B,int m,int n,int p){ //矩陣相乘
        float [][]C =new float[m][p];
        for(int i=0;i<m;i++){
            for(int j=0;j<p;j++){
                float T=0;
                for(int k=0;k<n;k++) {
                    T=T+A[i][k]*B[k][j];
                }
                C[i][j] = T;
            }
        }
        return C;
    }

}
