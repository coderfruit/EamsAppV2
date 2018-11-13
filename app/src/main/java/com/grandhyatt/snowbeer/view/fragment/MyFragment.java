package com.grandhyatt.snowbeer.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.view.Dialog;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.fragment.IFragmentBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.db.ObjectBoxHelper;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.utils.UpdateUtils;
import com.grandhyatt.snowbeer.utils.transform.GlideCircleTransform;
import com.grandhyatt.snowbeer.view.ActivityCollector;
import com.grandhyatt.snowbeer.view.activity.AboutActivity;
import com.grandhyatt.snowbeer.view.activity.EditPasswordActivity;
import com.grandhyatt.snowbeer.view.activity.LoginActivity;
import com.zhy.view.flowlayout.TagFlowLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyFragment extends FragmentBase implements IFragmentBase, View.OnClickListener {

    private final int ACTION_REFRESH_DATA = 0;

    @BindView(R.id.mIv_Avatar)
    ImageView mIv_Avatar;

    @BindView(R.id.mTv_ID)
    TextView mTv_ID;

    @BindView(R.id.mTv_Name)
    TextView mTv_Name;

    @BindView(R.id.mRL_PersonalInfo)
    RelativeLayout mRL_PersonalInfo;

    @BindView(R.id.mRL_ChangePassword)
    RelativeLayout mRL_ChangePassword;

    @BindView(R.id.mRL_Update)
    RelativeLayout mRL_Update;

    @BindView(R.id.mRL_About)
    RelativeLayout mRL_About;

    @BindView(R.id.mBtn_Logout)
    Button mBtn_Logout;

    @BindView(R.id.mTv_Version)
    TextView mTv_Version;

    @BindView(R.id.mRL_UserCorp)
    RelativeLayout mRL_UserCorp;

    @BindView(R.id.mTv_UserCorp)
    TextView mTv_UserCorp;




    private Unbinder mUnbinder;

    private static MyFragment mFragmentInstance;

    public static MyFragment newInstance() {
        if (mFragmentInstance == null) {
            mFragmentInstance = new MyFragment();
        }
        return mFragmentInstance;
    }

    public MyFragment() {

    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case ACTION_REFRESH_DATA:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, null);

        mUnbinder = ButterKnife.bind(this, view);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mRL_ChangePassword://修改密码
                IntentUtil.newIntent(getContext(), EditPasswordActivity.class);
                break;
            case R.id.mRL_Update://检查更新
                UpdateUtils.checkUpdate_SelectDialog((Activity)getContext(),true);
                break;
            case R.id.mRL_About://关于我们
                IntentUtil.newIntent(getContext(), AboutActivity.class);
                break;
            case R.id.mBtn_Logout://注销
                logout();
                break;
            case R.id.mRL_UserCorp:
                showUserCorp();
                break;
            default:
                break;
        }
    }

    @Override
    public void initView() {
        mTv_Version.setText(UpdateUtils.getVersionName(this.getContext()));

        CorporationEntity corp = SPUtils.getFirstLastLoginUserCorporations(this.getContext());
        if(corp != null)
        {
            mTv_UserCorp.setText("归属工厂:" + corp.getCorporationName());
        }
    }

    @Override
    public void bindEvent() {
        mRL_ChangePassword.setOnClickListener(this);
        mBtn_Logout.setOnClickListener(this);
        mRL_Update.setOnClickListener(this);
        mRL_About.setOnClickListener(this);
        mRL_UserCorp.setOnClickListener(this);
    }

    @Override
    public void refreshUI() {
        //加载头像、账号、用户名后面的背景图片
        Glide.with(this).load(R.drawable.snow5)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mRL_PersonalInfo) {
                    //括号里为需要加载的控件
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            this.view.setBackground(resource.getCurrent());
                        } else {
                            this.view.setBackgroundResource(R.drawable.snow4);
                        }

                    }
                });

        Glide.with(getContext()).load(SPUtils.getLastLoginUserAvatar(getContext())).centerCrop().transform(
                new GlideCircleTransform(getContext())).error(R.drawable.ic_default_head).placeholder(R.drawable.ic_default_head).into(mIv_Avatar);

        mTv_ID.setText("账号:" + SPUtils.getLastLoginUserCode(getContext()));
        mTv_Name.setText("姓名:" + SPUtils.getLastLoginUserName(getContext()));
    }

    @Override
    public void requestNetworkData() {

    }

    //注销
    private void logout(){
        Dialog.Builder builder = new Dialog.Builder(getContext());
        builder.setMessage("你真的要注销吗?");
        builder.setTitle("注销");
        builder.setPositiveButtonTextColor(getResources().getColor(R.color.blue_title_bar));
        builder.setNegativeButtonTextColor(getResources().getColor(R.color.blue_title_bar));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                SPUtils.clearLastLoginUserInfo(getContext());
                IntentUtil.newIntent(getContext(), LoginActivity.class);
                ActivityCollector.finishAll();
                getActivity().finish();
            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();
    }

    private void showUserCorp()
    {
        List<String> corpName = new ArrayList<>();
        final List<CorporationEntity> corpList = SPUtils.getLastLoginUserCorporations(this.getContext());
        if(corpList != null && corpList.size() > 1){

            for (CorporationEntity item : corpList) {
                corpName.add(item.getCorporationName());
            }

            showSelectDialog(new SelectDialog.SelectDialogListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    CorporationEntity corp = corpList.get(position);

                    mTv_UserCorp.setText("归属工厂:" + corp.getCorporationName());
                    //SPUtils.setLastLoginUserCorporation(getContext(), corp);
                }
            },corpName);
        }

    }

    /**
     * 显示选择对话框菜单
     */
    protected SelectDialog showSelectDialog(SelectDialog.SelectDialogListener listener, List<String> lists) {
        SelectDialog dialog = new SelectDialog(this.getActivity(), R.style.transparentFrameWindowStyle, listener, lists);
        dialog.show();
        return dialog;
    }
}
