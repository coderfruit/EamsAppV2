package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ListViewHeightUtil;
import com.grandhyatt.commonlib.utils.UnitUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.SelectFirstCheckBillDataListAdapter;
import com.grandhyatt.snowbeer.entity.CustomerEntity;
import com.grandhyatt.snowbeer.entity.FirstCheckBillEntity;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;
import com.grandhyatt.snowbeer.view.NumberEditText;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 第三步,确认复检数量
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 19:48
 */
public class SelectSecondCheckBillActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_DATA_CUSTOMER_DATA = "EXTRA_DATA_CUSTOMER_DATA";
    public static final String EXTRA_DATA_STORE_HOUSE_DATA = "EXTRA_DATA_STORE_HOUSE_DATA";

    /**
     * 标题栏
     */
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mTv_Customer)
    TextView mTv_Customer;

    @BindView(R.id.mTv_StoreHouse)
    TextView mTv_StoreHouse;

    /**
     * 验收数量
     */
    @BindView(R.id.mNEdt_Check)
    NumberEditText mNEdt_Check;

    /**
     * 缺损比率
     */
    @BindView(R.id.mNEdt_DefectRate)
    NumberEditText mNEdt_DefectRate;

    /**
     * 缺损瓶数
     */
    @BindView(R.id.mNEdt_Defect)
    NumberEditText mNEdt_Defect;

    /**
     * 可修复箱数
     */
    @BindView(R.id.mNEdt_Repairable)
    NumberEditText mNEdt_Repairable;

    /**
     * 不可修复箱数
     */
    @BindView(R.id.mNEdt_NotRepairable)
    NumberEditText mNEdt_NotRepairable;

    /**
     * 数据列表
     */
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRL_DefectRate)
    RelativeLayout mRL_DefectRate;

    /**
     * 下一步按钮
     */
    @BindView(R.id.mBtn_Next)
    Button mBtn_Next;

    /**
     * 重置按钮
     */
    @BindView(R.id.mBtn_Reset)
    Button mBtn_Reset;

    private FirstCheckBillEntity mFirstCheckBillEntity;

    private List<FirstCheckBillEntity> mDataList = new ArrayList<>();
    private SelectFirstCheckBillDataListAdapter mAdapter;

    private CustomerEntity mCustomerEntity;
    private StoreHouseEntity mStoreHouseEntity;
    private SecondChkListEntity mSecondChkListEntity = new SecondChkListEntity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_second_check_bill);

        mCustomerEntity = (CustomerEntity) getIntent().getSerializableExtra(EXTRA_DATA_CUSTOMER_DATA);
        mStoreHouseEntity = (StoreHouseEntity) getIntent().getSerializableExtra(EXTRA_DATA_STORE_HOUSE_DATA);
        mFirstCheckBillEntity = (FirstCheckBillEntity) getIntent().getSerializableExtra(EXTRA_DATA);

        ButterKnife.bind(this);


        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mBtn_Reset:
                resetNEditTextData();
                break;
            case R.id.mBtn_Next:
                HashMap<String, Object> hashMap = new HashMap<String, Object>();


                mSecondChkListEntity.setBillNO(mFirstCheckBillEntity.getBillNo());
                mSecondChkListEntity.setPackingName(mFirstCheckBillEntity.getPackingName());
                mSecondChkListEntity.setPackingCode(mFirstCheckBillEntity.getPackingCode());
                mSecondChkListEntity.setSecondCheckDate(mFirstCheckBillEntity.getFirstCheckDate());
                mSecondChkListEntity.setSecondCheckUser(mFirstCheckBillEntity.getFirstCheckUser());
                mSecondChkListEntity.setBadBoxCount(mNEdt_Repairable.getData());
                mSecondChkListEntity.setBrokenBoxCount(mNEdt_NotRepairable.getData());
                mSecondChkListEntity.setSecondCheckCount(mNEdt_Check.getData());
                mSecondChkListEntity.setReturnLoanCount(mNEdt_Repairable.getData());
                mSecondChkListEntity.setReturnForegiftCount(mNEdt_Repairable.getData());
                mSecondChkListEntity.setDepositCount(mNEdt_Repairable.getData());
                mSecondChkListEntity.setMoney(mNEdt_Repairable.getData());

                hashMap.put(ReCheckBillCommitActivity.EXTRA_DATA_SECOND_CHK_LIST_DATA, mSecondChkListEntity);
                hashMap.put(ReCheckBillCommitActivity.EXTRA_DATA_CUSTOMER_DATA, mCustomerEntity);
                hashMap.put(ReCheckBillCommitActivity.EXTRA_DATA_STORE_HOUSE_DATA, mStoreHouseEntity);

                IntentUtil.newIntentForResult(SelectSecondCheckBillActivity.this, ReCheckBillCommitActivity.class, hashMap, RESULT_RECHECK_COMPLETE_ACTIVITY);
                break;
            case R.id.mRL_DefectRate:
                showDefectRateListMenu();
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
        }
    }

    @Override
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        resetNEditTextData();
    }

    /**
     * 重置文本框数据
     */
    private void resetNEditTextData() {
        mNEdt_Check.setData(100);
        mNEdt_DefectRate.setData(2);
        mNEdt_Defect.setData(30);
        mNEdt_Repairable.setData(50);
        mNEdt_NotRepairable.setData(10);
    }

    @Override
    public void bindEvent() {
        mBtn_Reset.setOnClickListener(this);
        mBtn_Next.setOnClickListener(this);
        mRL_DefectRate.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("第3步,确认复检数量");

        if (mCustomerEntity != null) {
            mTv_Customer.setText(mCustomerEntity.getCustomerName());
        }

        if (mStoreHouseEntity != null) {
            mTv_StoreHouse.setText(mStoreHouseEntity.getStoreHouseName());
        }

        if (mFirstCheckBillEntity != null) {
            mDataList.add(mFirstCheckBillEntity);
            mAdapter = new SelectFirstCheckBillDataListAdapter(SelectSecondCheckBillActivity.this, mDataList);
            mLv_DataList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            ListViewHeightUtil.setListViewHeightBasedOnChildren(mLv_DataList, UnitUtils.dp2px(SelectSecondCheckBillActivity.this, 80));

            mLv_DataList.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }

    @Override
    public void requestNetworkData() {

    }

    /**
     * 显示组织机构列表菜单
     */
    private void showDefectRateListMenu() {
        List<String> dataList = new ArrayList<>();
        dataList.add("缺损比率 2%");
        dataList.add("缺损比率 5%");
        dataList.add("缺损比率 7%");
        dataList.add("缺损比率 9%");
        dataList.add("缺损比率 10%");


        showSelectDialog(new SelectDialog.SelectDialogListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int data = 0;
                switch (position) {
                    case 0:
                        data = 2;
                        break;
                    case 1:
                        data = 5;
                        break;
                    case 2:
                        data = 7;
                        break;
                    case 3:
                        data = 9;
                        break;
                    case 4:
                        data = 10;
                        break;
                    default:
                        data = 0;
                        break;
                }
                mNEdt_DefectRate.setData(data);
            }
        }, dataList);
    }
}
