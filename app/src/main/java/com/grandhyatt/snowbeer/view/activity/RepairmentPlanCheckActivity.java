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
import com.grandhyatt.snowbeer.adapter.RepairmentPlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.RepairmentPlanResult;
import com.grandhyatt.snowbeer.network.result.SpareInEquipmentResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备维护计划选择
 * Created by ycm on 2018/9/17.
 */

public class RepairmentPlanCheckActivity extends ActivityBase implements IActivityBase, View.OnClickListener{

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


    int _CheckCnt= 0;//用户选中的行数
    ArrayList<String> _CheckIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairment_plan_check);

        ButterKnife.bind(this);
        initView();

        Intent intent = getIntent();
        String _EquipmentID = intent.getStringExtra("_EquipmentID");
        final String _ReapirLevel = intent.getStringExtra("_ReapirLevel");
        mTv_RepairmentLevel.setText(_ReapirLevel);

        showLogingDialog();

        //设备、维修级别不为空
        if ( StringUtils.isNotEmpty(_EquipmentID) && StringUtils.isNotEmpty(_ReapirLevel) ) {

            if(_ReapirLevel.equals("大修")) {
                //获取设备维护计划
                SoapUtils.getEquipmentPlanAsync(RepairmentPlanCheckActivity.this, _EquipmentID, _ReapirLevel, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                        dismissLoadingDialog();

                        if (object == null) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        RepairmentPlanResult result = new Gson().fromJson(strData, RepairmentPlanResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }
                        List<RepairmentPlanEntity> data = result.getData();
                        //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                        if (data == null || data.size()==0) {
                            ToastUtils.showToast(RepairmentPlanCheckActivity.this,"该设备没有维护计划");
                            return;
                        }
                        else
                        {
                            RepairmentPlanCheckDataListAdapter adapter= new RepairmentPlanCheckDataListAdapter(RepairmentPlanCheckActivity.this,data);
                            mLv_DataList.setAdapter(adapter);

                            RepairmentPlanEntity entity = data.get(0);
                            if(entity != null) {
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
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
                    }
                });
            }
            else//定修、日常维修
            {
                //获取设备关联备件信息
                SoapUtils.getEquipmentSparesAsync(RepairmentPlanCheckActivity.this, _EquipmentID, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {
                        dismissLoadingDialog();

                        if (object == null) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        SpareInEquipmentResult result = new Gson().fromJson(strData, SpareInEquipmentResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }
                        List<SpareInEquipmentEntity> data = result.getData();
                        //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                        if (data == null) {
                            ToastUtils.showToast(RepairmentPlanCheckActivity.this,"该设备没有备件信息");
                        }
                        else
                        {
                            SpareInEquipmentDataListAdapter adapter= new SpareInEquipmentDataListAdapter(RepairmentPlanCheckActivity.this,data);
                            mLv_DataList.setAdapter(adapter);

                            SpareInEquipmentEntity entity = data.get(0);
                            if(entity != null) {
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
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
                    }
                });
            }
        }
        else{
            dismissLoadingDialog();
            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this,"设备ID、维修级别不能为空！");
        }

        bindEvent();
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

                CheckBox ckb = view.findViewById(R.id.mCkb_ID);
                TextView mTv_ID = view.findViewById(R.id.mTv_ID);
                String checkedID = mTv_ID.getText().toString();

                if(ckb.isChecked())
                {
                    if(_CheckCnt > 0) {
                        _CheckCnt--;
                        if(_CheckIDList.contains(checkedID))
                        {
                            _CheckIDList.remove(checkedID);
                        }
                        mTv_CheckCnt.setText("共选中" + _CheckCnt + "条");
                    }
                    ckb.setChecked(false);
                }else{
                    _CheckCnt++;
                    if(!_CheckIDList.contains(checkedID)) {
                        _CheckIDList.add(checkedID);
                    }
                    mTv_CheckCnt.setText("共选中" + _CheckCnt + "条");

                    ckb.setChecked(true);
                }

            }
        });

        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //数据是使用Intent返回
                Intent intent = new Intent();
                //把返回数据存入Intent
                if(mTv_RepairmentLevel.getText().toString().equals("大修")) {
                    intent.putStringArrayListExtra("_CheckPlanIDList", _CheckIDList);
                } else{
                    intent.putStringArrayListExtra("_CheckSpareIDList", _CheckIDList);
                }
                //设置返回数据
                RepairmentPlanCheckActivity.this.setResult(RESULT_OK, intent);
                //关闭Activity
                RepairmentPlanCheckActivity.this.finish();

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
