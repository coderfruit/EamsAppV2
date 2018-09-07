package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.ReCheckBillDataListAdapter;
import com.grandhyatt.snowbeer.entity.CustomerEntity;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;
import com.grandhyatt.snowbeer.helper.IData95RfidHelper;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.GetCustomerCallBack;
import com.grandhyatt.snowbeer.network.request.GetCustomerRequest;
import com.grandhyatt.snowbeer.network.result.GetCustomerResult;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
/**
 * 刷卡确认提交复检信息
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 23:31
 */
public class ReCheckBillCommitActivity extends ActivityBase implements IActivityBase,View.OnClickListener {

    public static final String EXTRA_DATA_SECOND_CHK_LIST_DATA = "EXTRA_DATA_SECOND_CHK_LIST_DATA";
    public static final String EXTRA_DATA_CUSTOMER_DATA = "EXTRA_DATA_CUSTOMER_DATA";
    public static final String EXTRA_DATA_STORE_HOUSE_DATA = "EXTRA_DATA_STORE_HOUSE_DATA";

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mTv_Customer)
    TextView mTv_Customer;

    @BindView(R.id.mTv_StoreHouse)
    TextView mTv_StoreHouse;

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mIv_ScanCard)
    ImageView mIv_ScanCard;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private IData95RfidHelper iData95RfidHelper = new IData95RfidHelper();

    private List<SecondChkListEntity> mDataList = new ArrayList<>();
    private ReCheckBillDataListAdapter mAdapter;

    private CustomerEntity mCustomerEntity;
    private StoreHouseEntity mStoreHouseEntity;
    private SecondChkListEntity mSecondChkListEntity = new SecondChkListEntity();

    private String mStr_CustomerCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check_bill_commit);

        ButterKnife.bind(this);

        mSecondChkListEntity = (SecondChkListEntity) getIntent().getSerializableExtra(EXTRA_DATA_SECOND_CHK_LIST_DATA);
        mCustomerEntity = (CustomerEntity) getIntent().getSerializableExtra(EXTRA_DATA_CUSTOMER_DATA);
        mStoreHouseEntity = (StoreHouseEntity) getIntent().getSerializableExtra(EXTRA_DATA_STORE_HOUSE_DATA);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mIv_ScanCard:
                mStr_CustomerCode = "170001714";
                getCustomer(mStr_CustomerCode);
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent() {
        mIv_ScanCard.setOnClickListener(this);

    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("第4步,刷卡确认提交复检信息");

        if (mCustomerEntity != null) {
            mTv_Customer.setText(mCustomerEntity.getCustomerName());
        }

        if (mStoreHouseEntity != null) {
            mTv_StoreHouse.setText(mStoreHouseEntity.getStoreHouseName());
        }

        if(mSecondChkListEntity != null){
            mDataList.add(mSecondChkListEntity);
            mAdapter = new ReCheckBillDataListAdapter(ReCheckBillCommitActivity.this,mDataList);
            mLv_DataList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void requestNetworkData() {

    }

    @Override
    protected void onDestroy() {
        iData95RfidHelper.closeDevice();
        super.onDestroy();
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
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        //判断用户是否按了iData95设备的黄色按钮,按下后,打开RFID设备读取客户卡
        if (keyCode == 139 && event.getAction() == KeyEvent.ACTION_DOWN) {
            mStr_CustomerCode = iData95RfidHelper.readRfidCardData();
            getCustomer(mStr_CustomerCode);
            return true;
        }

        //判断用户是否按下了iData95设备的Enter按钮,
        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
            mStr_CustomerCode = "170001714";
            getCustomer(mStr_CustomerCode);
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取客户信息
     * <p>
     * * @param null
     *
     * @return
     */
    private void getCustomer(String customerCode) {
        final GetCustomerRequest request = new GetCustomerRequest();
        request.setCustomerCode(customerCode);
        NetWorkRequestUtils.getCustomerAsync(ReCheckBillCommitActivity.this, request, new GetCustomerCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefreshLayout.finishRefresh();
                ToastUtils.showToast(ReCheckBillCommitActivity.this, "获取客户信息时发生错误:" + e.getMessage());
            }

            @Override
            public void onResponse(GetCustomerResult response, int id) {

                mRefreshLayout.finishRefresh();

                if (response == null) {
                    ToastUtils.showToast(ReCheckBillCommitActivity.this, "获取客户信息时发生错误!");
                    return;
                }

                if (response.data == null || response.code != Result.RESULT_CODE_SUCCSED || response.data == null) {
                    ToastUtils.showToast(ReCheckBillCommitActivity.this, "获取客户信息时发生错误:" + response.msg);
                    return;
                }

                mCustomerEntity = response.data;

                if (mCustomerEntity == null) {
                    ToastUtils.showToast(ReCheckBillCommitActivity.this, "客户信息为空,请重新扫描客户身份识别卡!");
                    return;
                }

                if (mCustomerEntity == null) {
                    ToastUtils.showToast(ReCheckBillCommitActivity.this, "复检现场信息为空,请下拉刷新复检现场信息!");
                    return;
                }

                //验证当前刷卡的客户是否与复检单的用户一致
                if(response.data.getCustomerCode() == mCustomerEntity.getCustomerCode()){
                    HashMap<String, Object> hashMap = new HashMap<String, Object>();
                    hashMap.put(SelectFirstCheckBillActivity.EXTRA_DATA_CUSTOMER_DATA, mCustomerEntity);
                    hashMap.put(SelectFirstCheckBillActivity.EXTRA_DATA_STORE_HOUSE_DATA, mStoreHouseEntity);

                    IntentUtil.newIntentForResult(ReCheckBillCommitActivity.this, ReCheckCompleteActivity.class, hashMap,RESULT_RECHECK_COMPLETE_ACTIVITY);
                }else{
                    ToastUtils.showToast(ReCheckBillCommitActivity.this,"当前刷卡客户与复检单客户信息不一致,请重试!");
                }
            }
        });
    }
}
