package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 化学仪器查询
 * Created by ycm on 2018/10/7.
 */

public class AssayQueryActivity extends ActivityBase implements IActivityBase, View.OnClickListener  {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assay_query);

        ButterKnife.bind(this);

        initView();
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("化学仪器检索");
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
