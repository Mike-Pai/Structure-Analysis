package com.example.Eric_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class ArrayOutput extends AppCompatActivity {
    LinearLayout root1,root2,root3,root4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_array_output);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Bundle bundle1 = intent.getBundleExtra("nodes1");
        Bundle bundle2 = intent.getBundleExtra("elements1");
        Bundle bundle3 = intent.getBundleExtra("forces1");
        Bundle bundle4 = intent.getBundleExtra("supports1");

        ArrayList<ArrayList> nodeS = (ArrayList<ArrayList>) bundle1.getSerializable("nodes1");
        Log.w("TAG", nodeS.toString());

        ArrayList<ArrayList> elementS = (ArrayList<ArrayList>) bundle2.getSerializable("elements1");
        Log.w("TAG",elementS.toString());

        ArrayList<Map<String,ArrayList>> forceS = (ArrayList<Map<String, ArrayList>>) bundle3.getSerializable("forces1");
        Log.w("TAG",forceS.toString());

        ArrayList<Map<String,ArrayList>> supportS = (ArrayList<Map<String, ArrayList>>) bundle4.getSerializable("supports1");
        Log.w("TAG",supportS.toString());

        Map<String, ArrayList> forcemapS = new HashMap<>();
        for (int i = 0; i < forceS.size(); i++) {
            forcemapS = forceS.get(i);
        }
        //Log.w("Tag", forcemapS.toString());

        Map<String, ArrayList> supportmapS = new HashMap<>();
        for (int i = 0; i < supportS.size(); i++) {
            supportmapS = supportS.get(i);
        }
        //Log.w("Tag", supportmapS.toString());

        Set<String> forcekeySet = forcemapS.keySet();
        String[] forcekeyArray = forcekeySet.toArray(new String[forcekeySet.size()]);
        Arrays.sort(forcekeyArray);
        StringBuilder sb1 = new StringBuilder();
        for (int i = 0; i < forcekeyArray.length; i++) {
            sb1.append(forcekeyArray[i]).append(", ").append(forcemapS.get(forcekeyArray[i])).append("\n");
        }

        Set<String> supportkeySet = supportmapS.keySet();
        String[] supportkeyArray = supportkeySet.toArray(new String[supportkeySet.size()]);
        Arrays.sort(supportkeyArray);
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < supportkeyArray.length; i++) {
            sb2.append(supportkeyArray[i]).append(", ").append(supportmapS.get(supportkeyArray[i])).append("\n");
        }

        root1 = findViewById(R.id.root1);
        root2 = findViewById(R.id.root2);
        root3 = findViewById(R.id.root3);
        root4 = findViewById(R.id.root4);

        TextView describe1 = new TextView(this);
        describe1.setTextSize(18);
        describe1.setText("節點資料");
        root1.addView(describe1);
        for (int i = 0; i < nodeS.size(); i++) {
            int j = i + 1;
            TextView pointview = new TextView(this);
            pointview.setTextSize(18);
            pointview.setText(j + ". " + nodeS.get(i).toString());
            root1.addView(pointview);
        }

        TextView describe2 = new TextView(this);
        describe2.setTextSize(18);
        describe2.setText("桿件資料");
        root2.addView(describe2);
        for (int i = 0; i < elementS.size(); i++) {
            int j = i + 1;
            TextView trussview = new TextView(this);
            trussview.setTextSize(18);
            trussview.setText(j + ". " + elementS.get(i).toString());
            root2.addView(trussview);
        }

        TextView describe3 = new TextView(this);
        describe3.setTextSize(18);
        describe3.setText("外力資料");
        root3.addView(describe3);
        String fmap = sb1.toString();
        TextView forceview = new TextView(this);
        forceview.setTextSize(18);
        forceview.setText(fmap);
        root3.addView(forceview);

        TextView describe4 = new TextView(this);
        describe4.setTextSize(18);
        describe4.setText("支點資料");
        root4.addView(describe4);
        String smap = sb2.toString();
        TextView supportview = new TextView(this);
        supportview.setTextSize(18);
        supportview.setText(smap);
        root4.addView(supportview);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
