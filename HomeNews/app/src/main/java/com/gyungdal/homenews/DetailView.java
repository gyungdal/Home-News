package com.gyungdal.homenews;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.gyungdal.homenews.Network.Read;

/**
 * Created by GyungDal on 2015-11-23.
 */
public class DetailView extends AppCompatActivity{
    private TextView stat;
    private TextView caution;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    private Detail_Text dt;
    private static final String TAG = "DetailView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_view);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        Init();
        Intent intent = getIntent();
        String Value = intent.getStringExtra("Value");
        final int Select = intent.getIntExtra("Select", 4);
        stat.setText(String.valueOf(Value));
        try {
            caution.setText(getString(new Detail_Text().getText(Select, Double.parseDouble(Value))));
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
        switch(Select){
            case 0 : toolbar.setTitle(R.string.Temp); break;
            case 1 : toolbar.setTitle(R.string.Humi); break;
            case 2 : toolbar.setTitle(R.string.Gas); break;
            case 3 : toolbar.setTitle(R.string.Dust); break;
            case 4 : Log.e(TAG, "Wrong Select Num"); toolbar.setTitle("Error"); break;
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stat.setText(new Read((byte)Select).get());
                Snackbar.make(findViewById(R.id.Deatail) , R.string.Sync, Snackbar.LENGTH_SHORT).show();
            }
        });
        setToolbar();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    private void setToolbar(){
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                    Log.i(TAG,"Detail View Finish");
                }
            });
        }
    }

    private void Init(){
        dt = new Detail_Text();
        stat = (TextView)findViewById(R.id.in_stat);
        caution = (TextView)findViewById(R.id.caution);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }
}
