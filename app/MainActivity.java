package com.example.Eric_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TrussPaintView trussPaintView;
    public float E = 200, Area = 10, X, Y, F = 10, Angle = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //建立paintview，所有繪圖行為都在上面執行
        trussPaintView = findViewById(R.id.PaintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        trussPaintView.init(metrics);
    }

    //建立選單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //建立選單選擇事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_dot:
                trussPaintView.setDot();
                trussPaintView.DotTurnBack();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_write:
                trussPaintView.selectnode();
                if (trussPaintView.selectnode() == true) {
                    openNodeDialog();
                    if (NodeAttributes.checkingnode == true) {
                        //trussPaintView.ResetNode();
                    }
                }
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_pin:
                trussPaintView.setPin();
                break;
            case R.id.VerticalRoller:
                trussPaintView.setVerRoller();
                break;
            case R.id.HorizentalRoller:
                trussPaintView.setHorRoller();
                break;
            case R.id.action_fix:
                trussPaintView.setFix();
                break;
            case R.id.action_truss:
                trussPaintView.setTruss();
                trussPaintView.DotTurnBack();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_beam:
                trussPaintView.selecttruss();
                if (trussPaintView.selecttruss() == true) {
                    openTrussDialog();
                }
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_F:
                trussPaintView.setForce();
                trussPaintView.DotTurnBack();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                Toast.makeText(MainActivity.this, item.getTitle().toString(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_M:
                trussPaintView.setMoment();
                trussPaintView.DotTurnBack();
                trussPaintView.TrussTurnBack();
                break;
            case R.id.action_test:
                trussPaintView.selectforce();
                if (trussPaintView.selectforce() == true) {
                    openForceDialog();
                }
                break;
            case R.id.Array:
                Intent intent1 = new Intent(this, ArrayOutput.class);
                intent1.putExtra("nodes1", trussPaintView.nodebundle1());
                intent1.putExtra("elements1", trussPaintView.elementbundle1());
                intent1.putExtra("forces1", trussPaintView.forcebundle1());
                intent1.putExtra("supports1", trussPaintView.supportbundle1());
                intent1.putExtra("attritruss", trussPaintView.attritrussbundle());
                startActivity(intent1);
                return true;
            case R.id.clear:
                trussPaintView.clear();
                return true;
            case R.id.calculation:
                Intent intent2 = new Intent(this, Calculation.class);
                intent2.putExtra("nodes2", trussPaintView.nodebundle2());
                intent2.putExtra("elements2", trussPaintView.elementbundle2());
                intent2.putExtra("forces2", trussPaintView.forcebundle2());
                intent2.putExtra("supports2", trussPaintView.supportbundle2());
                intent2.putExtra("attritruss", trussPaintView.attritrussbundle());
                intent2.putExtra("moment", trussPaintView.momentbundle());
                startActivity(intent2);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void openNodeDialog() {

        NodeAttributes nodeAttributes = new NodeAttributes(trussPaintView.ThrowNodeX(), trussPaintView.ThrowNodeY());
        nodeAttributes.show(getSupportFragmentManager(), "Node");
    }

    public void openTrussDialog() {
        TrussAttributes trussAttributes = new TrussAttributes(trussPaintView.ThrowTrussE(), trussPaintView.ThrowTrussA(),trussPaintView.ThrowTrussI());
        trussAttributes.show(getSupportFragmentManager(), "Truss");
    }

    public void openForceDialog() {
        ForceAttributes forceAttributes = new ForceAttributes(trussPaintView.ThrowForceF(), trussPaintView.ThrowForceAngle());
        forceAttributes.show(getSupportFragmentManager(), "Force");
    }
}
