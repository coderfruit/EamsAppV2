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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.grandhyatt.snowbeer.adapter.MaintenancePlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.MaintenancePlanViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentExPlanViewDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentMaterialEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceBillEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentExPlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.MaintenReportingRequest;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.MaintenanceBillResult;
import com.grandhyatt.snowbeer.network.result.StringResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
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
import static com.grandhyatt.snowbeer.utils.CommonUtils.compareDateMinutes;

/**
 * 设备保养操作界面
 * Created by tongzhiqiang on 2018-10-08.
 */

public class MaintenReportActivity extends ActivityBase implements IActivityBase, View.OnClickListener {
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
    @BindView(R.id.mBtn_marAdd)
    Button mBtn_marAdd;
    @BindView(R.id.mTv_jh)
    Button mTv_jh;
    @BindView(R.id.mEt_User)
    EditText mEt_User;
    @BindView(R.id.mEt_money)
    EditText mEt_money;
    @BindView(R.id.mTv_RepairmentDesc)
    TextView mTv_RepairmentDesc;

    @BindView(R.id.mLv_Show_plan)
    ListView mLv_Show_plan;
    BroadcastReceiver mReceiver;
    IntentFilter mFilter;

    @BindView(R.id.mTv_FaultDate)
    TextView mTv_FaultDate;
    @BindView(R.id.mTv_FaultDate1)
    TextView mTv_FaultDate1;
    @BindView(R.id.mTv_FaultDesc)
    TextView mTv_FaultDesc;
    @BindView(R.id.mTv_materialstand)
    TextView mTv_materialstand;
    @BindView(R.id.mTv_materialName)
    TextView mTv_materialName;
    @BindView(R.id.mTv_materialUnit)
    TextView mTv_materialUnit;
    @BindView(R.id.mEt_materialsum)
    EditText mEt_materialsum;
//    @BindView(R.id.mEt_materialprice)
//    EditText mEt_materialprice;
    @BindView(R.id.mLl_Plan)
    LinearLayout mLl_Plan;//保养计划容器


    @BindView(R.id.lL_material)
    LinearLayout lL_material;
    EquipmentEntity _EquipmentData;
    MaintenanceBillEntity _ReportEntity;
    List<MaintenanceItemEntity> _ReportFileEntitys;
    String[] _FaultDescArr;
    public static final int CHECK_PLAN_OK = 111;//选择执行计划返回码
    public static final int CHECK_MATERIAL_OK = 112;//选择维修用备件

