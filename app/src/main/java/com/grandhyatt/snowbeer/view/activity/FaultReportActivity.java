package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.grandhyatt.snowbeer.Ctrls.MyGridView;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.ShowImagesAdapter;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingAttachmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.FailureReportingRequest;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.FailureReportingResult;
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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhouzhuo.zzimagebox.ZzImageBox;

import static com.grandhyatt.snowbeer.Consts.CAMERA_BARCODE_SCAN;
import static com.grandhyatt.snowbeer.Consts.INSPECT_OPERATE_AFTER;
import static com.grandhyatt.snowbeer.Consts.MAINTEN_OPERATE_AFTER;
import static com.grandhyatt.snowbeer.Consts.REPAIR_EX_OPERATE_AFTER;
import static com.grandhyatt.snowbeer.Consts.REPAIR_OPERATE_AFTER;

/**
 * Created by ycm on 2018/8/14.
 */

public class FaultReportActivity extends com.grandhyatt.snowbeer.view.activity.ActivityBase implements IActivityBase, View.OnClickListener {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mSearchBar)
    SearchBarLayout mSearchBar;

    @BindView(R.id.zz_image_box_add_mode)
    ZzImageBox zz_image_box_add_mode;//提交报修用-图片添加控件

    @BindView(R.id.mGv_Show_Imgs)
    MyGridView mGv_Show_Imgs;//查看报修用-图片预览控件

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
    @BindView(R.id.mTv_FaultLevel)
    TextView mTv_FaultLevel;
    @BindView(R.id.mTv_FaultDesc)
    TextView mTv_FaultDesc;
    @BindView(R.id.mEt_User)
    EditText mEt_User;
    @BindView(R.id.mEt_Phone)
    EditText mEt_Phone;
    @BindView(R.id.mBtn_Voice)
    Button mBtn_Voice;
    @BindView(R.id.mIbtn_Voice)
    android.widget.ImageButton mIbtn_Voice;
    @BindView(R.id.mBtn_Submit)
    Button mBtn_Submit;
    @BindView(R.id.mTv_EquipInfo)
    TextView mTv_ReportNO;

    @BindView(R.id.mLl_Mgr)
    LinearLayout mLl_Mgr;
    @BindView(R.id.mBtn_Repair)
    Button mBtn_Repair;
    @BindView(R.id.mBtn_Mainten)
    Button mBtn_Mainten;
    @BindView(R.id.mBtn_Inspect)
    Button mBtn_Inspect;
    @BindView(R.id.mBtn_RepairEx)
    Button mBtn_RepairEx;
    @BindView(R.id.mBtn_Close)
    Button mBtn_Close;

    /**
     * 使用照相机拍照获取图片
     */
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    /**
     * 使用相册中的图片
     */
    public static final int SELECT_PIC_BY_PICK_PHOTO = 2;
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
    String _mVoiceFilePath;//录音文件路径

    /**
     * 设备信息
     */
    EquipmentEntity _EquipmentData;

    FailureReportingEntity _ReportEntity;
    List<FailureReportingAttachmentEntity> _ReportFileEntitys;

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        setContentView(R.layout.activity_fault_report);
        ButterKnife.bind(this);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        Intent intent = getIntent();
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");
        String isMgr = intent.getStringExtra("isMgr");//是否处理报修

        if (mTv_ReportID != null && mTv_EquipID != null) {  //查看报修、处理报修
            mToolBar.setTitle("查看报修信息");
            getReport(mTv_ReportID);
            getEquipmentInfoByID(mTv_EquipID);
            bindEventPart();

            //隐藏搜索栏
            mSearchBar.setVisibility(View.GONE);
            //隐藏录音、提交按钮
            mBtn_Voice.setVisibility(View.GONE);
            mBtn_Submit.setVisibility(View.GONE);
            //禁用联系人、电话输入
            mEt_User.setEnabled(false);
            mEt_Phone.setEnabled(false);
            //隐藏添加图片、显示图片控件
            zz_image_box_add_mode.setVisibility(View.GONE);
            mGv_Show_Imgs.setVisibility(View.VISIBLE);

            if (isMgr != null && isMgr.length() > 0) {
                mLl_Mgr.setVisibility(View.VISIBLE);
                mToolBar.setTitle("处理报修");
            }

        } else if (mTv_ReportID == null && mTv_EquipID != null){// 设备巡检报修
            mToolBar.setTitle("我要报修");
            mSearchBar.setVisibility(View.GONE);
            getEquipmentInfoByID(mTv_EquipID);
            initView();
            bindEventPart();
            bindEvent();
            refreshUI();
            requestNetworkData();
        } else {                                                //添加报修
            mToolBar.setTitle("我要报修");
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
        int id = v.getId();
        switch (id) {
            case R.id.mBtn_Repair://维修
                if (_ReportEntity == null) {
                    return;
                }else{
                    if(_EquipmentData == null){
                        ToastUtils.showLongToast(FaultReportActivity.this, "加载设备信息失败，请后退并重新进入该界面！");
                        return;
                    }
                }
                Intent intent = new Intent(FaultReportActivity.this, RepairmentReportActivity.class);
                intent.putExtra("type","0");
                intent.putExtra("mTv_EquipID", _EquipmentData.getID());
                startActivityForResult(intent, REPAIR_OPERATE_AFTER);

                break;
            case R.id.mBtn_Mainten://保养
                if (_ReportEntity == null) {
                    return;
                }else{
                    if(_EquipmentData == null){
                        ToastUtils.showLongToast(FaultReportActivity.this, "加载设备信息失败，请后退并重新进入该界面！");
                        return;
                    }
                }
                Intent intent1 = new Intent(FaultReportActivity.this, MaintenReportActivity.class);
                intent1.putExtra("type","0");
                intent1.putExtra("mTv_EquipID", _EquipmentData.getID());
                startActivityForResult(intent1,MAINTEN_OPERATE_AFTER);

                break;
            case R.id.mBtn_Inspect://检验
                if (_ReportEntity == null) {
                    return;
                }else{
                    if(_EquipmentData == null){
                        ToastUtils.showLongToast(FaultReportActivity.this, "加载设备信息失败，请后退并重新进入该界面！");
                        return;
                    }
                }
                Intent intent2 = new Intent(FaultReportActivity.this, InspectReportActivity.class);
                intent2.putExtra("type","0");
                intent2.putExtra("mTv_EquipID", _EquipmentData.getID());
                startActivityForResult(intent2,INSPECT_OPERATE_AFTER);
                break;
            case R.id.mBtn_RepairEx://外委维修
                ToastUtils.showLongToast(FaultReportActivity.this, "外委维修");
                //todo
                break;
            case R.id.mBtn_Close://关闭报修
                if (_ReportEntity == null) {
                    return;
                }

                final EditText inputContrl = new EditText(FaultReportActivity.this);
                inputContrl.setFocusable(true);

                AlertDialog.Builder builder = new AlertDialog.Builder(FaultReportActivity.this);
                builder.setTitle("请输入关闭理由")
                        .setIcon(R.drawable.logo32)
                        .setView(inputContrl)
                        .setNegativeButton("取消", null);
                builder.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                final String strComment  = inputContrl.getText().toString();
                                if(strComment == null || strComment.trim().length() == 0){
                                    ToastUtils.showLongToast(FaultReportActivity.this,"请输入关闭理由");
                                    return;
                                }
                                String failureReportID = String.valueOf(_ReportEntity.getID());
                                String status =  Consts.EnumFailureStatus.已关闭.toString();//处理状态
                                String OperateResult = Consts.EnumFailureResult.已关闭.toString();//处理结果
                                String OperateID = null;//操作单据ID
                                String OperateDesc = "报修被关闭";//操作描述
                                String remark = "[关闭理由：" + strComment + "]";//备注

                                ModifyFaultReport(failureReportID, status, OperateResult, OperateID, OperateDesc, remark);

                            }
                        });
                builder.show();




                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        //fillEquipInfo(_EquipmentData);

        mToolBar.setTitle("故障报修");
        mToolBar.hideMenuButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        //初始化用户及手机号
        mEt_User.setText(SPUtils.getLastLoginUserName(FaultReportActivity.this));
        mEt_Phone.setText(SPUtils.getLastLoginUserPhone(FaultReportActivity.this));

        //故障描述
        SoapListener callbackFailureReportingDesc = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "1获取故障描述数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "2获取故障描述数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "3获取故障描述数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "4获取故障描述数据失败" + statusCode + result.msg);
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
                ToastUtils.showLongToast(FaultReportActivity.this, "0获取故障描述数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(FaultReportActivity.this, "0获取故障描述数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(FaultReportActivity.this, Consts.EnumTextDictonay.FailureReportingDesc, callbackFailureReportingDesc);
        //故障级别
        SoapListener callbackFailureReportingLevel = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "1获取故障级别" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "2获取故障级别数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "3获取故障级别数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "4获取故障级别数据失败" + statusCode + result.msg);
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
                ToastUtils.showLongToast(FaultReportActivity.this, "0获取故障级别数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(FaultReportActivity.this, "0获取故障级别数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(FaultReportActivity.this, Consts.EnumTextDictonay.FailureReportingLevel, callbackFailureReportingLevel);
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
                    Intent intent = new Intent(FaultReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(FaultReportActivity.this, "无图片需显示");
                }
                return false;
            }
        });

        //播放语音
        mIbtn_Voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playVoice(_mVoiceFilePath);

            }
        });

        //图片浏览器
        mGv_Show_Imgs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //通过自定义图片显示控件显示图片
