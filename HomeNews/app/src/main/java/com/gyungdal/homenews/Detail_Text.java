package com.gyungdal.homenews;

import android.util.Log;

/**
 * Created by GyungDal on 2015-11-26.
 */
public class Detail_Text {
    private static final String TAG = "Detail View Select";
    public int getText(int Sel, double Val){
        Log.i(TAG, "String Select");
        switch(Sel){
            case 0 : return Temp(Val);
            case 1 : return Humi(Val);
            case 2 : return Gas(Val);
            case 3 : return Dust(Val);
        }
        return R.string.Debug;
    }

    private int Temp(double value){
        if(value > 30) return R.string.Temp_High;
        else if(value > 25) return R.string.Temp_Mid_High;
        else if(value <= 25 && value >= 10) return R.string.Temp_Safe;
        else if(value < 10 && value < 0) return R.string.Temp_Mid_Cold;
        else return R.string.Temp_Cold;
    }

    private int Humi(double value){
        if(value > 40 && value < 60) return R.string.Humi_Safe;
        else if(value > 50 && value < 70) return R.string.Humi_UnSafe;
        else if(value < 30) return R.string.Humi_Down_Danger;
        else return R.string.Humi_Up_Danger;
    }

    private int Gas(double value) {
        if (value < 1000) return R.string.Gas_Safe;
        else if (value < 3000) return R.string.Gas_UnSafe;
        else return R.string.Gas_Danger;
    }

    private int Dust(double value){
        if(value < 80) return R.string.Dust_Safe;
        else if(value < 200) return R.string.Dust_UnSafe;
        else return R.string.Dust_Danger;
    }
}
