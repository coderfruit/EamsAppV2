package com.grandhyatt.snowbeer.view.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.StringUtils;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.CommonCallback;
import com.grandhyatt.snowbeer.network.request.GetSMSCodeRequest;
import com.grandhyatt.snowbeer.network.request.ResetPasswordRequest;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class ResetPasswordActivity extends ActivityBase implements IActivityBase,View.OnClickListener{
    public final static String RESULT_DATA_ACCOUNT = "RESULT_DATA_ACCOUNT";
    public final static String RESULT_DATA_PASSWORD = "RESULT_DATA_PASSWORD";

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mEdt_Mobile)
    EditText mEdt_Mobile;

    @BindView(R.id.mEdt_SmsCode)
    EditText mEdt_SmsCode;

    @BindView(R.id.mBtn_GetSmsCode)
    Button mBtn_GetSmsCode;

    @BindView(R.id.mEdt_Password)
    EditText mEdt_Password;

    @BindView(R.id.mBtn_Commit)
    Button mBtn_Commit;

    @BindView(R.id.mChk_ShowPassword)
    CheckBox mChk_ShowPassword;

    private Boolean mBol_TimeFlag = true;

    Handler mHandler = new Handler() {
        @SuppressLint("NewApi")
        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what > 0) {
                mBtn_GetSmsCode.setEnabled(false);
                mBtn_GetSmsCode.setText(getString(R.string.common_text_get_sms_code_time, String.valueOf(msg.what)));
                mBtn_GetSmsCode.setTextColor(getColor(R.color.gray5));
            } else {
                mBtn_GetSmsCode.setEnabled(true);
                mBtn_GetSmsCode.setText(getString(R.string.common_text_get_sms_code));
                mBtn_GetSmsCode.setBackground(getResources().getDrawable(R.drawable.ic_get_code_before));
                mBtn_GetSmsCode.setTextColor(Color.parseColor("#c4c4c4"));
            }
        }
    };
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

    }

    @Override
    public void refreshUI(){
        mToolBar.setTitle("重置密码");
    }

    @Override
    public void requestNetworkData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent(){
        mBtn_GetSmsCode.setOnClickListener(this);
        mBtn_Commit.setOnClickListener(this);
        mChk_ShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mChk_ShowPassword.isChecked()) {
                    //设置EditText的密码为可见的
                    mEdt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    mEdt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_GetSmsCode:
                getSMSCode();
                break;
            case R.id.mBtn_Commit:
                resetPassword();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        mBol_TimeFlag = false;
    }


    private void getSMSCode(){

        mBtn_GetSmsCode.setEnabled(false);

        String strMobile = mEdt_Mobile.getText().toString();
        if(TextUtils.isEmpty(strMobile)){
            mBtn_GetSmsCode.setEnabled(true);
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_register_toast_input_mobile));
            return;
        }

        if (!StringUtils.isMobile(strMobile)) {
            mBtn_GetSmsCode.setEnabled(true);
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_register_toast_input_right_mobile));
            return;
        }

        showLogingDialog();

        GetSMSCodeRequest request = new GetSMSCodeRequest();
        request.setMobile(mEdt_Mobile.getText().toString());
        NetWorkRequestUtils.getResetPasswordSMSCodeAsync(this, request, new CommonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(ResetPasswordActivity.this,"短信验证码获取时发生错误!");
            }

            @Override
            public void onResponse(Result response, int id) {
                dismissLoadingDialog();

                mBol_TimeFlag = true;
                new Thread() {
                    public void run() {
                        for (int i = 60; i >= 0 && mBol_TimeFlag; i--) {
                            try {
                                sleep(1000);
                                Message msg = Message.obtain();
                                msg.what = i;
                                mHandler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch
                                // block
                                e.printStackTrace();
                            }
                        }
                    };
                }.start();

                if(null == response){
                    ToastUtils.showLongToast(ResetPasswordActivity.this,"短信验证码获取失败!");
                    return;
                }

                if(response.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(ResetPasswordActivity.this,"短信验证码获取失败!");
                    return;
                }
                ToastUtils.showLongToast(ResetPasswordActivity.this,"短信验证码获取成功");

                mBol_TimeFlag = true;
                new Thread() {
                    public void run() {
                        for (int i = 60; i >= 0 && mBol_TimeFlag; i--) {
                            try {
                                sleep(1000);
                                Message msg = Message.obtain();
                                msg.what = i;
                                mHandler.sendMessage(msg);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch
                                // block
                                e.printStackTrace();
                            }
                        }
                    };
                }.start();
            }
        });
    }

    private void resetPassword(){
        final String strMobile = mEdt_Mobile.getText().toString();
        final String strSmsCode = mEdt_SmsCode.getText().toString();
        final String strPassword = mEdt_Password.getText().toString();

        if (!StringUtils.isMobile(strMobile)) {
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_register_toast_input_right_mobile));
            return;
        }

        if(TextUtils.isEmpty(strMobile)){
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_register_toast_input_mobile));
            return;
        }

        if(TextUtils.isEmpty(strSmsCode)){
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.common_text_hint_sms_code));
            return;
        }

        if (mEdt_SmsCode.getText().toString().length() != 4) {
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.common_text_hint_sms_code_hint));
            return;
        }

        if(TextUtils.isEmpty(strPassword)){
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.common_text_toast_input_password));
            return;
        }

        if (mEdt_Password.getText().toString().length() < 6 || mEdt_Password.getText().toString().length() > 18) {
            ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.common_text_toast_input_password_hint));
            return;
        }

        mBtn_Commit.setEnabled(false);
        mBtn_GetSmsCode.setEnabled(false);

        showLogingDialog();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setMobile(strMobile);
        request.setCode(strSmsCode);
        request.setPwd(strPassword);

        NetWorkRequestUtils.resetPasswordAsync(ResetPasswordActivity.this, request, new CommonCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dismissLoadingDialog();

                mBtn_Commit.setEnabled(true);
                mBtn_GetSmsCode.setEnabled(true);
                ToastUtils.showToast(ResetPasswordActivity.this,getString(R.string.activity_reset_password_fail));
            }

            @Override
            public void onResponse(Result response, int id) {
                dismissLoadingDialog();

                mBtn_Commit.setEnabled(true);
                mBtn_GetSmsCode.setEnabled(true);

                if(null == response){
                    ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_reset_password_fail));
                    return;
                }

                if(response.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_reset_password_fail_param,response.msg));
                    return;
                }

                ToastUtils.showLongToast(ResetPasswordActivity.this,getString(R.string.activity_reset_password_succssed));
                Intent intent = new Intent();
                intent.putExtra(RESULT_DATA_ACCOUNT, strMobile);
                intent.putExtra(RESULT_DATA_PASSWORD, strPassword);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}
