package com.grandhyatt.snowbeer.view.activity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseReasonEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.EquipmentResult;
import com.grandhyatt.snowbeer.network.result.EquipmentUseReasonResult;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.CommonUtils;
import com.grandhyatt.snowbeer.utils.ImageUtils;
import com.grandhyatt.snowbeer.Ctrls.MyGridView;
import com.grandhyatt.snowbeer.view.SearchBarLayout;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 化学仪器使用
 * Created by ycm on 2018/10/7.
 */

public class AssayUseActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

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
    @BindView(R.id.mGv_Datas)
    MyGridView mGv_Datas;

    /**
     * 设备信息
     */
    EquipmentEntity _EquipmentData;
    List<EquipmentUseReasonEntity> _EquipUseReasonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assay_use);

        ButterKnife.bind(this);

        initView();
        bindEvent();

        Intent intent = getIntent();
        String mTv_ID = intent.getStringExtra("mTv_ID");//设备ID
        if (mTv_ID != null && mTv_ID != null){

            mSearchBar.setVisibility(View.GONE);

            getEquipmentInfoByID(mTv_ID);


        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView() {
        mToolBar.setTitle("化学仪器使用");
        //去除状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mReceiver = mSearchBar.getBroadcastReceiver();
        mFilter = mSearchBar.getFilter();
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
        //设备图片点击事件
        mIv_EquipImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                mIv_EquipImg.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(mIv_EquipImg.getDrawingCache());
                mIv_EquipImg.setDrawingCacheEnabled(false);
                String equipName = mTv_EquipName.getText().toString();

                if (bitmap != null && equipName != null) {
                    Intent intent = new Intent(AssayUseActivity.this, ImageViewerActivity.class);
                    intent.putExtra("bitmap", bitmap);
                    intent.putExtra("title", equipName);
                    startActivity(intent);
                } else {
                    ToastUtils.showToast(AssayUseActivity.this, "无图片需显示");
                }
                return false;
            }
        });

        //事由点击事件
        mGv_Datas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                HashMap<String,String> map=(HashMap<String,String>)mGv_Datas.getItemAtPosition(position);

                if(_EquipmentData != null && map != null){
                    String useReason = map.get("mTv_Desc");
                    addAssayEquipUseRecord(_EquipmentData.getID(), useReason);
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

    /**
     * 根据条码检索设备
     *
     * @param barcode
     */
    private void SearchBarcode(String barcode) {
        if (barcode == null) {
            ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        }
        if (barcode.trim().length() == 0) {
            ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_fault_report_barcode_empty));
            return;
        } else {
            showLogingDialog();
            //根据条码内容检索设备信息
            getEquipmentInfo(barcode);
        }
    }

    /**
     * 根据设备条码获取设备信息
     *
     * @param barcode
     */
    private void getEquipmentInfo(String barcode) {
        SoapUtils.getEquipmentByBarcodeAsync(AssayUseActivity.this, barcode, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取设备信息失败:" + fault);
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
            //获取设备使用事由
            getAssayEquipUseReason(data.getID());

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
     * 获取化学仪器事由事由
     *
     * @param equipID
     */
    private void getAssayEquipUseReason(String equipID) {
        SoapUtils.getAssayEquipUseReason(AssayUseActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    ToastUtils.showLongToast(AssayUseActivity.this, "1获取事由信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {

                    ToastUtils.showLongToast(AssayUseActivity.this, "2获取事由信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentUseReasonResult result = new Gson().fromJson(strData, EquipmentUseReasonResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(AssayUseActivity.this, "3获取事由信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(AssayUseActivity.this, "4获取事由信息数据失败" + statusCode + result.msg);
                    return;
                }
                _EquipUseReasonList = result.getData();
                //获取到的事由填充至页面
                fillEquipUseReasion(_EquipUseReasonList);

                if(_EquipUseReasonList == null || _EquipUseReasonList.size() == 0){
                    ToastUtils.showLongToast(AssayUseActivity.this, "请联系设备管理员在【化学仪器档案】中维护“设备使用事由”！");
                }

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取事由信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取事由信息失败:" + fault);
            }
        });
    }

    /**
     * 设备使用事由填充至界面
     *
     * @param equipUseReasonList
     */
    private void fillEquipUseReasion(List<EquipmentUseReasonEntity> equipUseReasonList) {
        List<Map<String, Object>> data_list = getData(equipUseReasonList);
        //创建适配器
        String[] from = {"mTv_ID", "mTv_Desc"};
        int[] to = {R.id.mTv_ID, R.id.mTv_Desc};//grid_view_item_equip_use_reason布局中的两个控件id

        SimpleAdapter simple_adapter = new SimpleAdapter(this, data_list, R.layout.grid_view_item_equip_use_reason, from, to);
        mGv_Datas.setAdapter(simple_adapter);

    }

    public List<Map<String, Object>> getData(List<EquipmentUseReasonEntity> equipUseReasonList) {
        List<Map<String, Object>> data_list = new ArrayList<Map<String, Object>>();

        for(EquipmentUseReasonEntity item : equipUseReasonList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("mTv_ID",item.getID());
            map.put("mTv_Desc", item.getReasonDesc());
            data_list.add(map);
        }
        return data_list;
    }

    /**
     * 添加化学仪器使用记录
     * @param equipID
     * @param useReason
     */
    private void addAssayEquipUseRecord(String equipID, final String useReason)
    {
        showLogingDialog();
        SoapUtils.addAssayEquipUseRecordAsync(AssayUseActivity.this, equipID, useReason, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();

                if(null == object){
                    ToastUtils.showLongToast(AssayUseActivity.this,getString(R.string.activity_update_password_fail));
                    return;
                }
                //------------------------------
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_update_password_fail_param, "接口连接失败！"));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                Result result = new Gson().fromJson(strData, Result.class);
                //校验接口返回代码
                if(result == null)
                {
                    ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_update_password_fail_param, "接口返回信息异常"));
                    return;
                }
                else if(result.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_update_password_fail_param, result.msg));
                    return;
                }
                CommonUtils.playMusic(AssayUseActivity.this);
                ToastUtils.showToast(AssayUseActivity.this, useReason + " 使用记录提交成功！");

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this,getString(R.string.activity_update_password_fail));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                if(fault != null) {
                    ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_update_password_fail) + fault.toString());
                }
                else
                {
                    ToastUtils.showLongToast(AssayUseActivity.this, getString(R.string.activity_update_password_fail));
                }
            }
        });

    }

    /**
     * 根据设备ID获取设备信息
     *
     * @param equipID
     */
    private void getEquipmentInfoByID(String equipID) {
        showLogingDialog();

        SoapUtils.getEquipmentByIDAsync(AssayUseActivity.this, equipID, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();
                if (object == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "1获取设备信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "2获取设备信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                EquipmentResult result = new Gson().fromJson(strData, EquipmentResult.class);

                //校验接口返回代码
                if (result == null) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "3获取设备信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    fillEquipInfo(null);
                    ToastUtils.showLongToast(AssayUseActivity.this, "4获取设备信息数据失败" + statusCode + result.msg);
                    return;
                }
                EquipmentEntity data = result.getData();
                fillEquipInfo(data);

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取设备信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(AssayUseActivity.this, "获取设备信息失败:" + fault);
            }
        });
    }

}
