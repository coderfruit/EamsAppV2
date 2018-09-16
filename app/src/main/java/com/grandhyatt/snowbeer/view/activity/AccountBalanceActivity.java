package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.AccountBalanceDataListAdapter;
import com.grandhyatt.snowbeer.entity.AccountBalanceInfoEntity;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountBalanceActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    public static final String EXTRA_DATA_ACCOUNT_BALANCE_DATA = "EXTRA_DATA_ACCOUNT_BALANCE_DATA";

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mELV_DataList)
    ExpandableListView mELV_DataList;

    private AccountBalanceDataListAdapter mAdapter;

    private List<AccountBalanceInfoEntity> mDataList = new ArrayList<>();

    private AccountBalanceInfoEntity mAccountBalanceInfoEntity;
    private List<AccountBalanceInfoEntity.AccountBalanceInfo> mAccountBalanceInfoDataList;
    private AccountBalanceInfoEntity.AccountBalanceInfo mAccountBalanceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_balance);

        ButterKnife.bind(AccountBalanceActivity.this);

//        mDataList = (List<AccountBalanceInfoEntity>) getIntent().getSerializableExtra(EXTRA_DATA_ACCOUNT_BALANCE_DATA);

        mAccountBalanceInfoDataList = new ArrayList<>();

        mAccountBalanceInfoEntity = new AccountBalanceInfoEntity();
        mAccountBalanceInfoEntity.setAccountType(0);
        mAccountBalanceInfoEntity.setAccountName("可用代保管包装物");

        mAccountBalanceInfo = new AccountBalanceInfoEntity.AccountBalanceInfo();
        mAccountBalanceInfo.setAccountType(0);
        mAccountBalanceInfo.setCount(10);
        mAccountBalanceInfo.setPackingCode("1001");
        mAccountBalanceInfo.setPackingName("580ml雪花专用辽宁绿瓶");

        mAccountBalanceInfoDataList.add(mAccountBalanceInfo);

        mAccountBalanceInfo = new AccountBalanceInfoEntity.AccountBalanceInfo();
        mAccountBalanceInfo.setAccountType(0);
        mAccountBalanceInfo.setCount(13);
        mAccountBalanceInfo.setPackingCode("1002");
        mAccountBalanceInfo.setPackingName("500ml雪花专用辽宁白瓶");
        mAccountBalanceInfoDataList.add(mAccountBalanceInfo);

        mAccountBalanceInfoEntity.setData(mAccountBalanceInfoDataList);

        mDataList.add(mAccountBalanceInfoEntity);

        //*************

        mAccountBalanceInfoDataList = new ArrayList<>();

        mAccountBalanceInfoEntity = new AccountBalanceInfoEntity();
        mAccountBalanceInfoEntity.setAccountType(1);
        mAccountBalanceInfoEntity.setAccountName("可用代保管包装物");

        mAccountBalanceInfo = new AccountBalanceInfoEntity.AccountBalanceInfo();
        mAccountBalanceInfo.setAccountType(1);
        mAccountBalanceInfo.setCount(120);
        mAccountBalanceInfo.setPackingCode("2001");
        mAccountBalanceInfo.setPackingName("500ml雪花专用吉林绿瓶");
        mAccountBalanceInfoDataList.add(mAccountBalanceInfo);

        mAccountBalanceInfo = new AccountBalanceInfoEntity.AccountBalanceInfo();
        mAccountBalanceInfo.setAccountType(1);
        mAccountBalanceInfo.setCount(133);
        mAccountBalanceInfo.setPackingCode("2002");
        mAccountBalanceInfo.setPackingName("500ml雪花专用吉林白瓶");
        mAccountBalanceInfoDataList.add(mAccountBalanceInfo);

        mAccountBalanceInfoEntity.setData(mAccountBalanceInfoDataList);

        mDataList.add(mAccountBalanceInfoEntity);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
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
        mToolBar.setTitle("账户结余信息");

        if(mDataList != null) {
            mAdapter = new AccountBalanceDataListAdapter(AccountBalanceActivity.this, mDataList);
            mELV_DataList.setAdapter(mAdapter);
            int intGroupCount = mDataList.size();
            for (int i = 0; i < intGroupCount; i++) {
                mELV_DataList.expandGroup(i);
            }

            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void requestNetworkData() {

    }
}
