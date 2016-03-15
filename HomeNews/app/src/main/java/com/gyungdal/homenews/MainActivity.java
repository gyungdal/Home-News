package com.gyungdal.homenews;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.gyungdal.homenews.Network.Check;
import com.gyungdal.homenews.Network.Read;
import com.gyungdal.homenews.Recycle.Recycle;
import com.gyungdal.homenews.Recycle.RecyclerAdapter;

public class MainActivity extends AppCompatActivity {
    public SharedPreferences sharedPref ;
    private List<Recycle> items;
    private Recycle[] item;

    public static Check Net;
    private RecyclerView recyclerView;
    public Read Read;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "Create main activity");
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Net = new Check(getApplicationContext());
        items=new ArrayList<>();
        item =new Recycle[4];
        Read = new Read();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        recyclerView=(RecyclerView)findViewById(R.id.recyclerview);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.clear();
                for (byte i = 0; i < 4; i++){
                    item[i].setDes();
                    items.add(item[i]);
                    recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),
                                                                items, R.layout.activity_main));
                }
            Snackbar.make(recyclerView,R.string.Sync,Snackbar.LENGTH_SHORT).
                    setAction("Action", null).show();
                }
    });
        setRecycleView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch(id){
            //Toast.makeText(this, "NotRead", Toast.LENGTH_SHORT).show();
            case R.id.action_settings :
                Url();
                return true;

            default :
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    private void setRecycleView(){

        //LinearLayout Init
        GridLayoutManager gridManager =  new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridManager);

        //List Add to Recycle
        int[] title = { R.string.Temp , R.string.Humi,R.string.Gas, R.string.Dust};

        for(int i=0;i<4;i++){
            item[i] = new Recycle(R.drawable.unknow,getString(title[i]),(byte)i);
            item[i].initDes();
            items.add(item[i]);
        }

        //Recycle View using Adapter

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(),items,R.layout.activity_main));
    }

    private void Url() {

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.Url_Title);
        alert.setMessage(R.string.Url_Des);

        // Set an EditText view to get user input
        final EditText Url = new EditText(this);
        try {
            Url.setHint(getString(R.string.Url_Hint) + Read.getUrl());
        }catch(NullPointerException e){
            Log.e(TAG,e.getMessage());
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
        /*final EditText password = new EditText(this);
        password.setHint(PASSWORD_HINT);*/
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(Url);
        //layout.addView(password);
        alert.setView(layout);

        alert.setPositiveButton(getString(R.string.Url_Ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                try {
                    Read.setUrl(Url.getText().toString());
                } catch (Exception e) {
                    Log.e(TAG, "URL input Error");
                }
                Snackbar.make(recyclerView,R.string.SaveUrl,Snackbar.LENGTH_SHORT).
                        setAction("Action",null).show();
            }
        });

        alert.setNegativeButton(getString(R.string.Url_Cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}

