package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;
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
        mToolBar.setTitle("设备预警信息");
    }

    @Override
    public void bindEvent() {
        mBt_EquipRepair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(WarningInfo_EquipActivity.this, WarningInfo_EquipRepairActivity.class);
            }
        });
        mBt_EquipMaintenance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(WarningInfo_EquipActivity.this, WarningInfo_EquipMaintenActivity.class);
            }
        });
        mBt_EquipInspection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(WarningInfo_EquipActivity.this, WarningInfo_EquipInspectionActivity.class);
            }
        });
        mBt_EquipSpareReplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(WarningInfo_EquipActivity.this, WarningInfo_EquipSpareReplaceActivity.class);
            }
        });
        mBt_EquipRepairEx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(WarningInfo_EquipActivity.this, WarningInfo_EquipRepairExActivity.class);
            }
        });
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

        CorporationEntity corp = SPUtils.getLastLoginUserCorporation(WarningInfo_EquipActivity.this);
        showLogingDialog();

        SoapUtils.getWarningInfoCount(WarningInfo_EquipActivity.this, corp.getID(),
                true, true, true, true, true, false, false, false, false, new SoapListener() {
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
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "获取预警消息数异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(WarningInfo_EquipActivity.this, "获取预警消息数失败:" + fault);
            }
        });
    }

    /**
     * 初始化预警消息消息条数
     */
    private void initWarningInfoCount(WarningInfoCountEntity data) {
        if(data != null) {
            new QBadgeView(this).bindTarget(mBt_EquipRepair).setBadgeNumber(data.getEquipRepairPlanCount());
            new QBadgeView(this).bindTarget(mBt_EquipMaintenance).setBadgeNumber(data.getEquipMaintenPlanCount());
            new QBadgeView(this).bindTarget(mBt_EquipInspection).setBadgeNumber(data.getEquipInspectPlanCount());
            new QBadgeView(this).bindTarget(mBt_EquipSpareReplace).setBadgeNumber(data.getEquipSpareReplacePlanCount());
            new QBadgeView(this).bindTarget(mBt_EquipRepairEx).setBadgeNumber(data.getEquipRepairExPlanCount());
        }else{
            new QBadgeView(this).bindTarget(mBt_EquipRepair).setBadgeNumber(0);
            new QBadgeView(this).bindTarget(mBt_EquipMaintenance).setBadgeNumber(0);
            new QBadgeView(this).bindTarget(mBt_EquipInspection).setBadgeNumber(0);
            new QBadgeView(this).bindTarget(mBt_EquipSpareReplace).setBadgeNumber(0);
            new QBadgeView(this).bindTarget(mBt_EquipRepairEx).setBadgeNumber(0);
        }
    }


}
