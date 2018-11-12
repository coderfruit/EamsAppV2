package com.grandhyatt.snowbeer.view.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.Dialog;
import com.grandhyatt.commonlib.view.SelectDialog;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.LoginSettingsListViewAdapter;
import com.grandhyatt.snowbeer.db.ObjectBoxHelper;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.entity.OrganizationEntity;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.GetOrganizationListCallBack;
import com.grandhyatt.snowbeer.network.result.GetOrganizationListResult;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * 登录设置
 *
 * @author wulifu
 * @email wulifu@travelsky.com
 * @mobile 18602438878
 * @create 2018/7/20 10:05
 */
public class LoginSettingsActivity extends ActivityBase implements IActivityBase, View.OnClickListener, AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener {

    public final static int SERVER_SETTINGS_ACTIVITY = 10001;

    /** 标题栏 */
    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;
    /** 数据列表 */
    @BindView(R.id.mLv_DataList)
    ListView mLv_DataList;
    @BindView(R.id.mTv_Url)
    TextView mTv_Url;

    private List<APIHostInfoEntity> mDataList;

    private LoginSettingsListViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_settings);

        ButterKnife.bind(this);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
        //载入数据
        requestObjectBoxData();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    @Override
    public void initView() {
        APIHostInfoEntity apiHostInfo = SPUtils.getAPIHostInfo(LoginSettingsActivity.this);
        if(apiHostInfo != null){
            mTv_Url.setText(apiHostInfo.getHost_url() + ":" + apiHostInfo.getPort());
        }

    }

    @Override
    public void bindEvent() {


    }

    @Override
    public void refreshUI() {
        
        mToolBar.setTitle("登录设置");

        mToolBar.setMenuText("添加");
        mToolBar.showMenuButton();
        mToolBar.setMenuButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.newIntentForResult(LoginSettingsActivity.this, ServerSettingsActivity.class, SERVER_SETTINGS_ACTIVITY);
            }
        });

    }

    /**
     * 请求网络
     *
     * * @param null
     * @return
     */
    @Override
    public void requestNetworkData() {


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SERVER_SETTINGS_ACTIVITY:
                requestObjectBoxData();
                break;
            default:
                break;
        }
    }

    /**
     * ListView 单击
     *
     * * @param null
     * @return
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        int count = mAdapter.getCount();

        if (position >= 0 && position < count) {
            APIHostInfoEntity apiHostInfo = (APIHostInfoEntity) mAdapter.getItem(position);

            mAdapter.setSelected(mDataList.get(position));
            mAdapter.notifyDataSetChanged();

            //保存用户选择的服务器地址
            SPUtils.setAPIHostInfo(LoginSettingsActivity.this, apiHostInfo);
            if(apiHostInfo != null){
                mTv_Url.setText(apiHostInfo.getHost_url() + ":" + apiHostInfo.getPort());
            }
        }
    }

    /**
     * ListView Item长按显示删除
     *
     * * @param null
     * @return
     */
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

        Dialog.Builder builder = new Dialog.Builder(this);
        builder.setMessage("你确定真的要删除吗?");
        builder.setTitle("删除");
        builder.setPositiveButtonTextColor(getResources().getColor(R.color.blue2));
        builder.setNegativeButtonTextColor(getResources().getColor(R.color.blue2));
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                //删除ObjectBox存储的数据
                ObjectBoxHelper.removeAPIHostInfo(mDataList.get(position));
                if (mDataList.size() >= position) {
                    mDataList.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });

        builder.setNegativeButton("取消",
                new android.content.DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builder.create().show();

        return false;
    }

    /**
     * 从ObjectBox载入数据
     *
     * */
    private void requestObjectBoxData() {
        mDataList = ObjectBoxHelper.getAPIHostInfoList();

        mAdapter = new LoginSettingsListViewAdapter(LoginSettingsActivity.this, mDataList);
        mLv_DataList.setAdapter(mAdapter);

        APIHostInfoEntity apiHostInfo = new APIHostInfoEntity();
        apiHostInfo = SPUtils.getAPIHostInfo(LoginSettingsActivity.this);
        mAdapter.setSelected(apiHostInfo);

        mLv_DataList.setOnItemClickListener(this);
        mLv_DataList.setOnItemLongClickListener(this);
        mAdapter.notifyDataSetChanged();


    }



}
