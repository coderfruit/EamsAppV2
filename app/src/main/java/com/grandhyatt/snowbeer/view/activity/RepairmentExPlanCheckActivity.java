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
import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.RepairmentExPlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentPlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentExPlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.RepairmentExPlanResult;
import com.grandhyatt.snowbeer.network.result.RepairmentPlanResult;
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
 * 设备维护计划选择
 * Created by ycm on 2018/9/17.
 */

public class RepairmentExPlanCheckActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;

    @BindView(R.id.mTv_EquipName)
    TextView mTv_EquipName;
    @BindView(R.id.mTv_EquipCode)
    TextView mTv_EquipCode;
    @BindView(R.id.mTv_RepairmentLevel)
    TextView mTv_RepairmentLevel;
    @BindView(R.id.mTv_CheckCnt)
    TextView mTv_CheckCnt;
    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;
    @BindView(R.id.mTv_AllCnt)
    TextView mTv_AllCnt;


    int _CheckCnt = 0;//用户选中的行数
    ArrayList<String> _CheckIDList = new ArrayList<>();//用户选择的数据行ID
    ArrayList<Object> _CheckEntityList = new ArrayList<>();//用户选择的数据行对象
    String _ReapirLevel = "";
    RepairmentExPlanCheckDataListAdapter adapter_Plan = null;//维护计划适配器
    SpareInEquipmentDataListAdapter adapter_Spare = null;  //备件适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairmentex_plan_check);

        ButterKnife.bind(this);
        initView();

        Intent intent = getIntent();
        String _EquipmentID = intent.getStringExtra("_EquipmentID");
        mTv_RepairmentLevel.setText(_ReapirLevel);

        showLogingDialog();
        bindEvent();

        //设备、维修级别不为空
        if (StringUtils.isNotEmpty(_EquipmentID) ) {


                //获取设备维护计划
                SoapUtils.getEquipmentExPlanAsync(RepairmentExPlanCheckActivity.this, _EquipmentID, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                        dismissLoadingDialog();

                        if (object == null) {
                            ToastUtils.showLongToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        RepairmentExPlanResult result = new Gson().fromJson(strData, RepairmentExPlanResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }
                        List<RepairmentExPlanEntity> data = result.getData();
                        //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                        if (data == null || data.size() == 0) {
                            ToastUtils.showToast(RepairmentExPlanCheckActivity.this, "该设备没有维护计划");
                            return;
                        } else {
                            mTv_AllCnt.setText("共" + data.size() + "条/");
                            adapter_Plan = new RepairmentExPlanCheckDataListAdapter(RepairmentExPlanCheckActivity.this, data);
                            mLv_DataList.setAdapter(adapter_Plan);

                            RepairmentExPlanEntity entity = data.get(0);
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
                        ToastUtils.showToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(RepairmentExPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
                    }
                });

        } else {
            dismissLoadingDialog();
            ToastUtils.showLongToast(RepairmentExPlanCheckActivity.this, "设备ID不能为空！");
        }


    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("选择维护计划");
    }

    @Override
    public void bindEvent() {

        mLv_DataList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                RepairmentExPlanEntity planEty = null;
                RepairmentExPlanEntity planEty1 = null;

                CheckBox ckb = view.findViewById(R.id.mCkb_ID);
                TextView mTv_ID = view.findViewById(R.id.mTv_ID);
                String checkedID = mTv_ID.getText().toString();

                boolean ckbValue = ckb.isChecked();
                if (!ckbValue)
                {//取消选中

                    if (_CheckCnt != -1)
                    {
                        if(_CheckCnt==position){
                            planEty1 = (RepairmentExPlanEntity) adapter_Plan.getItem(position);
                            planEty1.setIsCheck(true);
                        }
                        else {
                            planEty = (RepairmentExPlanEntity) adapter_Plan.getItem(_CheckCnt);
                            planEty.setIsCheck(false);
                            planEty1 = (RepairmentExPlanEntity) adapter_Plan.getItem(position);
                            planEty1.setIsCheck(true);

                        }
                    }
                    else {
                        planEty1 = (RepairmentExPlanEntity) adapter_Plan.getItem(position);
                        planEty1.setIsCheck(true);

                    }
                    _CheckCnt=position;

                }
                else {
                    planEty1 = (RepairmentExPlanEntity) adapter_Plan.getItem(position);
                    planEty1.setIsCheck(false);
                    _CheckCnt=-1;

                }
                adapter_Plan.notifyDataSetChanged();


            }
        });

        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                _CheckEntityList.clear();
                _CheckIDList.clear();
                RepairmentExPlanEntity rpEntity=null;
                if(adapter_Plan!=null || adapter_Plan.getCount()!=0){
                    for (int i = 0; i < adapter_Plan.getCount(); i++) {

                        rpEntity = (RepairmentExPlanEntity) adapter_Plan.getItem(i);
                        if (rpEntity!=null) {
                            if( rpEntity.getIsCheck()){
                                _CheckEntityList.add(rpEntity);
                                _CheckIDList.add(rpEntity.getID());
                            }

                        }
                    }
                }



                //把返回数据存入Intent
                intent.putStringArrayListExtra("_CheckPlanIDList", _CheckIDList);
                intent.putExtra("_CheckEntityList", _CheckEntityList);


                //设置返回数据
                RepairmentExPlanCheckActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                RepairmentExPlanCheckActivity.this.finish();

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