//                Intent intent = new Intent(FaultReportActivity.this, ImageViewerActivity.class);
//                intent.putExtra("imgPath", filePath);
//                intent.putExtra("title", "报修图片" + position);
//                startActivity(intent);

                //调用系统图片浏览器显示
                Uri mUri = (Uri) mGv_Show_Imgs.getAdapter().getItem(position);

                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(mUri, "image/*");
                startActivity(it);
            }
        });

        mBtn_Repair.setOnClickListener(this);
        mBtn_Mainten.setOnClickListener(this);
        mBtn_Inspect.setOnClickListener(this);
        mBtn_RepairEx.setOnClickListener(this);
        mBtn_Close.setOnClickListener(this);
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

        //图片浏览器
        zz_image_box_add_mode.setOnImageClickListener(new ZzImageBox.OnImageClickListener() {

            @Override
            public void onImageClick(int position, String filePath, ImageView iv) {
                //通过Intent调用系统的图片查看器
                Uri mUri = CommonUtils.getPathUri(filePath);
                Intent it = new Intent(Intent.ACTION_VIEW);
                it.setDataAndType(mUri, "image/*");
                startActivity(it);
            }

            @Override
            public void onDeleteClick(int position, String filePath) {
                zz_image_box_add_mode.removeImage(position);
            }

            @Override
            public void onAddClick() {
                showMenu();//弹出菜单
            }
        });

        //故障日期
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
        //故障级别
        mTv_FaultLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_FaultLevelArr != null && _FaultLevelArr.length > 0) {
                    for (String item : _FaultLevelArr) {
                        list.add(item);
                    }
                } else {
                    list.add("一般");
                    list.add("严重");
                }

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mTv_FaultLevel.setText(list.get(position).toString());
                    }
                }, list);
            }
        });
        //故障描述
        mTv_FaultDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> list = new ArrayList<String>();
                if (_FaultDescArr != null && _FaultDescArr.length > 0) {
                    for (String item : _FaultDescArr) {
                        list.add(item);
                    }
                } else {
                    list.add("无法正常启动");
                    list.add("设备异响");
                    list.add("运行异常");
                }
                list.add("其他");

                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                        if (str.equals("其他")) {
                            ShowInputDialogForTextView(FaultReportActivity.this, "请输入故障描述", mTv_FaultDesc);
                        } else {
                            mTv_FaultDesc.setText(str);
                        }
                    }
                }, list);

            }
        });

        //录入语音故障描述
        mBtn_Voice.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        String userCode = SPUtils.getLastLoginUserCode(v.getContext());
                        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        //录音文件名格式：Eams_Voice_yangcm_20180101090802.amr
                        String tmpName = "Eams_Voice_" + userCode + "_" + timeStampFormat.format(new Date()) + ".amr";
                        String storageDir = Environment.getExternalStorageDirectory().toString();
                        String publicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString();
                        _mVoiceFilePath = publicDir + "/" + userCode + "/" + tmpName;
                        //mBtn_Voice.setBackgroundColor(Color.YELLOW);
                        mBtn_Voice.setBackgroundResource(R.drawable.ic_login_button_false);
                        startVoice(_mVoiceFilePath);
                        break;
                    case MotionEvent.ACTION_UP:
                        stopVoice(_mVoiceFilePath);
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        //提交报修
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String faultDate = mTv_FaultDate.getText().toString();
                String faultLevel = mTv_FaultLevel.getText().toString();
                String faultDesc = mTv_FaultDesc.getText().toString();
                String user = mEt_User.getText().toString();
                String phone = mEt_Phone.getText().toString();
                //验证提交数据
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请首先确定要报修的设备！");
                    return;
                }
                if (faultDate == null || faultDate.length() == 0) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请选择报修日期！");
                    return;
                }
                if (faultLevel == null || faultLevel.length() == 0) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请选择故障级别！");
                    return;
                }
                if (faultDesc == null || faultDesc.length() == 0) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请选择故障描述！");
                    return;
                }
                if (user == null || user.length() == 0) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请填写联系人！");
                    return;
                }

                if (phone == null || phone.length() == 0) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "请填写联系人电话！");
                    return;
                }

                mBtn_Submit.setEnabled(false);
                showLogingDialog();

                int imgSize = zz_image_box_add_mode.getAllImages().size();

