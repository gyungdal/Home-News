package com.gyungdal.homenews.Card;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.gyungdal.homenews.R;
/**
 *
 * Created by GyungDal on 2015-11-15.
 */
public class Description extends AppCompatActivity {
    private static final String TAG = "Select Card Image";
    private static final String[] Sel = {"Temp : ", "Humi : ","Gas : ", "Dust : "};
    public int Image_Select(int What, double value, boolean Check) {
        int Value = (int)value;
        Log.i(TAG, Sel[What] +  String.valueOf(value));
        if (!Check)
            return R.drawable.unknow;
        else {
            switch (What) {
                case 0:
                    if (Value < 5 || Value > 35) return R.drawable.danger;
                    else if (Value < 13 || Value > 24) return R.drawable.unsafe;
                    else return R.drawable.safe;
                case 1:
                    if (Value < 30 || Value > 80) return R.drawable.danger;
                    else if (Value < 40 || Value > 70) return R.drawable.unsafe;
                    else return R.drawable.safe;
                case 2:
                    if (Value > 10000) return R.drawable.danger;
                    else if (Value > 5000) return R.drawable.unsafe;
                    else return R.drawable.safe;
                case 3:
                    if (Value > 700) return R.drawable.danger;
                    else if (Value > 500) return R.drawable.unsafe;
                    else return R.drawable.safe;
                default:
                    return R.drawable.unknow;
            }
        }
    }
}
