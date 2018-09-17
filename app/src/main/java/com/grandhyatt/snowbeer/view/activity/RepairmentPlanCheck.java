package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;

/**
 * Created by ycm on 2018/9/17.
 */

public class RepairmentPlanCheck extends ActivityBase implements IActivityBase, View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairment_plan_check);

        Intent intent = getIntent();
        String mTv_EquipmentID = intent.getStringExtra("mTv_EquipmentID");
        String mTv_ReapirLevel = intent.getStringExtra("mTv_ReapirLevel");

        //设备、维修级别不为空
        if ( StringUtils.isEmpty(mTv_EquipmentID) && StringUtils.isEmpty(mTv_ReapirLevel) ) {

        }
        else{


        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent() {

    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

    }
}
