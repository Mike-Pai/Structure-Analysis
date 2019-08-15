package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private TextView checkdata;
    private EditText Data;
    private double A[][]=new double[3][3];
    private ArrayList<String> matrix=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BuildView();



    }
    private void BuildView(){
        Data = findViewById(R.id.Data);
        checkdata = findViewById(R.id.checkdata);
    }
    public void caculate (View v) {
        A[1][1]=Double.parseDouble(Data.getText().toString());
        Log.d("Tag",matrix.toString());
        Intent intent = new Intent();
        intent.setClass(MainActivity.this,truss.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("Data", matrix);

        intent.putExtras(bundle);

        startActivity(intent);

    }
    public void check(View v){
        checkdata.setText(Data.getText().toString());
    }

}
