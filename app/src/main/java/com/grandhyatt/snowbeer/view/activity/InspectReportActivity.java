package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.codbking.widget.DatePickDialog;
import com.codbking.widget.OnSureLisener;
import com.codbking.widget.bean.DateType;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.Consts;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.InspectPlanViewDataListAdapter;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.InspectBillResult;
import com.grandhyatt.snowbeer.network.result.StringResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.SearchBarLayout;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grandhyatt.snowbeer.Consts.CAMERA_BARCODE_SCAN;

/**
 * 设备检验操作界面
 * Created by tongzhiqiang on 2018-10-08.
 */

public class InspectReportActivity extends ActivityBase implements IActivityBase, View.OnClickListener{
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    @BindView(R.id.mSearchBar)
    SearchBarLayout mSearchBar;

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
    @BindView(R.id.mBtn_Submit)
    Button mBtn_Submit;

    @BindView(R.id.mTv_jh)
    TextView mTv_jh;
    @BindView(R.id.mEt_User)
    EditText mEt_User;
    @BindView(R.id.mEt_Com)
    EditText mEt_Com;
    @BindView(R.id.mEt_money)
    EditText mEt_money;
    @BindView(R.id.mLv_Show_plan)
    ListView mLv_Show_plan;
    BroadcastReceiver mReceiver;
    IntentFilter mFilter;

    @BindView(R.id.mTv_InspTime)
    TextView mTv_InspTime;
    @BindView(R.id.mTv_inspItem)
    TextView mTv_inspItem;
    @BindView(R.id.mTv_InspResult)
    TextView mTv_InspResult;
    @BindView(R.id.mTv_InspMode)
    TextView mTv_InspMode;

    EquipmentEntity _EquipmentData;
    InspectBillEntity _ReportEntity;
    String[] _InspItemArr; //检验类型
    String[] _InspResultArr; //检验类型
    String[] _InspModeArr; //检验类型
    public static final int CHECK_PLAN_OK = 111;//选择执行计划返回码
    ArrayList<String> _CheckPlanIDList; //用户选中的维护计划ID
    List<InspectionPlanEntity> _CheckPlanEntityList = new ArrayList<>();//用户选择的数据行对象

