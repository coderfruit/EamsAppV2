package com.grandhyatt.snowbeer.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.fragment.IFragmentBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.HomeFunctionFragmentDataAdapter;
import com.grandhyatt.snowbeer.entity.HomeFunctionEntity;
import com.grandhyatt.snowbeer.utils.PowerUtils;
import com.grandhyatt.snowbeer.view.MyGridView;
import com.grandhyatt.snowbeer.view.activity.CardReCheckActivity;
import com.grandhyatt.snowbeer.view.activity.MyFaultReportActivity;
import com.grandhyatt.snowbeer.view.activity.NoCardReCheckActivity;
import com.grandhyatt.snowbeer.view.activity.RepairmentReportActivity;
import com.grandhyatt.snowbeer.view.activity.WarningInfo_EquipActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class HomeFunctionFragment extends FragmentBase implements IFragmentBase{

    private final int ACTION_REFRESH_DATA = 0;

    @BindView(R.id.mGridView)
    MyGridView mGridView;

    private Unbinder mUnbinder;

    private List<HomeFunctionEntity> mDataList;
    private HomeFunctionEntity mHomeFunctionEntity;
    private HomeFunctionFragmentDataAdapter mAdapter;

    private static HomeFunctionFragment mFragmentInstance;
    public static HomeFunctionFragment newInstance() {
        if (mFragmentInstance == null) {
            mFragmentInstance = new HomeFunctionFragment();
        }
        return mFragmentInstance;
    }

    public HomeFunctionFragment() {

    }

    private Handler mHandler = new Handler(){
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
        View view = inflater.inflate(R.layout.fragment_home_function, null);

        mUnbinder = ButterKnife.bind(this,view);

        initView();
        bindEvent();
        refreshUI();
        requestNetworkData();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void initView() {

    }

    @Override
    public void bindEvent() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://故障报修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu故障报修)) {
                            IntentUtil.newIntent(getActivity(), MyFaultReportActivity.class);
                        }
                        break;
                    case 1://报修处理
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu报修处理)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 2://设备巡检
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu设备巡检)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 3://预警提醒
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu生产设备预警提醒)) {
                            IntentUtil.newIntent(getContext(), WarningInfo_EquipActivity.class);
                        }
                        break;
                    case 4://化验仪器使用
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu化验仪器使用)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 5://生产设备查询
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu生产设备查询)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 6://化验仪器查询
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu化验设备查询)) {
                            IntentUtil.newIntent(getActivity(), NoCardReCheckActivity.class);
                        }
                        break;
                    case 7://维修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu维修)) {
                            IntentUtil.newIntent(getActivity(), RepairmentReportActivity.class);
                        }
                        break;
                    case 8://保养
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu保养)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 9://检验
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu检验)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
                    case 10://外委维修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu外委维修)) {
                            ToastUtils.showToast(getContext(), "正在开发中...");
                        }
                        break;
//                    case 11://化学仪器预警提醒
//                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu化学仪器预警提醒)) {
//                            ToastUtils.showToast(getContext(), "正在开发中...");
//                        }
//                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public void refreshUI() {
        mDataList = new ArrayList<>();

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_card_check_turnover);
        mHomeFunctionEntity.setName("我要报修");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_card_check_turnover);
        mHomeFunctionEntity.setName("报修处理");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_card_check_take_back);
        mHomeFunctionEntity.setName("设备巡检");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_card_recheck);
        mHomeFunctionEntity.setName("设备预警提醒");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ssdk_oks_skyblue_logo_facebookmessenger_checked);
        mHomeFunctionEntity.setName("化验仪器使用");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ssdk_oks_skyblue_logo_facebookmessenger_checked);
        mHomeFunctionEntity.setName("生产设备查询");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_no_card_recheck);
        mHomeFunctionEntity.setName("化验设备查询");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ssdk_oks_skyblue_logo_facebookmessenger_checked);
        mHomeFunctionEntity.setName("维修");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ssdk_oks_skyblue_logo_facebookmessenger_checked);
        mHomeFunctionEntity.setName("保养");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ssdk_oks_skyblue_logo_facebookmessenger_checked);
        mHomeFunctionEntity.setName("检验");
        mDataList.add(mHomeFunctionEntity);


        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.ic_packaging);
        mHomeFunctionEntity.setName("外委维修");
        mDataList.add(mHomeFunctionEntity);

//        mHomeFunctionEntity = new HomeFunctionEntity();
//        mHomeFunctionEntity.setImage(R.drawable.ic_packaging);
//        mHomeFunctionEntity.setName("化学仪器预警提醒");
//        mDataList.add(mHomeFunctionEntity);

        mAdapter = new HomeFunctionFragmentDataAdapter(getActivity(),mDataList);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void requestNetworkData() {

    }
}
