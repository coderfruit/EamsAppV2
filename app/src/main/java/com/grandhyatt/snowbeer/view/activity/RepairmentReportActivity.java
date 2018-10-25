package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
import android.widget.SimpleAdapter;
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
import com.grandhyatt.snowbeer.adapter.EquipRepairSpareViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentPlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentPlanViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.ShowImagesAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentViewDataListAdapter;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingAttachmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.request.RepairmentReportingRequest;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.FailureReportingResult;
import com.grandhyatt.snowbeer.network.result.RepairmentEquipmentResult;
import com.grandhyatt.snowbeer.network.result.RepairmentResult;
import com.grandhyatt.snowbeer.network.result.SpareInEquipmentResult;
import com.grandhyatt.snowbeer.network.result.StringResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.NumberEditText;
import com.grandhyatt.snowbeer.view.SearchBarLayout;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.objectbox.annotation.Convert;

import static com.grandhyatt.snowbeer.Consts.CAMERA_BARCODE_SCAN;


/**
 * 设备维修
 */
public class RepairmentReportActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mSearchBar)
    SearchBarLayout mSearchBar;




    BroadcastReceiver mReceiver;
    IntentFilter mFilter;

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

    @BindView(R.id.mTv_FaultDate)
    TextView mTv_FaultDate;
    @BindView(R.id.mTv_FaultDate1)
    TextView mTv_FaultDate1;
    @BindView(R.id.mTv_FaultLevel)
    TextView mTv_FaultLevel;
    @BindView(R.id.mTv_FaultDesc)
    TextView mTv_FaultDesc;
    @BindView(R.id.mTv_RepairmentDesc)
    TextView mTv_RepairmentDesc;
    @BindView(R.id.mTv_jh)
    TextView mTv_jh;
    @BindView(R.id.mEt_User)
    EditText mEt_User;
    @BindView(R.id.mEt_money)
    EditText mEt_money;

    @BindView(R.id.mBtn_Submit)
    Button mBtn_Submit;
    @BindView(R.id.mBtn_marAdd)
    Button mBtn_marAdd;

    @BindView(R.id.mLv_DataList_Spare)
    ListView mLv_DataList_Spare;
    @BindView(R.id.mLv_Show_plan)
    ListView mLv_Show_plan;

    public static final int CHECK_PLAN_OK = 111;//选择执行计划返回码
    public static final int CHECK_SPARE_OK = 112;//选择维修用备件

    ArrayList<String> _CheckPlanIDList; //用户选中的维护计划ID
    List<RepairmentPlanEntity> _CheckPlanEntityList = new ArrayList<>();//用户选择的数据行对象
    List<SpareInEquipmentEntity> _CheckSpareEntityList = new ArrayList<>();//用户选择的数据行对象
    List<EquipmentUseSpareEntity> _CheckSpareUseList=new ArrayList<>();// 页面选择备品配件返回数据

    /**
     * 获取到的图片路径
     */
    private String _PicPath;
    private Uri takePhotoUrl;

    /**
     * 故障级别
     */
    String[] _FaultLevelArr;
    /**
     * 故障描述
     */
    String[] _FaultDescArr;

    MediaRecorder mRecorder;


    /**
     * 设备信息
     */
    EquipmentEntity _EquipmentData;

    RepairmentBillEntity _ReportEntity;
    RepairmentPlanViewDataListAdapter adapter_Plan = null;//维护计划适配器
    SpareInEquipmentViewDataListAdapter adapter_Spare = null;  //备件适配器
    EquipRepairSpareViewDataListAdapter adapter_SpareView=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_repairment_report);
        ButterKnife.bind(this);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        //维修            type = 0  mTv_EquipID=设备ID
        //维修-备件更换   type = 1  mTv_EquipID=设备ID    mTv_ReportID = 备件ID
        //维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID，
        //维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID

        Intent intent = getIntent();
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");

        if (mTv_ReportID != null && mTv_EquipID != null) {
            mToolBar.setTitle("查看维修信息");
            getEquipmentInfoByID(mTv_EquipID);
           getReport(mTv_ReportID);

            bindEventPart();

            //隐藏搜索栏
            mSearchBar.setVisibility(View.GONE);

            mBtn_Submit.setVisibility(View.GONE);

            mEt_User.setEnabled(false);
            mEt_money.setEnabled(false);



        } else {              //添加报修
            mToolBar.setTitle("我要维修");
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
        switch (v.getId()) {
//            case R.id.mBtn_Login:
//
//                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        fillEquipInfo(null);

        mToolBar.setTitle("设备维修");
        mToolBar.hideMenuButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mLv_Show_plan.setVisibility(View.GONE);
        //初始化用户及手机号
        mEt_User.setText(SPUtils.getLastLoginUserName(RepairmentReportActivity.this));
        //mEt_money.setText(SPUtils.getLastLoginUserPhone(RepairmentReportActivity.this));


        SoapListener callbackFailureReportingDesc = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "1获取故障描述数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "2获取故障描述数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "3获取故障描述数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "4获取故障描述数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _FaultDescArr = value.split("\\|");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(RepairmentReportActivity.this, "0获取故障描述数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(RepairmentReportActivity.this, "0获取故障描述数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(RepairmentReportActivity.this, Consts.EnumTextDictonay.FaultClass, callbackFailureReportingDesc);
        //故障级别
        SoapListener callbackFailureReportingLevel = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "1获取故障级别" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "2获取故障级别数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "3获取故障级别数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "4获取故障级别数据失败" + statusCode + result.msg);
                    return;
                }
                TextDictionaryEntity data = result.getData();
                if (data != null) {
                    String value = data.getValue();
                    if (value != null && value.length() > 0) {
                        _FaultLevelArr = value.split("\\|");
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                ToastUtils.showLongToast(RepairmentReportActivity.this, "0获取故障级别数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(RepairmentReportActivity.this, "0获取故障级别数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(RepairmentReportActivity.this, Consts.EnumTextDictonay.RepairmentLevel, callbackFailureReportingLevel);
    }

    @Override
    public void refreshUI() {

    }

    @Override
    public void requestNetworkData() {

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
                    Intent intent = new Intent(RepairmentReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(RepairmentReportActivity.this, "无图片需显示");
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
        mTv_FaultDate.setOnClickListener(new View.OnClickListener() {
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
                        mTv_FaultDate.setText(strDate);
                    }
                });
                dialog.show();
            }
        });

        mTv_FaultDate1.setOnClickListener(new View.OnClickListener() {
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
                        mTv_FaultDate1.setText(strDate);
                    }
                });
                dialog.show();
            }
        });


        mTv_FaultLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_FaultLevelArr != null && _FaultLevelArr.length > 0) {
                    for (String item : _FaultLevelArr) {
                        list.add(item);
                    }
                } else {
                    list.add("大修");
                    list.add("定修");
                    list.add("日常维修");
                }

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mTv_FaultLevel.setText(list.get(position).toString());
                        if(list.get(position).toString().equals("大修")){
                            if(_CheckSpareEntityList!=null && _CheckSpareEntityList.size()>0){
                                _CheckSpareEntityList.clear();
                                mLv_Show_plan.setVisibility(View.GONE);
                                mLv_Show_plan.setAdapter(null);
                            }

                        }
                        else {

                            if(_CheckPlanEntityList!=null && _CheckPlanEntityList.size()>0){
                                _CheckPlanEntityList.clear();
                                _CheckPlanIDList.clear();
                                mLv_Show_plan.setVisibility(View.GONE);
                                mLv_Show_plan.setAdapter(null);
                            }
                        }
                    }
                }, list);
            }
        });

        mTv_FaultDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_FaultDescArr != null && _FaultDescArr.length > 0) {
                    for (String item : _FaultDescArr) {
                        list.add(item);
                    }
                } else {
                    list.add("机械故障");
                    list.add("电气故障");
                    list.add("传动故障");
                    list.add("润滑故障");
                    list.add("散热故障");
                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                            mTv_FaultDesc.setText(str);
                    }
                }, list);

            }
        });
       //添加备注
        mTv_RepairmentDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            ShowInputDialogForTextView(RepairmentReportActivity.this, "请输入设备维修描述", mTv_RepairmentDesc);
            }
        });
        mTv_jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                String _ReapirLevel = mTv_FaultLevel.getText().toString();
                if (_ReapirLevel == null || _ReapirLevel.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择维修级别！");
                    return;
                }

                Intent intent = new Intent(RepairmentReportActivity.this, RepairmentPlanCheckActivity.class);
                intent.putExtra("_EquipmentID",_EquipmentData.getID());
                intent.putExtra("_ReapirLevel", _ReapirLevel);
                startActivityForResult(intent,CHECK_PLAN_OK);

            }
        });

        mBtn_marAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                Intent intent = new Intent(RepairmentReportActivity.this, EquipMgrRepairSpareActivity.class);
                intent.putExtra("_EquipmentID",_EquipmentData.getID());
                startActivityForResult(intent,CHECK_SPARE_OK);


            }
        });


        //长按显示删除
        mLv_DataList_Spare.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                _CheckSpareUseList.remove(position);
