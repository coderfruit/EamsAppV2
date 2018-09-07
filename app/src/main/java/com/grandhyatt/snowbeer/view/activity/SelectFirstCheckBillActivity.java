package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.SelectFirstCheckBillDataListAdapter;
import com.grandhyatt.snowbeer.entity.CustomerEntity;
import com.grandhyatt.snowbeer.entity.FirstCheckBillEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * 第2步,选择初检单
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 20:14
 */
public class SelectFirstCheckBillActivity extends ActivityBase implements IActivityBase,View.OnClickListener {

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    public static final String EXTRA_DATA_CUSTOMER_DATA = "EXTRA_DATA_CUSTOMER_DATA";
    public static final String EXTRA_DATA_STORE_HOUSE_DATA = "EXTRA_DATA_STORE_HOUSE_DATA";

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.mTv_Customer)
    TextView mTv_Customer;

    @BindView(R.id.mTv_StoreHouse)
    TextView mTv_StoreHouse;

    private List<FirstCheckBillEntity> mDataList;
    private FirstCheckBillEntity mDataModel;
    private SelectFirstCheckBillDataListAdapter mAdapter;

    private CustomerEntity mCustomerEntity;
    private StoreHouseEntity mStoreHouseEntity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_first_check_bill);

        ButterKnife.bind(this);

        mCustomerEntity = (CustomerEntity) getIntent().getSerializableExtra(EXTRA_DATA_CUSTOMER_DATA);
        mStoreHouseEntity = (StoreHouseEntity) getIntent().getSerializableExtra(EXTRA_DATA_STORE_HOUSE_DATA);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void initView() {
        mToolBar.setTitle("第2步,选择初检单");
    }

    @Override
    public void bindEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                mRefreshLayout.finishRefresh(2000);
            }
        });
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.finishLoadMore(2000);
            }
        });

        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FirstCheckBillEntity dataModel = mDataList.get(position);
                HashMap<String,Object> hashMap = new HashMap<String, Object>();
                hashMap.put(SelectSecondCheckBillActivity.EXTRA_DATA,dataModel);
                hashMap.put(SelectSecondCheckBillActivity.EXTRA_DATA_CUSTOMER_DATA,mCustomerEntity);
                hashMap.put(SelectSecondCheckBillActivity.EXTRA_DATA_STORE_HOUSE_DATA,mStoreHouseEntity);

                IntentUtil.newIntentForResult(SelectFirstCheckBillActivity.this,SelectSecondCheckBillActivity.class,hashMap,RESULT_RECHECK_COMPLETE_ACTIVITY);
            }
        });
    }

    @Override
    public void refreshUI() {

        if(mCustomerEntity != null){
            mTv_Customer.setText(mCustomerEntity.getCustomerName());
        }

        if(mStoreHouseEntity !=null){
            mTv_StoreHouse.setText(mStoreHouseEntity.getStoreHouseName());
        }


        mDataList = new ArrayList<>();
        mDataModel = new FirstCheckBillEntity();
        mDataModel.setBillNo("1122");
        mDataModel.setFirstCheckDate("2018-07-08 12:26");
        mDataModel.setFirstCheckUser("老吴");
        mDataModel.setPackingCode("1101");
        mDataModel.setPackingName("纸箱500ml 大连干啤");
        mDataModel.setTruckNo("辽A 12345");
        mDataModel.setReceivingBillType("0");
        mDataList.add(mDataModel);

        mDataModel = new FirstCheckBillEntity();
        mDataModel.setBillNo("1123");
        mDataModel.setFirstCheckDate("2018-07-08 13:02");
        mDataModel.setFirstCheckUser("中吴");
        mDataModel.setPackingCode("1102");
        mDataModel.setPackingName("纸箱500ml 雪花干啤");
        mDataModel.setTruckNo("辽A 23456");
        mDataModel.setReceivingBillType("1");
        mDataList.add(mDataModel);

        mDataModel = new FirstCheckBillEntity();
        mDataModel.setBillNo("1124");
        mDataModel.setFirstCheckDate("2018-07-08 13:38");
        mDataModel.setFirstCheckUser("小吴");
        mDataModel.setPackingCode("1103");
        mDataModel.setPackingName("纸箱500ml 青岛干啤");
        mDataModel.setTruckNo("辽A 34567");
        mDataModel.setReceivingBillType("1");
        mDataList.add(mDataModel);

        mAdapter = new SelectFirstCheckBillDataListAdapter(SelectFirstCheckBillActivity.this,mDataList);
        mLv_DataList.setAdapter(mAdapter);


    }

    @Override
    public void requestNetworkData() {

    }

}
