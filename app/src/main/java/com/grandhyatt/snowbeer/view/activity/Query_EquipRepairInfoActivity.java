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
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.Equip_Inspect_EntityDataListAdapter;
import com.grandhyatt.snowbeer.adapter.Query_Equip_Repair_Adapter;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillItemEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.InspectPlanResult;
import com.grandhyatt.snowbeer.network.result.RepairmentBillItemResult;
import com.grandhyatt.snowbeer.network.result.RepairmentBillResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
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
 * 设备维修记录查询
 * Created by ycm on 2018/10/19.
 */

public class Query_EquipRepairInfoActivity extends ActivityBase implements IActivityBase, View.OnClickListener {
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
    private Query_Equip_Repair_Adapter mAdapter;

    String _EquipID;//传入的设备id
    String _CorpID; //传入的组织机构ID

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_equip_repair_info);
        ButterKnife.bind(this);

        initView();
        bindEvent();

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");
        _CorpID = intent.getStringExtra("corpID");

        requestNetworkDataByEquip(_CorpID, _EquipID);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("设备维修记录查询");
    }

    @Override
    public void bindEvent() {
        //显示备件列表
        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //获取维修单ID
                TextView mTv_ID = (TextView) view.findViewById(R.id.mTv_ID);
                String itemID = mTv_ID.getText().toString();

                //根据维修单获取维修单中的备件列表
                getRepairmentBillItems(itemID, view);
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = 0;
                mIsLoadMore = false;
                mRefreshLayout.setNoMoreData(false);

                requestNetworkDataByEquip(_CorpID, _EquipID);

            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                mIsLoadMore = true;

                requestNetworkDataByEquip(_CorpID, _EquipID);

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
     *
     * @param equipID
     */
    public void requestNetworkDataByEquip(String corpID, String equipID) {

        showLogingDialog();
        String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
        SoapUtils.getEquipRepairBills(Query_EquipRepairInfoActivity.this, corpID, equipID, currentLastIdx, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                RepairmentBillResult result = new Gson().fromJson(strData, RepairmentBillResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<RepairmentBillEntity> data = result.getData();
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
                    mAdapter = new Query_Equip_Repair_Adapter(Query_EquipRepairInfoActivity.this, data);
                    mLv_DataList.setAdapter(mAdapter);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志

                    if(data.size() == 0){
                        ToastUtils.showToast(Query_EquipRepairInfoActivity.this,"没有获取到记录！");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /***
     * 获取维修单明细
     * @param billID
     */
    private void getRepairmentBillItems(String billID, final View view) {
        showLogingDialog();
        SoapUtils.getRepairmentBillItems(Query_EquipRepairInfoActivity.this, billID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                RepairmentBillItemResult result = new Gson().fromJson(strData, RepairmentBillItemResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, "获取数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(Query_EquipRepairInfoActivity.this, "获取数据失败" + statusCode + result.msg);
                    return;
                }
                List<RepairmentBillItemEntity> data = result.getData();
                ShowPopWindow(data, view);
                if(data.size() == 0){
                    ToastUtils.showToast(Query_EquipRepairInfoActivity.this,"没有获取到记录！");
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(Query_EquipRepairInfoActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 弹出窗口显示维修细表
     *
     * @param data
     */
    private void ShowPopWindow(List<RepairmentBillItemEntity> data, View view) {
        List<String> list = new ArrayList<>();
        for (RepairmentBillItemEntity item : data) {
            list.add(item.getSpareName() + "(" + item.getSpareStander() + ")"  + " x " + item.getCount() + item.getSpareUnit());
        }
        showListPopupWindow(Query_EquipRepairInfoActivity.this, view, list, null);

//        final PopupWindowUtil popupWindow = new PopupWindowUtil(Query_EquipRepairInfoActivity.this, list);
//        popupWindow.show(view, 3);  //根据后面的数字 手动调节窗口的宽度
    }

}
