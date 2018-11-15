package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.Query_Assay_UseInfo_Adapter;
import com.grandhyatt.snowbeer.adapter.Query_Equip_Repair_Adapter;
import com.grandhyatt.snowbeer.entity.EquipmentAssayUseItemEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentAssayUseItemResult;
import com.grandhyatt.snowbeer.network.result.RepairmentBillResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 化学仪器使用记录查询
 * Created by ycm on 2018/10/26.
 */

public class Query_AssayUseInfoActivity extends ActivityBase implements IActivityBase, View.OnClickListener {
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
    private Query_Assay_UseInfo_Adapter mAdapter;

    String _EquipID;//传入的设备id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_equip_repair_info);
        ButterKnife.bind(this);

        initView();
        bindEvent();

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");

        if (_EquipID != null) {  //根据设备ID 获取维修记录
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
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolBar.setTitle("化学仪器使用记录查询");
    }

    @Override
    public void bindEvent() {
        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view1, int position, long id) {

                List<String> menuList = new ArrayList<String>();
                menuList.add("删除");
                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view2, int position, long id) {

                        switch (position) {
                            case 0:
                                TextView mTv_ID = (TextView) view1.findViewById(R.id.mTv_ID);
                                TextView mTv_EquipID = (TextView) view1.findViewById(R.id.mTv_EquipID);

                                String itemID = mTv_ID.getText().toString();
                                String equipID = mTv_EquipID.getText().toString();

                                removeAssayEquipUseInfo(equipID, itemID);
                                break;
                            default:
                                break;
                        }
                    }
                }, menuList);

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

    }

    /**
     * 根据设备ID获取设备维修记录
     * @param equipID
     */
    public void requestNetworkDataByEquip(String equipID) {

        showLogingDialog();
        String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
        SoapUtils.getAssayEquipUseInfo(Query_AssayUseInfoActivity.this, equipID, currentLastIdx, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentAssayUseItemResult result = new Gson().fromJson(strData, EquipmentAssayUseItemResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentAssayUseItemEntity> data = result.getData();
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
                    mAdapter = new Query_Assay_UseInfo_Adapter(Query_AssayUseInfoActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志

                    if(data.size() == 0){
                        ToastUtils.showToast(Query_AssayUseInfoActivity.this,"没有获取到记录！");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 移除化学仪器使用记录
     * @param equipID
     * @param itemID
     */
    private void removeAssayEquipUseInfo(String equipID, final String itemID)
    {
        SoapUtils.removeAssayEquipUseInfo(Query_AssayUseInfoActivity.this, equipID, itemID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();

                if(null == object){
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //------------------------------
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                Result result = new Gson().fromJson(strData, Result.class);
                //校验接口返回代码
                if(result == null)
                {
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                }
                else if(result.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, result.msg);
                    return;
                }
                mAdapter.removeItemByID(itemID);
                ToastUtils.showLongToast(Query_AssayUseInfoActivity.this, "删除成功！");

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(Query_AssayUseInfoActivity.this, "删除异常，请重试!" + error);

            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(Query_AssayUseInfoActivity.this, "删除失败，请重试!" + fault);

            }
        });
    }
}
