package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.Equip_Repair_EntityDataListAdapter;
import com.grandhyatt.snowbeer.adapter.Equip_SpareReplace_EntityDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.SpareInEquipmentResult;
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

public class WarningInfo_EquipSpareReplaceActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    private Equip_SpareReplace_EntityDataListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_info_equip_spare_replace);
        ButterKnife.bind(this);

        initView();
        bindEvent();
        requestNetworkData();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("备件更换提醒");
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
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {
        showLogingDialog();

        CorporationEntity corp = SPUtils.getLastLoginUserCorporation(this);
        if(corp != null){
            String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
            SoapUtils.getSpareReplaceInfo(this, corp.getID(), currentLastIdx, new SoapListener() {
                @Override
                public void onSuccess(int statusCode, SoapObject object) {
                    dismissLoadingDialog();
                    if (object == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err1));
                        return;
                    }
                    //判断接口连接是否成功
                    if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                        ToastUtils.showLongToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err2));
                        return;
                    }
                    //接口返回信息正常
                    String strData = object.getPropertyAsString(0);
                    SpareInEquipmentResult result = new Gson().fromJson(strData, SpareInEquipmentResult.class);
                    //校验接口返回代码
                    if (result == null) {
                        ToastUtils.showLongToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err3));
                        return;
                    } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                        ToastUtils.showLongToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                        return;
                    }
                    List<SpareInEquipmentEntity> data = result.getData();
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
                        mAdapter = new Equip_SpareReplace_EntityDataListAdapter(WarningInfo_EquipSpareReplaceActivity.this, data);
                        mLv_DataList.setAdapter(mAdapter);
                        mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
                    }
                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err5, error));
                }

                @Override
                public void onFailure(int statusCode, SoapFault fault) {
                    dismissLoadingDialog();
                    mRefreshLayout.finishRefresh(false);
                    mRefreshLayout.finishLoadMore(false);
                    ToastUtils.showToast(WarningInfo_EquipSpareReplaceActivity.this, getString(R.string.submit_soap_result_err4, fault));
                }
            });
        }
    }
}