    List<MaintenancePlanEntity> _CheckPlanEntityList = new ArrayList<>();//用户选择的数据行对象
    List<EquipmentMaterialEntity> _CheckMaterialList = new ArrayList<>();//用户选择的数据行对象
    MaintenancePlanCheckDataListAdapter adapter_Plan = null;
    private String _Type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_mainten_report);
        ButterKnife.bind(this);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        //保养            type = 0    mTv_EquipID=设备id
        //保养-保养计划   type = 2    mTv_EquipID=设备id  mTv_ReportID = 保养计划ID   entity = 保养计划对象
        //保养-显示保养单 type = 3    mTv_EquipID=设备id  mTv_ReportID = 保养单ID

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type != null)
            _Type = type;
        else
            _Type = "9";
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        Object entity = (Object) intent.getSerializableExtra("entity");
        //------------------------------------------------------------------------------------------------------
        //保养-显示保养单 type = 3    mTv_EquipID=设备id  mTv_ReportID = 保养单ID
        if ((type != null && type.equals("3")) && mTv_ReportID != null && mTv_EquipID != null) {
            getEquipmentInfoByID(mTv_EquipID);
            getReport(mTv_ReportID);
            mToolBar.setTitle("设备保养");
            lL_material.setVisibility(View.GONE);
            initView();
            bindEventPart();
            bindEvent();
            bindEvent_PlanRemove();
            refreshUI();
            requestNetworkData();
        }
        //------------------------------------------------------------------------------------------------------
        //保养-保养计划  type = 2    mTv_EquipID=设备id  mTv_ReportID = 保养计划ID
        else if ((type != null && type.equals("2")) && mTv_ReportID != null && mTv_EquipID != null) {
            mSearchBar.setVisibility(View.GONE);
            mToolBar.setTitle("设备保养-按计划");
            initView();
            bindEvent();
            mTv_FaultDesc.setEnabled(false);



            //保养计划
            Repair_RepairmentPlan(mTv_EquipID, entity);
        }
        //------------------------------------------------------------------------------------------------------
        //保养            type = 0    mTv_EquipID=设备id
        else if ((type != null && type.equals("0")) && mTv_EquipID != null) {
            mSearchBar.setVisibility(View.GONE);
            mToolBar.setTitle("设备保养");
            initView();
            bindEvent();
            bindEvent_PlanRemove();
            mTv_FaultDesc.setEnabled(true);
            mLl_Plan.setVisibility(View.GONE);
            mLv_Show_plan.setAdapter(null);
            lL_material.setVisibility(View.GONE);
            mTv_materialName.setText("");
            mTv_materialstand.setText("");
            mTv_materialUnit.setText("");
          //  mEt_materialprice.setText("0");
            mEt_materialsum.setText("0");
            //根据设备id获取设备信息
            getEquipmentInfoByID(mTv_EquipID);
        }
        //------------------------------------------------------------------------------------------------------
        //正常保养
        else {
            mToolBar.setTitle("设备保养");
            lL_material.setVisibility(View.GONE);
            initView();
            bindEventPart();
            bindEvent();
            bindEvent_PlanRemove();
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
        mEt_User.setText(SPUtils.getLastLoginUserName(MaintenReportActivity.this));
        mLl_Plan.setVisibility(View.GONE);

        SoapListener callbackFailureReportingDesc = new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                if (object == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "1获取保养类型数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "2获取保养类型数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                TextDictoryResult result = new Gson().fromJson(strData, TextDictoryResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "3获取保养类型数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "4获取保养类型数据失败" + statusCode + result.msg);
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
                ToastUtils.showLongToast(MaintenReportActivity.this, "0获取保养类型数据失败：" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                ToastUtils.showLongToast(MaintenReportActivity.this, "0获取保养类型数据失败：" + fault.toString());
            }
        };
        SoapUtils.getTextDictoryAsync(MaintenReportActivity.this, Consts.EnumTextDictonay.MaintenanceLevel, callbackFailureReportingDesc);

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
                    Intent intent = new Intent(MaintenReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(MaintenReportActivity.this, "无图片需显示");
                }
                return false;
            }
        });


    }

    @Override
    public void bindEvent() {
        mToolBar.showMenuButton();
        mToolBar.setMenuText("...");
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("保养记录");

                final PopupWindowUtil popupWindow = new PopupWindowUtil(MaintenReportActivity.this, list);
                popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (_EquipmentData == null) {
                            ToastUtils.showToast(MaintenReportActivity.this, "请先确定设备");
                            return;
                        }
                        popupWindow.dismiss();
                        switch (position) {
                            case 0:
                                Intent intent1 = new Intent(MaintenReportActivity.this, Query_EquipMaintenanceInfoActivity.class);
                                intent1.putExtra("equipID", _EquipmentData.getID());
                                startActivity(intent1);
                                break;
                            default:
                                break;
                        }
                    }
                });
                //根据后面的数字 手动调节窗口的宽度
                popupWindow.show(v, 3);
            }
        });


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
                dialog.setYearLimt(20);
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
                dialog.setYearLimt(20);
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


        mTv_FaultDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(MaintenReportActivity.this, "请先确定设备");
                    return;
                }

                final List<String> list = new ArrayList<String>();
                if (_FaultDescArr != null && _FaultDescArr.length > 0) {
                    for (String item : _FaultDescArr) {
                        list.add(item);
                    }
                } else {
                    list.add("润滑");
                    list.add("检修");
                    list.add("巡检");

                }


                showSelectDialog(new SelectDialog.SelectDialogListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String str = list.get(position).toString();
                        mTv_FaultDesc.setText(str);
                        if (str.equals("润滑")) {
                            lL_material.setVisibility(View.VISIBLE);
                            mBtn_marAdd.performClick();
                        } else {
                            mTv_materialName.setText("");
                            mTv_materialstand.setText("");
                            mTv_materialUnit.setText("");
                         //   mEt_materialprice.setText("0");
                            mEt_materialsum.setText("0");
                            lL_material.setVisibility(View.GONE);

                            ShowDialog(MaintenReportActivity.this, "提示", "是否按计划执行?",
                                    //是
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            mTv_jh.performClick();
                                        }
                                    },
                                    //否
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            _CheckPlanEntityList.clear();
                                            mLv_Show_plan.setAdapter(null);

                                            mLv_Show_plan.setVisibility(View.GONE);
                                            mLl_Plan.setVisibility(View.GONE);
                                        }
                                    });
                        }

                    }
                }, list);

            }
        });
        //添加备注
        mTv_RepairmentDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowInputDialogForTextView(MaintenReportActivity.this, "请输入设备保养描述", mTv_RepairmentDesc);
            }
        });
        mTv_jh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "请首先确定要保养的设备！");
                    return;
                }
                String _ReapirLevel = mTv_FaultDesc.getText().toString();
                if (_ReapirLevel == null || _ReapirLevel.length() == 0) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "请选择保养类别！");
                    return;
                }

                Intent intent = new Intent(MaintenReportActivity.this, MaintenPlanCheckActivity.class);
                intent.putExtra("_EquipmentID", _EquipmentData.getID());
                intent.putExtra("_ReapirLevel", _ReapirLevel);
                startActivityForResult(intent, CHECK_PLAN_OK);

            }
        });

        mBtn_marAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, "请首先确定要保养的设备！");
                    return;
                }
                Intent intent = new Intent(MaintenReportActivity.this, EquipMgrMaintenMaterialActivity.class);
                intent.putExtra("_EquipmentID", _EquipmentData.getID());
                startActivityForResult(intent, CHECK_MATERIAL_OK);

            }
        });
        //设备图片点击事件
        mIv_EquipImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mIv_EquipImg.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mIv_EquipImg.getDrawingCache());
                mIv_EquipImg.setDrawingCacheEnabled(false);
                String equipName = mTv_EquipName.getText().toString();

                if (bitmap != null && equipName != null) {
                    Intent intent = new Intent(MaintenReportActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(MaintenReportActivity.this, "无图片需显示");
                }
                return false;
            }
        });

        //提交
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitBill();

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
        SoapUtils.getEquipmentByBarcodeAsync(MaintenReportActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "1获取保养信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "2获取保养信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "3获取保养信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "4获取保养信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "获取保养信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "获取保养信息失败:" + fault);
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
            if(!data.getAssetTypeID().equals(Consts.AssetType_sc)){
                _EquipmentData = null;
                ToastUtils.showLongToast(MaintenReportActivity.this, data.getEquipmentName() + " 不是生产设备");
                return;
            }
            boolean ckRlt = CommonUtils.checkCorpIsInList(MaintenReportActivity.this, data.getCorporationLevelCode());
            if (!ckRlt) {
                _EquipmentData = null;
                ToastUtils.showLongToast(MaintenReportActivity.this, data.getEquipmentName() + "属于" + data.getCorporationName() + ",不属于用户当前归属组织机构");
                return;
            }

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
        SoapUtils.getEquipmentByIDAsync(MaintenReportActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "1获取保养信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "2获取保养信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "3获取保养信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(MaintenReportActivity.this, "4获取保养信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "获取保养信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "获取保养信息失败:" + fault);
            }
        });
    }


    private void getReport(String rptID) {

        showLogingDialog();

        MaintenReportingRequest request = new MaintenReportingRequest();
        request.setID(rptID);

        SoapUtils.getMaintenReportAsync(MaintenReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);

                MaintenanceBillResult result = new Gson().fromJson(strData, MaintenanceBillResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                _ReportEntity = result.getData();
                _ReportFileEntitys = result.getdateItemList();
                if (_ReportEntity != null) {
                    mTv_EquipCode.setText(_ReportEntity.getEquipmentCode());
                    mTv_EquipName.setText(_ReportEntity.getEquipmentName());
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
                    String ctime = formatter.format(_ReportEntity.getStartTime());
                    String ftime = formatter.format(_ReportEntity.getFinishTime());
                    mTv_FaultDate.setText(ctime.substring(0, 10));
                    mTv_FaultDate1.setText(ctime.substring(0, 10));


                    mTv_FaultDesc.setText(_ReportEntity.getMaintenanceLevel());
                    mEt_User.setText(_ReportEntity.getMaintenUser());
                    mEt_money.setText(_ReportEntity.getTotalMoney());
//                    mTv_ReportNO.setVisibility(View.VISIBLE);
//                    mTv_ReportNO.setText(_RepairmentBillEntity.getReportNO());


                    if (_ReportFileEntitys != null && _ReportFileEntitys.size() != 0) {
                        lL_material.setVisibility(View.VISIBLE);
                        MaintenanceItemEntity ma = _ReportFileEntitys.get(0);
                        mTv_materialName.setText(ma.getMaterialName());
                        mTv_materialstand.setText(ma.getStandard());
                        mTv_materialUnit.setText(ma.getUseUnit());
                      //  mEt_materialprice.setText(ma.getUsePrice());
                        mEt_materialsum.setText(ma.getUseCount());
                    } else {
                        lL_material.setVisibility(View.GONE);
                    }


                }

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {

                ToastUtils.showToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {

                ToastUtils.showToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
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
            ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.activity_fault_report_barcode_empty));
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

                _CheckPlanEntityList = (List<MaintenancePlanEntity>) data.getSerializableExtra("_CheckMaintenPlanList");
                if (_CheckPlanEntityList == null || _CheckPlanEntityList.size() == 0) {
                    mLv_Show_plan.setVisibility(View.GONE);
                    mLl_Plan.setVisibility(View.GONE);
                    mLv_Show_plan.setAdapter(null);
                } else {
                    mLv_Show_plan.setAdapter(null);
                    adapter_Plan = new MaintenancePlanCheckDataListAdapter(this, _CheckPlanEntityList);
                    mLv_Show_plan.setSelection(adapter_Plan.getCount());

                    mLv_Show_plan.setAdapter(adapter_Plan);
                    adapter_Plan.notifyDataSetChanged();
                    mLl_Plan.setVisibility(View.VISIBLE);
                    mLv_Show_plan.setVisibility(View.VISIBLE);

                    setListViewHeightBasedOnChildren(mLv_Show_plan);
                }

                ToastUtils.showLongToast(MaintenReportActivity.this, "共获取到" + _CheckPlanEntityList.size() + "条保养计划");


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
            case CHECK_MATERIAL_OK:
                _CheckMaterialList.clear();
                _CheckMaterialList = (List<EquipmentMaterialEntity>) data.getSerializableExtra("_CheckEntityList");

                if (_CheckMaterialList != null && _CheckMaterialList.size() != 0) {
                    mTv_materialName.setText(_CheckMaterialList.get(0).getMaterialName());
                    mTv_materialstand.setText(_CheckMaterialList.get(0).getStandard());
                    mTv_materialUnit.setText(_CheckMaterialList.get(0).getUnit());
                 //   mEt_materialprice.setText(_CheckMaterialList.get(0).getPrice());
                    mEt_materialsum.setText("");

                } else {
                    mTv_materialName.setText("");
                    mTv_materialstand.setText("");
                    mTv_materialUnit.setText("");
                   // mEt_materialprice.setText("");
                    mEt_materialsum.setText("");
                }

                if (!_Type.equals("2") && (_CheckPlanEntityList == null || _CheckPlanEntityList.size() == 0) ){
                    ShowDialog(MaintenReportActivity.this, "提示", "是否按计划执行?",
                            //是
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mTv_jh.performClick();
                                }
                            },
                            //否
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mLv_Show_plan.setAdapter(null);
                                    mLv_Show_plan.setVisibility(View.GONE);
                                    mLl_Plan.setVisibility(View.GONE);
                                }
                            });
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
     * 利用正则表达式判断字符串是否是数字
     *
     * @param str
     * @return
     */
    public boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    private void Repair_RepairmentPlan(String mTv_EquipID, Object entity) {
        getEquipmentInfoByID(mTv_EquipID);
        if (entity != null) {
            mTv_jh.setEnabled(false);
            // 将维修级别设置为“定修”

            //根据备件ID获取备件更换计划
            MaintenancePlanEntity planEntity = (MaintenancePlanEntity) entity;
            planEntity.setIsCheck(true);
            //维修级别
            mTv_FaultDesc.setText(planEntity.getMaintenanceLevel());
             if(planEntity.getMaintenanceLevel().equals("润滑")){
                 lL_material.setVisibility(View.VISIBLE);
             }
             else {
                 lL_material.setVisibility(View.GONE);
             }
            //将计划填充至计划列表
            _CheckPlanEntityList.add(planEntity);
            adapter_Plan = new MaintenancePlanCheckDataListAdapter(MaintenReportActivity.this, _CheckPlanEntityList);
            mLv_Show_plan.setAdapter(adapter_Plan);
            mLv_Show_plan.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(mLv_Show_plan);
            mLl_Plan.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 计划列表长按删除
     */
    private void bindEvent_PlanRemove() {
        //计划列表长按删除
        mLv_Show_plan.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                deletePlanRow(position);
                return false;
            }
        });
    }

    private void deletePlanRow(final int pron) {
        List<String> menuList = new ArrayList<String>();
        menuList.add("删除");
        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0://删除
                            MaintenancePlanEntity eus = (MaintenancePlanEntity) adapter_Plan.getItem(pron);
                            if (eus != null) {
                                if (_CheckPlanEntityList != null) {
                                    _CheckPlanEntityList.remove(eus);
                                    adapter_Plan.notifyDataSetChanged();
                                    setListViewHeightBasedOnChildren(mLv_Show_plan);
                                }
                            }

                        break;
                    default:
                        break;
                }
            }
        }, menuList);

    }

    private void submitBill() {
        showLogingDialog();

        String faultDate = mTv_FaultDate.getText().toString().trim();
        String faultDate1 = mTv_FaultDate1.getText().toString().trim();

        String faultDesc = mTv_FaultDesc.getText().toString().trim();
        String RepairmentDesc = mTv_RepairmentDesc.getText().toString().trim();
        String user = mEt_User.getText().toString().trim();
        String phone = mEt_money.getText().toString().trim();
        String useCout = mEt_materialsum.getText().toString().trim();
        String mprice ="0";


        //验证提交数据

        if (_EquipmentData == null) {
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "请首先确定要保养的设备！");
            return;
        }
        if (faultDate == null || faultDate.length() == 0) {
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "请选择开始保养日期！");
            return;
        }
        if (faultDate1 == null || faultDate1.length() == 0) {
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "请选择结束保养日期！");
            return;
        }
        if(compareDateMinutes(faultDate,faultDate1) <= 0){
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "开始时间不应大于或等于结束时间！");
            return;
        }
        if (faultDesc == null || faultDesc.length() == 0) {
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "请选择保养类型！");
            return;
        }

        if (user == null || user.length() == 0) {
            dismissLoadingDialog();
            ToastUtils.showLongToast(MaintenReportActivity.this, "请填写联系人！");
            return;
        }
        if (phone == null || phone.length() == 0) {
            phone = "0";
//                    dismissLoadingDialog();
//                    ToastUtils.showLongToast(MaintenReportActivity.this, "填写费用金额不能为空！");
//                    return;
        } else {

        }
        if (faultDesc.equals("润滑")) {

            if (mTv_materialName.getText().toString().trim() == "" || mTv_materialName.getText().toString().trim().length() == 0) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "请选择设备保养的机物料！");
                return;
            }
            if (useCout == null || useCout.length() == 0) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, "请确定要保养的设备选择物资的数量！");
                return;
            } else {
                if (!isNumeric(useCout)) {
                    dismissLoadingDialog();
                    ToastUtils.showLongToast(MaintenReportActivity.this, "请确定要保养的设备选择物资的数量为数字！");
                    return;
                } else {

                    if (Integer.parseInt(useCout) <= 0) {
                        dismissLoadingDialog();
                        ToastUtils.showLongToast(MaintenReportActivity.this, "请确定要保养的设备选择物资的数量大于0！");
                        return;
                    }
                }
            }
