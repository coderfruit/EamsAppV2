package com.grandhyatt.snowbeer.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.callback.CommonCallback;
import com.grandhyatt.snowbeer.network.request.EditPasswordRequest;
import com.grandhyatt.snowbeer.network.result.LoginResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

public class EditPasswordActivity extends ActivityBase implements IActivityBase,View.OnClickListener{

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mEdt_OldPassword)
    EditText mEdt_OldPassword;

    @BindView(R.id.mEdt_NewPassword)
    EditText mEdt_NewPassword;

    @BindView(R.id.mEdt_ConfirmPassword)
    EditText mEdt_ConfirmPassword;

    @BindView(R.id.mBtn_OK)
    Button mBtn_OK;

    @BindView(R.id.bodyView)
    LinearLayout bodyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent() {
        mBtn_OK.setOnClickListener(this);
        bodyView.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("修改密码");
    }

    @Override
    public void requestNetworkData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.mBtn_OK:
                updatePassword();
                break;
            case R.id.bodyView:
                bodyClick(v);
                break;
            default:
                break;
        }
    }

    /**
     * 点击空白处关闭虚拟键盘
     * @param v
     */
    private void bodyClick(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void updatePassword(){
        if(TextUtils.isEmpty(mEdt_OldPassword.getText().toString())){
            ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_old_password_empty));
            mEdt_OldPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mEdt_NewPassword.getText().toString())){
            ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_new_password_empty));
            mEdt_NewPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(mEdt_ConfirmPassword.getText().toString())){
            ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_confirm_password_empty));
            mEdt_ConfirmPassword.requestFocus();
            return;
        }

        if(!mEdt_ConfirmPassword.getText().toString().equals(mEdt_NewPassword.getText().toString())){
            ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_new_confirm_password));
            mEdt_ConfirmPassword.requestFocus();
            return;
        }

        showLogingDialog();

        EditPasswordRequest request = new EditPasswordRequest();
        request.setPassword(mEdt_OldPassword.getText().toString());
        request.setPasswordNew(mEdt_NewPassword.getText().toString());

        SoapUtils.updatePasswordAsync(this, request, new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {
                dismissLoadingDialog();

                if(null == object){
                    ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_fail));
                    return;
                }
                //------------------------------
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(EditPasswordActivity.this, getString(R.string.activity_update_password_fail_param, "接口连接失败！"));
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                Result result = new Gson().fromJson(strData, Result.class);
                //校验接口返回代码
                if(result == null)
                {
                    ToastUtils.showLongToast(EditPasswordActivity.this, getString(R.string.activity_update_password_fail_param, "接口返回信息异常"));
                    return;
                }
                else if(result.code != Result.RESULT_CODE_SUCCSED){
                    ToastUtils.showLongToast(EditPasswordActivity.this, getString(R.string.activity_update_password_fail_param, result.msg));
                    return;
                }

                ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_succssed));
                finish();
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                dismissLoadingDialog();
                ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_fail));
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                dismissLoadingDialog();
                if(fault != null) {
                    ToastUtils.showLongToast(EditPasswordActivity.this, getString(R.string.activity_update_password_fail) + fault.toString());
                }
                else
                {
                    ToastUtils.showLongToast(EditPasswordActivity.this, getString(R.string.activity_update_password_fail));
                }
            }
        });


//        NetWorkRequestUtils.updatePasswordAsync(this, request, new CommonCallback() {
//            @Override
//            public void onError(Call call, Exception e, int id) {
//                dismissLoadingDialog();
//                ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_fail));
//            }
//
//            @Override
//            public void onResponse(Result response, int id) {
//                dismissLoadingDialog();
//
//                if(null == response){
//                    ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_fail));
//                    return;
//                }
//
//                if(response.code != Result.RESULT_CODE_SUCCSED){
//                    ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_fail_param,response.msg));
//                    return;
//                }
//
//                ToastUtils.showLongToast(EditPasswordActivity.this,getString(R.string.activity_update_password_succssed));
//                finish();
//            }
//        });
    }
}