    InspectPlanViewDataListAdapter adapter_Plan=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_card_re_check);
        setContentView(R.layout.activity_inspect_report);
        ButterKnife.bind(this);


        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        //检验            type = 0    mTv_EquipID=设备id
        //检验-检验计划   type = 2    mTv_EquipID=设备id  mTv_ReportID = 检验计划ID
        //检验-显示检验单 type = 3    mTv_EquipID=设备id  mTv_ReportID = 检验单ID
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");

        //检验-显示检验单 type = 3    mTv_EquipID=设备id  mTv_ReportID = 检验单ID
        if ((type != null && type.equals("3")) && mTv_ReportID != null && mTv_EquipID != null) {
            mToolBar.setTitle("查看检验信息");
            getEquipmentInfoByID(mTv_EquipID);
            getReport(mTv_ReportID);
            bindEventPart();
            //隐藏搜索栏
            mSearchBar.setVisibility(View.GONE);
            mBtn_Submit.setVisibility(View.GONE);
            bindUIData(_ReportEntity);
        }
        //检验-检验计划   type = 2    mTv_EquipID=设备id  mTv_ReportID = 检验计划ID
        else  if ((type != null && type.equals("2")) && mTv_ReportID != null && mTv_EquipID != null){

        }
        //检验            type = 0    mTv_EquipID=设备id
        else if ((type != null && type.equals("0")) && mTv_EquipID != null){

        }
        //正常检验
        else {
            mToolBar.setTitle("我要检验");
            initView();
            bindEventPart();
            bindEvent();
            refreshUI();
            requestNetworkData();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFilter = new IntentFilter("android.intent.action.SCAN_RESULT");
        // 在用户自行获取数据时，将广播的优先级调到最高 1000，***此处必须***
        mFilter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        // 注册广播来获取扫描结果
        this.registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onPause() {
        // 注销获取扫描结果的广播
        this.unregisterReceiver(mReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mReceiver = null;
        mFilter = null;
        super.onDestroy();
    }
    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        fillEquipInfo(null);

        mToolBar.setTitle("设备保养");
        mToolBar.hideMenuButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mLv_Show_plan.setVisibility(View.GONE);
        //初始化用户及手机号
        mEt_User.setText(SPUtils.getLastLoginUserName(InspectReportActivity.this));
        mEt_money.setText(SPUtils.getLastLoginUserPhone(InspectReportActivity.this));


        SoapListener callbackFailureReportingDesc = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "1获取检验项目数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "2获取检验项目数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "3获取检验项目数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "4获取检验项目数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _InspItemArr = value.split("\\|");
                    }
                    else {
                        _InspItemArr=null;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取保养类型数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取保养类型数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(InspectReportActivity.this, Consts.EnumTextDictonay.InspectionItem, callbackFailureReportingDesc);

        SoapListener callbackInspResult = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "1获取检验结果数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "2获取检验结果数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "3获取检验结果数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "4获取检验结果数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _InspResultArr = value.split("\\|");
                    }
                    else {
                        _InspResultArr=null;
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取检验结果数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取检验结果数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(InspectReportActivity.this, Consts.EnumTextDictonay.InspectionResult, callbackInspResult);

        SoapListener callbackInspMode = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "1获取检验方式数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "2获取检验方式数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "3获取检验方式数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "4获取检验方式数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _InspModeArr = value.split("\\|");
                    }
                    else _InspModeArr=null;
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取检验方式数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(InspectReportActivity.this, "0获取检验方式数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(InspectReportActivity.this, Consts.EnumTextDictonay.InspectionMode, callbackInspMode);

    }

    public void bindEventPart() {

        //设备图片点击事件
        mIv_EquipImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mIv_EquipImg.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mIv_EquipImg.getDrawingCache());
                mIv_EquipImg.setDrawingCacheEnabled(false);
                String equipName = mTv_EquipName.getText().toString();

                if (bitmap != null && equipName != null) {
                    Intent intent = new Intent(InspectReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(InspectReportActivity.this, "无图片需显示");
                }
                return false;
            }
        });






    }

    @Override
    public void bindEvent() {

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


        //日期
        mTv_InspTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickDialog dialog = new DatePickDialog(v.getContext());
                //设置上下年分限制
                dialog.setYearLimt(5);
                //设置标题
                dialog.setTitle("选择时间");
                //设置类型
                dialog.setType(DateType.TYPE_YMDHM);
                //设置消息体的显示格式，日期格式
                dialog.setMessageFormat("yyyy-MM-dd HH:mm");
                //设置选择回调
                dialog.setOnChangeLisener(null);
                //设置点击确定按钮回调
                dialog.setOnSureLisener(new OnSureLisener() {
                    @Override
                    public void onSure(Date date) {

                        String strDate = CommonUtils.getStringDate(date);
                        mTv_InspTime.setText(strDate);
                    }
                });
                dialog.show();
            }
        });

      //检验项目
        mTv_inspItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_InspItemArr != null && _InspItemArr.length > 0) {
                    for (String item : _InspItemArr) {
                        list.add(item);
                    }
                } else {


                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                        mTv_inspItem.setText(str);

                    }
                }, list);

            }
        });

        //检验结果
        mTv_InspResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_InspResultArr != null && _InspResultArr.length > 0) {
                    for (String item : _InspResultArr) {
                        list.add(item);
                    }
                } else {
//                    list.add("润滑");

                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                        mTv_InspResult.setText(str);

                    }
                }, list);

            }
        });
        //检验方式
        mTv_InspMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_InspModeArr != null && _InspModeArr.length > 0) {
                    for (String item : _InspModeArr) {
                        list.add(item);
                    }
                } else {
//                    list.add("润滑");

                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                        mTv_InspMode.setText(str);

                    }
                }, list);

            }
        });

        mTv_jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请首先确定要检验的设备！");
                    return;
                }
                Intent intent = new Intent(InspectReportActivity.this, InspectPlanCheckActivity.class);
                intent.putExtra("_EquipmentID",_EquipmentData.getID());
                startActivityForResult(intent,CHECK_PLAN_OK);

            }
        });

        //提交
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogingDialog();

                String faultDate = mTv_InspTime.getText().toString().trim();


                String inspItem = mTv_inspItem.getText().toString().trim();
                String InspResult = mTv_InspResult.getText().toString().trim();
                String user = mEt_User.getText().toString().trim();
                String money = mEt_money.getText().toString().trim();
                String InspMode=mTv_InspMode.getText().toString().trim();
                String corp=mEt_Com.getText().toString().trim();




                //验证提交数据

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请首先确定要检验的设备！");
                    return;
                }
                if (faultDate == null || faultDate.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请选择开始检验日期！");
                    return;
                }
                if (inspItem == null || inspItem.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请选择检验项目！");
                    return;
                }

                if (InspResult == null || InspResult.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请选择检验结果！");
                    return;
                }
                if (InspMode == null || InspMode.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请选择检验方式！");
                    return;
                }
                if (user == null || user.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "请填写联系人！");
                    return;
                }
                if (money == null || money.length() == 0) {
                    ToastUtils.showLongToast(InspectReportActivity.this, "填写费用金额不能为空！");
                    return;
                }
                else{

                }

                mBtn_Submit.setEnabled(false);
                //提交数据
                InspectBillResult result = new InspectBillResult();

                InspectBillEntity rpen=new InspectBillEntity();
                rpen.setEquipmentID(_EquipmentData.getID());
                rpen.setCorporationID(_EquipmentData.getCorporationID());
                rpen.setTotalMoney(money);

                rpen.setInspectionUser(user);
                rpen.setInspectionDate(faultDate);
                rpen.setInspectionCorp(corp);
                rpen.setInspectionResult(InspResult);
                rpen.setInspectionMode(InspMode);
                rpen.setInspectionItem(inspItem);
                if(_CheckPlanIDList!=null ){
                   rpen.setInspectionPlanID(_CheckPlanIDList.get(0));
                }
                else
                    rpen.setInspectionPlanID(null);
                result.setData(rpen);





               // 提交数据
                SoapUtils.submitNewEquipInspectRepairAsync(InspectReportActivity.this, result , new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                        dismissLoadingDialog();
                        if (object == null) {
                            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err1));
                            mBtn_Submit.setEnabled(true);
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err2));
                            mBtn_Submit.setEnabled(true);
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        StringResult result = new Gson().fromJson(strData, StringResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err3));
                            mBtn_Submit.setEnabled(true);
                            return;
                        }
                        else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            mBtn_Submit.setEnabled(true);
                            return;
                        }


                        mBtn_Submit.setEnabled(false);
                        ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.activity_inspect_submit_ok));
                        finish();

                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.activity_inspect_submit_err, error.getMessage()));
                        mBtn_Submit.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.activity_inspect_submit_fail, fault));
                        mBtn_Submit.setEnabled(true);
                    }
                });

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
     * 根据设备条码获取设备信息
     *
     * @param barcode
     */
    private void getEquipmentInfo(String barcode) {
        SoapUtils.getEquipmentByBarcodeAsync(InspectReportActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "1获取保养信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "2获取保养信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "3获取保养信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "4获取保养信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(InspectReportActivity.this, "获取保养信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(InspectReportActivity.this, "获取保养信息失败:" + fault);
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
        } else {//没有获取到设备信息
            mTv_EquipCode.setText("");
            mTv_EquipName.setText("");
            mTv_EquipCorp.setText("");
            mTv_EquipDept.setText("");
            mTv_EquipKeeper.setText("");
            mTv_EquipLocation.setText("");
            mIv_EquipImg.setImageBitmap(null);
        }
    }

    /**
     * 根据设备ID获取设备信息
     *
     * @param equipID
     */
    private void getEquipmentInfoByID(String equipID) {
        SoapUtils.getEquipmentByIDAsync(InspectReportActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "1获取保养信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "2获取保养信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "3获取保养信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(InspectReportActivity.this, "4获取保养信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(InspectReportActivity.this, "获取保养信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(InspectReportActivity.this, "获取保养信息失败:" + fault);
            }
        });
    }


    private void getReport(String rptID) {

        showLogingDialog();

        SoapUtils.getInspectReportAsync(InspectReportActivity.this, rptID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);

                InspectBillResult result = new Gson().fromJson(strData, InspectBillResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                _ReportEntity = result.getData();
                _CheckPlanEntityList=result.getlistInspPlan();
                showLogingDialog();

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                showLogingDialog();
                ToastUtils.showToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                showLogingDialog();
                ToastUtils.showToast(InspectReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 根据条码检索设备
     *
     * @param barcode
     */
    private void SearchBarcode(String barcode) {
        if (barcode == null) {
            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(InspectReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        } else {
            showLogingDialog();
            //根据条码内容检索设备信息
            getEquipmentInfo(barcode);
            dismissLoadingDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.e("TAG", "ActivityResult resultCode error");
            return;
        }
        switch (requestCode) {

            case CHECK_PLAN_OK:

                    _CheckPlanIDList = data.getExtras().getStringArrayList("_CheckPlanIDList");//得到新Activity 关闭后返回的数据
                    _CheckPlanEntityList = (List<InspectionPlanEntity>)data.getSerializableExtra("_CheckInspectPlanList");
                    if(_CheckPlanEntityList.size()==0){
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    }else {
                        mLv_Show_plan.setAdapter(null);
                        adapter_Plan= new InspectPlanViewDataListAdapter(this, _CheckPlanEntityList);
                        mLv_Show_plan.setSelection(adapter_Plan.getCount());


                        mLv_Show_plan.setAdapter(adapter_Plan);
                        adapter_Plan.notifyDataSetChanged();
                        mLv_Show_plan.setVisibility(View.VISIBLE);
                        setListViewHeightBasedOnChildren(mLv_Show_plan);
                    }

                    ToastUtils.showLongToast(InspectReportActivity.this,"共获取到" + _CheckPlanEntityList.size() + "条检验计划");
                break;

            case CAMERA_BARCODE_SCAN://相机扫码

                IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
                if (result != null) {
                    if (result.getContents() == null) {
                        Toast.makeText(this, "扫码异常请重试！", Toast.LENGTH_LONG).show();
                    } else {
                        String barcode = result.getContents();
                        mSearchBar.setBarcode(barcode);
                    }
                } else {
                    super.onActivityResult(requestCode, resultCode, data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    public void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            // 计算子项View 的宽高
            listItem.measure(0, 0);
            // 统计所有子项的总高度
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
    /**
     * 利用正则表达式判断字符串是否是数字
     * @param str
     * @return
     */
    public boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /***
     * 绑定检验单到页面
     */
    private void bindUIData(InspectBillEntity entity) {
        if (entity != null) {
            SimpleDateFormat formatter;
            formatter = new SimpleDateFormat ("yyyy-MM-dd KK:mm:ss a");
            String ctime = formatter.format(entity.getInspectionDate());
            mTv_InspTime.setText(ctime.substring(0,10));
            mTv_inspItem.setText(entity.getInspectionItem());
            mTv_InspResult.setText(entity.getInspectionResult());
            mTv_InspMode.setText(entity.getInspectionMode());
            mEt_Com.setText(entity.getInspectionCorp());
            mEt_User.setText(entity.getInspectionUser());
            mEt_money.setText(entity.getTotalMoney());
            if(_CheckPlanEntityList==null || _CheckPlanEntityList.size()==0){
                mLv_Show_plan.setVisibility(View.GONE);
                mLv_Show_plan.setAdapter(null);
            }else {
                mLv_Show_plan.setAdapter(null);
                adapter_Plan= new InspectPlanViewDataListAdapter(this, _CheckPlanEntityList);
                mLv_Show_plan.setSelection(adapter_Plan.getCount());
                mLv_Show_plan.setAdapter(adapter_Plan);
                adapter_Plan.notifyDataSetChanged();
                mLv_Show_plan.setVisibility(View.VISIBLE);
                setListViewHeightBasedOnChildren(mLv_Show_plan);
            }
        }
        mEt_User.setEnabled(false);
        mEt_money.setEnabled(false);
    }
}
