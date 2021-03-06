package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.ListView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.Equip_Inspect_EntityDataListAdapter;
import com.grandhyatt.snowbeer.adapter.Query_Equip_Inspect_Adapter;
import com.grandhyatt.snowbeer.adapter.Query_Equip_Mainten_Adapter;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceBillEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.InspectBillsResult;
import com.grandhyatt.snowbeer.network.result.MaintenanceBillsResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
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
 * 设备检验记录查询
 * Created by ycm on 2018/10/19.
 */

public class Query_EquipInspectionInfoActivity extends ActivityBase implements IActivityBase, View.OnClickListener {
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
    private Query_Equip_Inspect_Adapter mAdapter;

    String _EquipID;//传入的设备id
    String _CorpID; //传入的组织机构ID
    String _DeptID; //传入的部门ID
    String _TypeID;//传入的设备类型ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_equip_inspection_info);
        ButterKnife.bind(this);

        initView();
        bindEvent();

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");
        _CorpID = intent.getStringExtra("corpID");
        _DeptID = intent.getStringExtra("deptID");
        _TypeID = intent.getStringExtra("typeID");

        requestNetworkDataByEquip(_CorpID, _EquipID,_DeptID,_TypeID);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolBar.setTitle("设备检验记录查询");
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

                requestNetworkDataByEquip(_CorpID, _EquipID,_DeptID,_TypeID);

            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                mIsLoadMore = true;

                requestNetworkDataByEquip(_CorpID, _EquipID,_DeptID,_TypeID);

            }
        });
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

    }

    public void requestNetworkDataByEquip(String corpID, String equipID, String deptID,String typeID) {

        showLogingDialog();
        String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
        SoapUtils.getEquipInspectionBills(Query_EquipInspectionInfoActivity.this, corpID, equipID,deptID, typeID,   currentLastIdx, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                InspectBillsResult result = new Gson().fromJson(strData, InspectBillsResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<InspectBillEntity> data = result.getData();
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
                    mAdapter = new Query_Equip_Inspect_Adapter(Query_EquipInspectionInfoActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志

                    if(data.size() == 0){
                        ToastUtils.showToast(Query_EquipInspectionInfoActivity.this,"没有获取到记录！");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_EquipInspectionInfoActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }
}