//                if (imgSize == 0) {
//                    boolean diagValue = ShowDialog(FaultReportActivity.this, "系统提示", "没有添加故障图片是否继续？");
//                    if (diagValue) {
//                        return;
//                    }
//                }

                //获取base64格式的图片数据
                List<String> imgBase64 = new ArrayList<String>();
                List<String> imgUrls = zz_image_box_add_mode.getAllImages();
                for (String itemUrl : imgUrls) {
                    String base64 = ImageUtils.imageToBase64(itemUrl);
                    imgBase64.add(base64);
                }

                //获取base64格式的语音数据
                String voiceBase64 = null;
                try {
                    if (_mVoiceFilePath != null && _mVoiceFilePath.length() > 0) {
                        voiceBase64 = CommonUtils.encodeFileToBase64(_mVoiceFilePath);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                //提交数据
                FailureReportingRequest request = new FailureReportingRequest();
                request.setReportDate(faultDate);
                request.setEquipmentID(_EquipmentData.getID());
                request.setFailureLevel(faultLevel);
                request.setFailureDesc(faultDesc);
                request.setLinkUser(user);
                request.setLinkMobile(phone);
                request.setBase64Imgs(imgBase64);
                request.setBase64Voice(voiceBase64);

                //提交数据
                SoapUtils.submitFaultReportAsync(FaultReportActivity.this, request, new SoapListener() {
                    @Override
                    public void onSuccess(int statusCode, SoapObject object) {

                        dismissLoadingDialog();
                        if (object == null) {
                            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err1));
                            return;
                        }
                        //判断接口连接是否成功
                        if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err2));
                            return;
                        }
                        //接口返回信息正常
                        String strData = object.getPropertyAsString(0);
                        FailureReportingResult result = new Gson().fromJson(strData, FailureReportingResult.class);
                        //校验接口返回代码
                        if (result == null) {
                            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err3));
                            return;
                        } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                            return;
                        }
                        FailureReportingEntity data = result.getData();
                        if (data.getReportNO().length() > 0) {
                            mTv_ReportNO.setVisibility(View.VISIBLE);
                            mTv_ReportNO.setText(data.getReportNO());
                            mBtn_Submit.setEnabled(false);
                            mBtn_Voice.setEnabled(false);
                            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.activity_fault_report_submit_ok));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, String content, Throwable error) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.activity_fault_report_submit_err, error.getMessage()));
                        mBtn_Submit.setEnabled(true);
                    }

                    @Override
                    public void onFailure(int statusCode, SoapFault fault) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.activity_fault_report_submit_fail, fault));
                        mBtn_Submit.setEnabled(true);
                    }
                });


            }
        });


    }

    private void playVoice(String voicePath) {
        try {
            player = new MediaPlayer();
            player.setDataSource(voicePath);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "播放失败" + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 根据条码检索设备
     *
     * @param barcode
     */
    private void SearchBarcode(String barcode) {
        if (barcode == null) {
            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        } else {
            showLogingDialog();
            //根据条码内容检索设备信息
            getEquipmentInfo(barcode);
        }
    }

    /**
     * 显示操作菜单
     */
    private void showMenu() {

        List<String> menuList = new ArrayList<String>();
        menuList.add("拍照");
        menuList.add("从相册选择");

        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        takePhoto();
                        break;
                    case 1:
                        pickPhoto();
                        break;
                    default:
                        break;
                }

            }
        }, menuList);
    }

    /**
     * 从相册中查找图片
     */
    private void pickPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, SELECT_PIC_BY_PICK_PHOTO);
    }

    /**
     * 调用相机拍照
     */
    private void takePhoto() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            String filename = timeStampFormat.format(new Date());
            ContentValues values = new ContentValues(); //使用本地相册保存拍摄照片
            values.put(MediaStore.Images.Media.TITLE, filename);
            takePhotoUrl = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoUrl);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(getApplicationContext(), "内存卡不存在,无法拍照",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            Log.e("TAG", "ActivityResult resultCode error");
            return;
        }
        switch (requestCode) {
            case REPAIR_OPERATE_AFTER://维修完毕
                //得到维修Activity 关闭后返回的数据
                String OperateID = data.getExtras().getString("BillID");//操作单据ID
                String OperateDesc = data.getExtras().getString("BillNO");//操作单据号
                if(OperateID != null && OperateDesc != null) {
                    String failureReportID = String.valueOf(_ReportEntity.getID());
                    String status = Consts.EnumFailureStatus.已处理.toString();//处理状态
                    String OperateResult = Consts.EnumFailureResult.已维修.toString();//处理结果
                    ModifyFaultReport(failureReportID, status, OperateResult, OperateID, OperateDesc, "");
                    finish();
                }

                break;
            case MAINTEN_OPERATE_AFTER://保养完毕
                //得到维修Activity 关闭后返回的数据
                OperateID = data.getExtras().getString("BillID");//操作单据ID
                OperateDesc = data.getExtras().getString("BillNO");//操作单据号
                if(OperateID != null && OperateDesc != null) {
                    String failureReportID = String.valueOf(_ReportEntity.getID());
                    String status = Consts.EnumFailureStatus.已处理.toString();//处理状态
                    String OperateResult = Consts.EnumFailureResult.已保养.toString();//处理结果
                    ModifyFaultReport(failureReportID, status, OperateResult, OperateID, OperateDesc, "");
                    finish();
                }
                break;
            case INSPECT_OPERATE_AFTER://检验完毕
                //得到维修Activity 关闭后返回的数据
                OperateID = data.getExtras().getString("BillID");//操作单据ID
                OperateDesc = data.getExtras().getString("BillNO");//操作单据号
                if(OperateID != null && OperateDesc != null) {
                    String failureReportID = String.valueOf(_ReportEntity.getID());
                    String status = Consts.EnumFailureStatus.已处理.toString();//处理状态
                    String OperateResult = Consts.EnumFailureResult.已检验.toString();//处理结果
                    ModifyFaultReport(failureReportID, status, OperateResult, OperateID, OperateDesc, "");
                    finish();
                }
                break;
            case REPAIR_EX_OPERATE_AFTER://外委完毕
                //得到维修Activity 关闭后返回的数据
                OperateID = data.getExtras().getString("BillID");//操作单据ID
                OperateDesc = data.getExtras().getString("BillNO");//操作单据号
                if(OperateID != null && OperateDesc != null) {
                    String failureReportID = String.valueOf(_ReportEntity.getID());
                    String status = Consts.EnumFailureStatus.已处理.toString();//处理状态
                    String OperateResult = Consts.EnumFailureResult.已外委维修.toString();//处理结果
                    ModifyFaultReport(failureReportID, status, OperateResult, OperateID, OperateDesc, "");
                    finish();
                }
                break;
            case SELECT_PIC_BY_PICK_PHOTO://从相册查找
                Uri uri = data.getData();
                if (!TextUtils.isEmpty(uri.getAuthority())) {
                    //查询选择图片
                    Cursor cursor = getContentResolver().query(
                            uri,
                            new String[]{MediaStore.Images.Media.DATA},
                            null,
                            null,
                            null);
                    //返回 没找到选择图片
                    if (null == cursor) {
                        return;
                    }
                    cursor.moveToFirst();//光标移动至开头 获取图片路径
                    _PicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    zz_image_box_add_mode.addImage(_PicPath); //添加图片到界面控件中
                }
                break;
            case SELECT_PIC_BY_TACK_PHOTO://拍照
                Cursor cursor = null;
                if (takePhotoUrl != null) {
                    String[] proj = {MediaStore.Images.Media.DATA};
                    cursor = managedQuery(takePhotoUrl, proj, null, null, null);
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                    cursor.moveToFirst();
                    _PicPath = cursor.getString(column_index);
                    zz_image_box_add_mode.addImage(_PicPath); //添加图片到界面控件中

                    //在android 4.0及其以上的版本中，Cursor会自动关闭，不需要自己关闭。
                    if (cursor != null && !cursor.isClosed() && Build.VERSION.SDK_INT < 14) {
                        cursor.close();
                    }
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
        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    /**
     * 开始录音
     *
     * @param mFileName
     */
    private void startVoice(String mFileName) {
        // 设置录音保存路径
        String state = Environment.getExternalStorageState();

        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File directory = new File(mFileName).getParentFile();
            if (!directory.exists() && !directory.mkdirs()) {
                Log.i("startVoice", "Path to file could not be created");
            }
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mRecorder.setOutputFile(mFileName);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "打开录音失败" + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
            mRecorder.start();
        }
    }

    /**
     * 停止录音
     *
     * @param mFileName
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void stopVoice(String mFileName) {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(getApplicationContext(), "保存录音" + mFileName, Toast.LENGTH_SHORT).show();

        mIbtn_Voice.setVisibility(View.VISIBLE);

        mBtn_Voice.setBackgroundResource(R.drawable.ic_login_button_true);
    }

    /**
     * 根据设备条码获取设备信息
     *
     * @param barcode
     */
    private void getEquipmentInfo(String barcode) {
        SoapUtils.getEquipmentByBarcodeAsync(FaultReportActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(FaultReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(FaultReportActivity.this, "获取设备信息失败:" + fault);
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
        SoapUtils.getEquipmentByIDAsync(FaultReportActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(FaultReportActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(FaultReportActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(FaultReportActivity.this, "获取设备信息失败:" + fault);
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

        FailureReportingRequest request = new FailureReportingRequest();
        request.setID(rptID);

        SoapUtils.getFailureReportAsync(FaultReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                FailureReportingResult result = new Gson().fromJson(strData, FailureReportingResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                _ReportEntity = result.getData();

                if (_ReportEntity != null) {
                    mTv_EquipCode.setText(_ReportEntity.getEquipmentCode());
                    mTv_EquipName.setText(_ReportEntity.getEquipmentName());
                    mTv_FaultDate.setText(_ReportEntity.getReportDate());
                    mTv_FaultLevel.setText(_ReportEntity.getFailureLevel());
                    mTv_FaultDesc.setText(_ReportEntity.getFailureDesc());
                    mEt_User.setText(_ReportEntity.getLinkUser());
                    mEt_Phone.setText(_ReportEntity.getLinkMobile());
                    mTv_ReportNO.setVisibility(View.VISIBLE);
                    mTv_ReportNO.setText(_ReportEntity.getReportNO());

                    _ReportFileEntitys = _ReportEntity.getFailureReportingAttachmentModelList();

                    for (FailureReportingAttachmentEntity file : _ReportFileEntitys) {
                        String filePath = getFilePath(_ReportEntity, file);
                        zz_image_box_add_mode.addImage(filePath);

                    }

                    //获取报修附件信息至本地
                    getReportFiles(_ReportFileEntitys);
                }

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

                ToastUtils.showToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {

                ToastUtils.showToast(FaultReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    /**
     * 获取附件数据信息
     *
     * @param files
     */
    private void getReportFiles(final List<FailureReportingAttachmentEntity> files) {

        final List<Uri> bpPathlist = new ArrayList<>();

        final int[] idx = {0};
        for (final FailureReportingAttachmentEntity file : files) {
            //获取附件数据
            SoapUtils.getFileDataAsync(FaultReportActivity.this, file.getFileGuid(), new SoapListener() {
                @Override
                public void onSuccess(int statusCode, SoapObject object) {

                    dismissLoadingDialog();
                    if (object == null) {
                        ToastUtils.showLongToast(FaultReportActivity.this, "1获取附件信息数据失败" + statusCode);
                        return;
                    }
                    //判断接口连接是否成功
                    if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                        ToastUtils.showLongToast(FaultReportActivity.this, "2获取附件信息数据失败" + statusCode);
                        return;
                    }
                    //接口返回信息正常
                    String strData = object.getPropertyAsString(0);
                    StringResult result = new Gson().fromJson(strData, StringResult.class);

                    String filePath = getFilePath(_ReportEntity, file);
                    String base64Str = result.data;

                    byte[] btFile = CommonUtils.base64ToByte(base64Str);

                    if (file.getAttachmentType().equals("图片")) {
                        try {
                            CommonUtils.encodeBase64ToFile(btFile, filePath);
                            Uri mUri = CommonUtils.getPathUri(filePath);
                            bpPathlist.add(mUri);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            CommonUtils.encodeBase64ToFile(btFile, filePath);
                            _mVoiceFilePath = filePath;
                            mIbtn_Voice.setVisibility(View.VISIBLE);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    idx[0]++;
                    if (idx[0] == files.size()) {
                        final ShowImagesAdapter mlvAdapter = new ShowImagesAdapter(FaultReportActivity.this, bpPathlist);
                        mGv_Show_Imgs.setAdapter(mlvAdapter);
                    }

                }

                @Override
                public void onFailure(int statusCode, String content, Throwable error) {
                    idx[0]++;
                    if (idx[0] == files.size()) {
                        final ShowImagesAdapter mlvAdapter = new ShowImagesAdapter(FaultReportActivity.this, bpPathlist);
                        mGv_Show_Imgs.setAdapter(mlvAdapter);
                    }
                }

                @Override
                public void onFailure(int statusCode, SoapFault fault) {
                    idx[0]++;
                    if (idx[0] == files.size()) {
                        final ShowImagesAdapter mlvAdapter = new ShowImagesAdapter(FaultReportActivity.this, bpPathlist);
                        mGv_Show_Imgs.setAdapter(mlvAdapter);
                    }
                }
            });
        }

    }

    /**
     * 获取文件路径
     *
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

    /**
     * 修改报修信息
     * @param failureReportID 故障ID
     * @param status 处理状态(待审核，待处理，已处理)
     * @param operateResult 处理结果(已关闭、已维修、已保养、已检验)
     * @param operateID 操作单据ID可为空(维修主表ID、保养主表ID、检验主表ID)
     * @param operateDesc 操作描述，或记录处理的单号（维修主表BillNO、保养主表BillNO、检验主表BillNO）
     * @param remark 记录关闭理由或其他
     */
    private void ModifyFaultReport(String failureReportID, String status, String operateResult, String operateID, String operateDesc, String remark) {
        SoapUtils.modifyFailureReportingAsync(FaultReportActivity.this, failureReportID, status, operateResult, operateID, operateDesc, remark, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();

                if (null == object) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "接口连接失败");
                    return;
                }
                //------------------------------
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "接口连接失败！");
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                Result result = new Gson().fromJson(strData, Result.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, "接口返回信息异常");
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(FaultReportActivity.this, result.msg);
                    return;
                }
                CommonUtils.playMusic(FaultReportActivity.this);
                ToastUtils.showToast(FaultReportActivity.this, "报修关闭成功！");
                finish();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(FaultReportActivity.this, "接口访问异常，请重试");
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                if (fault != null) {
                    ToastUtils.showLongToast(FaultReportActivity.this, fault.toString());
                } else {
                    ToastUtils.showLongToast(FaultReportActivity.this, "接口访问失败，请重试");
                }
            }
        });
    }

}
