package com.grandhyatt.snowbeer.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.DepartmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * Created by ycm on 2018/9/28.
 */

public class WarningInfo_EquipActivity  extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mBt_EquipRepair)
    Button mBt_EquipRepair;
    @BindView(R.id.mBt_EquipMaintenance)
    Button mBt_EquipMaintenance;
    @BindView(R.id.mBt_EquipInspection)
    Button mBt_EquipInspection;
    @BindView(R.id.mBt_EquipSpareReplace)
    Button mBt_EquipSpareReplace;
    @BindView(R.id.mBt_EquipRepairEx)
    Button mBt_EquipRepairEx;
    @BindView(R.id.mTv_UserCorp)
    TextView mTv_UserCorp;
    @BindView(R.id.mRL_UserCorp)
    RelativeLayout mRL_UserCorp;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

    Badge qBv_mBt_EquipRepair;
    Badge qBv_mBt_EquipMaintenance;
    Badge qBv_mBt_EquipInspection;
    Badge qBv_mBt_EquipSpareReplace;
    Badge qBv_mBt_EquipRepairEx;

    List<DepartmentEntity> _DepartmentList;        //设备对应的部门信息
    List<String> _DeptNamelist = new ArrayList<>();//部门名称列表
    DepartmentEntity _SelectedDept = null;         //选中的部门对象
    @BindView(R.id.mTv_Dept)
    TextView mTv_Dept;

    CorporationEntity _SelectedCorp;                //选中的组织机构

    List<DepartmentEntity> _EquipmentTypeList;        //设备对应的设备类型
    List<String> _EquipmentTypeNamelist = new ArrayList<>();//设备类型名称列表
    DepartmentEntity _SelectedEquipmentType = null;         //选中的设备类型对象

    @BindView(R.id.mTv_EquipType)
    TextView mTv_EquipType;
    @BindView(R.id.mBtn_Reset)
    Button mBtn_Reset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_info_equip);

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
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mToolBar.setTitle("设备预警信息");
        mToolBar.setMenuText("...");
        mToolBar.showMenuButton();
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initMenuButton(v);
            }
        });

        List<CorporationEntity> corps = SPUtils.getLastLoginUserCorporations(this);
        if(corps != null){
            if(corps.size() == 1){
                mRL_UserCorp.setVisibility(View.GONE);
            }else{
                CorporationEntity corp = SPUtils.getFirstLastLoginUserCorporations(this);
                if(corp != null)
                {
                    mTv_UserCorp.setText(corp.getCorporationName());
                }
                mRL_UserCorp.setVisibility(View.VISIBLE);
            }
        }

        qBv_mBt_EquipRepair = new QBadgeView(this).bindTarget(mBt_EquipRepair);
        qBv_mBt_EquipMaintenance = new QBadgeView(this).bindTarget(mBt_EquipMaintenance);
        qBv_mBt_EquipInspection = new QBadgeView(this).bindTarget(mBt_EquipInspection);
        qBv_mBt_EquipSpareReplace = new QBadgeView(this).bindTarget(mBt_EquipSpareReplace);
        qBv_mBt_EquipRepairEx = new QBadgeView(this).bindTarget(mBt_EquipRepairEx);

        //设备类型
        getEquipmentType();
    }

    /**
     * 初始化页面菜单按钮事件
     * @param v
     */
    private void initMenuButton(View v) {
        List<String> list = new ArrayList<String>();
        list.add("维修记录");
        list.add("保养记录");
        list.add("检验记录");
        list.add("外委维修记录");

        CorporationEntity corp = null;
        String userCorp = mTv_UserCorp.getText().toString();
        if(userCorp.length() > 0) {
            corp = SPUtils.getLastLoginUserCorporations(WarningInfo_EquipActivity.this, userCorp);
        }else{
            corp = SPUtils.getFirstLastLoginUserCorporations(WarningInfo_EquipActivity.this);
        }
        final CorporationEntity finalCorp = corp;

        final PopupWindowUtil popupWindow = new PopupWindowUtil(WarningInfo_EquipActivity.this, list);

        popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                popupWindow.dismiss();
                switch (position){
                    case 0:
                        Intent intent1 = new Intent(WarningInfo_EquipActivity.this, Query_EquipRepairInfoActivity.class);
                        intent1.putExtra("corpID", finalCorp.getID());
                        startActivity(intent1);
                        break;
                    case 1:
                        Intent intent2 = new Intent(WarningInfo_EquipActivity.this, Query_EquipMaintenanceInfoActivity.class);
                        intent2.putExtra("corpID", finalCorp.getID());
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(WarningInfo_EquipActivity.this, Query_EquipInspectionInfoActivity.class);
                        intent3.putExtra("corpID", finalCorp.getID());
                        startActivity(intent3);
                        break;
                    case 3:
                        Intent intent4 = new Intent(WarningInfo_EquipActivity.this, Query_EquipRepairExInfoActivity.class);
                        intent4.putExtra("corpID", finalCorp.getID());
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }

            }
        });
        //根据后面的数字 手动调节窗口的宽度
        popupWindow.show(v, 3);
    }

    @Override
    public void bindEvent() {
        mBt_EquipRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorporationEntity corp = getCheckedCorporationEntity();
                Intent intent = new Intent(WarningInfo_EquipActivity.this, WarningInfo_EquipRepairActivity.class);
                intent.putExtra("corpID", corp.getID());
                if(_SelectedDept != null) {
                    intent.putExtra("deptID", _SelectedDept.getID());
                }
                if(_SelectedEquipmentType != null) {
                    intent.putExtra("typeID", _SelectedEquipmentType.getID());
                }
                startActivity(intent);
            }
        });
        mBt_EquipMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorporationEntity corp = getCheckedCorporationEntity();
                Intent intent = new Intent(WarningInfo_EquipActivity.this, WarningInfo_EquipMaintenActivity.class);
                intent.putExtra("corpID", corp.getID());
                if(_SelectedDept != null) {
                    intent.putExtra("deptID", _SelectedDept.getID());
                }
                if(_SelectedEquipmentType != null) {
                    intent.putExtra("typeID", _SelectedEquipmentType.getID());
                }
                startActivity(intent);
            }
        });
        mBt_EquipInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorporationEntity corp = getCheckedCorporationEntity();
                Intent intent = new Intent(WarningInfo_EquipActivity.this, WarningInfo_EquipInspectionActivity.class);
                intent.putExtra("corpID", corp.getID());
                if(_SelectedDept != null) {
                    intent.putExtra("deptID", _SelectedDept.getID());
                }
                if(_SelectedEquipmentType != null) {
                    intent.putExtra("typeID", _SelectedEquipmentType.getID());
                }
                startActivity(intent);
            }
        });
        mBt_EquipSpareReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorporationEntity corp = getCheckedCorporationEntity();
                Intent intent = new Intent(WarningInfo_EquipActivity.this, WarningInfo_EquipSpareReplaceActivity.class);
                intent.putExtra("corpID", corp.getID());
                if(_SelectedDept != null) {
                    intent.putExtra("deptID", _SelectedDept.getID());
                }
                if(_SelectedEquipmentType != null) {
                    intent.putExtra("typeID", _SelectedEquipmentType.getID());
                }
                startActivity(intent);
            }
        });
        mBt_EquipRepairEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CorporationEntity corp = getCheckedCorporationEntity();
                Intent intent = new Intent(WarningInfo_EquipActivity.this, WarningInfo_EquipRepairExActivity.class);
                intent.putExtra("corpID", corp.getID());
                if(_SelectedDept != null) {
                    intent.putExtra("deptID", _SelectedDept.getID());
                }
                if(_SelectedEquipmentType != null) {
                    intent.putExtra("typeID", _SelectedEquipmentType.getID());
                }
                startActivity(intent);
            }
        });

        mTv_UserCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCorp();
            }
        });

        mTv_Dept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDept();
            }
        });

        mTv_EquipType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEquipmentType();
            }
        });

        mBtn_Reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTv_Dept.setText("部门");
                mTv_EquipType.setText("设备类型");
                _SelectedDept = null;
                _SelectedEquipmentType = null;
                requestNetworkData();
            }
        });

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                mRefreshLayout.setNoMoreData(false);
                requestNetworkData();
            }
        });
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {
        CorporationEntity corp = null;
        String userCorp = mTv_UserCorp.getText().toString();
        if(userCorp.length() > 0) {
            corp = SPUtils.getLastLoginUserCorporations(WarningInfo_EquipActivity.this, userCorp);
        }else{
            corp = SPUtils.getFirstLastLoginUserCorporations(WarningInfo_EquipActivity.this);
        }
        String deptID = null;
        if(_SelectedDept != null){
            deptID = _SelectedDept.getID();
        }
        String equipTypeID = null;
        if(_SelectedEquipmentType != null){
            equipTypeID = _SelectedEquipmentType.getID();
        }

        showLogingDialog();

        SoapUtils.getWarningInfoCount(WarningInfo_EquipActivity.this, corp.getID(),deptID,equipTypeID,
                true, true, true, true, true, false, false, false, false,false, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                WarningInfoCountResult result = new Gson().fromJson(strData, WarningInfoCountResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                WarningInfoCountEntity data = result.getData();
                if(data != null) {
                    initWarningInfoCount(data);
                }
                mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "获取预警消息数异常:" + error.getMessage());
                mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "获取预警消息数失败:" + fault);
                mRefreshLayout.finishRefresh(true); //设置SmartRefreshLayout刷新完成标志
            }
        });
    }

    /**
     * 初始化预警消息消息条数
     */
    private void initWarningInfoCount(WarningInfoCountEntity data) {
        if(data != null) {
            qBv_mBt_EquipRepair.setBadgeNumber(data.getEquipRepairPlanCount());
            qBv_mBt_EquipMaintenance.setBadgeNumber(data.getEquipMaintenPlanCount());
            qBv_mBt_EquipInspection.setBadgeNumber(data.getEquipInspectPlanCount());
            qBv_mBt_EquipSpareReplace.setBadgeNumber(data.getEquipSpareReplacePlanCount());
            qBv_mBt_EquipRepairEx.setBadgeNumber(data.getEquipRepairExPlanCount());
        }else{
            qBv_mBt_EquipRepair.setBadgeNumber(0);
            qBv_mBt_EquipMaintenance.setBadgeNumber(0);
            qBv_mBt_EquipInspection.setBadgeNumber(0);
            qBv_mBt_EquipSpareReplace.setBadgeNumber(0);
            qBv_mBt_EquipRepairEx.setBadgeNumber(0);
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

//            showSelectDialog(new SelectDialog.SelectDialogListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    CorporationEntity corp = corpList.get(position);
//
//                    mTv_UserCorp.setText(corp.getCorporationName());
//                    //SPUtils.setLastLoginUserCorporation(WarningInfo_EquipActivity.this, corp);
//
//                    requestNetworkData();
//                }
//            },corpName);

            showSelectDialog("组织机构列表", corpName, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _SelectedCorp = corpList.get(which);
                    mTv_UserCorp.setText(_SelectedCorp.getCorporationName());
                    getDepartmentInfo(_SelectedCorp.getID());
                    requestNetworkData();
                }
            });
        }

    }

    private CorporationEntity getCheckedCorporationEntity(){
        CorporationEntity corp = null;
        String userCorp = mTv_UserCorp.getText().toString();
        if(userCorp.length() > 0) {
            corp = SPUtils.getLastLoginUserCorporations(WarningInfo_EquipActivity.this, userCorp);
        }else{
            corp = SPUtils.getFirstLastLoginUserCorporations(WarningInfo_EquipActivity.this);
        }
        return corp;
    }

    private void showDept(){

        if (_DepartmentList == null || _DepartmentList.size() == 0) {
            if (_SelectedCorp != null) {
                getDepartmentInfo(_SelectedCorp.getID());
            } else {
                CorporationEntity corpEntity = SPUtils.getFirstLastLoginUserCorporations(this);
                if (corpEntity != null) {
                    getDepartmentInfo(corpEntity.getID());
                }
            }
        }
        if (_DeptNamelist != null && _DeptNamelist.size() > 0) {
            showSelectDialog("部门列表",_DeptNamelist,new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _SelectedDept = _DepartmentList.get(which);
                    mTv_Dept.setText(_SelectedDept.getDepartmentName());
                    requestNetworkData();
                }
            });
        }else{
            ToastUtils.showToast(getApplicationContext(),"没有获取到部门信息");
        }
    }

    /**
     * 获取组织机构下的部门信息
     *
     * @param corporationID
     */
    private void getDepartmentInfo(String corporationID) {
        SoapUtils.getDepartment(WarningInfo_EquipActivity.this, corporationID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                //dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {

                    _DepartmentList = null;
                    _DeptNamelist = new ArrayList<>();
                    mTv_Dept.setText("部门");
                    _SelectedDept = null;

                    ToastUtils.showToast(WarningInfo_EquipActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _DepartmentList = data;
                    _DeptNamelist = new ArrayList<>();
                    bindDepart(_DepartmentList);
                    mTv_Dept.setText("部门");
                    _SelectedDept = null;
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //dismissLoadingDialog();
                ToastUtils.showToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                // dismissLoadingDialog();
                ToastUtils.showToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err4, fault));
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
            String value = item.getDepartmentName();
            _DeptNamelist.add(value);
        }
    }

    private void getEquipmentType() {
        SoapUtils.getEquipmentType(WarningInfo_EquipActivity.this, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                //dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                DepartmentResult result = new Gson().fromJson(strData, DepartmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<DepartmentEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    ToastUtils.showToast(WarningInfo_EquipActivity.this, "没有获取到部门信息");
                    return;
                } else {
                    _EquipmentTypeList = data;

                    bindEquipmentType(_EquipmentTypeList);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                //dismissLoadingDialog();
                ToastUtils.showToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                //dismissLoadingDialog();
                ToastUtils.showToast(WarningInfo_EquipActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    private void bindEquipmentType(List<DepartmentEntity> equpmentTypeList) {

        for (DepartmentEntity item : equpmentTypeList) {
            String value = item.getDepartmentName();
            _EquipmentTypeNamelist.add(value);
        }
    }

    private void showEquipmentType() {
        if(_EquipmentTypeNamelist != null && _EquipmentTypeNamelist.size() > 0) {
            showSelectDialog("设备类型列表", _EquipmentTypeNamelist, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    _SelectedEquipmentType = _EquipmentTypeList.get(which);
                    mTv_EquipType.setText(_SelectedEquipmentType.getDepartmentName());
                    requestNetworkData();
                }
            });
        }else{
            ToastUtils.showToast(getApplicationContext(),"没有获取到设备类型信息");
        }
    }
}
