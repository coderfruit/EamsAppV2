package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
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
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
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

        List<CorporationEntity> corps = SPUtils.getLastLoginUserCorporations(this);
        if(corps != null){
            if(corps.size() == 1){
                mRL_UserCorp.setVisibility(View.GONE);
            }else{
                CorporationEntity corp = SPUtils.getLastLoginUserCorporation(this);
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

        mTv_UserCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCorp();
            }
        });

        mRL_UserCorp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUserCorp();
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
            corp = SPUtils.getLastLoginUserCorporation(WarningInfo_EquipActivity.this);
        }

        showLogingDialog();

        SoapUtils.getWarningInfoCount(WarningInfo_EquipActivity.this, corp.getID(),
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

            showSelectDialog(new SelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorporationEntity corp = corpList.get(position);

                    mTv_UserCorp.setText(corp.getCorporationName());
                    //SPUtils.setLastLoginUserCorporation(WarningInfo_EquipActivity.this, corp);

                    requestNetworkData();
                }
            },corpName);
        }

    }

}
