package com.grandhyatt.snowbeer.view.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.ReCheckBillCompleteDataListAdapter;
import com.grandhyatt.snowbeer.adapter.ReCheckBillDataListAdapter;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.GetSecondChkListCallBack;
import com.grandhyatt.snowbeer.network.result.GetSecondChkListResult;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ReCheckCompleteActivity extends ActivityBase implements IActivityBase,View.OnClickListener {

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    private final int ACTION_REFRESH_DATA = 0;

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mTv_BillNo)
    TextView mTv_BillNo;

    @BindView(R.id.mTv_Money)
    TextView mTv_Money;

    @BindView(R.id.mRL_Money)
    RelativeLayout mRL_Money;

    @BindView(R.id.mBtn_Complete)
    Button mBtn_Complete;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    private ReCheckBillCompleteDataListAdapter mAdapter;

    private List<SecondChkListEntity> mDataList;

    private GetSecondChkListResult mSecondChkListResult;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_REFRESH_DATA:
                    refreshListView();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_re_check_complete);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mRL_Money:

                IntentUtil.newIntent(ReCheckCompleteActivity.this,AccountBalanceActivity.class);
                break;
            case R.id.mBtn_Complete:
                setResult(RESULT_OK);
                finish();
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
        mRL_Money.setOnClickListener(this);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestNetworkData();
            }
        });
        mBtn_Complete.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("复检单提交完成");
    }

    private void refreshListView(){
        if(mDataList != null) {
            mAdapter = new ReCheckBillCompleteDataListAdapter(ReCheckCompleteActivity.this, mDataList);
            mLv_DataList.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();

            mTv_BillNo.setText(mSecondChkListResult.billNo);
            mTv_Money.setText(String.valueOf(mSecondChkListResult.money / 100));
        }
    }

    @Override
    public void requestNetworkData() {
        NetWorkRequestUtils.getSecondChkListAsync(ReCheckCompleteActivity.this, new GetSecondChkListCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showToast(ReCheckCompleteActivity.this,"获取复检单数据发生错误!");
            }

            @Override
            public void onResponse(GetSecondChkListResult response, int id) {
                if (response == null){
                    ToastUtils.showToast(ReCheckCompleteActivity.this,"获取复检单数据发生错误!");
                    return;
                }

                if(response.code != Result.RESULT_CODE_SUCCSED || response.data == null){
                    ToastUtils.showToast(ReCheckCompleteActivity.this,"获取复检单数据失败:" + response.msg);
                    return;
                }
                mSecondChkListResult = response;
                mDataList = mSecondChkListResult.data;
                mHandler.sendEmptyMessage(ACTION_REFRESH_DATA);

            }
        });
    }
}
