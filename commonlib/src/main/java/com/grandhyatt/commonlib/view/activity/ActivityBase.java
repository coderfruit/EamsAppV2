package com.grandhyatt.commonlib.view.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.grandhyatt.commonlib.R;
import com.grandhyatt.commonlib.view.LoadingDialog;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

/**
 * Activity基类
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:50
 */
public class ActivityBase extends SwipeBackActivity {

    private static final String TAG = ActivityBase.class.getSimpleName();

    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSwipeBackEnable(true);
        } else {
            setSwipeBackEnable(false);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish(); // back button
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示加载对话框
     */
    protected void showLogingDialog() {
        mLoadingDialog = new LoadingDialog(ActivityBase.this, R.style.LoadingDialog);
        mLoadingDialog.show();
    }

    /**
     * 隐藏加载对话框
     */
    protected void dismissLoadingDialog() {
        if (mLoadingDialog == null) {
            return;
        }
        mLoadingDialog.dismiss();
    }


}
