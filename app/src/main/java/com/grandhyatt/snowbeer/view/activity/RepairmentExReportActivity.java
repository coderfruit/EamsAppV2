package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
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
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.EquipRepairSpareShowViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.EquipRepairSpareViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentExPlanViewDataListAdapter;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingAttachmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentExPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.RepairmentReportingRequest;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.RepairmentEquipmentResult;
import com.grandhyatt.snowbeer.network.result.RepairmentResult;
import com.grandhyatt.snowbeer.network.result.StringResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.view.NumberEditText;
import com.grandhyatt.snowbeer.view.SearchBarLayout;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.grandhyatt.snowbeer.Consts.CAMERA_BARCODE_SCAN;


/**
 * 设备外委维修操作界面
 */
public class RepairmentExReportActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    @BindView(R.id.mTv_FaultDesc)
    TextView mTv_FaultDesc;
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
    List<RepairmentExPlanEntity> _CheckPlanEntityList = new ArrayList<>();//用户选择的数据行对象
    List<EquipmentUseSpareEntity> _CheckSpareUseList=new ArrayList<>();// 页面选择备品配件返回数据
    List<SpareInEquipmentEntity> _CheckSpareEquiList=new ArrayList<>();// 页面选择备品配件返回数据
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
    RepairmentExPlanViewDataListAdapter adapter_Plan = null;//维护计划适配器
    EquipRepairSpareShowViewDataListAdapter adapter_SpareView = null;  //备件适配器
    EquipRepairSpareViewDataListAdapter adapter_Spare=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_repairmentex_report);
        ButterKnife.bind(this);
        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        //外委维修            type = 0  mTv_EquipID=设备ID
        //外委维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID，
        //外委维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");

        //外委维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID
        if ((type != null && type.equals("3")) && mTv_ReportID != null && mTv_EquipID != null) {
            mToolBar.setTitle("查看维修信息");
            getEquipmentInfoByID(mTv_EquipID);
            getReport(mTv_ReportID);
            bindEventPart();
            bindEvent();
            //隐藏搜索栏
            mSearchBar.setVisibility(View.GONE);
            mBtn_Submit.setVisibility(View.GONE);
            mEt_User.setEnabled(false);
            mEt_money.setEnabled(false);
        }
        //外委维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID，
        else if ((type != null && type.equals("2")) && mTv_ReportID != null && mTv_EquipID != null) {


        }
        //外委维修            type = 0  mTv_EquipID=设备ID
        else if ((type != null && type.equals("0")) && mTv_EquipID != null) {

        }
        //正常外委维修
        else {
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

        mToolBar.setTitle("设备外委维修");
        mToolBar.hideMenuButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mLv_Show_plan.setVisibility(View.GONE);
        //初始化用户及手机号
       // mEt_User.setText(SPUtils.getLastLoginUserName(RepairmentExReportActivity.this));
        //mEt_money.setText(SPUtils.getLastLoginUserPhone(RepairmentReportActivity.this));

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
                    Intent intent = new Intent(RepairmentExReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(RepairmentExReportActivity.this, "无图片需显示");
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

        mTv_jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                Intent intent = new Intent(RepairmentExReportActivity.this, RepairmentExPlanCheckActivity.class);
                intent.putExtra("_EquipmentID",_EquipmentData.getID());

                startActivityForResult(intent,CHECK_PLAN_OK);

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
                    list.add("包工包料");
                    list.add("包工不包料");

                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                            mTv_FaultDesc.setText(str);
                        if(str.equals("包工包料")){
                            if(_CheckSpareUseList!=null && _CheckSpareUseList.size()>0){
                               _CheckSpareUseList.clear();
                                mLv_DataList_Spare.setAdapter(null);
                                adapter_Spare.notifyDataSetChanged();
                            }
                        }
                        else {

                            if(_CheckSpareEquiList!=null && _CheckSpareEquiList.size()>0){
                                _CheckSpareEquiList.clear();
                                mLv_DataList_Spare.setAdapter(null);
                                adapter_SpareView.notifyDataSetChanged();
                            }
                        }
                    }
                }, list);

            }
        });

        mBtn_marAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                String wtsx=mTv_FaultDesc.getText().toString().trim();
                if(wtsx==""){
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请首先确定外委委托形式！");
                    return;
                }
                if(wtsx.equals("包工不包料")){
                    Intent intent = new Intent(RepairmentExReportActivity.this, EquipMgrRepairSpareActivity.class);
                    intent.putExtra("_EquipmentID",_EquipmentData.getID());
                    startActivityForResult(intent,CHECK_SPARE_OK);
                }
                else{
                    Intent intent = new Intent(RepairmentExReportActivity.this, EquipMgrRepairExSpareCheckActivity.class);
                    intent.putExtra("_EquipmentID",_EquipmentData.getID());
                    startActivityForResult(intent,CHECK_SPARE_OK);
                }

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

                String faultDesc = mTv_FaultDesc.getText().toString().trim();

                String user = mEt_User.getText().toString().trim();
                String phone = mEt_money.getText().toString().trim();


                //验证提交数据
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请首先确定要维修的设备！");
                    return;
                }
                if (faultDate == null || faultDate.length() == 0) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请选择开始维修日期！");
                    return;
                }
                if (faultDate1 == null || faultDate1.length() == 0) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请选择结束维修日期！");
                    return;
                }

                if (faultDesc == null || faultDesc.length() == 0) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请选择故障类型！");
                    return;
                }

                if (user == null || user.length() == 0) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "请填写联系人！");
                    return;
                }
                if (phone == null || phone.length() == 0) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "填写费用金额不能为空！");
                    return;
                }
                else{

                }
                if(faultDesc.equals("包工不包料")){
                    if(_CheckSpareUseList==null || _CheckSpareUseList.size()==0){
                        ToastUtils.showLongToast(RepairmentExReportActivity.this, "请添加备件信息！");
                        return;
                    }
                }
                else if(faultDesc.equals("包工包料")){
                    if(_CheckSpareEquiList==null || _CheckSpareEquiList.size()==0){
                        ToastUtils.showLongToast(RepairmentExReportActivity.this, "请添加备件信息！");
                        return;
                    }
                }



               // mBtn_Submit.setEnabled(false);


                //提交数据

                RepairmentEquipmentResult request = new RepairmentEquipmentResult();
              //  request.setData(_EquipmentData);

                RepairmentBillEntity rpen=new RepairmentBillEntity();
                rpen.setEquipmentID(_EquipmentData.getID());
                rpen.setCorporationID(_EquipmentData.getCorporationID());

                rpen.setRepairUser(user);
                rpen.setTotalMoney(phone);

                rpen.setFaultClass(faultDesc);
                rpen.setStartTime(faultDate);
                rpen.setFinishTime(faultDate1);
                rpen.setShutDownMinutes("0");
                request.setRepairmentBillData(rpen);
                View vw=null;
                TextView tvid=null;
                NumberEditText mNEdt_Check=null;
                TextView mSumCount=null;

                if(faultDesc.equals("包工不包料")){
                    if(_CheckSpareUseList!=null || _CheckSpareUseList.size()!=0){

                        for (int i = 0; i < mLv_DataList_Spare.getChildCount(); i++) {
                            vw = mLv_DataList_Spare.getChildAt(i);
                            tvid= (TextView) vw.findViewById(R.id.mTv_SpareID);
                            double edtcount=0;
                            int sumcount=0;
                            for (EquipmentUseSpareEntity e : _CheckSpareUseList){
                                if(e.getSpareID()==tvid.getText().toString().trim()){
                                    mNEdt_Check= (NumberEditText) vw.findViewById(R.id.mNEdt_Check);
                                    mSumCount= (TextView) vw.findViewById(R.id.mTv_SumCountx);
                                    edtcount=mNEdt_Check.getData();
                                    sumcount=Integer.parseInt( mSumCount.getText().toString().trim());
                                    if(edtcount>sumcount){
                                        mBtn_Submit.setEnabled(true);
                                        ToastUtils.showLongToast(RepairmentExReportActivity.this, "备件:"+e.getSpareName()+" 数量超出库存数量！");
                                        return;
                                    }
                                    else {
                                        e.setCount(Double.toString(edtcount));

                                    }
                                }
                            }
                        }
                        request.setSpareInEquipmentData(_CheckSpareUseList);
                    }
                }
                else if(faultDesc.equals("包工包料")) {

                    if(_CheckSpareEquiList!=null || _CheckSpareEquiList.size()!=0){
                        for (int i = 0; i < mLv_DataList_Spare.getChildCount(); i++) {
                            vw = mLv_DataList_Spare.getChildAt(i);
                            tvid= (TextView) vw.findViewById(R.id.mTv_SpareID);
                            double edtcount=0;
                            int sumcount=0;
                            for (SpareInEquipmentEntity e : _CheckSpareEquiList){
                                if(e.getSpareID()==tvid.getText().toString().trim()){
                                    mNEdt_Check= (NumberEditText) vw.findViewById(R.id.mNEdt_Check);
                                    mSumCount= (TextView) vw.findViewById(R.id.mTv_SumCountx);
                                    edtcount=mNEdt_Check.getData();
                                    sumcount=Integer.parseInt( mSumCount.getText().toString().trim());
                                    if(edtcount>sumcount){
                                        mBtn_Submit.setEnabled(true);
                                        ToastUtils.showLongToast(RepairmentExReportActivity.this, "备件:"+e.getSpareName()+" 数量超出库存数量！");
                                        return;
                                    }
                                    else {
                                        e.setReplaceCount(Double.toString(edtcount));

                                    }
                                }
                            }
                        }
                        request.setSpareE(_CheckSpareEquiList);
                    }
                }


                //提交数据
                SoapUtils.submitNewEquipReparimentExRepairAsync(RepairmentExReportActivity.this,faultDesc, request ,_CheckPlanIDList, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                       dismissLoadingDialog();
                        mBtn_Submit.setEnabled(true);
                        if (object == null) {
                            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        StringResult result = new Gson().fromJson(strData, StringResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        }
                        else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }


                            mBtn_Submit.setEnabled(true);
                            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.activity_repairment_submit_ok));
                            finish();

                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.activity_repairment_submit_err, error.getMessage()));
                        mBtn_Submit.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.activity_repairment_submit_fail, fault));
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
            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
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
                    _CheckPlanEntityList = (List<RepairmentExPlanEntity>)data.getSerializableExtra("_CheckEntityList");
                      if(_CheckPlanEntityList.size()==0){
                          mLv_Show_plan.setVisibility(View.GONE);
                          mLv_Show_plan.setAdapter(null);
                      }else
                          {
                          mLv_Show_plan.setAdapter(null);
                          adapter_Plan= new RepairmentExPlanViewDataListAdapter(this, _CheckPlanEntityList);
                          mLv_Show_plan.setSelection(adapter_Plan.getCount());


                          mLv_Show_plan.setAdapter(adapter_Plan);
                          mLv_Show_plan.setVisibility(View.VISIBLE);
                         setListViewHeightBasedOnChildren(mLv_Show_plan);
                      }

                    ToastUtils.showLongToast(RepairmentExReportActivity.this,"共获取到" + _CheckPlanEntityList.size() + "条外委维护计划");



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
                if(mTv_FaultDesc.getText().toString().trim().equals("包工不包料")){
                    _CheckSpareUseList.clear();
                    _CheckSpareEquiList.clear();
                    _CheckSpareUseList = (List<EquipmentUseSpareEntity>)data.getSerializableExtra("_CheckEntityList");
                    if(_CheckSpareUseList != null && _CheckSpareUseList.size() > 0){

                        adapter_Spare = new EquipRepairSpareViewDataListAdapter(RepairmentExReportActivity.this, _CheckSpareUseList);
                        mLv_DataList_Spare.setAdapter(adapter_Spare);
                        adapter_Spare.notifyDataSetChanged();
                        setListViewHeightBasedOnChildren(mLv_DataList_Spare);

                    }
                }
                else if (mTv_FaultDesc.getText().toString().trim().equals("包工包料")) {
                    _CheckSpareEquiList.clear();
                    _CheckSpareUseList.clear();
                    _CheckSpareEquiList = (List<SpareInEquipmentEntity>)data.getSerializableExtra("_CheckEntityList");
                    if(_CheckSpareEquiList != null && _CheckSpareEquiList.size() > 0){

                        adapter_SpareView = new EquipRepairSpareShowViewDataListAdapter(RepairmentExReportActivity.this, _CheckSpareEquiList);
                        mLv_DataList_Spare.setAdapter(adapter_SpareView);
                        adapter_SpareView.notifyDataSetChanged();
                       setListViewHeightBasedOnChildren(mLv_DataList_Spare);

                    }
                }
                else{
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
        SoapUtils.getEquipmentByBarcodeAsync(RepairmentExReportActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);
                dismissLoadingDialog();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentExReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentExReportActivity.this, "获取设备信息失败:" + fault);
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
        SoapUtils.getEquipmentByIDAsync(RepairmentExReportActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentExReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(RepairmentExReportActivity.this, "获取设备信息失败:" + fault);
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

        SoapUtils.getRepairmentReportAsync(RepairmentExReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                RepairmentResult result = new Gson().fromJson(strData, RepairmentResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
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

                    mTv_FaultDesc.setText(_ReportEntity.getDescription());
                    mEt_User.setText(_ReportEntity.getRepairUser());
                    mEt_money.setText(String.valueOf(_ReportEntity.getTotalMoney()));
//                    mTv_ReportNO.setVisibility(View.VISIBLE);
//                    mTv_ReportNO.setText(_RepairmentBillEntity.getReportNO());
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

                ToastUtils.showToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {

                ToastUtils.showToast(RepairmentExReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
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
                        if( mTv_FaultDesc.getText().toString().trim().equals("包工包料")){
                            SpareInEquipmentEntity eus=(SpareInEquipmentEntity)adapter_SpareView.getItem(pron);
                            _CheckSpareEquiList.remove(eus);
                            adapter_SpareView = new EquipRepairSpareShowViewDataListAdapter(RepairmentExReportActivity.this, _CheckSpareEquiList);
                            mLv_DataList_Spare.setAdapter(adapter_SpareView);
                            adapter_SpareView.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(mLv_DataList_Spare);
                        }
                        else {
                            EquipmentUseSpareEntity eus=(EquipmentUseSpareEntity)adapter_Spare.getItem(pron);
                            _CheckSpareUseList.remove(eus);
                            adapter_Spare = new EquipRepairSpareViewDataListAdapter(RepairmentExReportActivity.this, _CheckSpareUseList);
                            mLv_DataList_Spare.setAdapter(adapter_Spare);
                            adapter_Spare.notifyDataSetChanged();
                            setListViewHeightBasedOnChildren(mLv_DataList_Spare);
                        }

                        break;
                    default:
                        break;
                }
            }
        }, menuList);

    }
}
