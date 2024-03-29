package com.example.Eric_app;

import android.util.Log;
import java.util.ArrayList;
import java.util.Collections;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class TrussCaculation1 {
    public static boolean Toastcheck =true;
    public static void truss(){
        ArrayList<ArrayList> InerForce = new ArrayList<>();
        ArrayList<ArrayList> ForceOnNode = new ArrayList<>();
        ArrayList<Integer> checkn = new ArrayList<>();
        for(int i=0;i<TrussPaintView.element.size();i++){
            for(int j=0;j<2;j++){
                if (!checkn.contains(TrussPaintView.element.get(i).get(j))) {
                    checkn.add((int) TrussPaintView.element.get(i).get(j));
                }
            }
        }
        Log.d("NWeWant",checkn.toString());
        Collections.sort(checkn);
        Log.d("NWeWantCh",checkn.toString());
        int n = checkn.size();
        int m = TrussPaintView.element.size();
        float[][] K = new float[2*n][2*n];                               //K矩陣
        float[][] Coordinate = new float[n][2];                                     //點座標(Coordinate)
        for (int i = 0; i < n; i++) {
            Coordinate[i][0] = (float)TrussPaintView.nodearray1.get(checkn.get(i)).get(1);
            Coordinate[i][1] = (float)TrussPaintView.nodearray1.get(checkn.get(i)).get(2);
            Log.d("coordx",String.valueOf(Coordinate[i][0]));
            Log.d("coordy",String.valueOf(Coordinate[i][1]));
        }
        int[][] Information = new int[m][2];                                                        //桿件連接訊息(Information)
        for (int i = 0; i < m; i++) {
            Information[i][0] = (int)TrussPaintView.element.get(i).get(0);
            Information[i][1] = (int)TrussPaintView.element.get(i).get(1);
            Log.d("infstart",String.valueOf(Information[i][0]));
            Log.d("infend",String.valueOf(Information[i][1]));
        }
        int [][] PointFreedom = new int[n][2];                            //接點自由度(不可動0，可動=1，預位移=-1)
        int count = 0;
        for (int i = 0; i < n; i++) {
            PointFreedom[i][0] = (int)TrussPaintView.supportmap.get(String.valueOf(checkn.get(i))).get(0);
            if (PointFreedom[i][0] == 1){
                count++;
            }
            PointFreedom[i][1] = (int)TrussPaintView.supportmap.get(String.valueOf(checkn.get(i))).get(1);
            if (PointFreedom[i][1] == 1){
                count++;
            }
            Log.d("freex",String.valueOf(PointFreedom[i][0]));
            Log.d("freey",String.valueOf(PointFreedom[i][1]));
        }
        Log.d("count",String.valueOf(count));
        float[][] F = new float[count][1];
        int z=0;
        int z1=0;
        for (int i = 0; i < count; i++) {
            F:for (int j =z;j<PointFreedom.length;j++){
                for(int k=z1;k<2;k++){
                    if(PointFreedom[j][k] == 1){
                        F[i][0] = (float)TrussPaintView.forcemap.get(String.valueOf(j)).get(k);
                        Log.d("f",String.valueOf(F[i][0]));
                        if(z1==1){
                            z1=0;
                        }else {
                            z1=z1+1;
                        }
                        if(z1==2){
                            z=z+1;
                        }
                        break F;
                    }
                }
            }

        }
        float [][] Dp = {{0},{0},{0},{0},{0}};
        ArrayList<ArrayList> LandaXYMatrix =new ArrayList<>();
        ArrayList<ArrayList> LengthM =new ArrayList<>();
        try {
            int sum = -1;
            for (int k = 0; k < m; k++) {
                ArrayList<Float> LabdaXY = new ArrayList<>();
                ArrayList<Float> Length = new ArrayList<>();

                sum = sum + 1;
                int dot1 = Information[sum][0];        //起點
                int dot2 = Information[sum][1];        //終點
                float x1 = Coordinate[dot1][0];       //起點X座標
                float y1 = Coordinate[dot1][1];       //起點Y座標
                Log.d("StartX,StartY", x1 + "," + y1);
                float x2 = Coordinate[dot2][0];       //終點X座標
                float y2 = Coordinate[dot2][1];       //終點Y座標
                Log.d("EndX,EndY", x2 + "," + y2);
                float L = (float) sqrt(pow((x2 - x1), 2) + pow((y2 - y1), 2));     //桿件長度
                float cos = (x2 - x1) / L;                            //cos值
                float sin = (y2 - y1) / L;                            //sin值
                float EA =(float)TrussPaintView.attritrussarray.get(k).get(0)*(float)TrussPaintView.attritrussarray.get(k).get(1);
                /*=====填矩陣=====*/
                K[(2 * dot1)][(2 * dot1)] = K[(2 * dot1)][(2 * dot1)] + EA*((cos * cos) / L);
                K[(2 * dot1)][(2 * dot1) + 1] = K[(2 * dot1)][(2 * dot1) + 1] + EA*((cos * sin) / L);
                K[(2 * dot1)][(2 * dot2)] = K[(2 * dot1)][(2 * dot2)] + EA*((-(cos * cos)) / L);
                K[(2 * dot1)][(2 * dot2) + 1] = K[(2 * dot1)][(2 * dot2) + 1] + EA*((-(cos * sin)) / L);

                K[(2 * dot1) + 1][(2 * dot1)] =  K[(2 * dot1) + 1][(2 * dot1)] + EA*((cos * sin) / L);
                K[(2 * dot1) + 1][(2 * dot1) + 1] =  K[(2 * dot1) + 1][(2 * dot1) + 1] + EA*((sin * sin) / L);
                K[(2 * dot1) + 1][(2 * dot2)] =  K[(2 * dot1) + 1][(2 * dot2)] + EA*((-(cos * sin)) / L);
                K[(2 * dot1) + 1][(2 * dot2) + 1] =  K[(2 * dot1) + 1][(2 * dot2) + 1] + EA*((-(sin * sin)) / L);

                K[(2 * dot2)][(2 * dot1)] =  K[(2 * dot2)][(2 * dot1)] + EA*((-(cos * cos)) / L);
                K[(2 * dot2)][(2 * dot1) + 1] =  K[(2 * dot2)][(2 * dot1) + 1] + EA*((-(cos * sin)) / L);
                K[(2 * dot2)][(2 * dot2)] =  K[(2 * dot2)][(2 * dot2)] + EA*((cos * cos) / L);
                K[(2 * dot2)][(2 * dot2) + 1] =  K[(2 * dot2)][(2 * dot2) + 1] + EA*((cos * sin) / L);

                K[(2 * dot2) + 1][(2 * dot1)] =  K[(2 * dot2) + 1][(2 * dot1)] + EA*((-(cos * sin)) / L);
                K[(2 * dot2) + 1][(2 * dot1) + 1] =  K[(2 * dot2) + 1][(2 * dot1) + 1] + EA*((-(sin * sin)) / L);
                K[(2 * dot2) + 1][(2 * dot2)] =  K[(2 * dot2) + 1][(2 * dot2)] + EA*((cos * sin) / L);
                K[(2 * dot2) + 1][(2 * dot2) + 1] =  K[(2 * dot2) + 1][(2 * dot2) + 1] + EA*((sin * sin) / L);
                /*=====填矩陣=====*/
                LabdaXY.add(cos);
                LabdaXY.add(sin);
                LandaXYMatrix.add(LabdaXY);
                Length.add(L);
                LengthM.add(Length);


            }
            //Log.w("LandaX",LandaXYMatrix.toString());
            /*for(int i=0;i<K.length;i++){
                for(int j=0;j<K.length;j=j+6){
                    Log.w("K",K[i][j]+","+K[i][j+1]+","+K[i][j+1]+","+K[i][j+2]+","+K[i][j+3]+","+K[i][j+4]+","+K[i][j+5]);
                }
            }
            */

            int k2sum = 0;                                  //k2矩陣大小的計數器
            for(int i =0 ; i < (n) ; i++) {
                for(int j =0 ; j < 2 ; j++) {
                    if(PointFreedom[i][j] == 1) {
                        k2sum = k2sum + 1;
                    }
                }
            }
            int Dpcount=0; //預位移數量
            for(int i=0;i<Dp.length;i++){ //計算有多少個預位移
                if(Dp[i][0]!=0){
                    Dpcount=Dpcount+1;
                }
            }
            //Log.d("Dp", String.valueOf(Dpcount));
            float [][]K2 = new float[k2sum][k2sum];        //k2=用來帶入LU矩陣的K矩陣
            int t=0; //PFn的計數器
            int q=0; //PFn1的計數器
            int w=0;
            int PFn [] = new int[k2sum];    //可移動之點號矩陣
            int PFn1 [] = new int[2*n-k2sum];   //不可移動之點號矩陣
            int PFn2 [] = new int[Dpcount]; //有預位移的點之矩陣
            for (int i = 0; i < PointFreedom.length; i++) { //紀錄接點可動與不可動的自由度
                for (int j = 0; j < 2; j++) {
                    if (PointFreedom[i][j] == 1) {
                        PFn[t]=2*i+j;
                        t=t+1;
                    }
                    if (PointFreedom[i][j] == 0 || PointFreedom[i][j] == -1){
                        PFn1[q]=2*i+j;
                        q=q+1;
                    }
                    if (PointFreedom[i][j] == -1){
                        PFn2[w]=2*i+j;
                        w=w+1;
                    }
                }
            }
            for(int a=0;a<K2.length;a++) {  //填入所需矩陣
                for (int b = 0; b < K2.length; b++) {
                    K2[a][b]=K[PFn[a]][PFn[b]];
                }
            }

       /* for (int i=0;i<K2.length;i++) {
            for (int j = 0; j < K2.length; j += 4) {
                textView2.setText(textView2.getText().toString()+K2[i][j]+","+K2[i][j+1]+","+K2[i][j+2]+","+K2[i][j+3]+","+"\n");
            }
        }
        */
            float [][] LessDp = new float[k2sum][2*n-k2sum]; //暫存矩陣(存有m乘n，m為可移動之點號,n為不可移動之點號)
            float [][] StoreLDp; //用來存預位移產生之力效應，欲使用來相減的矩陣
            f:for(int a =0 ; a < PointFreedom.length ; a++) { //將預位移產生的效應消去
                for (int b = 0; b < 2; b++) {
                    if (PointFreedom[a][b] == -1){
                        for(int a1=0;a1<LessDp.length;a1++) {  //填入所需矩陣
                            for (int b1 = 0; b1 < 2*n-k2sum; b1++) {
                                LessDp[a1][b1] = K[PFn[a1]][PFn1[b1]];
                            }
                        }
                        StoreLDp=TrussCaculation1.MMultiply(LessDp,Dp,k2sum,2*n-k2sum,1);
                        for(int i=0;i<F.length;i++){
                            F[i][0] = (F[i][0])-StoreLDp[i][0];
                        }
                        break f;
                    }
                }
            }
            for (int i=0;i<LessDp.length;i++) {
                for (int j=0;j<2*n-k2sum;j++)
                    Log.d("LessDp",String.valueOf(LessDp[i][j]));
            }
            for(int i=0;i<F.length;i++){
                Log.d("F",String.valueOf(F[i][0]));
            }
            for(int i=0;i<PFn2.length;i++){
                Log.d("PFn2",String.valueOf(PFn2[i]));
            }


            //Log.d("k2sum",String.valueOf(k2sum));

            float[][] d=LU(K2,F,k2sum,k2sum); //紀錄可動自由度之位移
            float [][] D=new  float[2*n][1]; //全部點的位移，用來算出最終之結果
            int t1=0;   //解出可動自由度位移後(d矩陣)，d的計數器
            int t2=0;   //PFn的計數器，目的是要讓第二個迴圈可接續前面的順序
            for (int i=0;i<D.length;i++){ //填入算出之d(位移)
                for(int j=t2;j<PFn.length;j++){
                    if(i!=PFn[j]){
                        D[i][0]=0;
                        break;
                    }
                    if(i==PFn[j]){
                        D[i][0]=d[t1][0];
                        t2=t2+1;
                        t1=t1+1;
                        break;
                    }
                }
            }
            int t3=0;   //PFn2的計數器，目的用以接續迴圈
            for(int i=0;i<D.length;i++){ //將預位移填回
                for (int j=t3;j<PFn2.length;j++){
                    if (i == PFn2[j]){
                        for(int k=t3;k<Dp.length;k++){
                            if (Dp[k][0]!=0){
                                D[i][0]=Dp[k][0];
                                t3=t3+1;
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            for(int i=0;i<D.length;i++){
                Log.d("D",String.valueOf(D[i][0]));
            }
            float [][] Q =MMultiply(K,D,2*n,2*n,1);

            for (int i = 0; i < PointFreedom.length; i++) {
                for (int j = 0; j < 2; j++) {
                    if(PointFreedom[i][j]==0){
                        Q[2*i+j][0]=Q[2*i+j][0]-(float)TrussPaintView.forcemap.get(String.valueOf(i)).get(j);
                        Log.d("Q",Q[2*i+j][0]+" ");
                    }
                }
            }
            for (int i = 0; i < PointFreedom.length; i++) {
                ArrayList<Float> addforceOnNode = new ArrayList<>();
                for (int j = 0; j < 2; j++) {
                    if(PointFreedom[i][j]==0){
                       addforceOnNode.add((float)0);
                    }else {
                        addforceOnNode.add(Q[2*i+j][0]);
                    }
                }
                ForceOnNode.add(addforceOnNode);
            }
            Log.d("ForceOnNode",String.valueOf(ForceOnNode));
            for (int i = 0; i < Q.length; i++) {
                if (Float.isNaN(Q[i][0])) {
                    Toastcheck =false;
                    return;
                }
            }
            for (int elementvalue = 0; elementvalue < m; elementvalue++) {  //計算桿件內力
                ArrayList<Float> InternalF = new ArrayList<>();
                float[][] displacement = new float[4][1];
                int nodefreedom1 = (int) TrussPaintView.element.get(elementvalue).get(0);
                int nodefreedom2 = (int) TrussPaintView.element.get(elementvalue).get(1);
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
                float EA = (float)TrussPaintView.attritrussarray.get(elementvalue).get(0)*(float)TrussPaintView.attritrussarray.get(elementvalue).get(1);
                float f = EA*(fM[0][0] / (float) LengthM.get(elementvalue).get(0));
                Log.w("Interforce", String.valueOf(f));
                InternalF.add(f);
                InerForce.add(InternalF);
            }
            Log.d("InerForce",String.valueOf(InerForce));
            for(int i=0;i<n;i++){
                for(int j=0;j<2;j++){
                    if(j==0){
                        TrussPaintView.nodearray1.get(i).set(j+1,(float)TrussPaintView.nodearray1.get(i).get(j+1)+10*D[2*i+j][0]);
                        TrussPaintView.oriNodearray.get(i).set(j,(float)TrussPaintView.oriNodearray.get(i).get(j)+10*D[2*i+j][0]);
                    }else {
                        TrussPaintView.nodearray1.get(i).set(j+1,(float)TrussPaintView.nodearray1.get(i).get(j+1)-10*D[2*i+j][0]);
                        TrussPaintView.oriNodearray.get(i).set(j,(float)TrussPaintView.oriNodearray.get(i).get(j)-10*D[2*i+j][0]);
                    }

                }
            }
            Log.w("NodeChange",TrussPaintView.nodearray1.toString());
        }   catch (ArrayIndexOutOfBoundsException e){
            Toastcheck = false;
        }
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
        for(int i=0;i<F.length;i++) {
            Log.d("F",F[i][0]+",");
        }
        for(int i=0;i<K1.length;i++) {
            for(int j=0;j<K1.length;j++)
                Log.d("K1",K1[i][j]+",");
        }
        for(int i=0;i<X.length;i++) {
            Log.d("X",X[i][0]+",");
        }

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
