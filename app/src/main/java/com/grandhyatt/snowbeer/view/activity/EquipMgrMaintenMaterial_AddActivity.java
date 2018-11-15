package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备保养用机物料添加页面
 * Created by ycm on 2018/11/14.
 */

public class EquipMgrMaintenMaterial_AddActivity extends ActivityBase implements IActivityBase, View.OnClickListener{

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;
    @BindView(R.id.mTv_EquipCorp)
    TextView mTv_EquipCorp;

    //当前页码
    private int mPageIndex = 0;
    //页面数据数量
    private int mPageSize = 10;
    //加载类型
    private boolean mIsLoadMore = false;

    String _EquipID;//传入的设备ID
    String _EquipCode;//传入的设备Cpde
    String _EquipName;//传入的设备Name
    String _CorpID; //传入的组织机构ID


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mgr_mainten_material_add);

        ButterKnife.bind(this);
        initView();

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");
        _EquipCode = intent.getStringExtra("equipCode");
        _EquipName = intent.getStringExtra("equipName");
        _CorpID = intent.getStringExtra("corpID");

        if (_CorpID != null) {  //根据设备ID 获取预警信息

        }

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
