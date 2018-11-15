package com.grandhyatt.snowbeer.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.FailureReportingEntityDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.result.FailureReportingsResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.SPUtils;
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
 * 我的报修
 * Created by ycm on 2018/8/28.
 */

public class FaultReport_MyActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.mTv_Status)
    TextView mTv_Status;


    @BindView(R.id.mTv_UserCorp)
    TextView mTv_UserCorp;
    @BindView(R.id.mRL_UserCorp)
    RelativeLayout mRL_UserCorp;

    //当前页码
    private int mPageIndex = 0;
    //页面数据数量
    private int mPageSize = 10;
    //加载类型
    private boolean mIsLoadMore = false;

    private FailureReportingEntityDataListAdapter mAdapter;

    public static final int RESULT_REPORT_COMPLETE_ACTIVITY = 10001;

    /**
     * 故障处理状态
     */
    String[] _FaultStatusArr;

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
        if(v.getId() == R.id.mTv_Status){
            final List<String> list = new ArrayList<String>();
            if (_FaultStatusArr != null && _FaultStatusArr.length > 0) {
                for (String item : _FaultStatusArr) {
                    list.add(item);
                }
            } else {
                list.add("待审核");
                list.add("待处理");
                list.add("已处理");
                list.add("已关闭");
            }
            list.add(0,"全部");


            showSelectDialog(new SelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mTv_Status.setText(list.get(position).toString());

                    requestNetworkData();
                }
            }, list);
        }
    }

    @Override
    public void initView() {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolBar.setTitle("我的报修");

        List<CorporationEntity> corps = SPUtils.getLastLoginUserCorporations(this);
        if(corps != null){
            if(corps.size() == 1){
                mRL_UserCorp.setVisibility(View.GONE);
            }else{
                CorporationEntity corp = SPUtils.getFirstLastLoginUserCorporations(this);
                if(corp != null)
                {
                    mTv_UserCorp.setText(corp.getCorporationName());
                }
                mRL_UserCorp.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public void bindEvent() {

        mRL_UserCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCorp();
            }
        });

        mTv_UserCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCorp();
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

                Intent intent = new Intent(FaultReport_MyActivity.this, FaultReportActivity.class);
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

        mTv_Status.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {
        requestNetworkData();
    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        FailureReportingRequest request = new FailureReportingRequest();
        if(mTv_Status.getText().toString().equals("状态") || mTv_Status.getText().toString().equals("全部")){
            request.setStatus("");
        }else {
            request.setStatus(mTv_Status.getText().toString());
        }
//        CorporationEntity corp = SPUtils.getFirstLastLoginUserCorporations(this);
//        if(corp != null){
//            request.setCorpID(corp.getID());
//        }

        CorporationEntity corp = null;
        String userCorp = mTv_UserCorp.getText().toString();
        if(userCorp.length() > 0) {
            corp = SPUtils.getLastLoginUserCorporations(FaultReport_MyActivity.this, userCorp);
        }else{
            corp = SPUtils.getFirstLastLoginUserCorporations(FaultReport_MyActivity.this);
        }
        request.setCorpID(corp.getID());

        String userName = SPUtils.getLastLoginUserName(this);
        request.setReportUser(userName);
        request.setCurrentLastIdx(String.valueOf(mPageIndex * mPageSize));

        SoapUtils.getFailureReportsAsync(FaultReport_MyActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                FailureReportingsResult result = new Gson().fromJson(strData, FailureReportingsResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
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
                    mAdapter = new FailureReportingEntityDataListAdapter(FaultReport_MyActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                }


            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(FaultReport_MyActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

        //获取故障状态
        SoapListener callbackFailureReportingStatus = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, "1获取故障状态数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, "2获取故状态述数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, "3获取故障状态数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReport_MyActivity.this, "4获取故障状态数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _FaultStatusArr = value.split("\\|");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(FaultReport_MyActivity.this, "0获取故障状态数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(FaultReport_MyActivity.this, "0获取故障状态数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(FaultReport_MyActivity.this, Consts.EnumTextDictonay.FailureReportingStatus, callbackFailureReportingStatus);
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
                        SoapUtils.removeFailureReportingAsync(FaultReport_MyActivity.this, RptID, new SoapListener() {
                            @Override
                            public void onSuccess(int statusCode, SoapObject object) {
                                if(object != null)
                                {
                                    ToastUtils.showLongToast(FaultReport_MyActivity.this,"删除成功");
                                    requestNetworkData();
                                }
                            }

                            @Override
                            public void onFailure(int statusCode, String content, Throwable error) {
                                ToastUtils.showLongToast(FaultReport_MyActivity.this,"删除异常，请重试");
                            }

                            @Override
                            public void onFailure(int statusCode, SoapFault fault) {
                                ToastUtils.showLongToast(FaultReport_MyActivity.this,"删除失败，请重试");
                            }
                        });

                        break;
                    default:
                        break;
                }

            }
        }, menuList);

    }

    /**
     * 显示用户归属组织机构
     */
    private void showUserCorp()
    {
        List<String> corpName = new ArrayList<>();
        final List<CorporationEntity> corpList = SPUtils.getLastLoginUserCorporations(this);
        if(corpList != null && corpList.size() > 1){

            for (CorporationEntity item : corpList) {
                corpName.add(item.getCorporationName());
            }

            showSelectDialog("组织机构列表", corpName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    CorporationEntity corp = corpList.get(which);
                    mTv_UserCorp.setText(corp.getCorporationName());
                    requestNetworkData();
                }
            });
        }

    }
}
