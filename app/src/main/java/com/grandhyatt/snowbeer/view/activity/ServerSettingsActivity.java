package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.db.ObjectBoxHelper;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置服务器IP地址和端口页面
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/20 09:11
 */
public class ServerSettingsActivity extends ActivityBase {

    /**标题栏*/
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    /** IP地址 */
    @BindView(R.id.mEdt_IPAddress)
    EditText mEdt_IPAddress;
    /** 端口 */
    @BindView(R.id.mEdt_Port)
    EditText mEdt_Port;
    /** 确定按钮 */
    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_settings);

        ButterKnife.bind(this);

        //初始化视图
        initView();
    }

    /**
     * 初始化视图
     *
     * */
    private void initView() {
        mToolBar.setTitle("IP地址设置");
        mBtn_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    /**
     * 检测IPAddress有效性
     * */
    private boolean checkIPAddress(String ipAddress) {
        /*正则表达式*/
        String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";//限定输入格式
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        boolean bolResult = matcher.matches();
        if (TextUtils.isEmpty(ipAddress)) return false;

        if (bolResult == false) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 检测Port有效性
     * */
    private boolean checkPort(String port) {
        return TextUtils.isDigitsOnly(port) && Integer.valueOf(port) < 65536;
    }

    /**
     * 保存设置
     * */
    private void saveData() {
        String strIPAddress = this.mEdt_IPAddress.getText().toString();
        String strPort = this.mEdt_Port.getText().toString();

        if (TextUtils.isEmpty(strIPAddress)) {
            ToastUtils.showToast(ServerSettingsActivity.this, "IP地址不能为空,请重新输入!");
            return;
        }

        if (TextUtils.isEmpty(strPort)) {
            ToastUtils.showToast(ServerSettingsActivity.this, "端口不能为空,请重新输入!");
            return;
        }

//        if (!checkIPAddress(strIPAddress)) {
//            ToastUtils.showToast(ServerSettingsActivity.this, "IP地址格式不正确,请重新输入!");
//            return;
//        }

        if (!checkPort(strPort)) {
            ToastUtils.showToast(ServerSettingsActivity.this, "端口格式不正确,请重新输入!");
            return;
        }

        APIHostInfoEntity apiHostInfo = new APIHostInfoEntity();
        apiHostInfo.setHost_url(strIPAddress);
        apiHostInfo.setPort(strPort);


        ObjectBoxHelper.saveAPIHostInfo(apiHostInfo);

        ToastUtils.showToast(ServerSettingsActivity.this, "保存成功!");
        finish();
    }
}
