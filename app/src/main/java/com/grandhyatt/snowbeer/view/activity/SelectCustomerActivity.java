package com.grandhyatt.snowbeer.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.activity.IActivityBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.CustomerAdapter;
import com.grandhyatt.snowbeer.adapter.SpinnerAdapter;
import com.grandhyatt.snowbeer.entity.CustomerEntity;
import com.grandhyatt.snowbeer.network.NetWorkRequestUtils;
import com.grandhyatt.snowbeer.network.callback.GetCustomerCallBack;
import com.grandhyatt.snowbeer.network.request.GetCustomerRequest;
import com.grandhyatt.snowbeer.network.result.GetCustomerResult;
import com.grandhyatt.snowbeer.view.LetterComparator;
import com.grandhyatt.snowbeer.view.PinnedHeaderDecoration;
import com.grandhyatt.snowbeer.view.ToolBarLayout;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import cc.solart.wave.WaveSideBarView;
import okhttp3.Call;

/**
 * 有卡复检->第一步.确定复检及客户
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 19:23
 */
public class SelectCustomerActivity extends ActivityBase implements IActivityBase, View.OnClickListener {

    public static final int RESULT_RECHECK_COMPLETE_ACTIVITY = 10001;

    @BindView(R.id.mToolBar)
    ToolBarLayout mToolBar;

    @BindView(R.id.mRL_Filter)
    RelativeLayout mRL_Filter;

//    @BindView(R.id.mRefreshLayout)
//    SmartRefreshLayout mRefreshLayout;

    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.mSideBarView)
    WaveSideBarView mSideBarView;

    // 抽屉菜单对象
    private ActionBarDrawerToggle mDrawerBar;

    @BindView(R.id.mDrawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.mRL_LeftDrawer)
    RelativeLayout mRL_LeftDrawer;

    @BindView(R.id.mSpr_Area)
    Spinner mSpr_Area;

    @BindView(R.id.mSpr_Province)
    Spinner mSpr_Province;

    @BindView(R.id.mTFL_Level)
    TagFlowLayout mTFL_Level;

    private List<String> mTagFlowDataList = new ArrayList<>();

    private CustomerAdapter mAdapter;

    private List<CustomerEntity> mDataList = new ArrayList<>();

    private SpinnerAdapter mAreaAdapter;
    private SpinnerAdapter mProvinceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_customer);
        ButterKnife.bind(this);


        setSwipeBackEnable(false);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initView() {
        mToolBar.setTitle("选择客户");


        //设置菜单内容之外其他区域的背景色
        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getTestData();

        final PinnedHeaderDecoration decoration = new PinnedHeaderDecoration();
        decoration.registerTypePinnedHeader(1, new PinnedHeaderDecoration.PinnedHeaderCreator() {
            @Override
            public boolean create(RecyclerView parent, int adapterPosition) {
                return true;
            }
        });

        mRecyclerView.addItemDecoration(decoration);

    }

    @Override
    public void bindEvent() {

        mRL_Filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLeftLayout();
            }
        });

//        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                requestNetworkData();
//            }
//        });

        mSideBarView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = mAdapter.getLetterPosition(letter);

                if (pos != -1) {
                    mRecyclerView.scrollToPosition(pos);
                    LinearLayoutManager mLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    mLayoutManager.scrollToPositionWithOffset(pos, 0);
                }
            }
        });

        mDrawerBar = new ActionBarDrawerToggle(this, mDrawerLayout, R.mipmap.ic_launcher, R.string.open, R.string.close) {
            //菜单打开
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            // 菜单关闭
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerBar);
    }

    //左边菜单开关事件
    public void openLeftLayout() {
        if (mDrawerLayout.isDrawerOpen(mRL_LeftDrawer)) {
            mDrawerLayout.closeDrawer(mRL_LeftDrawer);
        } else {
            mDrawerLayout.openDrawer(mRL_LeftDrawer);
        }
    }

    @Override
    public void refreshUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Type listType = new TypeToken<ArrayList<CustomerEntity>>() {
                }.getType();
                Gson gson = new Gson();
//                final List<CustomerEntity> list = gson.fromJson(City.DATA, listType);
                List<CustomerEntity> dataList = new ArrayList<CustomerEntity>();
                dataList = mDataList;
                Collections.sort(dataList, new LetterComparator());
                final List<CustomerEntity> finalDataList = dataList;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter = new CustomerAdapter(SelectCustomerActivity.this, finalDataList);
                        mRecyclerView.setAdapter(mAdapter);

                        mAdapter.setOnItemClickListener(new CustomerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view) {
                                int position = mRecyclerView.getChildAdapterPosition(view);
                                ToastUtils.showToast(SelectCustomerActivity.this, mDataList.get(position).getCustomerName());
                            }

                            @Override
                            public void onItemLongClick(View view) {

                            }
                        });
                    }
                });
            }
        }).start();

        mTagFlowDataList.add("一批");
        mTagFlowDataList.add("二批");
        mTagFlowDataList.add("一级");
        mTagFlowDataList.add("二级");
        mTagFlowDataList.add("生产公司物流回瓶");

        final LayoutInflater mInflater = LayoutInflater.from(SelectCustomerActivity.this);
        mTFL_Level.setAdapter(new TagAdapter<String>(mTagFlowDataList) {

            @Override
            public View getView(FlowLayout parent, int position, String t) {
                TextView tagTextView = (TextView) mInflater.inflate(R.layout.view_item_tag_flow_layout_text, mTFL_Level, false);
                tagTextView.setText(t);
                return tagTextView;
            }
        });

        mTFL_Level.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet) {
                ToastUtils.showToast(SelectCustomerActivity.this, selectPosSet.toString());
            }
        });

        /****************************************************************************************/
        List<String> areaDataList = new ArrayList<>();
        areaDataList.add("和平区");
        areaDataList.add("沈河区");

