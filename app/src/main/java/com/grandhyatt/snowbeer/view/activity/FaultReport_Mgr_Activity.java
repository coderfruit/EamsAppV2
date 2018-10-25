package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.FailureReportingEntityDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.result.FailureReportingsResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
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
 * 处理报修
 * Created by ycm on 2018/10/7.
 */

public class FaultReport_Mgr_Activity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

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
    String _EquipID;//传入的设备ID
    String _EquipName;//传入的设备名称
    String _OperateUser;//传入处理人
    String _Status;//传入处理状态

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_report_mgr);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");
        _EquipName = intent.getStringExtra("equipName");
        _OperateUser = intent.getStringExtra("operateUser");
        _Status = intent.getStringExtra("status");
        _Status = "待处理";

        initView();
        bindEvent();
        requestNetworkData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("待处理报修");
        mToolBar.setMenuText("...");
        mToolBar.showMenuButton();
        if(_EquipID != null){
            mToolBar.hideMenuButton();
            if(_EquipName != null) {
                mToolBar.setTitle(_EquipName + "待处理报修");
            }
        }

        List<CorporationEntity> corps = SPUtils.getLastLoginUserCorporations(this);
        if(corps != null){
            if(corps.size() == 1){
                mRL_UserCorp.setVisibility(View.GONE);
            }else{
                CorporationEntity corp = SPUtils.getLastLoginUserCorporation(this);
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
                TextView mTv_Status = (TextView) view.findViewById(R.id.mTv_Status);

                Intent intent = new Intent(FaultReport_Mgr_Activity.this, FaultReportActivity.class);
                intent.putExtra("mTv_ReportID", mTv_ReportID.getText().toString());
                intent.putExtra("mTv_EquipID", mTv_EquipID.getText().toString());
                intent.putExtra("mTv_Status", mTv_Status.getText().toString());
                intent.putExtra("isMgr", "true");//是从待处理报修页面跳转

                startActivityForResult(intent,RESULT_REPORT_COMPLETE_ACTIVITY);
            }
        });

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

        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("待处理的报修");
                list.add("我处理的报修");

                final PopupWindowUtil popupWindow = new PopupWindowUtil(FaultReport_Mgr_Activity.this, list);
                popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        popupWindow.dismiss();
                        switch (position){
                            case 0://待处理
                                _Status = "待处理";

                                requestNetworkData();
                                break;
                            case 1://我处理的
                                _Status = "已处理";
                                _OperateUser = SPUtils.getLastLoginUserName(FaultReport_Mgr_Activity.this);

                                requestNetworkData();
                                break;
                            default:
                                break;
                        }

                    }
                });
                //根据后面的数字 手动调节窗口的宽度
                popupWindow.show(v, 3);
            }
        });
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        FailureReportingRequest request = new FailureReportingRequest();
        request.setCurrentLastIdx(String.valueOf(mPageIndex * mPageSize));

        if(_Status != null){
            request.setStatus(_Status);
        }
        if(_EquipID != null){
            request.setEquipmentID(_EquipID);
        }
        if(_OperateUser != null){
            request.setOperateUser(_OperateUser);
        }

        CorporationEntity corp = null;
        String userCorp = mTv_UserCorp.getText().toString();
        if(userCorp.length() > 0) {
            corp = SPUtils.getLastLoginUserCorporations(FaultReport_Mgr_Activity.this, userCorp);
        }else{
            corp = SPUtils.getLastLoginUserCorporation(FaultReport_Mgr_Activity.this);
        }

        if(corp != null){
            request.setCorpID(corp.getID());
        }

        SoapUtils.getFailureReportsAsync(FaultReport_Mgr_Activity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                FailureReportingsResult result = new Gson().fromJson(strData, FailureReportingsResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err4, result.msg));
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
                    mAdapter = new FailureReportingEntityDataListAdapter(FaultReport_Mgr_Activity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                }


            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(FaultReport_Mgr_Activity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == RESULT_REPORT_COMPLETE_ACTIVITY){
            requestNetworkData();
        }

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

            showSelectDialog(new SelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorporationEntity corp = corpList.get(position);

                    mTv_UserCorp.setText(corp.getCorporationName());
                    //SPUtils.setLastLoginUserCorporation(FaultReport_Mgr_Activity.this, corp);

                    requestNetworkData();
                }
            },corpName);
        }

    }
}
