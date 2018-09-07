package com.grandhyatt.snowbeer.view.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.utils.PackageUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 关于页面
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 13:27
 */
public class AboutActivity extends ActivityBase implements IActivityBase {

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mTv_AppVersion)
    TextView mTv_AppVersion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

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

    }

    @Override
    public void refreshUI() {
        mToolBar.setTitle("关于");
        mTv_AppVersion.setText(PackageUtils.getPackageVersionName(this));

    }

    @Override
    public void requestNetworkData() {

    }
}
