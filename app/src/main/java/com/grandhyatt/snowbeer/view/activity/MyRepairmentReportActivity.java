package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.FailureReportingEntityDataListAdapter;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.result.FailureReportingsResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ycm on 2018/8/28.
 */

public class MyRepairmentReportActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    //当前页码
    private int mPageIndex = 0;
    //页面数据数量
    private int mPageSize = 10;
    //加载类型
    private boolean mIsLoadMore = false;

    private FailureReportingEntityDataListAdapter mAdapter;

    public static final int RESULT_REPORT_COMPLETE_ACTIVITY = 10001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_fault_report);

        ButterKnife.bind(this);

        initView();
        refreshUI();
        bindEvent();

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {

        mToolBar.setTitle("我的报修");
        mToolBar.showMenuButton();
        mToolBar.setMenuText("我要报修");
    }

    @Override
    public void bindEvent() {

        //我要报修
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntentForResult(MyRepairmentReportActivity.this, FaultReportActivity.class, RESULT_REPORT_COMPLETE_ACTIVITY);
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = 0;
                mIsLoadMore = false;
                mRefreshLayout.setNoMoreData(false);
                requestNetworkData();
            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                mIsLoadMore = true;
                requestNetworkData();
            }
        });
//单个报修选中查看
        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView mTv_ReportID = (TextView) view.findViewById(R.id.mTv_ReportID);
                TextView mTv_EquipID = (TextView) view.findViewById(R.id.mTv_EquipID);

                HashMap<String, Object> maps = new HashMap<String, Object>();
                maps.put("mTv_ReportID", mTv_ReportID.getText().toString());
                maps.put("mTv_EquipID", mTv_EquipID.getText().toString());


                Intent intent = new Intent(MyRepairmentReportActivity.this, FaultReportActivity.class);
                intent.putExtra("mTv_ReportID", mTv_ReportID.getText().toString());
                intent.putExtra("mTv_EquipID", mTv_EquipID.getText().toString());
                startActivity(intent);

//                IntentUtil.newIntent(FaultReport_MyActivity.this, FailureReportingEntity.class, maps);
            }
        });

        //长按显示删除
        mLv_DataList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView mTv_ReportID = (TextView) view.findViewById(R.id.mTv_ReportID);
                deleteRow(mTv_ReportID.getText().toString());

                return false;
            }
        });
    }

    @Override
    public void refreshUI() {
        requestNetworkData();
    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        FailureReportingRequest request = new FailureReportingRequest();
        request.setCurrentLastIdx(String.valueOf(mPageIndex * mPageSize));

        SoapUtils.getFailureReportsAsync(MyRepairmentReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                FailureReportingsResult result = new Gson().fromJson(strData, FailureReportingsResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<FailureReportingEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                //判断是否是加载更多
                if (data != null && mIsLoadMore) {
                    //更新数据源
                    mAdapter.loadMore(data);
                    mRefreshLayout.finishLoadMore(true);//设置SmartRefreshLayout加载更多的完成标志
                } else if (data != null) {
                    //设置数据
                    mAdapter = new FailureReportingEntityDataListAdapter(MyRepairmentReportActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                }


            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(MyRepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            Log.e("TAG", "ActivityResult resultCode error");
//            return;
//        }
        mPageIndex = 0;
        mIsLoadMore = false;
        mRefreshLayout.setNoMoreData(false);
        requestNetworkData();
    }

    private void deleteRow(final String RptID) {
        List<String> menuList = new ArrayList<String>();
        menuList.add("删除");
        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        //删除
                        SoapUtils.removeFailureReportingAsync(MyRepairmentReportActivity.this, RptID, new SoapListener() {
                            @Override
                            public void onSuccess(int statusCode, SoapObject object) {
                                if(object != null)
                                {
                                    ToastUtils.showLongToast(MyRepairmentReportActivity.this,"删除成功");

                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String content, Throwable error) {

                            }

                            @Override
                            public void onFailure(int statusCode, SoapFault fault) {

                            }
                        });

                        break;
                    default:
                        break;
                }

            }
        }, menuList);

    }

}
