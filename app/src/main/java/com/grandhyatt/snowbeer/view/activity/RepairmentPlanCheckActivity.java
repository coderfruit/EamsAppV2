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
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设备维护计划选择
 * Created by ycm on 2018/9/17.
 */

public class RepairmentPlanCheckActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    RepairmentPlanCheckDataListAdapter adapter_Plan = null;//维护计划适配器
    SpareInEquipmentDataListAdapter adapter_Spare = null;  //备件适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repairment_plan_check);

        ButterKnife.bind(this);
        initView();

        Intent intent = getIntent();
        String _EquipmentID = intent.getStringExtra("_EquipmentID");
        _ReapirLevel = intent.getStringExtra("_ReapirLevel");

        mTv_RepairmentLevel.setText(_ReapirLevel);

        showLogingDialog();
        bindEvent();

        //设备、维修级别不为空
        if (StringUtils.isNotEmpty(_EquipmentID) && StringUtils.isNotEmpty(_ReapirLevel)) {

            if (_ReapirLevel.equals("大修")) {
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
                        if (data == null || data.size() == 0) {
                            ToastUtils.showToast(RepairmentPlanCheckActivity.this, "该设备没有维护计划");
                            return;
                        } else {
                            mTv_AllCnt.setText("共" + data.size() + "条/");
                            adapter_Plan = new RepairmentPlanCheckDataListAdapter(RepairmentPlanCheckActivity.this, data);
                            mLv_DataList.setAdapter(adapter_Plan);

                            RepairmentPlanEntity entity = data.get(0);
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
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
                    }
                });
            } else//定修、日常维修
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
                        if (data == null || data.size() == 0) {
                            ToastUtils.showToast(RepairmentPlanCheckActivity.this, "该设备没有备件信息");
                        } else {
                            mTv_AllCnt.setText("共" + data.size() + "条/");
                            adapter_Spare = new SpareInEquipmentDataListAdapter(RepairmentPlanCheckActivity.this, data);
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
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err5, error));
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showToast(RepairmentPlanCheckActivity.this, getString(R.string.submit_soap_result_err4, fault));
                    }
                });
            }
        } else {
            dismissLoadingDialog();
            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, "设备ID、维修级别不能为空！");
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

                RepairmentPlanEntity planEty = null;
                SpareInEquipmentEntity spareEty = null;

                CheckBox ckb = view.findViewById(R.id.mCkb_ID);
                TextView mTv_ID = view.findViewById(R.id.mTv_ID);
                String checkedID = mTv_ID.getText().toString();

                boolean ckbValue = ckb.isChecked();
                if (ckbValue) {//取消选中
                    if (adapter_Plan != null) {//维护计划
                        if (_CheckCnt > 0) {
                            _CheckCnt--;
                            if (_CheckIDList.contains(checkedID)) {
                                _CheckIDList.remove(checkedID);
                            }
                            mTv_CheckCnt.setText("选中" + _CheckCnt + "条");
                            planEty = (RepairmentPlanEntity) adapter_Plan.getItem(position);
                            planEty.setIsCheck(false);
                            adapter_Plan.notifyDataSetChanged();

                            if (planEty != null) {
                                if (_CheckEntityList.contains(planEty)) {
                                    _CheckEntityList.remove(planEty);
                                }
                            }
                        }
                    } else if (adapter_Spare != null) {//备件列表
                        if (_CheckCnt > 0) {
                            _CheckCnt--;
                            if (_CheckIDList.contains(checkedID)) {
                                _CheckIDList.remove(checkedID);
                            }
                            mTv_CheckCnt.setText("选中" + _CheckCnt + "条");
                            spareEty = (SpareInEquipmentEntity) adapter_Spare.getItem(position);
                            spareEty.setIsCheck(false);
                            adapter_Spare.notifyDataSetChanged();

                            if (spareEty != null) {
                                if (_CheckEntityList.contains(spareEty)) {
                                    _CheckEntityList.remove(spareEty);
                                }
                            }
                        }
                    }
                } else {        //选中
                    if (adapter_Plan != null) {//维护计划
                        if (_CheckCnt == 1) {
                            ToastUtils.showLongToast(RepairmentPlanCheckActivity.this, "维护计划只允许选择1条");
                            return;
                        }
                        _CheckCnt++;
                        if (!_CheckIDList.contains(checkedID)) {
                            _CheckIDList.add(checkedID);
                        }
                        mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                        planEty = (RepairmentPlanEntity) adapter_Plan.getItem(position);
                        planEty.setIsCheck(true);
                        adapter_Plan.notifyDataSetChanged();
                        if (planEty != null) {
                            if (!_CheckEntityList.contains(planEty)) {
                                _CheckEntityList.add(planEty);
                            }
                        }
                    } else if (adapter_Spare != null) {//备件
                        _CheckCnt++;
                        if (!_CheckIDList.contains(checkedID)) {
                            _CheckIDList.add(checkedID);
                        }
                        mTv_CheckCnt.setText("选中" + _CheckCnt + "条");

                        spareEty = (SpareInEquipmentEntity) adapter_Spare.getItem(position);
                        spareEty.setIsCheck(true);
                        adapter_Spare.notifyDataSetChanged();
                        if (spareEty != null) {
                            if (!_CheckEntityList.contains(spareEty)) {
                                _CheckEntityList.add(spareEty);
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
                _CheckEntityList.clear();
                _CheckIDList.clear();
                if(adapter_Plan==null && adapter_Spare == null) {
                    //关闭Activity
                    RepairmentPlanCheckActivity.this.finish();
                }
                else {
                    if (_ReapirLevel.equals("大修")) {
                        RepairmentPlanEntity rpEntity = null;
                        for (int i = 0; i < adapter_Plan.getCount(); i++) {

                            rpEntity = (RepairmentPlanEntity) mLv_DataList.getAdapter().getItem(i);
                            if (rpEntity != null) {
                                if (rpEntity.getIsCheck()) {
                                    _CheckEntityList.add(rpEntity);
                                    _CheckIDList.add(rpEntity.getID());
                                }
                            }
                        }
                    } else {
                        SpareInEquipmentEntity rpEntity = null;
                        for (int i = 0; i < adapter_Spare.getCount(); i++) {
                            rpEntity = (SpareInEquipmentEntity) mLv_DataList.getAdapter().getItem(i);
                            if (rpEntity != null) {
                                if (rpEntity.getIsCheck()) {
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
                    RepairmentPlanCheckActivity.this.setResult(RESULT_OK, intent);
                    //关闭Activity
                    RepairmentPlanCheckActivity.this.finish();
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


}
