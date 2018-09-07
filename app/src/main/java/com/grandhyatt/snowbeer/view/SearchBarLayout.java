package com.grandhyatt.snowbeer.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.activity.ScanActivity;

/**
 * Created by ycm on 2018/8/14.
 */

public class SearchBarLayout extends RelativeLayout {
    BroadcastReceiver mReceiver;
    IntentFilter mFilter;

    Button mBT_OpenCamera;

    Button mBT_Search;

    EditText mEdt_Barcode;

    public SearchBarLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_activity_searchbar, this);

        mBT_OpenCamera =(Button)findViewById(R.id.mIB_OpenCamera);
        mBT_Search =(Button)findViewById(R.id.mBT_Search);
        mEdt_Barcode =(EditText)findViewById(R.id.mEdt_Barcode);

        //调用相机读取条码
        mBT_OpenCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //默认横屏扫码
                //new IntentIntegrator((Activity)context).initiateScan(); //初始化扫描
                //竖屏扫码
                IntentIntegrator integrator = new IntentIntegrator((Activity)context);
                // 设置要扫描的条码类型，ONE_D_CODE_TYPES：一维码，QR_CODE_TYPES-二维码
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
                integrator.setCaptureActivity(ScanActivity.class);
                integrator.setPrompt("请扫描条码"); //底部的提示文字，设为""可以置空
                integrator.setCameraId(0); //前置或者后置摄像头
                integrator.setBeepEnabled(true); //扫描成功的「哔哔」声，默认开启
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();

            }
        });

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // 让其它的广播注册者无法获取广播信息，***此处必须***
                this.abortBroadcast();

                // 此处获取扫描结果信息
                final String scanResult = intent.getStringExtra("value");
                setBarcode(scanResult);

                //执行按钮点击事件
                mBT_Search.performClick();
            }
        };
    }

    public IntentFilter getFilter() {
        mFilter = new IntentFilter("android.intent.action.SCAN_RESULT");
        // 在用户自行获取数据时，将广播的优先级调到最高 1000，***此处必须***
        mFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        return mFilter;
    }

    public BroadcastReceiver getBroadcastReceiver() {
        return mReceiver;
    }



    /**
     * 设置搜索按钮单击监听
     */
    public void setSearchButtonOnClickListener(OnClickListener listener) {
        mBT_Search.setOnClickListener(listener);
    }

    /**
     * 设置条码输入框回车事件
     * @param listener
     */
    public void setBarcodeEnterListener(TextView.OnEditorActionListener listener){
        mEdt_Barcode.setOnEditorActionListener(listener);
    }

    /**
     * 获取条码内容
     * @return
     */
    public String getBarcode()
    {
         return mEdt_Barcode.getText().toString();
    }

    /**
     * 设置条码
     * @param barcode
     */
    public void setBarcode(String barcode)
    {
        mEdt_Barcode.setText(barcode);
        //执行按钮点击事件
        mBT_Search.performClick();
    }

    public void setButtonSearchText(String str)
    {
        mBT_Search.setText(str);
    }


}
