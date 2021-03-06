package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.os.Bundle;
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
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipMgrRepairSpareDataListAdapter;
import com.grandhyatt.snowbeer.adapter.EquipRepairExSpareViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.DepartmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentUseSpareResult;
import com.grandhyatt.snowbeer.network.result.SpareInEquipmentResult;
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

public class EquipMgrRepairExSpareCheckActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    TextView mTv_SpareDept;//用户选择的部门

    @BindView(R.id.mEt_SpareCond)
    EditText mEt_SpareCond;//用户录入的备件条件

    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;
    @BindView(R.id.mTv_AllCnt)
    TextView mTv_AllCnt;
    @BindView(R.id.mTv_CheckCnt)
    TextView mTv_CheckCnt;
    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;
    @BindView(R.id.mBt_Search)
    Button mBt_Search;          //搜索

    //设备对应的部门信息
    List<DepartmentEntity> _DepartmentList;
    //部门名称列表
    List<String> _DeptNamelist = new ArrayList<>();
    //选中的部门对象
    DepartmentEntity _SelectedDept = null;
    //选中的备件信息
    String _SpareCond = null;

    //设备ID
    String _EquipID;
    //用户扫码获取的设备
    EquipmentEntity _Equipment;

    SpareInEquipmentDataListAdapter adapter_Spare = null;//备件列表适配器
    int _CheckCnt = 0;//用户选中的行数
    ArrayList<String> _CheckIDList = new ArrayList<>();//用户选择的数据行ID
    ArrayList<SpareInEquipmentEntity> _CheckEntityList = new ArrayList<>();//用户选择的数据行对象


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mgr_repairex_spare_check);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("_EquipmentID");

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

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolBar.setTitle("设备外委维修-备件选择");
        getEquipmentInfo(_EquipID);
        _SpareCond = mEt_SpareCond.getText().toString();

        getSparesInfo(_EquipID, _SelectedDept, _SpareCond);
    }

    @Override
    public void bindEvent() {

        mBt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogingDialog();

                _SpareCond = mEt_SpareCond.getText().toString();

                getSparesInfo(_EquipID, _SelectedDept, _SpareCond);
            }
        });

        mTv_SpareDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String name = _DeptNamelist.get(position);
                        _SelectedDept = _DepartmentList.get(position);
                        mTv_SpareDept.setText(name);

                        showLogingDialog();

                        _SpareCond = mEt_SpareCond.getText().toString();

                        getSparesInfo(_EquipID, _SelectedDept, _SpareCond);

                    }
                }, _DeptNamelist);
            }
        });


        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SpareInEquipmentEntity spareEntity = null;

                CheckBox ckb = view.findViewById(R.id.mCkb_ID);
                TextView mTv_ID = view.findViewById(R.id.mTv_ID);
                String checkedID = mTv_ID.getText().toString();

                boolean ckbValue = ckb.isChecked();
                if (ckbValue) {  //取消选中
                    if (adapter_Spare != null) {//适配器有值
                        if (_CheckCnt > 0) {
                            _CheckCnt--;
                            if (_CheckIDList.contains(checkedID)) {
                                _CheckIDList.remove(checkedID);
                            }
                            mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                            spareEntity = (SpareInEquipmentEntity) adapter_Spare.getItem(position);
                            spareEntity.setIsCheck(false);
                            adapter_Spare.notifyDataSetChanged();

                            if (spareEntity != null) {
                                if (_CheckEntityList.contains(spareEntity)) {
                                    _CheckEntityList.remove(spareEntity);
                                }
                            }
                        }
                    }
                } else {        //选中
                    if (adapter_Spare != null) {//适配器有值

                        _CheckCnt++;
                        if (!_CheckIDList.contains(checkedID)) {
                            _CheckIDList.add(checkedID);
                        }
                        mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                        spareEntity = (SpareInEquipmentEntity) adapter_Spare.getItem(position);
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

                if (_CheckIDList == null || _CheckIDList.size() == 0) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, "请选择使用的备件（单选或多选）");
                    return;
                }

                boolean isChk = false;
                // 部门校验