//        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, areaDataList);
//        areaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

//        ArrayAdapter<String> areaAdapter = new ArrayAdapter<String>(this, R.layout.view_item_spinner_text, areaDataList);
//        areaAdapter.setDropDownViewResource(R.layout.view_item_spinner_text);
//        mSpr_Area.setAdapter(areaAdapter);

        mAreaAdapter = new SpinnerAdapter(SelectCustomerActivity.this, areaDataList);
        //绑定 Adapter到控件
        mSpr_Area.setAdapter(mAreaAdapter);

        mSpr_Area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strArea = parent.getItemAtPosition(position).toString();
                ToastUtils.showToast(SelectCustomerActivity.this, "你点击的是:" + strArea);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        /****************************************************************************************/

        List<String> provinceDataList = new ArrayList<>();
        provinceDataList.add("辽宁省");
        provinceDataList.add("吉林省");


        mProvinceAdapter = new SpinnerAdapter(SelectCustomerActivity.this, provinceDataList);
        //绑定 Adapter到控件
        mSpr_Province.setAdapter(mProvinceAdapter);

        mSpr_Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String strProvince = parent.getItemAtPosition(position).toString();
                ToastUtils.showToast(SelectCustomerActivity.this, "你点击的是:" + strProvince);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

    }

    @Override
    public void requestNetworkData() {

    }

    @Override
    public void onClick(View v) {
//        switch (v.getID()) {
//            case R.id.mIv_ScanCard:
//
//                break;
//            case R.id.mRL_StoreHouse:
//
//                break;
//            default:
//                break;
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
    }


    /**
     * 获取客户信息
     *
     * @param filter
     * @return
     */
    private void getCustomerList(String filter) {
        final GetCustomerRequest request = new GetCustomerRequest();
        request.setCustomerCode(filter);
        NetWorkRequestUtils.getCustomerAsync(SelectCustomerActivity.this, request, new GetCustomerCallBack() {
            @Override
            public void onError(Call call, Exception e, int id) {
//                mRefreshLayout.finishRefresh();
                ToastUtils.showToast(SelectCustomerActivity.this, "获取客户信息时发生错误:" + e.getMessage());
            }

            @Override
            public void onResponse(GetCustomerResult response, int id) {

//                mRefreshLayout.finishRefresh();

                if (response == null) {
                    ToastUtils.showToast(SelectCustomerActivity.this, "获取客户信息时发生错误!");
                    return;
                }

                if (response.data == null || response.code != Result.RESULT_CODE_SUCCSED || response.data == null) {
                    ToastUtils.showToast(SelectCustomerActivity.this, "获取客户信息时发生错误:" + response.msg);
                    return;
                }


            }
        });
    }

    private void getTestData() {
        CustomerEntity customerEntity;

        customerEntity = new CustomerEntity();
        customerEntity.setPys("A");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("B");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("C");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("D");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("E");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("F");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("G");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("H");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("I");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("J");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("K");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("L");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("M");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("N");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("O");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("P");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("Q");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("R");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("S");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("T");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("U");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("V");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("W");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("X");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("Y");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setPys("Z");
        customerEntity.setType(1);
        mDataList.add(customerEntity);

        /******************************************************************************************/

        customerEntity = new CustomerEntity();
        customerEntity.setId(1l);
        customerEntity.setCustomerName("安道全");
        customerEntity.setPys("ADQ");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(2l);
        customerEntity.setCustomerName("白雪");
        customerEntity.setPys("BX");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(3l);
        customerEntity.setCustomerName("陈晨");
        customerEntity.setPys("CC");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(4l);
        customerEntity.setCustomerName("陈二发");
        customerEntity.setPys("CEF");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(5l);
        customerEntity.setCustomerName("党建设");
        customerEntity.setPys("DJS");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(6l);
        customerEntity.setCustomerName("厄齐尔");
        customerEntity.setPys("EQE");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(7l);
        customerEntity.setCustomerName("范进");
        customerEntity.setPys("FJ");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(8l);
        customerEntity.setCustomerName("范统");
        customerEntity.setPys("FT");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(9l);
        customerEntity.setCustomerName("高峰");
        customerEntity.setPys("GF");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(10l);
        customerEntity.setCustomerName("高良");
        customerEntity.setPys("GL");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(11l);
        customerEntity.setCustomerName("高良久");
        customerEntity.setPys("GLJ");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(12l);
        customerEntity.setCustomerName("哈二斌");
        customerEntity.setPys("HEB");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(12l);
        customerEntity.setCustomerName("哈二斌");
        customerEntity.setPys("HEB");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(13l);
        customerEntity.setCustomerName("纪伯伦");
        customerEntity.setPys("JBL");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(1l);
        customerEntity.setCustomerName("老吴");
        customerEntity.setPys("LW");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(2l);
        customerEntity.setCustomerName("小吴");
        customerEntity.setPys("XW");
        customerEntity.setType(0);
        mDataList.add(customerEntity);

        customerEntity = new CustomerEntity();
        customerEntity.setId(3l);
        customerEntity.setCustomerName("中吴");
        customerEntity.setPys("ZW");
        mDataList.add(customerEntity);
    }

}
