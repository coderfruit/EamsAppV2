package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.SearchBarLayout;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * 设备巡检
 * Created by ycm on 2018/9/30.
 */

public class EquipRoutingInspectionActivity extends com.grandhyatt.snowbeer.view.activity.ActivityBase implements IActivityBase {
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mSearchBar)
    SearchBarLayout mSearchBar;

    @BindView(R.id.mBt_WarningInfo_Repair)
    Button mBt_WarningInfo_Repair;//维修预警
    @BindView(R.id.mBt_WarningInfo_Mainten)
    Button mBt_WarningInfo_Mainten;//保养预警
    @BindView(R.id.mBt_WarningInfo_Inspect)
    Button mBt_WarningInfo_Inspect;//检验预警
    @BindView(R.id.mBt_WarningInfo_Replace)
    Button mBt_WarningInfo_Replace;//备件更换预警
    @BindView(R.id.mBt_WarningInfo_RepairEx)
    Button mBt_WarningInfo_RepairEx;//外委维修预警
    @BindView(R.id.mBt_faultRptInfo)
    Button mBt_faultRptInfo;//报修预警

    @BindView(R.id.mBt_Repair)
    Button mBt_Repair;      //去维修
    @BindView(R.id.mBt_Mainten)
    Button mBt_Mainten; //去保养
    @BindView(R.id.mBt_Inspect)
    Button mBt_Inspect; //去检验
    @BindView(R.id.mBt_RepairEx)
    Button mBt_RepairEx; //去外委维修
    @BindView(R.id.mBt_ReportFail)
    Button mBt_ReportFail;//去报修
    @BindView(R.id.mBt_Back)
    Button mBt_Back;        //返回

    BroadcastReceiver mReceiver;
    IntentFilter mFilter;
    /**
     * 设备信息
     */
    EquipmentEntity _EquipmentData;

    @BindView(R.id.mTv_EquipName)
    TextView mTv_EquipName;
    @BindView(R.id.mTv_EquipCorp)
    TextView mTv_EquipCorp;
    @BindView(R.id.mTv_EquipDept)
    TextView mTv_EquipDept;
    @BindView(R.id.mTv_EquipKeeper)
    TextView mTv_EquipKeeper;
    @BindView(R.id.mTv_EquipCode)
    TextView mTv_EquipCode;
    @BindView(R.id.mTv_EquipLocation)
    TextView mTv_EquipLocation;
    @BindView(R.id.mIv_EquipImg)
    ImageView mIv_EquipImg;

    @BindView(R.id.mBt_NoOperate)
    Button mBt_NoOperate;//巡检-正常运行
    @BindView(R.id.mBt_Operate)
    Button mBt_Operate;//巡检-需处理
    @BindView(R.id.mLl_Operate)
    LinearLayout mLl_Operate;//操作按钮组

    Badge bDg_WarningInfo_Repair;
    Badge bDg_WarningInfo_Mainten;
    Badge bDg_WarningInfo_Inspect;
    Badge bDg_WarningInfo_Replace;
    Badge bDg_WarningInfo_RepairEx;
    Badge bDg_faultRptInfo;

    public static final int REPAIAR_OPERATE_AFTER = 10001;//维修操作之后
    public static final int MAINTEN_OPERATE_AFTER = 10002;//保养操作之后
    public static final int INSPECT_OPERATE_AFTER = 10003;//检验操作之后
    public static final int REPLACE_OPERATE_AFTER = 10004;//备件更换操作之后
    public static final int REPAIR_EX_OPERATE_AFTER = 10005;//外委维修之后
    public static final int FAULT_REPORT_OPERATE_AFTER = 10006;//报修查看操作之后


    public static final int GO_FAULT_REPORT_OPERATE_AFTER = 10011;//去报修操作之后

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_routing_inspection);

        ButterKnife.bind(this);
        initView();
        bindEvent();

        Intent intent = getIntent();
        String mTv_ID = intent.getStringExtra("mTv_ID");//设备ID
        if (mTv_ID != null && mTv_ID != null) {

            mSearchBar.setVisibility(View.GONE);

            getEquipmentInfoByID(mTv_ID);
        }
    }

    @Override
    public void initView() {

        mToolBar.setTitle("设备巡检");

        mToolBar.showMenuButton();
        mToolBar.setMenuText("检索");


        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        bDg_WarningInfo_Repair = new QBadgeView(this).bindTarget(mBt_WarningInfo_Repair);
        bDg_WarningInfo_Mainten = new QBadgeView(this).bindTarget(mBt_WarningInfo_Mainten);
        bDg_WarningInfo_Inspect = new QBadgeView(this).bindTarget(mBt_WarningInfo_Inspect);
        bDg_WarningInfo_Replace = new QBadgeView(this).bindTarget(mBt_WarningInfo_Replace);
        bDg_WarningInfo_RepairEx = new QBadgeView(this).bindTarget(mBt_WarningInfo_RepairEx);
        bDg_faultRptInfo = new QBadgeView(this).bindTarget(mBt_faultRptInfo);

    }

    @Override
    public void bindEvent() {

        //显示检索菜单
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
                List<String> list = new ArrayList<String>();
                list.add("维修记录");
                list.add("保养记录");
                list.add("检验记录");
                list.add("外委维修记录");

                showSelectDialog("请选择检索内容", list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Intent intent1 = new Intent(EquipRoutingInspectionActivity.this, Query_EquipRepairInfoActivity.class);
                                intent1.putExtra("equipID", _EquipmentData.getID());
                                startActivity(intent1);
                                break;
                            case 1:
                                Intent intent2 = new Intent(EquipRoutingInspectionActivity.this, Query_EquipMaintenanceInfoActivity.class);
                                intent2.putExtra("equipID", _EquipmentData.getID());
                                startActivity(intent2);
                                break;
                            case 2:
                                Intent intent3 = new Intent(EquipRoutingInspectionActivity.this, Query_EquipInspectionInfoActivity.class);
                                intent3.putExtra("equipID", _EquipmentData.getID());
                                startActivity(intent3);
                                break;
                            case 3:
                                Intent intent4 = new Intent(EquipRoutingInspectionActivity.this, Query_EquipRepairExInfoActivity.class);
                                intent4.putExtra("equipID", _EquipmentData.getID());
                                startActivity(intent4);
                                break;
                            default:
                                break;
                        }
                    }
                });
            }
        });

        //------------------------------------------------------------------------------------------------
        //操作按钮
        //------------------------------------------------------------------------------------------------
        //巡检-正常运行
        mBt_NoOperate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){
                    String userID = SPUtils.getLastLoginUserID(getApplicationContext());
                    newEquipmentRoutingInspect(_EquipmentData.getID(),userID,"运转正常","","","");
                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }

            }
        });

        //巡检-运行异常
        mBt_Operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){
                    mBt_NoOperate.setVisibility(View.GONE);
                    mBt_Operate.setVisibility(View.GONE);
                    mLl_Operate.setVisibility(View.VISIBLE);
                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //巡检-需处理-返回
        mBt_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBt_NoOperate.setVisibility(View.VISIBLE);
                mBt_Operate.setVisibility(View.VISIBLE);
                mLl_Operate.setVisibility(View.GONE);
            }
        });
        //巡检-需处理-去维修
        mBt_Repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){

                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //巡检-需处理-去保养
        mBt_Mainten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){

                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });

        //巡检-需处理-去检验
        mBt_Inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){

                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });

        //巡检-需处理-去外委维修
        mBt_RepairEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_EquipmentData != null){

                }else{
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });

        //巡检-需处理-去报修
        mBt_ReportFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, FaultReportActivity.class);
                    intent.putExtra("mTv_EquipID", _EquipmentData.getID());
                    startActivityForResult(intent,GO_FAULT_REPORT_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });

        //------------------------------------------------------------------------------------------------
        //检索事件
        mSearchBar.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String barcode = mSearchBar.getBarcode();
                SearchBarcode(barcode);
            }
        });
        //条码文本框回车事件
        mSearchBar.setBarcodeEnterListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                String barcode = mSearchBar.getBarcode();
                SearchBarcode(barcode);

                return false;
            }
        });
        //------------------------------------------------------------------------------------------------
        //预警信息
        //------------------------------------------------------------------------------------------------
        //维修预警
        mBt_WarningInfo_Repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, WarningInfo_EquipRepairActivity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, REPAIAR_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //保养预警
        mBt_WarningInfo_Mainten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, WarningInfo_EquipMaintenActivity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, MAINTEN_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //检验预警
        mBt_WarningInfo_Inspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, WarningInfo_EquipInspectionActivity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, INSPECT_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //备件更换预警
        mBt_WarningInfo_Replace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, WarningInfo_EquipSpareReplaceActivity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, REPLACE_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //外委维修
        mBt_WarningInfo_RepairEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, WarningInfo_EquipRepairExActivity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, REPAIR_EX_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
                }
            }
        });
        //报修预警
        mBt_faultRptInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData != null) {
                    Intent intent = new Intent(EquipRoutingInspectionActivity.this, FaultReport_Mgr_Activity.class);
                    intent.putExtra("equipID", _EquipmentData.getID());
                    startActivityForResult(intent, FAULT_REPORT_OPERATE_AFTER);
                } else {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "请先确定设备");
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
     * 根据条码检索设备
     *
     * @param barcode
     */
    private void SearchBarcode(String barcode) {
        if (barcode == null) {
            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        } else {
            showLogingDialog();
            //根据条码内容检索设备信息
            getEquipmentInfo(barcode);
        }
    }

    /**
     * 根据设备条码获取设备信息
     *
     * @param barcode
     */
    private void getEquipmentInfo(String barcode) {
        SoapUtils.getEquipmentByBarcodeAsync(EquipRoutingInspectionActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取设备信息失败:" + fault);
            }
        });
    }

    /**
     * 获取到的设备信息，填充至页面
     *
     * @param data
     */
    private void fillEquipInfo(EquipmentEntity data) {
        _EquipmentData = data;//全局变量赋值
        if (data != null) {//获取到设备信息
            mTv_EquipCode.setText(data.getEquipmentCode());
            mTv_EquipName.setText(data.getEquipmentName());
            mTv_EquipCorp.setText(data.getCorporationName());
            mTv_EquipDept.setText(data.getDepartmentName());
            mTv_EquipKeeper.setText(data.getKeeperName());
            mTv_EquipLocation.setText(data.getInstallLoaction());
            String imgBase64 = data.getEquipImg();
            if (imgBase64 != null && imgBase64.length() > 0) {
                byte[] imgbyte = CommonUtils.base64ToByte(imgBase64);
                if (imgbyte != null && imgbyte.length > 0) {
                    Bitmap equipImgBmp = ImageUtils.Bytes2Bimap(imgbyte);
                    if (equipImgBmp != null) {
                        mIv_EquipImg.setImageBitmap(equipImgBmp);
                    }
                }
            }

            getEquipWarinigInfo(data.getID());//获取设备预警、报修条数

        } else {//没有获取到设备信息
            mTv_EquipCode.setText("");
            mTv_EquipName.setText("");
            mTv_EquipCorp.setText("");
            mTv_EquipDept.setText("");
            mTv_EquipKeeper.setText("");
            mTv_EquipLocation.setText("");
            mIv_EquipImg.setImageBitmap(null);

            initWarningInfoCount(null);//没有获取到设备信息，重置设备预警、报修条数
        }
    }

    /**
     * 获取设备预警信息
     *
     * @param equipID
     */
    private void getEquipWarinigInfo(String equipID) {
        SoapUtils.getWarningInfo_Equip(EquipRoutingInspectionActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                WarningInfoCountResult result = new Gson().fromJson(strData, WarningInfoCountResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                WarningInfoCountEntity data = result.getData();
                if (data != null) {
                    initWarningInfoCount(data);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取预警信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取预警信息失败:" + fault);
            }
        });

    }

    /**
     * 初始化预警消息消息条数
     */
    private void initWarningInfoCount(WarningInfoCountEntity data) {
        if (data != null) {
            int iRpCnt = data.getEquipRepairPlanCount();
            int iMtnCnt = data.getEquipMaintenPlanCount();
            int iIspCnt = data.getEquipInspectPlanCount();
            int iSpRpCnt = data.getEquipSpareReplacePlanCount();
            int iRpExCnt = data.getEquipRepairExPlanCount();

            bDg_WarningInfo_Repair.setBadgeNumber(iRpCnt);
            bDg_WarningInfo_Mainten.setBadgeNumber(iMtnCnt);
            bDg_WarningInfo_Inspect.setBadgeNumber(iIspCnt);
            bDg_WarningInfo_Replace.setBadgeNumber(iSpRpCnt);
            bDg_WarningInfo_RepairEx.setBadgeNumber(iRpExCnt);

            bDg_faultRptInfo.setBadgeNumber(data.getReportFaultCount());
        } else {
            bDg_WarningInfo_Repair.setBadgeNumber(0);
            bDg_WarningInfo_Mainten.setBadgeNumber(0);
            bDg_WarningInfo_Inspect.setBadgeNumber(0);
            bDg_WarningInfo_Replace.setBadgeNumber(0);
            bDg_WarningInfo_RepairEx.setBadgeNumber(0);
            bDg_faultRptInfo.setBadgeNumber(0);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (_EquipmentData == null)
            return;

        switch (requestCode) {

            case REPAIAR_OPERATE_AFTER://维修之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case MAINTEN_OPERATE_AFTER://保养之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case INSPECT_OPERATE_AFTER://检验之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case REPLACE_OPERATE_AFTER://更换之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case REPAIR_EX_OPERATE_AFTER://外委维修之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case FAULT_REPORT_OPERATE_AFTER://报修查看之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            case GO_FAULT_REPORT_OPERATE_AFTER://去报修之后
                getEquipWarinigInfo(_EquipmentData.getID());
                break;
            default:
                break;
        }
    }

    /**
     * 根据设备ID获取设备信息
     *
     * @param equipID
     */
    private void getEquipmentInfoByID(String equipID) {
        showLogingDialog();

        SoapUtils.getEquipmentByIDAsync(EquipRoutingInspectionActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "获取设备信息失败:" + fault);
            }
        });
    }

    /**
     * 添加设备巡检记录
     */
    private void newEquipmentRoutingInspect(String equipID, String routingUserID, String routingStatus, String operateType,
                                            String operateID,String operateBillNO) {
        showLogingDialog();

        SoapUtils.newEquipmentRoutingInspect(EquipRoutingInspectionActivity.this, equipID, routingUserID, routingStatus, operateType, operateID, operateBillNO,
                new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {
                        dismissLoadingDialog();
                        if (object == null) {
                            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "1提交巡检信息失败" + statusCode);
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "2提交巡检信息失败" + statusCode);
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        Result result = new Gson().fromJson(strData, Result.class);
                        //校验接口返回代码
                        if(result == null)
                        {
                            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "提交巡检信息异常");
                            return;
                        }
                        else if(result.code != Result.RESULT_CODE_SUCCSED){
                            ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, result.msg);
                            return;
                        }
                        CommonUtils.playMusic(EquipRoutingInspectionActivity.this);
                        ToastUtils.showToast(EquipRoutingInspectionActivity.this,"提交巡检信息成功！");
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "提交巡检信息异常:" + error.getMessage());
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(EquipRoutingInspectionActivity.this, "提交巡检数据失败:" + fault);
                    }
                });

    }
}
