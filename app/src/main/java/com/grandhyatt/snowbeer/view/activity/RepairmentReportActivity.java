package com.grandhyatt.snowbeer.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
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
import com.grandhyatt.snowbeer.adapter.EquipMgrRepairSpareDataListAdapter;
import com.grandhyatt.snowbeer.adapter.EquipRepairSpareViewDataListAdapter;
import com.grandhyatt.snowbeer.adapter.RepairmentPlanCheckDataListAdapter;
import com.grandhyatt.snowbeer.adapter.SpareInEquipmentDataListAdapter;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;
import com.grandhyatt.snowbeer.entity.TextDictionaryEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.RepairmentReportingRequest;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentUseSpareResult;
import com.grandhyatt.snowbeer.network.result.RepairmentEquipmentResult;
import com.grandhyatt.snowbeer.network.result.RepairmentResult;
import com.grandhyatt.snowbeer.network.result.StringResult;
import com.grandhyatt.snowbeer.network.result.TextDictoryResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.utils.PopupWindowUtil;
import com.grandhyatt.snowbeer.utils.SPUtils;
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
import static com.grandhyatt.snowbeer.utils.CommonUtils.compareDateMinutes;


/**
 * 设备维修操作界面
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

    @BindView(R.id.mTv_StartDate)
    TextView mTv_StartDate;
    @BindView(R.id.mTv_FinishDate)
    TextView mTv_FinishDate;
    @BindView(R.id.mTv_RepairLevel)
    TextView mTv_RepairLevel;
    @BindView(R.id.mTv_FaultClass)
    TextView mTv_FaultClass;
    @BindView(R.id.mTv_RepairDesc)
    TextView mTv_RepairDesc;

    @BindView(R.id.mBtn_ChoicePlan)
    Button mBtn_ChoicePlan;

    @BindView(R.id.mEt_User)
    EditText mEt_User;
    @BindView(R.id.mEt_money)
    EditText mEt_money;

    @BindView(R.id.mBtn_Submit)
    Button mBtn_Submit;
    @BindView(R.id.mBtn_AddSpare)
    Button mBtn_AddSpare;

    @BindView(R.id.mLv_DataList_Spare)
    ListView mLv_DataList_Spare;
    @BindView(R.id.mLv_Show_plan)
    ListView mLv_Show_plan;

    @BindView(R.id.mLl_Plan)
    LinearLayout mLl_Plan;//维修计划信息容器
    @BindView(R.id.mLl_Spare)
    LinearLayout mLl_Spare;//备件信息容器

    public static final int CHECK_PLAN_OK = 111;//选择执行计划返回码
    public static final int CHECK_SPARE_OK = 112;//选择维修用备件

    ArrayList<String> _CheckPlanIDList = new ArrayList<>();; //用户选中的维护计划ID
    List<RepairmentPlanEntity> _CheckPlanEntityList = new ArrayList<>();//用户选择的数据行对象
    List<SpareInEquipmentEntity> _CheckSpareEntityList = new ArrayList<>();//用户选择的数据行对象
    List<EquipmentUseSpareEntity> _CheckSpareUseList = new ArrayList<>();// 页面选择备品配件返回数据

    /**
     * 获取到的图片路径
     */
    private String _PicPath;
    private Uri takePhotoUrl;
    private String _Type;
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
    RepairmentBillEntity _RepairmentBillEntity;

    RepairmentPlanCheckDataListAdapter adapter_Plan = null;      //维护计划适配器
    SpareInEquipmentDataListAdapter adapter_Spare = null;  //备件更换适配器

    EquipRepairSpareViewDataListAdapter adapter_SpareView = null; //添加配件适配器


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_repairment_report);
        ButterKnife.bind(this);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();

        //维修            type = 0  mTv_EquipID=设备ID
        //维修-备件更换   type = 1  mTv_EquipID=设备ID    mTv_ReportID = 备件与设备关系ID   entity = 备件与设备关系对象
        //维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID         entity = 维修计划对象
        //维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if (type != null)
            _Type = type;
        else
            _Type = "9";
        String mTv_EquipID = intent.getStringExtra("mTv_EquipID");
        String mTv_ReportID = intent.getStringExtra("mTv_ReportID");
        Object entity = (Object) intent.getSerializableExtra("entity");
        //-------------------------------------------------------------------------------------------------------------
        //维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID
        if ((type != null && type.equals("3")) && mTv_ReportID != null && mTv_EquipID != null) {

        }
        //-------------------------------------------------------------------------------------------------------------
        //维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID， entity = 维修计划对象
        else if ((type != null && type.equals("2")) && mTv_ReportID != null && mTv_EquipID != null) {
            mSearchBar.setVisibility(View.GONE);
            mToolBar.setTitle("设备维修-按计划");
            initView();
            bindEvent();
            bindEvent_SpareRemove();

            //维修-维修计划
            Repair_RepairmentPlan(mTv_EquipID, entity);
        }
        //-------------------------------------------------------------------------------------------------------------
        //维修-备件更换   type = 1  mTv_EquipID=设备ID    mTv_ReportID = 备件与设备关系ID entity = 备件与设备关系对象
        else if ((type != null && type.equals("1")) && mTv_ReportID != null && mTv_EquipID != null) {
            mSearchBar.setVisibility(View.GONE);
            mToolBar.setTitle("设备维修-按备件");
            initView();
            bindEvent();

            //维修-备件更换
            Repair_SpareReplace(mTv_EquipID, entity);
        }
        //-------------------------------------------------------------------------------------------------------------
        //维修            type = 0  mTv_EquipID=设备ID
        else if ((type != null && type.equals("0")) && mTv_EquipID != null) {
            mSearchBar.setVisibility(View.GONE);
            mToolBar.setTitle("设备维修");
            initView();
            bindEvent();
            bindEvent_PlanRemove();
            bindEvent_SpareRemove();
            //根据设备id获取设备信息
            getEquipmentInfoByID(mTv_EquipID);
        }
        //-------------------------------------------------------------------------------------------------------------
        //正常维修
        else {
            mToolBar.setTitle("设备维修");
            initView();
            bindEvent();
            bindEvent_PlanRemove();
            bindEvent_SpareRemove();
        }
    }

    /**
     * 维修-维修计划
     *
     * @param mTv_EquipID
     * @param entity
     */
    private void Repair_RepairmentPlan(String mTv_EquipID, Object entity) {
        getEquipmentInfoByID(mTv_EquipID);
        if (entity != null) {
            mBtn_ChoicePlan.setEnabled(false);
            // 将维修级别设置为“定修”
            mTv_RepairLevel.setText("定修");
            //根据备件ID获取备件更换计划
            RepairmentPlanEntity planEntity = (RepairmentPlanEntity) entity;
            planEntity.setIsCheck(true);
            //维修级别
            mTv_RepairLevel.setText(planEntity.getRepairmentLevel());
            mTv_RepairLevel.setEnabled(false);
            //将计划填充至计划列表
            _CheckPlanEntityList.add(planEntity);
            adapter_Plan = new RepairmentPlanCheckDataListAdapter(RepairmentReportActivity.this, _CheckPlanEntityList);
            _CheckPlanIDList.add(planEntity.getID());
            mLv_Show_plan.setAdapter(adapter_Plan);
            mLv_Show_plan.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(mLv_Show_plan);
            mLl_Plan.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 维修-备件更换
     *
     * @param mTv_EquipID
     * @param entity
     */
    private void Repair_SpareReplace(String mTv_EquipID, Object entity) {
        //根据设备id获取设备信息
        getEquipmentInfoByID(mTv_EquipID);
        // 将维修级别设置为“定修”
        mTv_RepairLevel.setText("定修");
        mTv_RepairLevel.setEnabled(false);
        if (entity != null) {
            mBtn_ChoicePlan.setEnabled(false);
            //根据备件ID获取备件更换计划
            SpareInEquipmentEntity spInEpEntity = (SpareInEquipmentEntity) entity;
            spInEpEntity.setCheck(true);
            //将计划填充至计划列表
            _CheckSpareEntityList.add(spInEpEntity);
            adapter_Spare = new SpareInEquipmentDataListAdapter(RepairmentReportActivity.this, _CheckSpareEntityList);
            mLv_Show_plan.setAdapter(adapter_Spare);
            mLv_Show_plan.setVisibility(View.VISIBLE);
            setListViewHeightBasedOnChildren(mLv_Show_plan);
            mLl_Plan.setVisibility(View.VISIBLE);

            mBtn_AddSpare.setEnabled(false);
            //将备件填充至备件列表
            EquipmentUseSpareEntity useSpare = new EquipmentUseSpareEntity();
            useSpare.setSpareID(spInEpEntity.getSpareID());
            useSpare.setSpareCode(spInEpEntity.getSpareCode());
            useSpare.setSpareName(spInEpEntity.getSpareName());
            useSpare.setSpareUnit(spInEpEntity.getSpareUnit());
            useSpare.setUserInputCount(spInEpEntity.getReplaceCount());

            _CheckSpareUseList.add(useSpare);
            adapter_SpareView = new EquipRepairSpareViewDataListAdapter(RepairmentReportActivity.this, _CheckSpareUseList);
            mLv_DataList_Spare.setAdapter(adapter_SpareView);
            setListViewHeightBasedOnChildren(mLv_DataList_Spare);
            //获取可用量
            getSparesInfo(spInEpEntity.getEquipmentID(), spInEpEntity.getSpareID(), spInEpEntity.getDeptID());

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
    public void refreshUI() {

    }

    @Override
    public void initView() {
        fillEquipInfo(null);

        mToolBar.setTitle("设备维修");
        mToolBar.hideMenuButton();

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);  //去除状态栏

        mLv_Show_plan.setVisibility(View.GONE);
        mEt_User.setText(SPUtils.getLastLoginUserName(RepairmentReportActivity.this)); //初始化用户

        //设备故障类别
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
        //维修级别
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
    public void requestNetworkData() {

    }

    @Override
    public void bindEvent() {

        mToolBar.showMenuButton();
        mToolBar.setMenuText("...");
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> list = new ArrayList<String>();
                list.add("维修记录");

                final PopupWindowUtil popupWindow = new PopupWindowUtil(RepairmentReportActivity.this, list);
                popupWindow.setItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if (_EquipmentData == null) {
                            ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
                            return;
                        }
                        popupWindow.dismiss();
                        switch (position) {
                            case 0:
                                Intent intent1 = new Intent(RepairmentReportActivity.this, Query_EquipRepairInfoActivity.class);
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
        //开始日期
        mTv_StartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
                    return;
                }
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
                        mTv_StartDate.setText(strDate);
                    }
                });
                dialog.show();
            }
        });
        //结束日期
        mTv_FinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
                    return;
                }
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
                        mTv_FinishDate.setText(strDate);
                    }
                });
                dialog.show();
            }
        });
        //维修级别
        mTv_RepairLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                repairLevelSelect();
            }
        });
        //故障类别
        mTv_FaultClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
                    return;
                }
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
                        mTv_FaultClass.setText(str);
                    }
                }, list);
            }
        });
        //故障描述
        mTv_RepairDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
                    return;
                }
                ShowInputDialogForTextView(RepairmentReportActivity.this, "请输入设备维修描述", mTv_RepairDesc);
            }
        });
        //选择计划
        mBtn_ChoicePlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备！");
                    return;
                }
                String _ReapirLevel = mTv_RepairLevel.getText().toString();
                if (_ReapirLevel == null || _ReapirLevel.length() == 0) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请选择维修级别！");
                    return;
                }
                Intent intent = new Intent(RepairmentReportActivity.this, RepairmentPlanCheckActivity.class);
                intent.putExtra("_EquipmentID", _EquipmentData.getID());
                intent.putExtra("_ReapirLevel", _ReapirLevel);

                startActivityForResult(intent, CHECK_PLAN_OK);
            }
        });

        //添加备件
        mBtn_AddSpare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_EquipmentData == null) {
                    ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备！");
                    return;
                }
                Intent intent = new Intent(RepairmentReportActivity.this, EquipMgrRepairSpareActivity.class);
                intent.putExtra("_EquipmentID", _EquipmentData.getID());
                startActivityForResult(intent, CHECK_SPARE_OK);
            }
        });

        //提交
        mBtn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitRepairmentBill();
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

    /**
     * 备件列表长按删除
     */
    private void bindEvent_SpareRemove() {
        //备件列表长按删除
        mLv_DataList_Spare.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TextView mTv_ReportID = (TextView) view.findViewById(R.id.mTv_SpareID);
                deleteSpareRow(position);
                setListViewHeightBasedOnChildren(mLv_DataList_Spare);
                return false;
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
                if (mTv_RepairLevel.getText().toString().equals("大修"))   //大修
                {
                    _CheckSpareEntityList.clear();
                    _CheckPlanIDList = data.getExtras().getStringArrayList("_CheckPlanIDList");//得到新Activity 关闭后返回的数据
                    _CheckPlanEntityList = (List<RepairmentPlanEntity>) data.getSerializableExtra("_CheckEntityList");
                    if (_CheckPlanEntityList.size() == 0) {
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    } else {
                        mLv_Show_plan.setAdapter(null);
                        adapter_Plan = new RepairmentPlanCheckDataListAdapter(this, _CheckPlanEntityList);
                        mLv_Show_plan.setSelection(adapter_Plan.getCount());
                        mLv_Show_plan.setAdapter(adapter_Plan);
                        mLv_Show_plan.setVisibility(View.VISIBLE);
                        setListViewHeightBasedOnChildren(mLv_Show_plan);
                    }
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "共获取到" + _CheckPlanEntityList.size() + "条维护计划");
                } else//定修、日常维修
                {
                    _CheckPlanEntityList.clear();
                    _CheckSpareEntityList = (List<SpareInEquipmentEntity>) data.getSerializableExtra("_CheckEntityList");
                    if (_CheckSpareEntityList.size() == 0) {
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    } else {
                        mLv_Show_plan.setAdapter(null);
                        adapter_Spare = new SpareInEquipmentDataListAdapter(this, _CheckSpareEntityList);
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
                _CheckSpareUseList = (List<EquipmentUseSpareEntity>) data.getSerializableExtra("_CheckEntityList");
                if (_CheckSpareUseList != null && _CheckSpareUseList.size() > 0) {
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
            if(!data.getAssetTypeID().equals(Consts.AssetType_sc)){
                _EquipmentData = null;
                ToastUtils.showLongToast(RepairmentReportActivity.this, data.getEquipmentName() + " 不是生产设备");
                return;
            }
            boolean ckRlt = CommonUtils.checkCorpIsInList(RepairmentReportActivity.this, data.getCorporationLevelCode());
            if (!ckRlt) {
                _EquipmentData = null;
                ToastUtils.showLongToast(RepairmentReportActivity.this, data.getEquipmentName() + "属于" + data.getCorporationName() + ",不属于用户当前归属组织机构");
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
     * 获取维修单信息
     *
     * @param billID 维修单ID
     */
    private void getRepairmentBill(String billID) {

        showLogingDialog();
        RepairmentReportingRequest request = new RepairmentReportingRequest();
        request.setID(billID);

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
                _RepairmentBillEntity = result.getData();
                if (_RepairmentBillEntity != null) {
                    SimpleDateFormat formatter;
                    formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
                    String ctime1 = formatter.format(_RepairmentBillEntity.getStartTime());
                    String ctime2 = formatter.format(_RepairmentBillEntity.getFinishTime());
                    mTv_StartDate.setText(ctime1.substring(0, 10));
                    mTv_FinishDate.setText(ctime2.substring(0, 10));
                    mTv_FaultClass.setText(_RepairmentBillEntity.getFaultClass());
                    mTv_RepairDesc.setText(_RepairmentBillEntity.getDescription());
                    mEt_User.setText(_RepairmentBillEntity.getRepairUser());
                    mEt_money.setText(String.valueOf(_RepairmentBillEntity.getTotalMoney()));
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
     * 维修级别选择处理
     */
    private void repairLevelSelect() {
        if (_EquipmentData == null) {
            ToastUtils.showToast(RepairmentReportActivity.this, "请先确定设备");
            return;
        }
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
                //界面赋值
                mTv_RepairLevel.setText(list.get(position).toString());

                //大修
                if (list.get(position).toString().equals("大修")) {
                    mLl_Plan.setVisibility(View.VISIBLE);
                    mBtn_ChoicePlan.performClick();

                    if (_CheckSpareEntityList != null && _CheckSpareEntityList.size() > 0) {
                        _CheckSpareEntityList.clear();
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    }
                }
                //定修、日常维修
                else {
                    ShowDialog(RepairmentReportActivity.this, "提示", "是否按计划执行?",
                            //是
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mBtn_ChoicePlan.performClick();
                                    mLl_Plan.setVisibility(View.VISIBLE);
                                }
                            },
                            //否
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mLl_Plan.setVisibility(View.GONE);
                                }
                            });

                    if (_CheckPlanEntityList != null && _CheckPlanEntityList.size() > 0) {
                        _CheckPlanEntityList.clear();
                        _CheckPlanIDList.clear();
                        mLv_Show_plan.setVisibility(View.GONE);
                        mLv_Show_plan.setAdapter(null);
                    }
                }
            }
        }, list);
    }

    /**
     * 根据设备、部门、备件信息获取可用备件库存信息
     *
     * @param equipID
     */
    private void getSparesInfo(String equipID, String spareID, String deptID) {

        SoapUtils.getEquipmentSparesStoreInfo(RepairmentReportActivity.this, equipID, deptID, spareID, new SoapListener() {
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
                EquipmentUseSpareResult result = new Gson().fromJson(strData, EquipmentUseSpareResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                List<EquipmentUseSpareEntity> data = result.getData();
                //当前页面索引大于或等于总页数时,设置SmartRefreshLayout 完成加载并标记没有更多数据
                if (data == null || data.size() == 0) {
                    if (adapter_SpareView != null && adapter_SpareView.getCount() > 0) {

                        adapter_SpareView.modifyCount(0, "0");
                    }
                    ToastUtils.showLongToast(RepairmentReportActivity.this, "没有获取到该备件的可用量");
                } else {
                    if (data.size() > 0) {
                        if (adapter_SpareView != null && adapter_SpareView.getCount() > 0) {
                            EquipmentUseSpareEntity useSpareQty = data.get(0);
                            adapter_SpareView.modifyCount(0, useSpareQty.getCount());
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err5, error));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, fault));
            }
        });
    }

    private void deleteSpareRow(final int pron) {
        List<String> menuList = new ArrayList<String>();
        menuList.add("删除");
        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        //删除
                        EquipmentUseSpareEntity eus = (EquipmentUseSpareEntity) adapter_SpareView.getItem(pron);
                        _CheckSpareUseList.remove(eus);
                        adapter_SpareView.removeItem(pron);
                        break;
                    default:
                        break;
                }
            }
        }, menuList);

    }

    private void deletePlanRow(final int pron) {
        List<String> menuList = new ArrayList<String>();
        menuList.add("删除");
        showSelectDialog(new SelectDialog.SelectDialogListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0://删除
                        String reapirLevel = mTv_RepairLevel.getText().toString();
                        if (reapirLevel.equals("大修") && adapter_Plan != null && adapter_Plan.getCount() > 0) {
                            RepairmentPlanEntity eus = (RepairmentPlanEntity) adapter_Plan.getItem(pron);
                            if (eus != null) {
                                if (_CheckPlanEntityList != null) {
                                    _CheckPlanEntityList.remove(eus);
                                    adapter_Plan.removeItem(pron);
                                    setListViewHeightBasedOnChildren(mLv_Show_plan);
                                }
                            }
                        } else if (adapter_Spare != null && adapter_Spare.getCount() > 0) {
                            SpareInEquipmentEntity spare = (SpareInEquipmentEntity) adapter_Spare.getItem(pron);
                            if (spare != null) {
                                if (_CheckSpareEntityList != null) {
                                    _CheckSpareEntityList.remove(spare);
                                    adapter_Spare.removeItem(pron);
                                    setListViewHeightBasedOnChildren(mLv_Show_plan);
                                }
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }, menuList);

    }

    /**
     * 提交维修单
     */
    private void submitRepairmentBill() {
        String faultDate = mTv_StartDate.getText().toString().trim();
        String faultDate1 = mTv_FinishDate.getText().toString().trim();
        String faultLevel = mTv_RepairLevel.getText().toString().trim();
        String faultDesc = mTv_FaultClass.getText().toString().trim();
        String RepairmentDesc = mTv_RepairDesc.getText().toString().trim();
        String user = mEt_User.getText().toString().trim();
        String money = mEt_money.getText().toString().trim();

        //验证提交数据
        if (_EquipmentData == null) {
            ToastUtils.showLongToast(RepairmentReportActivity.this, "请先确定设备！");
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
        if(compareDateMinutes(faultDate,faultDate1)<=0){
            ToastUtils.showLongToast(RepairmentReportActivity.this, "开始时间不应大于或等于结束时间！");
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
        if (money == null || money.length() == 0) {
            money = "0";
//            ToastUtils.showLongToast(RepairmentReportActivity.this, "填写费用金额不能为空！");
//            return;
        }
        if (_CheckSpareUseList == null || _CheckSpareUseList.size() == 0) {
            ToastUtils.showLongToast(RepairmentReportActivity.this, "请添加备品备件信息！");
            return;
        }
        mBtn_Submit.setEnabled(false);

        //提交数据
        RepairmentEquipmentResult request = new RepairmentEquipmentResult();
        if (faultLevel.equals("大修")) {
            if (_CheckPlanIDList == null || _CheckPlanIDList.size() == 0) {
                ToastUtils.showLongToast(RepairmentReportActivity.this, "未找到【大修】的维修计划，请选择！");
                return;
            }
        } else {
            if (_CheckPlanIDList != null) {
                _CheckPlanIDList.clear();
            }
        }
        RepairmentBillEntity rpen = new RepairmentBillEntity();
        rpen.setEquipmentID(_EquipmentData.getID());
        rpen.setCorporationID(_EquipmentData.getCorporationID());
        rpen.setDescription(RepairmentDesc);
        rpen.setRepairUser(user);
        rpen.setTotalMoney(money);
        rpen.setRepairmentLevel(faultLevel);
        rpen.setFaultClass(faultDesc);
        rpen.setStartTime(faultDate);
        rpen.setFinishTime(faultDate1);
        rpen.setShutDownMinutes("0");
        request.setRepairmentBillData(rpen);

        TextView tvid = null;
        NumberEditText mNEdt_Check = null;
        TextView mSumCount = null;
        if (_CheckSpareUseList == null || _CheckSpareUseList.size() == 0) {
            mBtn_Submit.setEnabled(true);
            ToastUtils.showLongToast(RepairmentReportActivity.this, "请选择维修所需备件！");
            return;
        }
        for (int i = 0; i < mLv_DataList_Spare.getChildCount(); i++) {
            View vw = mLv_DataList_Spare.getChildAt(i);
            tvid = (TextView) vw.findViewById(R.id.mTv_SpareID);
            double edtcount = 0;
            double sumcount = 0;

            for (EquipmentUseSpareEntity e : _CheckSpareUseList) {
                if (e.getSpareID() == tvid.getText().toString().trim()) {
                    mNEdt_Check = (NumberEditText) vw.findViewById(R.id.mNEdt_Check);
                    mSumCount = (TextView) vw.findViewById(R.id.mTv_SumCountx);
                    edtcount = mNEdt_Check.getData();
                    sumcount = new Double(mSumCount.getText().toString().trim()).doubleValue();
                    if (edtcount > sumcount) {
                        mBtn_Submit.setEnabled(true);
                        ToastUtils.showLongToast(RepairmentReportActivity.this, "备件:" + e.getSpareName() + " 数量超出库存数量！");
                        return;
                    } else {
                        e.setCount(Double.toString(edtcount));
                    }
                }
            }
        }
        request.setSpareInEquipmentData(_CheckSpareUseList);

        showLogingDialog();
        //提交数据
        SoapUtils.submitNewEquipReparimentRepairAsync(RepairmentReportActivity.this, request, _CheckPlanIDList, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err1));
                    mBtn_Submit.setEnabled(true);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err2));
                    mBtn_Submit.setEnabled(true);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                StringResult result = new Gson().fromJson(strData, StringResult.class);
                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err3));
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(RepairmentReportActivity.this, getString(R.string.submit_soap_result_err4, result.msg));
                    return;
                }
                //维修            type = 0  mTv_EquipID=设备ID
                //维修-备件更换   type = 1  mTv_EquipID=设备ID    mTv_ReportID = 备件与设备关系ID   entity = 备件与设备关系对象
                //维修-维修计划   type = 2  mTv_EquipID=设备ID    mTv_ReportID = 维修计划ID         entity = 维修计划对象
                //维修-显示维修单 type = 3  mTv_EquipID=设备ID    mTv_ReportID = 维修单ID
                if (_Type.equals("0")) {
                    String[] reDate = result.msg.split(",");
                    //数据是使用Intent返回
                    Intent intent = new Intent();
                    //把返回数据存入Intent
                    intent.putExtra("BillID", reDate[0]);
                    intent.putExtra("BillNO", reDate[1]);
                    //设置返回数据
                    RepairmentReportActivity.this.setResult(RESULT_OK, intent);
                }

                mBtn_Submit.setEnabled(true);
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
}