//                for (SpareInEquipmentEntity item : _CheckEntityList) {
//                    String spareDeptCode = item.getDeptCode().substring(0,4);
//                    String equipDeptCode = _Equipment.getDepartmentCode().substring(0,4);
//
//                    if (!spareDeptCode.equals(equipDeptCode))
//                    {
//                        isChk = true;
//                    }
//                }
                if (isChk) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, "不允许备件与设备跨大部门使用!");
                    return;
                }

                //数据是使用Intent返回
                Intent intent = new Intent();

                //把返回数据存入Intent
                intent.putStringArrayListExtra("_CheckIDList", _CheckIDList);
                intent.putExtra("_CheckEntityList", _CheckEntityList);


                //设置返回数据
                EquipMgrRepairExSpareCheckActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                EquipMgrRepairExSpareCheckActivity.this.finish();

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
     * 根据设备ID 获取设备信息
     *
     * @param equipmentID
     */
    private void getEquipmentInfo(String equipmentID) {

        showLogingDialog();

        SoapUtils.getEquipmentByIDAsync(EquipMgrRepairExSpareCheckActivity.this, equipmentID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                EquipmentEntity data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, "没有获取到该设备信息");
                    return;
                } else {
                    _Equipment = data;

                    mTv_EquipDeptID.setText(data.getDepartmentID());
                    mTv_EquipID.setText(data.getID());
                    mTv_EquipCorpID.setText(data.getCorporationID());

                    mTv_EquipDept.setText(data.getDepartmentName().toString());
                    mTv_EquipCode.setText(data.getEquipmentCode());
                    mTv_EquipName.setText(data.getEquipmentName());

                    mTv_SpareDeptID.setText(String.valueOf(data.getDepartmentID()));
                    mTv_SpareDept.setText(data.getDepartmentName().toString());

                    //获取设备对应部门信息
                    //    getDepartmentInfo(String.valueOf(data.getCorporationID()));

                    showLogingDialog();

                    _SelectedDept = new DepartmentEntity();
                    _SelectedDept.setID(data.getDepartmentID());

                    //获取设备可用的备件库存信息
                    getSparesInfo(data.getID(), _SelectedDept, "");
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 获取组织机构下的部门信息
     *
     * @param corporationID
     */
    private void getDepartmentInfo(String corporationID) {
        SoapUtils.getDepartment(EquipMgrRepairExSpareCheckActivity.this, corporationID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _DepartmentList = data;

                    bindDepart(_DepartmentList);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

    }

    /**
     * 根据设备、部门、备件信息获取可用备件库存信息
     *
     * @param equipID
     * @param spareContent
     */
    private void getSparesInfo(String equipID, DepartmentEntity dept, String spareContent) {
        String deptID = "";
        if (dept != null) {
            deptID = dept.getID();
        }
        if (spareContent == null) {
            spareContent = "";
        }
        //获取设备关联备件信息
        SoapUtils.getEquipmentSparesDeptAsync(EquipMgrRepairExSpareCheckActivity.this, equipID, deptID, spareContent, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();

                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                SpareInEquipmentResult result = new Gson().fromJson(strData, SpareInEquipmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<SpareInEquipmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, "该设备没有备件信息");
                } else {
                    mTv_AllCnt.setText("共" + data.size() + "条/");
                    adapter_Spare = new SpareInEquipmentDataListAdapter(EquipMgrRepairExSpareCheckActivity.this, data);
                    mLv_DataList.setAdapter(adapter_Spare);

                    SpareInEquipmentEntity entity = data.get(0);
                    if (entity != null) {
                        String equipmentCode = entity.getEquipmentCode();
                        String equipmentName = entity.getEquipmentName();

                        mTv_EquipCode.setText(equipmentCode);
                        mTv_EquipName.setText(equipmentName);
                    }
                }

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrRepairExSpareCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

    }

    /**
     * 部门下拉列表数据源赋值
     *
     * @param dptList
     */
    private void bindDepart(List<DepartmentEntity> dptList) {

        for (DepartmentEntity item : dptList) {
            String value = item.getDepartmentName();// + "-" + item.getDepartmentCode();
            _DeptNamelist.add(value);
        }
    }


}
