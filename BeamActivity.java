package com.example.Eric_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class BeamActivity extends AppCompatActivity {

    private BeamPaintView beamPaintView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beam_paint_view);

        //建立paintview，所有繪圖行為都在上面執行
        beamPaintView = findViewById(R.id.BeamPaintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        beamPaintView.init(metrics);

    }

    //建立選單
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.beam_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //建立選單選擇事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_dot:
                beamPaintView.setDot();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_writenode:
                beamPaintView.selectnode();
                if (BeamPaintView.checkN == true) {
                    openNodeDialog();
                    BeamPaintView.checkN = false;
                }
                break;
            case R.id.action_pin:
                beamPaintView.setPin();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.VerticalRoller:
                beamPaintView.setVerRoller();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.HorizentalRoller:
                beamPaintView.setHorRoller();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_fix:
                beamPaintView.setFix();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_beam:
                beamPaintView.setTruss();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_writebeam:
                beamPaintView.selecttruss();
                if (BeamPaintView.checkT == true) {
                    openTrussDialog();
                    BeamPaintView.checkT = false;
                }
                break;
            case R.id.action_F:
                beamPaintView.setForce();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_M:
                beamPaintView.setMoment();
                beamPaintView.TrussTurnBack();
                beamPaintView.ForceTurnBack();
                beamPaintView.DotTurnBack();
                break;
            case R.id.action_writeforce:
                beamPaintView.selectforce();
                if (BeamPaintView.checkF == true) {
                    openForceDialog();
                    BeamPaintView.checkF = false;
                }
                if (BeamPaintView.checkM == true) {
                    openMomentDialog();
                    BeamPaintView.checkM = false;
                }
                break;
            case R.id.Array:
                Intent intent1 = new Intent(this, ArrayOutput.class);
                intent1.putExtra("nodes1", beamPaintView.nodebundle1());
                intent1.putExtra("elements1", beamPaintView.elementbundle1());
                intent1.putExtra("forces1", beamPaintView.forcebundle1());
                intent1.putExtra("supports1", beamPaintView.supportbundle1());
                intent1.putExtra("attritruss", beamPaintView.attribeambundle1());
                intent1.putExtra("moment", beamPaintView.momentbundle1());
                startActivity(intent1);
                return true;
            case R.id.clear:
                beamPaintView.clear();
                return true;
            case R.id.calculation:
                BeamCaculation1.Beam();
                if(BeamCaculation1.Toastcheck1 == false){
                    Toastshow();
                    BeamCaculation1.Toastcheck1=true;
                }else {
                    //TrussPaintView.canvas.drawColor(0, PorterDuff.Mode.CLEAR);
                    RebuildMethod2.RebuildSupport();
                    RebuildMethod2.RebuildTruss();
                    RebuildMethod2.RebuildNode();
                }
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void openNodeDialog() {
        NodeAttributes2 nodeAttributes2 = new NodeAttributes2(beamPaintView.ThrowNodeX(), beamPaintView.ThrowNodeY());
        nodeAttributes2.show(getSupportFragmentManager(), "Node");
    }

    public void openTrussDialog() {
        TrussAttributes2 trussAttributes2 = new TrussAttributes2(beamPaintView.ThrowTrussE(), beamPaintView.ThrowTrussA());
        trussAttributes2.show(getSupportFragmentManager(), "Truss");
    }

    public void openForceDialog() {
        ForceAttributes2 forceAttributes2 = new ForceAttributes2(beamPaintView.ThrowForceF(), beamPaintView.ThrowForceAngle());
        forceAttributes2.show(getSupportFragmentManager(), "Force");
    }

    public void openMomentDialog() {
        MomentAttributes momentAttributes = new MomentAttributes(beamPaintView.ThrowMomentM(), beamPaintView.ThrowDirectionD());
        momentAttributes.show(getSupportFragmentManager(), "Force");
    }
    public void Toastshow(){
        Toast.makeText(BeamActivity.this,"此桿件不存在!!",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        beamPaintView.clear();
    }
}
