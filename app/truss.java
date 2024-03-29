package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class truss extends AppCompatActivity {
    TextView textView2,shoelu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truss);
        textView2 = findViewById(R.id.textView2);
        shoelu = findViewById(R.id.shoelu);
        int n = 4;
        int m = 6;
        float[][] K = new float[2*n][2*n];                               //K矩陣
        float[][] Coordinate = {{0,0,0},{1,2,0},{2,2,2},{3,0,2}};                 //點座標(Coordinate)
        int[][] Information = {{0,1},{0,2},{0,3},{3,2},{3,1},{1,2}};                               //桿件連接訊息(Information)
        float[][] Material = new float[m][3];                         //桿件材料屬性(Material)
        float[][] F = {{0},{0},{0},{2},{-4}};

        int sum = -1;
        for(int k = 0 ; k < m ; k++)
        {
            sum = sum + 1;
            int dot1 = Information[sum][0];        //起點
            int dot2 = Information[sum][1];        //終點
            float x1 = Coordinate[dot1][1];       //起點X座標
            float y1 = Coordinate[dot1][2];       //起點Y座標
            float x2 = Coordinate[dot2][1];       //終點X座標
            float y2 = Coordinate[dot2][2];       //終點Y座標
            float L = (float) sqrt(pow((x2-x1),2)+pow((y2-y1),2));     //桿件長度
            float cos = (x2-x1)/L;                            //cos值
            float sin = (y2-y1)/L;                            //sin值
            /*=====填矩陣=====*/
            K[(2*dot1)][(2*dot1)] = K[(2*dot1)][(2*dot1)] + ((cos*cos)/L);
            K[(2*dot1)][(2*dot1)+1] = K[(2*dot1)][(2*dot1)+1] + ((cos*sin)/L);
            K[(2*dot1)][(2*dot2)] = K[(2*dot1)][(2*dot2)] + ((-(cos*cos))/L);
            K[(2*dot1)][(2*dot2)+1] = K[(2*dot1)][(2*dot2)+1] + ((-(cos*sin))/L);

            K[(2*dot1)+1][(2*dot1)] = K[(2*dot1)+1][(2*dot1)] + ((cos*sin)/L);
            K[(2*dot1)+1][(2*dot1)+1] = K[(2*dot1)+1][(2*dot1)+1] + ((sin*sin)/L);
            K[(2*dot1)+1][(2*dot2)] = K[(2*dot1)+1][(2*dot2)] + ((-(cos*sin))/L);
            K[(2*dot1)+1][(2*dot2)+1] = K[(2*dot1)+1][(2*dot2)+1] + ((-(sin*sin))/L);

            K[(2*dot2)][(2*dot1)] = K[(2*dot2)][(2*dot1)] + ((-(cos*cos))/L);
            K[(2*dot2)][(2*dot1)+1] = K[(2*dot2)][(2*dot1)+1] + ((-(cos*sin))/L);
            K[(2*dot2)][(2*dot2)] = K[(2*dot2)][(2*dot2)] + ((cos*cos)/L);
            K[(2*dot2)][(2*dot2)+1] = K[(2*dot2)][(2*dot2)+1] + ((cos*sin)/L);

            K[(2*dot2)+1][(2*dot1)] = K[(2*dot2)+1][(2*dot1)] + ((-(cos*sin))/L);
            K[(2*dot2)+1][(2*dot1)+1] = K[(2*dot2)+1][(2*dot1)+1] + ((-(sin*sin))/L);
            K[(2*dot2)+1][(2*dot2)] = K[(2*dot2)+1][(2*dot2)] + ((cos*sin)/L);
            K[(2*dot2)+1][(2*dot2)+1] = K[(2*dot2)+1][(2*dot2)+1] + ((sin*sin)/L);
            /*=====填矩陣=====*/
        }



        int [][]PointFreedom={                           //接點自由度(不可動0，可動=1，預位移=-1)
                {1,1},
                {0,1},
                {0,0},
                {1,1}
        };

        int k2sum = 0;                                  //k2矩陣大小的計數器
        for(int i =0 ; i < (n) ; i++) {
            for(int j =0 ; j < 2 ; j++) {
                if(PointFreedom[i][j] == 1) {
                    k2sum = k2sum + 1;
                    }
                }
            }

        float [][]K2 = new float[k2sum][k2sum];        //k2=用來帶入LU矩陣的K矩陣
        int t=0;
        int PFn [] =new int[k2sum];
        for (int i = 0; i < PointFreedom.length; i++) { //紀錄接點可動的自由度
            for (int j = 0; j < 2; j++) {
                if (PointFreedom[i][j] == 1) {
                    PFn[t]=2*i+j;
                    t=t+1;
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
        float[][] d=LU(K2,F,k2sum,k2sum);
        float [][] D=new  float[2*n][1];
        int t1=0;
        int t2=0;
        for (int i=0;i<D.length;i++){
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
        float [][] Q =MMultiply(K,D,2*n,2*n,1);
        for (int i=0;i<Q.length;i++) {
                textView2.setText(textView2.getText().toString()+Q[i][0]+","+"\n");
        }



    }

    public float[][] LU(float [][]K1,float[][]F,int p,int q){
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
        for (int i=0;i<X.length;i++) {
            for(int j=0;j<1;j++) {
                shoelu.setText(shoelu.getText().toString()+"\n"+X[i][j]+",");
            }
        }

        return X;
    }

    private class caculate{
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
    public float[][] MMultiply(float [][] A,float [][]B,int m,int n,int p){ //矩陣相乘
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
