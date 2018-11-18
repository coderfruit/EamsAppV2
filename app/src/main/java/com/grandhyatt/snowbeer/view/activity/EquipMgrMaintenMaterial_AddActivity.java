package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipMgrMaintenMaterialDataListAdapter;
import com.grandhyatt.snowbeer.adapter.Query_Equip_Inspect_Adapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentMaterialEntity;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentMaterialResult;
import com.grandhyatt.snowbeer.network.result.InspectBillsResult;
import com.grandhyatt.snowbeer.network.result.SpareInEquipmentResult;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备保养用机物料添加页面
 * Created by ycm on 2018/11/14.
 */

public class EquipMgrMaintenMaterial_AddActivity extends ActivityBase implements IActivityBase, View.OnClickListener{

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;


    @BindView(R.id.mTv_EquipCorp)
    TextView mTv_EquipCorp;
    @BindView(R.id.mTv_EquipName)
    TextView mTv_EquipName;
    @BindView(R.id.mTv_EquipCode)
    TextView mTv_EquipCode;
    @BindView(R.id.mEt_SpareCond)
    EditText mEt_SpareCond;//用户录入的备件条件

    @BindView(R.id.mTv_AllCnt)
    TextView mTv_AllCnt;
    @BindView(R.id.mTv_CheckCnt)
    TextView mTv_CheckCnt;
    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;
    @BindView(R.id.mBt_Search)
    Button mBt_Search;          //搜索
    //当前页码
    private int mPageIndex = 0;
    //页面数据数量
    private int mPageSize = 10;
    //加载类型
    private boolean mIsLoadMore = false;
    int _CheckCnt = -1;//用户选中的行数
    int _checkCount = 0;//选择记录数
    String _EquipID;//传入的设备ID
    String _EquipCode;//传入的设备Cpde
    String _EquipName;//传入的设备Name
    String _CorpID; //传入的组织机构ID
    String _CorpName; //传入的组织机构ID
    //选中的备件信息
    String _SpareCond = null;
    EquipMgrMaintenMaterialDataListAdapter adapter_MM=null;
    ArrayList<EquipmentMaterialEntity> _CheckEntityList = new ArrayList<>();//用户选择的数据行对象
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mgr_mainten_material_add);

        ButterKnife.bind(this);


        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("equipID");
        _EquipCode = intent.getStringExtra("equipCode");
        _EquipName = intent.getStringExtra("equipName");
        _CorpID = intent.getStringExtra("corpID");
        _CorpName = intent.getStringExtra("corpName");
        initView();
        bindEvent();
        mEt_SpareCond.setClickable(false);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mToolBar.setTitle("设备保养润滑-添加物资");
        _SpareCond = mEt_SpareCond.getText().toString().trim();
        mLv_DataList.setVisibility(View.GONE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void bindEvent() {
        mBt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogingDialog();

                _SpareCond = mEt_SpareCond.getText().toString().trim();
                requestNetworkDataByMaterial(_CorpID,_SpareCond);
            }
        });
        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_CheckEntityList == null || _CheckEntityList.size() == 0) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterial_AddActivity.this, "请选择要添加的物资（单选或多选）");
                    return;
                }


                //数据是使用Intent返回
                Intent intent = new Intent();

                //把返回数据存入Intent
                intent.putExtra("_IsOk", 1);
                //设置返回数据
                EquipMgrMaintenMaterial_AddActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                EquipMgrMaintenMaterial_AddActivity.this.finish();

            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mPageIndex = 0;
                mIsLoadMore = false;
                mRefreshLayout.setNoMoreData(false);
                requestNetworkDataByMaterial(_CorpID,_SpareCond);

            }
        });
        /*****************************************************************************************/
        //上拉加载更多
        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                mPageIndex++;
                mIsLoadMore = true;
                requestNetworkDataByMaterial(_CorpID, _SpareCond);

            }
        });

        //选择事件
        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                EquipmentMaterialEntity spareEntity = null;

                CheckBox ckb = view.findViewById(R.id.mCkb_IsCheck);


                boolean ckbValue = ckb.isChecked();
                if (ckbValue) {  //取消选中
                    if (adapter_MM != null) {//适配器有值
                        if (_CheckCnt > 0) {
                            _CheckCnt--;

                            mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                            spareEntity = (EquipmentMaterialEntity) adapter_MM.getItem(position);
                            spareEntity.setIsCheck(false);
                            adapter_MM.notifyDataSetChanged();

                            if (spareEntity != null) {
                                if (_CheckEntityList.contains(spareEntity)) {
                                    _CheckEntityList.remove(spareEntity);
                                }
                            }
                        }
                    }
                } else {        //选中
                    if (adapter_MM != null) {//适配器有值

                        _CheckCnt++;

                        mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                        spareEntity = (EquipmentMaterialEntity) adapter_MM.getItem(position);
                        spareEntity.setIsCheck(true);
                        adapter_MM.notifyDataSetChanged();
                        if (spareEntity != null) {
                            if (!_CheckEntityList.contains(spareEntity)) {
                                _CheckEntityList.add(spareEntity);
                            }
                        }
                    }
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

    public void requestNetworkDataByMaterial(String corpID,String cond) {

        showLogingDialog();

        String currentLastIdx = String.valueOf(mPageIndex * mPageSize);
        SoapUtils.getMaterialCorpInfo(EquipMgrMaintenMaterial_AddActivity.this, corpID,cond, currentLastIdx, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentMaterialResult result = new Gson().fromJson(strData, EquipmentMaterialResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentMaterialEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    mRefreshLayout.finishLoadMoreWithNoMoreData();
                }
                //判断是否是加载更多
                if (data != null && mIsLoadMore) {
                    //更新数据源
                    adapter_MM.loadMore(data);
                    mRefreshLayout.finishLoadMore(true);//设置SmartRefreshLayout加载更多的完成标志
                } else if (data != null) {
                    //设置数据
                    adapter_MM = new EquipMgrMaintenMaterialDataListAdapter(EquipMgrMaintenMaterial_AddActivity.this, data);
                    mLv_DataList.setAdapter(adapter_MM);
                    mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志

                    if(data.size() == 0){
                        ToastUtils.showToast(EquipMgrMaintenMaterial_AddActivity.this,"没有获取到记录！");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mRefreshLayout.finishRefresh(false);
                mRefreshLayout.finishLoadMore(false);
                ToastUtils.showToast(EquipMgrMaintenMaterial_AddActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }
}


