package com.grandhyatt.snowbeer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.snowbeer.entity.ResourceEntity;
import com.grandhyatt.snowbeer.utils.UpdateUtils;
import com.grandhyatt.snowbeer.view.ActivityCollector;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.grandhyatt.snowbeer.view.activity.MainActivityBase;
import com.grandhyatt.snowbeer.view.fragment.BillFunctionFragment;
import com.grandhyatt.snowbeer.view.fragment.HomeFunctionFragment;
import com.grandhyatt.snowbeer.view.fragment.MyFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import crossoverone.statuslib.StatusUtil;

public class MainActivity extends MainActivityBase implements View.OnClickListener, BottomNavigationBar.OnTabSelectedListener  {

    private ArrayList<Fragment> mFragmentList;

    private static final int INTENT_PERMISSIONS_REQUEST_CODE = 0x01;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x02;

    /**标题栏*/
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    /**Fragment容器*/
    @BindView(R.id.fragment_container)
    FrameLayout mFragment_Container;

    /**底部Tab栏*/
    @BindView(R.id.mBottomNavigationBar)
    BottomNavigationBar mBottomNavigationBar;

    /**保存用户按返回键的时间*/
    private long mExitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // 第二个参数是状态栏色值:#4183d7
        // 第三个参数是兼容5.0到6.0之间的状态栏颜色字体只能是白色，如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体（可以找ui给一个合适颜色值）。
        // 如果您的项目是6.0以上机型或者某些界面不适用沉浸, 推荐使用两个参数的setUseStatusBarColor
        StatusUtil.setUseStatusBarColor(this, Color.parseColor("#4183d7"), Color.parseColor("#33000000"));
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色
        StatusUtil.setSystemStatus(this, false, false);

        //初始化视图
        initView();

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // 延迟3秒执行更新 避免与引导图冲突
                autoObtainPermissin();
            }
        }, 3000);
    }

    /**
     * 动态申请权限
     */
    private void autoObtainPermissin(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)) {
                ToastUtils.showToast(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE}, INTENT_PERMISSIONS_REQUEST_CODE);
        }else{
            UpdateUtils.checkUpdate_SelectDialog(MainActivity.this,false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相册申请Sdcard权限回调
            case INTENT_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    UpdateUtils.checkUpdate_SelectDialog(MainActivity.this,false);
                } else {
                    ToastUtils.showToast(this, "请允许获取访问网络和存储设备权限!");
                }
                break;
            default:
                break;
        }
    }

    /**
     * 初始化视图
     *
     * */
    private void initView(){
        //隐藏标题栏返回按钮
        mToolBar.hideBackButton();

        //设置底部Tab栏属性
        mBottomNavigationBar.setActiveColor(R.color.red).setInActiveColor(R.color.gray3);
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        //添加Tab项
        mBottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_home_normal, "首页"))
                //.addItem(new BottomNavigationItem(R.drawable.ic_tab_home_bill, "单据"))
                .addItem(new BottomNavigationItem(R.drawable.ic_tab_home_my, "我的"))
                .setFirstSelectedPosition(0)
                .initialise();
        //获取Fragment列表
        mFragmentList = getFragments();
        //设置默认Fragment
        setDefaultFragment();
        //设置TabSelected监听
        mBottomNavigationBar.setTabSelectedListener(this);
    }

    /**
     * 设置默认的页
     */
    private void setDefaultFragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fragment_container, HomeFunctionFragment.newInstance());
        transaction.commit();
    }

    /**
     * 获取Fragment列表
     * */
    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        //主界面
        fragments.add(HomeFunctionFragment.newInstance());
        //单据
        //fragments.add(BillFunctionFragment.newInstance());
        //我的
        fragments.add(MyFragment.newInstance());

        return fragments;
    }

    /**
     * 按Back键延迟处理
     * */
    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            ToastUtils.showLongToast(MainActivity.this,getString(R.string.common_toast_exit));
            mExitTime = System.currentTimeMillis();
        } else {
            MainActivity.this.finish();
            ActivityCollector.finishAll();
        }
    }

    /**
     * Tab Selected 监听
     * */
    @Override
    public void onTabSelected(int position) {
        if (mFragmentList != null) {
            if (position < mFragmentList.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mFragmentList.get(position);
                if (fragment.isAdded()) {
                    ft.replace(R.id.fragment_container, fragment);
                } else {
                    ft.add(R.id.fragment_container, fragment);
                }
                ft.commitAllowingStateLoss();
            }
        }
    }

    /**
     *
     * */
    @Override
    public void onTabUnselected(int position) {
        if (mFragmentList != null) {
            if (position < mFragmentList.size()) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                Fragment fragment = mFragmentList.get(position);
                ft.remove(fragment);
                ft.commitAllowingStateLoss();
            }
        }
    }

    @Override
    public void onTabReselected(int position) {

    }

    /**
     * 点击事件
     * */
    @Override
    public void onClick(View v) {

    }
}
