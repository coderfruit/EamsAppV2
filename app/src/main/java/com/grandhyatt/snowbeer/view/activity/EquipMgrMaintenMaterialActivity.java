package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipMgrMaintenMaterialDataListAdapter;
import com.grandhyatt.snowbeer.adapter.EquipMgrRepairSpareDataListAdapter;
import com.grandhyatt.snowbeer.adapter.MaintenancePlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentMaterialEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.DepartmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentMaterialResult;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentUseSpareResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grandhyatt.snowbeer.Consts.CAMERA_BARCODE_SCAN;

/**
 * Created by ycm on 2018/9/20.
 * 设备保养用机物料选择
 */

public class EquipMgrMaintenMaterialActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    public static final int ADDMATERIAL = 1;
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

    EquipMgrMaintenMaterialDataListAdapter adapter_Spare = null;//备件列表适配器
    int _CheckCnt = -1;//用户选中的行数
    ArrayList<EquipmentMaterialEntity> _CheckEntityList = new ArrayList<>();//用户选择的数据行对象



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mgr_mainten_material);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        _EquipID = intent.getStringExtra("_EquipmentID");

        initView();

        bindEvent();
        mEt_SpareCond.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void initView() {
        mToolBar.setTitle("选择保养用机物料");


        getEquipmentInfo(_EquipID);
        mToolBar.setMenuText("...");
        mToolBar.showMenuButton();
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("添加机物料");
                final PopupWindowUtil popupWindow = new PopupWindowUtil(EquipMgrMaintenMaterialActivity.this, list);
                popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        popupWindow.dismiss();
                        switch (position){
                            case 0:
//                                String _EquipID;//传入的设备ID
//                                String _EquipCode;//传入的设备Cpde
//                                String _EquipName;//传入的设备Name
//                                String _CorpID; //传入的组织机构ID
                                Intent intent1 = new Intent(EquipMgrMaintenMaterialActivity.this, EquipMgrMaintenMaterial_AddActivity.class);
                                intent1.putExtra("equipID", _EquipID);
                                intent1.putExtra("equipCode", _Equipment.getEquipmentCode());
                                intent1.putExtra("equipName", _Equipment.getEquipmentName());
                                intent1.putExtra("corpID", _Equipment.getCorporationID());
                               intent1 .putExtra("corpName", _Equipment.getCorporationName());
                                startActivityForResult(intent1,ADDMATERIAL);
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    @Override
    public void bindEvent() {

        mBt_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogingDialog();

                _SpareCond = mEt_SpareCond.getText().toString();

                getMaterialInfo(_EquipID, _SpareCond);
            }
        });

//        mTv_SpareDept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showSelectDialog(new SelectDialog.SelectDialogListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        String name = _DeptNamelist.get(position);
//                        _SelectedDept = _DepartmentList.get(position);
//                        mTv_SpareDept.setText(name);
//
//                        showLogingDialog();
//
//                        _SpareCond = mEt_SpareCond.getText().toString();
//
//                       // getSparesInfo(_EquipID, _SelectedDept, _SpareCond);
//
//                    }
//                }, _DeptNamelist);
//            }
//        });



        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EquipmentMaterialEntity planEty = null;
                EquipmentMaterialEntity planEty1 = null;
                CheckBox ckb = view.findViewById(R.id.mCkb_IsCheck);
                boolean ckbValue = ckb.isChecked();
                if (!ckbValue)
                {//取消选中

                    if (_CheckCnt != -1)
                    {
                        if(_CheckCnt==position){
                            planEty1 = (EquipmentMaterialEntity) adapter_Spare.getItem(position);
                            planEty1.setIsCheck(true);
                        }
                        else {
                            planEty = (EquipmentMaterialEntity) adapter_Spare.getItem(_CheckCnt);
                            planEty.setIsCheck(false);
                            planEty1 = (EquipmentMaterialEntity) adapter_Spare.getItem(position);
                            planEty1.setIsCheck(true);

                        }
                    }
                    else {
                        planEty1 = (EquipmentMaterialEntity) adapter_Spare.getItem(position);
                        planEty1.setIsCheck(true);

                    }
                    _CheckCnt=position;

                }
                else {
                    planEty1 = (EquipmentMaterialEntity) adapter_Spare.getItem(position);
                    planEty1.setIsCheck(false);
                    _CheckCnt=-1;

                }
                adapter_Spare.notifyDataSetChanged();

            }
        });


        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                _CheckEntityList.clear();

                EquipmentMaterialEntity rpEntity=null;
                if(adapter_Spare!=null && adapter_Spare.getCount()>0) {


                    for (int i = 0; i < adapter_Spare.getCount(); i++) {

                        rpEntity = (EquipmentMaterialEntity) mLv_DataList.getAdapter().getItem(i);
                        if (rpEntity != null) {
                            if (rpEntity.getIsCheck()) {
                                _CheckEntityList.add(rpEntity);

                            }

                        }
                    }
                    if (_CheckEntityList == null || _CheckEntityList.size() == 0) {
                        ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, "请选择要使用的机物料（单选）");
                        return;
                    }
                    //数据是使用Intent返回
                    Intent intent = new Intent();

                    //把返回数据存入Intent

                    intent.putExtra("_CheckEntityList", _CheckEntityList);


                    //设置返回数据
                    EquipMgrMaintenMaterialActivity.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    EquipMgrMaintenMaterialActivity.this.finish();
                }
                else {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, "当前保养设备下无备品备件，请返回！");
                    return;
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
     * 根据设备ID 获取设备信息
     *
     * @param equipmentID
     */
    private void getEquipmentInfo(String equipmentID) {

        showLogingDialog();

        SoapUtils.getEquipmentByIDAsync(EquipMgrMaintenMaterialActivity.this, equipmentID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                EquipmentEntity data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null) {
                    ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, "没有获取到该设备信息");
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

//                    _SelectedDept = new DepartmentEntity();
//                    _SelectedDept.setID( data.getDepartmentID());

                    //获取设备可用的备件库存信息
                    getMaterialInfo(data.getID(),  "");
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 获取组织机构下的部门信息
     *
     * @param corporationID
     */
    private void getDepartmentInfo(String corporationID) {
        SoapUtils.getDepartment(EquipMgrMaintenMaterialActivity.this, corporationID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _DepartmentList = data;

                   // bindDepart(_DepartmentList);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });

    }

    /**
     * 根据设备、部门、备件信息获取可用备件库存信息
     *
     * @param equipID
     * @param spareContent
     */
    private void getMaterialInfo(String equipID, String spareContent) {


        SoapUtils.getEquipmentMaterialStoreInfo(EquipMgrMaintenMaterialActivity.this, equipID, spareContent, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentMaterialResult result = new Gson().fromJson(strData, EquipmentMaterialResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentMaterialEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, "没有为该设备设置保养用机物料信息");
                    mLv_DataList.setAdapter(null);
                    return;
                } else {

                    int cnt = data.size();
                    mTv_AllCnt.setText("共" + String.valueOf(cnt) + "条");

                    adapter_Spare = new EquipMgrMaintenMaterialDataListAdapter(EquipMgrMaintenMaterialActivity.this, data);
                    mLv_DataList.setAdapter(adapter_Spare);

                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(EquipMgrMaintenMaterialActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case ADDMATERIAL:
                _SpareCond = mEt_SpareCond.getText().toString();
                getMaterialInfo(_EquipID,_SpareCond);

                break;


        }
        super.onActivityResult(requestCode, resultCode, data);

    }

}
