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
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.Equip_Maintence_EntityDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.MaintenPlanResult;
import com.grandhyatt.snowbeer.network.result.RepairmentPlanResult;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ycm on 2018/9/28.
 */

public class WarningInfo_EquipMaintenActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    private Equip_Maintence_EntityDataListAdapter mAdapter;

    String _EquipID;//传入的设备ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_info_equip_mainten);
        ButterKnife.bind(this);

        initView();
        bindEvent();

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");

        if (_EquipID != null) {  //根据设备ID 获取预警信息
            requestNetworkDataByEquip(_EquipID);
        }else{                   //根据组织机构获取预警信息
            requestNetworkData();
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("设备保养提醒");
    }

    @Override
    public void bindEvent() {
        //列表明细单击事件
        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView mTv_ReportID = (TextView) view.findViewById(R.id.mTv_ID);
                TextView mTv_EquipID = (TextView) view.findViewById(R.id.mTv_EquipID);

                //保养-保养计划  type = 2    mTv_EquipID=设备id  mTv_ReportID = 保养计划ID，
                Intent intent = new Intent(WarningInfo_EquipMaintenActivity.this, RepairmentReportActivity.class);
                intent.putExtra("type","2");
                intent.putExtra("mTv_EquipID", mTv_EquipID.getText());
                intent.putExtra("mTv_ReportID", mTv_ReportID.getText());
                startActivityForResult(intent, Consts.MAINTEN_OPERATE_AFTER);
            }
        });
        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = 0;
                mIsLoadMore = false;
                mRefreshLayout.setNoMoreData(false);
                if (_EquipID != null) {  //根据设备ID 获取预警信息
                    requestNetworkDataByEquip(_EquipID);
                }else{                   //根据组织机构获取预警信息
                    requestNetworkData();
                }
            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                mIsLoadMore = true;
                if (_EquipID != null) {  //根据设备ID 获取预警信息
                    requestNetworkDataByEquip(_EquipID);
                }else{                   //根据组织机构获取预警信息
                    requestNetworkData();
                }
            }
        });
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        CorporationEntity corp = SPUtils.getLastLoginUserCorporation(this);
        if(corp != null) {
            String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
            SoapUtils.getMaintenancePlan(this, corp.getID(), currentLastIdx, new SoapListener() {
                @Override
                public void onSuccess(int statusCode, SoapObject object) {
                    dismissLoadingDialog();
                    if (object == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err1));
                        return;
                    }
                    //判断接口连接是否成功
                    if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err2));
                        return;
                    }
                    //接口返回信息正常
                    String strData = object.getPropertyAsString(0);
                    MaintenPlanResult result = new Gson().fromJson(strData, MaintenPlanResult.class);
                    //校验接口返回代码
                    if (result == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err3));
                        return;
                    } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                        return;
                    }
                    List<MaintenancePlanEntity> data = result.getData();
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
                        mAdapter = new Equip_Maintence_EntityDataListAdapter(WarningInfo_EquipMaintenActivity.this, data);
                        mLv_DataList.setAdapter(mAdapter);
                        mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                    }
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err5, error));
                }

                @Override
                public void onFailure(int statusCode, SoapFault fault) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err4, fault));
                }
            });
        }
    }

    /**
     * 根据设备获取设备预警信息
     */
    public void requestNetworkDataByEquip(String equipID) {
        showLogingDialog();
        if(equipID != null) {
            String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
            SoapUtils.getMaintenancePlan_Equip(this, equipID, currentLastIdx, new SoapListener() {
                @Override
                public void onSuccess(int statusCode, SoapObject object) {
                    dismissLoadingDialog();
                    if (object == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err1));
                        return;
                    }
                    //判断接口连接是否成功
                    if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err2));
                        return;
                    }
                    //接口返回信息正常
                    String strData = object.getPropertyAsString(0);
                    MaintenPlanResult result = new Gson().fromJson(strData, MaintenPlanResult.class);
                    //校验接口返回代码
                    if (result == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err3));
                        return;
                    } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                        ToastUtils.showLongToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                        return;
                    }
                    List<MaintenancePlanEntity> data = result.getData();
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
                        mAdapter = new Equip_Maintence_EntityDataListAdapter(WarningInfo_EquipMaintenActivity.this, data);
                        mLv_DataList.setAdapter(mAdapter);
                        mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                    }
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err5, error));
                }

                @Override
                public void onFailure(int statusCode, SoapFault fault) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipMaintenActivity.this, getString(R.string.submit_soap_result_err4, fault));
                }
            });
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {

            case  Consts.MAINTEN_OPERATE_AFTER://维修之后
                if (_EquipID != null) {  //根据设备ID 获取预警信息
                    requestNetworkDataByEquip(_EquipID);
                }else{                   //根据组织机构获取预警信息
                    requestNetworkData();
                }
                break;
            default:
                break;
        }
    }

}
