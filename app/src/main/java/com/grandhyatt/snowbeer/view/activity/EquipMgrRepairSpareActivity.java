package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipMgrRepairSpareDataListAdapter;
import com.grandhyatt.snowbeer.adapter.EquipRepairSpareViewDataListAdapter;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.DepartmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentUseSpareResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ycm on 2018/9/20.
 * 设备维修备件选择
 */

public class EquipMgrRepairSpareActivity extends com.grandhyatt.snowbeer.view.activity.ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mTv_EquipCorpID)
    TextView mTv_EquipCorpID;
    @BindView(R.id.mTv_EquipDeptID)
    TextView mTv_EquipDeptID;
    @BindView(R.id.mTv_EquipID)
    TextView mTv_EquipID;
    @BindView(R.id.mTv_EquipDept)
    TextView mTv_EquipDept;
    @BindView(R.id.mTv_EquipName)
    TextView mTv_EquipName;
    @BindView(R.id.mTv_EquipCode)
    TextView mTv_EquipCode;
    @BindView(R.id.mTv_SpareDeptID)
    TextView mTv_SpareDeptID;
    @BindView(R.id.mTv_SpareDept)
    TextView mTv_SpareDept;
    @BindView(R.id.mTv_SpareCond)
    TextView mTv_SpareCond;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;
    @BindView(R.id.mTv_AllCnt)
    TextView mTv_AllCnt;
    @BindView(R.id.mTv_CheckCnt)
    TextView mTv_CheckCnt;
    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;

    //设备对应的部门信息
    List<DepartmentEntity> _DepartmentList;
    //设备ID
    String _EquipID;
    EquipMgrRepairSpareDataListAdapter adapter_Spare = null;//备件列表适配器
    int _CheckCnt= 0;//用户选中的行数
    ArrayList<String> _CheckIDList = new ArrayList<>();//用户选择的数据行ID
    ArrayList<EquipmentUseSpareEntity> _CheckEntityList = new ArrayList<>();//用户选择的数据行对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mgr_repair_spare);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("_EquipmentID");

        initView();

        bindEvent();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        getEquipmentInfo(_EquipID);
    }

    @Override
    public void bindEvent() {

        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipmentUseSpareEntity spareEntity = null;

                CheckBox ckb = view.findViewById(R.id.mCkb_IsCheck);
                TextView mTv_ID = view.findViewById(R.id.mTv_ID);
                String checkedID = mTv_ID.getText().toString();

                boolean ckbValue = ckb.isChecked();
                if(ckbValue) {  //取消选中
                    if (adapter_Spare != null) {//适配器有值
                        if (_CheckCnt > 0) {
                            _CheckCnt--;
                            if (_CheckIDList.contains(checkedID)) {
                                _CheckIDList.remove(checkedID);
                            }
                            mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                            spareEntity = (EquipmentUseSpareEntity) adapter_Spare.getItem(position);
                            spareEntity.setIsCheck(false);
                            adapter_Spare.notifyDataSetChanged();

                            if (spareEntity != null) {
                                if (_CheckEntityList.contains(spareEntity)) {
                                    _CheckEntityList.remove(spareEntity);
                                }
                            }
                        }
                    }
                }
                else {        //选中
                    if (adapter_Spare != null) {//适配器有值

                        _CheckCnt++;
                        if (!_CheckIDList.contains(checkedID)) {
                            _CheckIDList.add(checkedID);
                        }
                        mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                        spareEntity = (EquipmentUseSpareEntity) adapter_Spare.getItem(position);
                        spareEntity.setIsCheck(true);
                        adapter_Spare.notifyDataSetChanged();
                        if (spareEntity != null) {
                            if (!_CheckEntityList.contains(spareEntity)) {
                                _CheckEntityList.add(spareEntity);
                            }
                        }
                    }
                }


            }
        });


        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();

//                    View vw = null;
//                    for (int i = 0; i < mLv_DataList.getChildCount(); i++) {
//                        vw = mLv_DataList.getChildAt(i);
//                        CheckBox checkb = (CheckBox) vw.findViewById(R.id.mCkb_ID);
//                        if (checkb.isChecked()) {
//                            EquipmentUseSpareEntity rpEntity = (EquipmentUseSpareEntity) mLv_DataList.getAdapter().getItem(i);
//                            _CheckEntityList.add(rpEntity);
//                        }
//                    }


                //把返回数据存入Intent
                intent.putStringArrayListExtra("_CheckIDList", _CheckIDList);
                intent.putExtra("_CheckEntityList", _CheckEntityList);


                //设置返回数据
                EquipMgrRepairSpareActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                EquipMgrRepairSpareActivity.this.finish();

            }
        });

    }



    /**
     * 根据设备ID 获取设备信息
     * @param equipmentID
     */
    private void getEquipmentInfo(String equipmentID) {

        showLogingDialog();

        SoapUtils.getEquipmentByIDAsync(EquipMgrRepairSpareActivity.this, equipmentID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                EquipmentEntity data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    ToastUtils.showToast(EquipMgrRepairSpareActivity.this,"没有获取到该设备信息");
                    return;
                }
                else {
                    mTv_EquipDeptID.setText(data.getDepartmentID());
                    mTv_EquipID.setText(data.getID());
                    mTv_EquipCorpID.setText(data.getCorporationID());

                    mTv_EquipDept.setText(data.getDepartmentName().toString());
                    mTv_EquipCode.setText(data.getEquipmentCode());
                    mTv_EquipName.setText(data.getEquipmentName());

                    mTv_SpareDeptID.setText(String.valueOf(data.getDepartmentID()));
                    mTv_SpareDept.setText(data.getDepartmentName().toString());

                    //获取设备对应部门信息
                    getDepartmentInfo(String.valueOf(data.getCorporationID()));

                    //获取设备可用的备件库存信息
                    getSparesInfo(data.getID(),String.valueOf(data.getDepartmentID()),"");
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 获取组织机构下的部门信息
     * @param corporationID
     */
    private  void getDepartmentInfo(String corporationID)
    {
        SoapUtils.getDepartment(EquipMgrRepairSpareActivity.this, corporationID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipMgrRepairSpareActivity.this,"没有获取到部门信息");
                    return;
                }
                else {
                    _DepartmentList = data;
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

    }

    /**
     * 根据设备、部门、备件信息获取可用备件库存信息
     * @param equipID
     * @param deptID
     * @param spareContent
     */
    private void getSparesInfo(String equipID, String deptID, String spareContent) {
        SoapUtils.getEquipmentSparesStoreInfo(EquipMgrRepairSpareActivity.this, equipID, deptID, spareContent, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentUseSpareResult result = new Gson().fromJson(strData, EquipmentUseSpareResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentUseSpareEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null && data.size() == 0) {
                    ToastUtils.showToast(EquipMgrRepairSpareActivity.this,"没有获取到该设备可用的备件信息");
                    return;
                }
                else {

                    int cnt = data.size();
                    mTv_AllCnt.setText( "共" + String.valueOf(cnt) + "条/");

                    adapter_Spare = new EquipMgrRepairSpareDataListAdapter(EquipMgrRepairSpareActivity.this ,data);
                    mLv_DataList.setAdapter(adapter_Spare);

                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairSpareActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }


    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

    }

}
