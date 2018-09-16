package com.grandhyatt.snowbeer.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.LoginUserInfoEntity;
import com.grandhyatt.snowbeer.entity.ResourceEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.request.LoginRequest;
import com.grandhyatt.snowbeer.network.result.LoginResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PackageUtils;
import com.grandhyatt.snowbeer.MainActivity;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 登录窗体
 * 杨春苗
 * 20180907
 * 20180916 yangcm111
 */
public class LoginActivity extends ActivityBase implements IActivityBase,View.OnClickListener, TextView.OnEditorActionListener{

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mEdt_Account)
    EditText mEdt_Account;

    @BindView(R.id.mEdt_Password)
    EditText mEdt_Password;

    @BindView(R.id.mChk_ShowPassword)
    CheckBox mChk_ShowPassword;

    @BindView(R.id.mBtn_Login)
    Button mBtn_Login;

    @BindView(R.id.mBtn_Cancel)
    Button mBtn_Cancel;

    @BindView(R.id.mTv_Register)
    TextView mTv_Register;

    @BindView(R.id.mTv_ResetPassword)
    TextView mTv_ResetPassword;

    @BindView(R.id.mTv_Version)
    TextView mTv_Version;

    @BindView(R.id.bodyView)
    RelativeLayout bodyView;

    private int mInt_InputPhoneCount = 0;
    private int mInt_InputPasswordCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        //禁止滑动退出
        setSwipeBackEnable(false);
        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_Login:
                login();
                break;
            case R.id.mTv_Register:
                IntentUtil.newIntent(LoginActivity.this, RegisterActivity.class);
                break;
            case R.id.mTv_ResetPassword:
                IntentUtil.newIntent(LoginActivity.this, ResetPasswordActivity.class);
                break;
            case R.id.mBtn_Cancel:
                reset();
                break;
            case R.id.bodyView:
                bodyClick(v);
                break;
            default:
                break;
        }
    }

    /**
     * 重置
     */
    private void reset() {


    }

    /**
     * 点击空白处关闭虚拟键盘
     * @param v
     */
    private void bodyClick(View v) {
        InputMethodManager imm = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 密码文本框键盘事件监听
     *
     */
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (actionId) {
            case EditorInfo.IME_NULL:

                break;
            case EditorInfo.IME_ACTION_SEND:

                break;
            case EditorInfo.IME_ACTION_DONE:

                break;
            case EditorInfo.IME_ACTION_SEARCH:

                break;
            case EditorInfo.IME_ACTION_GO:
                login();
                break;
        }
        return true;
    }

    /**
     * 初始化ui
     *
     */
    @Override
    public void initView() {
        mToolBar.hideBackButton();
        mToolBar.showMenuButton();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }

    /**
     * 绑定事件
     *
     */
    @Override
    public void bindEvent() {

        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntent(LoginActivity.this,LoginSettingsActivity.class);
            }
        });

        /**
         * 手机号textWatcher
         */
        mEdt_Account.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                mInt_InputPhoneCount = s.length();
                if (mInt_InputPhoneCount > 0 || mInt_InputPasswordCount > 0) {
                    mBtn_Login.setEnabled(true);
                }

                if (mInt_InputPhoneCount == 0 && mInt_InputPasswordCount == 0) {
                    mBtn_Login.setEnabled(false);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        /**
         * 密码textWatcher
         */
        mEdt_Password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                mInt_InputPasswordCount = s.length();
                if (mInt_InputPhoneCount > 0 || mInt_InputPasswordCount > 0) {
                    mBtn_Login.setEnabled(true);
                }

                if (mInt_InputPhoneCount == 0 && mInt_InputPasswordCount == 0) {
                    mBtn_Login.setEnabled(false);
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }
        });

        /**
         * 查看密码
         */
        mChk_ShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (mChk_ShowPassword.isChecked()) {
                    //设置EditText的密码为可见的
                    mEdt_Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //设置密码为隐藏的
                    mEdt_Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        mBtn_Login.setOnClickListener(this);
        mBtn_Cancel.setOnClickListener(this);
        mTv_Register.setOnClickListener(this);
        mTv_ResetPassword.setOnClickListener(this);
        mEdt_Password.setOnEditorActionListener(this);
        bodyView.setOnClickListener(this);
    }

    /**
     * 刷新ui
     *
     */
    @Override
    public void refreshUI() {
        mTv_Version.setText(getString(R.string.login_activity_version,PackageUtils.getPackageVersionName(this)));

        mEdt_Account.setText(SPUtils.getLastLoginUserCode(LoginActivity.this));
        mEdt_Password.setText(SPUtils.getLastLoginUserPassword(LoginActivity.this));

        mEdt_Password.requestFocus();
        mEdt_Account.requestFocus();

    }

    @Override
    public void requestNetworkData() {


    }

    /**
     * 登录
     *
     */
    private void login() {
        final String username = mEdt_Account.getText().toString().trim();
        final String password = mEdt_Password.getText().toString().trim();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            ToastUtils.showLongToast(LoginActivity.this,getString(R.string.activity_login_toast_please_input_not_empty));
            return;
        }

        login(username, password);

    }

    /**
     * 登录
     *
     */
    private void login(final  String userCode, final String password) {
        showLogingDialog();
        LoginRequest request = new LoginRequest();
        request.setAccount(userCode);
        request.setPassword(password);

        mBtn_Login.setEnabled(false);

        SoapUtils.loginAsync(LoginActivity.this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                dismissLoadingDialog();
                mBtn_Login.setEnabled(true);
                if (object == null) {
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail));
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail_error, "接口连接失败！"));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                LoginResult result = new Gson().fromJson(strData, LoginResult.class);

                //校验接口返回代码
                if(result == null)
                {
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail_error, "接口返回信息异常"));
                    return;
                }
                else if(result.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail_error, result.msg));
                    return;
                }

                LoginUserInfoEntity data = result.getData();

                // 用户归属工厂
                CorporationEntity userCorp;
                if (data.getCorporations().size() == 0) {
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail_error, "没有为用户设置归属工厂，请联系管理员！"));
                    return;
                }
                else {
                    userCorp = data.getCorporations().get(0);
                }
                //用户权限校验
                String powers = "";
                List<ResourceEntity> resList = data.getResources();
                if(resList == null || resList.size() == 0)
                {
                    ToastUtils.showLongToast(LoginActivity.this, getString(R.string.activity_login_toast_login_fail_error, "没有为用户设置任何权限，请联系管理员！"));
                    return;
                }
                else{
                    for (ResourceEntity item : resList) {
                        powers += item.getResourceCode() + ",";
                    }
                }
                //保存当前登录用户账号和密码
                SPUtils.setLastLoginUserID(LoginActivity.this, String.valueOf(data.getID()));//用户ID
                SPUtils.setLastLoginUserCode(LoginActivity.this, userCode);          //用户编码
                SPUtils.setLastLoginUserName(LoginActivity.this, data.getUserName());//用户名
                SPUtils.setLastLoginUserPassword(LoginActivity.this, password);     //密码
                SPUtils.setLastLoginUserCorporation(LoginActivity.this, userCorp);  //用户组织机构
                SPUtils.setLastLoginUserPower(LoginActivity.this,powers);           //权限
                SPUtils.setLastLoginUserPhone(LoginActivity.this,data.getPhone());  //电话
                SPUtils.setToken(LoginActivity.this,result.getToken());

                IntentUtil.newIntent(LoginActivity.this, MainActivity.class);
                finish();

            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                mBtn_Login.setEnabled(true);
                ToastUtils.showLongToast(LoginActivity.this,getString(R.string.activity_login_toast_login_fail_error,error.getMessage()));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                mBtn_Login.setEnabled(true);
                ToastUtils.showLongToast(LoginActivity.this,getString(R.string.activity_login_toast_login_fail_error,fault.toString()));
            }
        });



    }



}