//                adapter_SpareView.notifyDataSetChanged();
                TextView mTv_ReportID = (TextView) view.findViewById(R.id.mTv_SpareID);
                deleteRow(mTv_ReportID.getText().toString(),position);
                setListViewHeightBasedOnChildren(mLv_DataList_Spare);
               return false;
            }
        });
        //提交
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showLogingDialog();

                String faultDate = mTv_FaultDate.getText().toString().trim();
                String faultDate1 = mTv_FaultDate1.getText().toString().trim();
                String faultLevel = mTv_FaultLevel.getText().toString().trim();
                String faultDesc = mTv_FaultDesc.getText().toString().trim();
                String RepairmentDesc = mTv_RepairmentDesc.getText().toString().trim();
                String user = mEt_User.getText().toString().trim();
                String phone = mEt_money.getText().toString().trim();



                //验证提交数据
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                if (faultDate == null || faultDate.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择开始维修日期！");
                    return;
                }
                if (faultDate1 == null || faultDate1.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择结束维修日期！");
                    return;
                }
                if (faultLevel == null || faultLevel.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择维修级别！");
                    return;
                }
                if (faultDesc == null || faultDesc.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择故障类型！");
                    return;
                }

                if (user == null || user.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请填写联系人！");
                    return;
                }
                if (phone == null || phone.length() == 0) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "填写费用金额不能为空！");
                    return;
                }
                else{

                }
                if(_CheckSpareUseList==null || _CheckSpareUseList.size()==0){
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请添加备品备件信息！");
                    return;
                }


                mBtn_Submit.setEnabled(false);


                //提交数据

                RepairmentEquipmentResult request = new RepairmentEquipmentResult();
              //  request.setData(_EquipmentData);

                if(faultLevel.equals("大修")){
                    if(_CheckPlanIDList==null || _CheckPlanIDList.size()==0){
                        ToastUtils.showLongToast(RepairmentReportActivity.this, "未找到【大修】的维修计划，请选择！");
                        return;
                    }

                }
                else {
                    _CheckPlanIDList.clear();
                }

                RepairmentBillEntity rpen=new RepairmentBillEntity();
                rpen.setEquipmentID(_EquipmentData.getID());
                rpen.setCorporationID(_EquipmentData.getCorporationID());
                rpen.setDescription(RepairmentDesc);
                rpen.setRepairUser(user);
                rpen.setTotalMoney(phone);
                rpen.setRepairmentLevel(faultLevel);
                rpen.setFaultLevel(faultDesc);
                rpen.setStartTime(faultDate);
                rpen.setFinishTime(faultDate1);
                rpen.setShutDownMinutes("0");
                request.setRepairmentBillData(rpen);
                View vw=null;
                TextView tvid=null;
                NumberEditText mNEdt_Check=null;
                TextView mSumCount=null;
                if(_CheckSpareUseList==null || _CheckSpareUseList.size()==0){
                    mBtn_Submit.setEnabled(true);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择维修所需备件！");
                    return;
                }
                for (int i = 0; i < mLv_DataList_Spare.getChildCount(); i++) {
                    vw = mLv_DataList_Spare.getChildAt(i);
                     tvid= (TextView) vw.findViewById(R.id.mTv_SpareID);
                    int edtcount=0;
                    int sumcount=0;
                    for (EquipmentUseSpareEntity e : _CheckSpareUseList){
                        if(e.getSpareID()==tvid.getText().toString().trim()){
                               mNEdt_Check= (NumberEditText) vw.findViewById(R.id.mNEdt_Check);
                            mSumCount= (TextView) vw.findViewById(R.id.mTv_SumCountx);
                             edtcount=mNEdt_Check.getData();
                             sumcount=Integer.parseInt( mSumCount.getText().toString().trim());
                            if(edtcount>sumcount){
                                mBtn_Submit.setEnabled(true);
                                ToastUtils.showLongToast(RepairmentReportActivity.this, "备件:"+e.getSpareName()+" 数量超出库存数量！");
                                return;
                            }
                            else {
                                e.setCount(Integer.toString(edtcount));

                            }
                        }
                    }
                }
                request.setSpareInEquipmentData(_CheckSpareUseList);
                //提交数据
                SoapUtils.submitNewEquipReparimentRepairAsync(RepairmentReportActivity.this, request ,_CheckPlanIDList, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                       dismissLoadingDialog();
                        if (object == null) {
                            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        StringResult result = new Gson().fromJson(strData, StringResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        }
                        else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }


                            mBtn_Submit.setEnabled(false);
                            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.activity_repairment_submit_ok));
                            finish();

                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.activity_repairment_submit_err, error.getMessage()));
                        mBtn_Submit.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.activity_repairment_submit_fail, fault));
                        mBtn_Submit.setEnabled(true);
                    }
                });

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
            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
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

            case CHECK_PLAN_OK: //yangcm 0917 获取用户选择计划id

                if(mTv_FaultLevel.getText().toString().equals("大修"))   //大修
                {

                    _CheckSpareEntityList.clear();
                    _CheckPlanIDList = data.getExtras().getStringArrayList("_CheckPlanIDList");//得到新Activity 关闭后返回的数据
                    _CheckPlanEntityList = (List<RepairmentPlanEntity>)data.getSerializableExtra("_CheckEntityList");
                      if(_CheckPlanEntityList.size()==0){
                          mLv_Show_plan.setVisibility(View.GONE);
                          mLv_Show_plan.setAdapter(null);
                      }else {
                          mLv_Show_plan.setAdapter(null);
                          adapter_Plan= new RepairmentPlanViewDataListAdapter(this, _CheckPlanEntityList);
                          mLv_Show_plan.setSelection(adapter_Plan.getCount());


                          mLv_Show_plan.setAdapter(adapter_Plan);
                          mLv_Show_plan.setVisibility(View.VISIBLE);
                          setListViewHeightBasedOnChildren(mLv_Show_plan);
                      }

                    ToastUtils.showLongToast(RepairmentReportActivity.this,"共获取到" + _CheckPlanEntityList.size() + "条维护计划");
                }
                else//定修、日常维修
                {
                    _CheckPlanEntityList.clear();
                    _CheckSpareEntityList = (List<SpareInEquipmentEntity>) data.getSerializableExtra("_CheckEntityList");
                    if (_CheckSpareEntityList.size() == 0) {
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    } else {
                        mLv_Show_plan.setAdapter(null);
                        adapter_Spare = new SpareInEquipmentViewDataListAdapter(this, _CheckSpareEntityList);
                        mLv_Show_plan.setAdapter(adapter_Spare);
                        mLv_Show_plan.setVisibility(View.VISIBLE);
                        setListViewHeightBasedOnChildren(mLv_Show_plan);
                    }

                    ToastUtils.showLongToast(RepairmentReportActivity.this, "共获取到" + _CheckSpareEntityList.size() + "条备件记录");
                }


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
            case CHECK_SPARE_OK:
                _CheckSpareUseList.clear();
                _CheckSpareUseList = (List<EquipmentUseSpareEntity>)data.getSerializableExtra("_CheckEntityList");
                if(_CheckSpareUseList != null && _CheckSpareUseList.size() > 0){
                    adapter_SpareView = new EquipRepairSpareViewDataListAdapter(RepairmentReportActivity.this, _CheckSpareUseList);
                    mLv_DataList_Spare.setAdapter(adapter_SpareView);
                    setListViewHeightBasedOnChildren(mLv_DataList_Spare);

                }


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
     * 根据设备条码获取设备信息
     *
     * @param barcode
     */
    private void getEquipmentInfo(String barcode) {
        SoapUtils.getEquipmentByBarcodeAsync(RepairmentReportActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentReportActivity.this, "获取设备信息失败:" + fault);
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
        SoapUtils.getEquipmentByIDAsync(RepairmentReportActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentReportActivity.this, "获取设备信息失败:" + fault);
            }
        });
    }

    /**
     * 获取单个报修信息
     *
     * @param rptID 报修ID
     */
    private void getReport(String rptID) {

        showLogingDialog();

        RepairmentReportingRequest request = new RepairmentReportingRequest();
        request.setID(rptID);

        SoapUtils.getRepairmentReportAsync(RepairmentReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                RepairmentResult result = new Gson().fromJson(strData, RepairmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                _ReportEntity = result.getData();

                if (_ReportEntity != null) {

                     SimpleDateFormat formatter;
                       formatter = new SimpleDateFormat ("yyyy-MM-dd KK:mm:ss a");
                        String ctime = formatter.format(_ReportEntity.getStartTime());
                    String ctime1 = formatter.format(_ReportEntity.getFinishTime());
                    mTv_FaultDate.setText(ctime.substring(0,10));
                    mTv_FaultDate1.setText(ctime1.substring(0,10));
                    mTv_FaultLevel.setText(_ReportEntity.getFaultLevel());
                    mTv_FaultDesc.setText(_ReportEntity.getDescription());
                    mEt_User.setText(_ReportEntity.getRepairUser());
                    mEt_money.setText(String.valueOf(_ReportEntity.getTotalMoney()));
//                    mTv_ReportNO.setVisibility(View.VISIBLE);
//                    mTv_ReportNO.setText(_ReportEntity.getReportNO());



                }

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

                ToastUtils.showToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {

                ToastUtils.showToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }



    /**
     * 获取文件路径
     * @param entity
     * @param file
     * @return
     */
    @NonNull
    private String getFilePath(FailureReportingEntity entity, FailureReportingAttachmentEntity file) {
        String publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
        String rptNO = entity.getReportNO();
        return publicDir + "/" + rptNO + "/" + file.getFileGuid() + "." + file.getFileType();
    }

    private void deleteRow(final String RptID,final  int pron) {
        List<String> menuList = new ArrayList<String>();
        menuList.add("删除");
        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        //删除
                        EquipmentUseSpareEntity eus=(EquipmentUseSpareEntity)adapter_SpareView.getItem(pron);
                               _CheckSpareUseList.remove(eus);
                                adapter_SpareView.removeItem(pron);
                        break;
                    default:
                        break;
                }
            }
        }, menuList);

    }
}
