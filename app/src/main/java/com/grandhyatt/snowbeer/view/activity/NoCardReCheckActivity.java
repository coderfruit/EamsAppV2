package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.CustomerEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;
import com.grandhyatt.snowbeer.helper.IData95RfidHelper;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.GetCustomerCallBack;
import com.grandhyatt.snowbeer.network.callback.GetStoreHouseListCallBack;
import com.grandhyatt.snowbeer.network.request.GetCustomerRequest;
import com.grandhyatt.snowbeer.network.result.GetCustomerResult;
import com.grandhyatt.snowbeer.network.result.GetStoreHouseListResult;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 有卡复检->第一步.确定复检及客户
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 19:23
 */
public class NoCardReCheckActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mRL_StoreHouse)
    RelativeLayout mRL_StoreHouse;

    @BindView(R.id.mTv_StoreHouse)
    TextView mTv_StoreHouse;

    @BindView(R.id.mRL_SelectCustomer)
    RelativeLayout mRL_SelectCustomer;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private String mStr_CustomerCode = "";

    private IData95RfidHelper iData95RfidHelper = new IData95RfidHelper();

    private List<StoreHouseEntity> mDataList_StoreHouse;
    private List<String> mStoreHouseList = new ArrayList<>();

    private CustomerEntity mCustomerEntity;
    private StoreHouseEntity mStoreHouseEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_card_re_check);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

    }

    @Override
    protected void onDestroy() {
        iData95RfidHelper.closeDevice();
        super.onDestroy();
    }

    @Override
    public void initView() {
        mToolBar.setTitle("第1步,确定复检及客户");
    }

    @Override
    public void bindEvent() {
        mRL_SelectCustomer.setOnClickListener(this);
        mRL_StoreHouse.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestNetworkData();
            }
        });
    }

    @Override
    public void refreshUI() {
        String strStoreHouse = SPUtils.getStoreHouse(NoCardReCheckActivity.this);
        mTv_StoreHouse.setText(TextUtils.isEmpty(strStoreHouse)?"请选择复检现场":strStoreHouse);
        mStoreHouseEntity = SPUtils.getStoreHouseEntity(NoCardReCheckActivity.this);
    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();
        NetWorkRequestUtils.getStoreHouseListAsync(NoCardReCheckActivity.this, new GetStoreHouseListCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();
                ToastUtils.showToast(NoCardReCheckActivity.this, "获取复检现场信息时发生错误!");
            }

            @Override
            public void onResponse(GetStoreHouseListResult response, int id) {
                dismissLoadingDialog();
                if (response == null || response.data == null || response.data.size() <= 0) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "获取复检现场信息时发生错误!");
                    return;
                }

                mDataList_StoreHouse = response.data;
                for (int i = 0; i < mDataList_StoreHouse.size(); i++) {
                    mStoreHouseList.add(mDataList_StoreHouse.get(i).getStoreHouseCode() + "|" + mDataList_StoreHouse.get(i).getStoreHouseName());
                }

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mRL_SelectCustomer:
                IntentUtil.newIntentForResult(NoCardReCheckActivity.this,InputCustomerInfoActivity.class,InputCustomerInfoActivity.REQUEST_DATA_CAR_PLATE_CODE);
                break;
            case R.id.mRL_StoreHouse:
                if(mDataList_StoreHouse == null || mDataList_StoreHouse.size() <= 0){
                    requestNetworkData();
                }else {
                    showStoreHouseListMenu();
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
            return;
        }

        if(requestCode == InputCustomerInfoActivity.REQUEST_DATA_CAR_PLATE_CODE){
//            String customerCode = data.getStringExtra(InputCustomerInfoActivity.RESULT_DATA_CAR_PLATE);
            String customerCode = "170001714";
            getCustomer(customerCode);
        }


    }

    /**
     * 显示组织机构列表菜单
     */
    private void showStoreHouseListMenu() {
        showSelectDialog(new SelectDialog.SelectDialogListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mStoreHouseEntity = mDataList_StoreHouse.get(position);
                mTv_StoreHouse.setText(mStoreHouseEntity.getStoreHouseCode() + "|" + mStoreHouseEntity.getStoreHouseName());
                SPUtils.setStoreHouseEntity(NoCardReCheckActivity.this,mStoreHouseEntity);
                SPUtils.setStoreHouse(NoCardReCheckActivity.this,mStoreHouseEntity.getStoreHouseCode() + "|" + mStoreHouseEntity.getStoreHouseName());
            }
        }, mStoreHouseList);
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
        NetWorkRequestUtils.getCustomerAsync(NoCardReCheckActivity.this, request, new GetCustomerCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                mRefreshLayout.finishRefresh();
                ToastUtils.showToast(NoCardReCheckActivity.this, "获取客户信息时发生错误:" + e.getMessage());
            }

            @Override
            public void onResponse(GetCustomerResult response, int id) {

                mRefreshLayout.finishRefresh();

                if (response == null) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "获取客户信息时发生错误!");
                    return;
                }

                if (response.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "获取客户信息时发生错误:" + response.msg);
                    return;
                }

                if (response.data == null) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "此客户信息不存在!");
                    return;
                }

                mCustomerEntity = response.data;

                if (mCustomerEntity == null) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "客户信息为空,请重新扫描客户身份识别卡!");
                    return;
                }

                if (mCustomerEntity == null) {
                    ToastUtils.showToast(NoCardReCheckActivity.this, "复检现场信息为空,请下拉刷新复检现场信息!");
                    return;
                }

                HashMap<String, Object> hashMap = new HashMap<String, Object>();
                hashMap.put(SelectFirstCheckBillActivity.EXTRA_DATA_CUSTOMER_DATA, mCustomerEntity);
                hashMap.put(SelectFirstCheckBillActivity.EXTRA_DATA_STORE_HOUSE_DATA, mStoreHouseEntity);

                IntentUtil.newIntentForResult(NoCardReCheckActivity.this, SelectFirstCheckBillActivity.class, hashMap,InputCustomerInfoActivity.REQUEST_DATA_CAR_PLATE_CODE);
            }
        });
    }



}
