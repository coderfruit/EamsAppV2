package com.grandhyatt.commonlib.utils;


import android.content.Context;
import android.widget.Toast;

public class ToastUtils {


    /**
     * 显示短Toast
     * */
    public static void showToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示长Toast
     *
     * */
    public static void showLongToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_LONG).show();
    }
}
