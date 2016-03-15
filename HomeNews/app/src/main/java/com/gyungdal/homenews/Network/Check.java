package com.gyungdal.homenews.Network;
/**
 * Created by GyungDal on 2015-11-22.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class Check extends BroadcastReceiver {
    private static String TAG = "NetWork Check Service";
    private static Context context;
    public Check(Context context){
        this.context = context;
    }
    public Check(){}
    public boolean isNetwork(){
        try {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();  //wifi
            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState(); // mobile ConnectivityManager.TYPE_MOBILE
            if (wifi == NetworkInfo.State.CONNECTED || wifi == NetworkInfo.State.CONNECTING){
                Log.i(TAG,"Wifi connected");
                return true;
            }
            if (mobile == NetworkInfo.State.CONNECTED || mobile == NetworkInfo.State.CONNECTING) {
                Log.i(TAG,"Mobile network connected");
                return true;
            }
        } catch (NullPointerException e) {
            Log.e(TAG,e.getMessage());
            return false;
        } catch(Exception e){
            Log.e(TAG,e.getMessage());
            return false;
        }
        return false;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        this.context = context;
        // 네트웍에 변경이 일어났을때 발생하는 부분
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            ConnectivityManager connectivityManager =
                    (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            Toast.makeText(context,"Active Network Type : " + activeNetInfo.getTypeName() , Toast.LENGTH_SHORT).show();
            Toast.makeText(context,"Mobile Network Type : " + mobNetInfo.getTypeName() , Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isNetConnect(String url) {
        try {
            URL murl = new URL(url);
            URLConnection urlc = murl.openConnection();
            HttpURLConnection HttpCode = (HttpURLConnection)urlc;
            int Rcode= ((HttpCode.getResponseCode() - (HttpCode.getResponseCode() % 100)) / 100);
            switch(Rcode){
                case 2 : return true; //Response code 2xx (Success)
                default: return false; //Response code not 2xx (Not success)
            }
        }catch(Exception e){
            Log.e(TAG, e.getMessage());
            return false;
        }
    }
}
