package com.example.Eric_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class TrussActivity extends AppCompatActivity {

    private TrussPaintView trussPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truss_paint_view);

        //建立paintview，所有繪圖行為都在上面執行
        trussPaintView = findViewById(R.id.TrussPaintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        trussPaintView.init(metrics);
    }

    //建立選單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.truss_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //建立選單選擇事件
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_dot:
                trussPaintView.setDot();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.action_writenode:
                trussPaintView.selectnode();
                if (TrussPaintView.checkN == true) {
                    openNodeDialog();
                    TrussPaintView.checkN = false;
                }
                break;
            case R.id.action_pin:
                trussPaintView.setPin();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.VerticalRoller:
                trussPaintView.setVerRoller();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.HorizentalRoller:
                trussPaintView.setHorRoller();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.action_fix:
                trussPaintView.setFix();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.action_truss:
                trussPaintView.setTruss();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.action_writetruss:
                trussPaintView.selecttruss();
                if (TrussPaintView.checkT == true) {
                    openTrussDialog();
                    TrussPaintView.checkT = false;
                }
                break;
            case R.id.action_F:
                trussPaintView.setForce();
                trussPaintView.TrussTurnBack();
                trussPaintView.ForceTurnBack();
                trussPaintView.DotTurnBack();
                break;
            case R.id.action_writeforce:
                trussPaintView.selectforce();
                if (TrussPaintView.checkF == true) {
                    openForceDialog();
                    TrussPaintView.checkF = false;
                }
                break;
            case R.id.Array:
                Intent intent1 = new Intent(this, ArrayOutput.class);
                intent1.putExtra("nodes1", trussPaintView.nodebundle1());
                intent1.putExtra("elements1", trussPaintView.elementbundle1());
                intent1.putExtra("forces1", trussPaintView.forcebundle1());
                intent1.putExtra("supports1", trussPaintView.supportbundle1());
                intent1.putExtra("attritruss", trussPaintView.attribeambundle1());
                startActivity(intent1);
                return true;
            case R.id.clear:
                trussPaintView.clear();
                return true;
            case R.id.calculation:
                TrussCaculation1.truss();
                if(TrussCaculation1.Toastcheck == false){
                    Toastshow();
                    TrussCaculation1.Toastcheck=true;
                }else {
                    //TrussPaintView.canvas.drawColor(0, PorterDuff.Mode.CLEAR);

                    RebuildMethod1.RebuildSupport();
                    RebuildMethod1.RebuildTruss();
                    RebuildMethod1.RebuildNode();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void openNodeDialog() {
        NodeAttributes1 nodeAttributes1 = new NodeAttributes1(trussPaintView.ThrowNodeX(), trussPaintView.ThrowNodeY());
        nodeAttributes1.show(getSupportFragmentManager(), "Node");
    }

    public void openTrussDialog() {
        TrussAttributes1 trussAttributes1 = new TrussAttributes1(trussPaintView.ThrowTrussE(), trussPaintView.ThrowTrussA());
        trussAttributes1.show(getSupportFragmentManager(), "Truss");
    }

    public void openForceDialog() {
        ForceAttributes1 forceAttributes1 = new ForceAttributes1(trussPaintView.ThrowForceF(), trussPaintView.ThrowForceAngle());
        forceAttributes1.show(getSupportFragmentManager(), "Force");
    }
    public void Toastshow(){
        Toast.makeText(TrussActivity.this,"此桿件不存在!!",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trussPaintView.clear();
    }
}