//            if (mprice == null || mprice.length() == 0) {
//                dismissLoadingDialog();
//                ToastUtils.showLongToast(MaintenReportActivity.this, "请确定要保养的设备选择物资的单价！");
//                return;
//            }
        }
        mBtn_Submit.setEnabled(false);
        //提交数据
        MaintenanceBillResult request = new MaintenanceBillResult();
        //  request.setData(_EquipmentData);


        MaintenanceBillEntity rpen = new MaintenanceBillEntity();
        rpen.setEquipmentID(_EquipmentData.getID());
        rpen.setCorporationID(_EquipmentData.getCorporationID());
        rpen.setDepartmentID(_EquipmentData.getDepartmentID());

        rpen.setTotalMoney(phone);

        rpen.setMaintenUser(user);
        rpen.setStartTime(faultDate);
        rpen.setFinishTime(faultDate1);
        rpen.setEquipmentID(_EquipmentData.getID());
        rpen.setShutDownMinutes("0");
        rpen.setMaintenanceLevel(faultDesc);

        if( adapter_Plan != null &&adapter_Plan.getCount() > 0){
            MaintenancePlanEntity checkEntity = (MaintenancePlanEntity)adapter_Plan.getItem(0);
            String strPlanID = checkEntity.getID();
            rpen.setMaintenancePlanID(strPlanID);
        }

        rpen.setRemark(RepairmentDesc);
        request.setData(rpen);
        if (faultDesc.equals("润滑")) {
            List<MaintenanceItemEntity> listMItem = new ArrayList<>();
            MaintenanceItemEntity mItem = new MaintenanceItemEntity();
            mItem.setMaterialID(_CheckMaterialList.get(0).getMaterialID());
            mItem.setUseCount(useCout);
            mItem.setUsePrice(mprice);
            mItem.setMakeUser(user);
            mItem.setUseUnit(_CheckMaterialList.get(0).getUnit());
            mItem.setTotalMoney(phone);
            listMItem.add(mItem);
            request.setdateItemList(listMItem);
        }


        // 提交数据
        SoapUtils.submitNewEquipMaintenanceRepairAsync(MaintenReportActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err1));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err2));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                StringResult result = new Gson().fromJson(strData, StringResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }

                if (_Type.equals("0")) {
                    String[] reDate = result.msg.split(",");
                    //数据是使用Intent返回
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("BillID", reDate[0]);
                    intent.putExtra("BillNO", reDate[1]);
                    //设置返回数据
                    MaintenReportActivity.this.setResult(RESULT_OK, intent);
                }
                mBtn_Submit.setEnabled(false);
                ToastUtils.showLongToast(MaintenReportActivity.this,getString(R.string.activity_maintenance_submit_ok) );
                finish();

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.activity_maintenance_submit_err, error.getMessage()));
                mBtn_Submit.setEnabled(true);
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(MaintenReportActivity.this, getString(R.string.activity_maintenance_submit_fail, fault));
                mBtn_Submit.setEnabled(true);
            }
        });
    }
}
