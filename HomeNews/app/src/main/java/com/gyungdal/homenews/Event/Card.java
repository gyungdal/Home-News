package com.gyungdal.homenews.Event;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.gyungdal.homenews.DetailView;
import com.gyungdal.homenews.MainActivity;

/**
 * Created by GyungDal on 2015-11-23.
 */
public class Card extends MainActivity{

    private static final String TAG = "Intent Service";

    public void intent(String value,int num, Context context)
    {
        try{
            Intent i= new Intent(context ,DetailView.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            i.putExtra("Value",value);
            i.putExtra("Select", num);
            context.startActivity(i);
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
        }
    }
}
