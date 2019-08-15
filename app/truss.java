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
        double [][] K = new double[2*n][2*n];                               //K矩陣
        double [][] Coordinate = {{0,0,0},{1,2,0},{2,2,2},{3,0,2}};                 //點座標(Coordinate)
        int [][] Information = {{0,1},{0,2},{0,3},{3,2},{3,1},{1,2}};                               //桿件連接訊息(Information)
        double [][] Material = {{8*pow(10,3)}};                            //桿件材料屬性(Material)
        double [][] F = {{0},{0},{0},{2},{-4}};                     //外力
        double [][] Dp = {{0},{0},{0},{0},{0}};                     //預位移

        int sum = -1;
        for(int k = 0 ; k < m ; k++)
        {
            sum = sum + 1;
            int dot1 = Information[sum][0];        //起點
            int dot2 = Information[sum][1];        //終點
            double x1 = Coordinate[dot1][1];       //起點X座標
            double y1 = Coordinate[dot1][2];       //起點Y座標
            double x2 = Coordinate[dot2][1];       //終點X座標
            double y2 = Coordinate[dot2][2];       //終點Y座標
            double L = sqrt(pow((x2-x1),2)+pow((y2-y1),2));     //桿件長度
            double cos = (x2-x1)/L;                            //cos值
            double sin = (y2-y1)/L;                            //sin值
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



        int [][]PointFreedom={{1,1},    //接點自由度(不可動0，可動=1，預位移=-1)
                              {0,1},
                              {0,0},
                              {1,1}};

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
        Log.d("Dp", String.valueOf(Dpcount));
        double [][]K2 = new double[k2sum][k2sum];        //k2=用來帶入LU矩陣的K矩陣
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
       double [][] LessDp = new double[k2sum][2*n-k2sum]; //暫存矩陣(存有m乘n，m為可移動之點號,n為不可移動之點號)
       double [][] StoreLDp; //用來存預位移產生之力效應，欲使用來相減的矩陣
        f:for(int a =0 ; a < PointFreedom.length ; a++) { //將預位移產生的效應消去
            for (int b = 0; b < 2; b++) {
                if (PointFreedom[a][b] == -1){
                    for(int a1=0;a1<LessDp.length;a1++) {  //填入所需矩陣
                        for (int b1 = 0; b1 < 2*n-k2sum; b1++) {
                            LessDp[a1][b1] = K[PFn[a1]][PFn1[b1]];
                        }
                    }
                    StoreLDp=MMultiply(LessDp,Dp,k2sum,2*n-k2sum,1);
                    for(int i=0;i<F.length;i++){
                        F[i][0] = (F[i][0]/Material[0][0])-StoreLDp[i][0];
                    }
                    break f;
                }
            }
        }
        /*for (int i=0;i<LessDp.length;i++) {
            for (int j=0;j<2*n-k2sum;j++)
            Log.d("LessDp",String.valueOf(LessDp[i][j]));
        }
        for(int i=0;i<F.length;i++){
            Log.d("F",String.valueOf(F[i][0]));
        }
        for(int i=0;i<PFn2.length;i++){
            Log.d("PFn2",String.valueOf(PFn2[i]));
        }
        */

        //Log.d("k2sum",String.valueOf(k2sum));

        double[][] d=LU(K2,F,k2sum,k2sum); //紀錄可動自由度之位移
        double [][] D=new  double[2*n][1]; //全部點的位移，用來算出最終之結果
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
        /*for(int i=0;i<D.length;i++){
            Log.d("D",String.valueOf(D[i][0]));
        }
        */
        double [][] Q =MMultiply(K,D,2*n,2*n,1);
        for(int i=0;i<Dp.length;i++){
            if(Dp[i][0]!=0){
                for(int j=0;j<Q.length;j++){ //乘上材料系數
                    Q[i][0]=Q[i][0]*Material[0][0];
                }
                break;
            }
        }

        for (int i=0;i<Q.length;i++) {
                textView2.setText(textView2.getText().toString()+Q[i][0]+","+"\n");
        }



    }

    public double[][] LU(double [][]K1,double[][]F,int p,int q){
        double[][] A = new double[p][q];
        for(int i=0;i<A.length;i++){
            for(int j=0;j<A.length;j++){
                A[i][j]=K1[i][j];
            }
        }
        double[][] B = new double[p][1];
        for(int i=0;i<B.length;i++){
                B[i][0]=F[i][0];
        }
        double[][] L = new double[p][q];
        double[][] U = new double[p][q];
        for (int i = 0; i < p; i++)//將A矩陣分解成LU矩陣
        {
            for (int j = 0; j < q; j++) {
                double sum1 = 0; //暫存器
                double sum2 = 0;
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
        double[][] Y = ca.ForwardSupstitution(L, B, p);
        double[][] X = ca.BackwardSupstitution(U, Y, p);
        for (int i=0;i<X.length;i++) {
            for(int j=0;j<1;j++) {
                shoelu.setText(shoelu.getText().toString()+"\n"+X[i][j]+",");
            }
        }

        return X;
    }

    private class caculate{
        private double[][] ForwardSupstitution(double L[][],double B[][],int n){
            double [][]y=new double[n][1];
            double T1=0;
            for(int i=0;i<n;i++) {
                T1=0;
                for(int j=0;j<=i-1;j++) {
                    T1=T1+L[i][j]*y[j][0];
                }
                y[i][0]=(B[i][0]-T1)/L[i][i];
            }
            return y;
        }
        private double[][] BackwardSupstitution(double U[][],double Y[][],int n) {
            double [][]x=new double[n][1];
            double T2=0;
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
    public double[][] Addt(double anglex,double angley){
        double [][] T = new  double[2][4];
        T[0][0]=anglex;
        T[0][1]=angley;
        T[1][2]=anglex;
        T[1][3]=angley;
        return T;
    }
    public double[][] MTransposed(double [][] A, int m,int n){ //轉至矩陣計算
        double[][] tsp= new double[n][m];
        for(int i=0;i<n;i++){
            for(int j=0;j<m;j++){
                tsp[i][j]=A[i][j];
            }
        }
        return  tsp;
    }
    public double[][] MMultiply(double [][] A,double [][]B,int m,int n,int p){ //矩陣相乘
        double [][]C =new double[m][p];
        for(int i=0;i<m;i++){
            for(int j=0;j<p;j++){
                double T=0;
                for(int k=0;k<n;k++) {
                    T=T+A[i][k]*B[k][j];
                }
                C[i][j] = T;
            }
        }
        return C;
    }
}
