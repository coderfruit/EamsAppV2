package com.grandhyatt.snowbeer.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.commonlib.view.fragment.IFragmentBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.HomeFunctionFragmentDataAdapter;
import com.grandhyatt.snowbeer.adapter.view.HomeFunctionMainActivityGridItemView;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.HomeFunctionEntity;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;
import com.grandhyatt.snowbeer.network.SoapUtils;
import com.grandhyatt.snowbeer.network.result.WarningInfoCountResult;
import com.grandhyatt.snowbeer.soapNetWork.SoapHttpStatus;
import com.grandhyatt.snowbeer.soapNetWork.SoapListener;
import com.grandhyatt.snowbeer.utils.PowerUtils;
import com.grandhyatt.snowbeer.utils.SPUtils;
import com.grandhyatt.snowbeer.view.MyGridView;
import com.grandhyatt.snowbeer.view.SquareLayout;
import com.grandhyatt.snowbeer.view.activity.AssayQueryActivity;
import com.grandhyatt.snowbeer.view.activity.AssayUseActivity;
import com.grandhyatt.snowbeer.view.activity.EquipRoutingInspectionActivity;
import com.grandhyatt.snowbeer.view.activity.EquipmentQueryActivity;
import com.grandhyatt.snowbeer.view.activity.FaultReport_Mgr_Activity;
import com.grandhyatt.snowbeer.view.activity.FaultReport_MyActivity;
import com.grandhyatt.snowbeer.view.activity.InspectReportActivity;
import com.grandhyatt.snowbeer.view.activity.MaintenReportActivity;
import com.grandhyatt.snowbeer.view.activity.RepairmentExReportActivity;
import com.grandhyatt.snowbeer.view.activity.RepairmentReportActivity;
import com.grandhyatt.snowbeer.view.activity.WarningInfo_EquipActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;


public class HomeFunctionFragment extends FragmentBase implements IFragmentBase{

    private final int ACTION_REFRESH_DATA = 0;

    @BindView(R.id.mGridView)
    MyGridView mGridView;
    @BindView(R.id.mRefreshLayout)
    SmartRefreshLayout mRefreshLayout;

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


        bindEvent();
        refreshUI();
        requestNetworkData();

        initView();
        return view;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void initView() {

        initWarningInfo();

    }



    @Override
    public void bindEvent() {
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0://故障报修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu故障报修)) {
                            IntentUtil.newIntent(getActivity(), FaultReport_MyActivity.class);
                        }
                        break;
                    case 1://报修处理
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu报修处理)) {
                            IntentUtil.newIntent(getActivity(), FaultReport_Mgr_Activity.class);
                        }
                        break;
                    case 2://设备巡检
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu设备巡检)) {
                            IntentUtil.newIntent(getActivity(), EquipRoutingInspectionActivity.class);
                        }
                        break;
                    case 3://预警提醒
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu生产设备预警提醒)) {
                            IntentUtil.newIntent(getContext(), WarningInfo_EquipActivity.class);
                        }
                        break;
                    case 4://化验仪器使用
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu化验仪器使用)) {
                            IntentUtil.newIntent(getContext(), AssayUseActivity.class);
                        }
                        break;
                    case 5://生产设备查询
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu生产设备查询)) {
                            IntentUtil.newIntent(getContext(), EquipmentQueryActivity.class);
                        }
                        break;
                    case 6://化验仪器查询
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu化验设备查询)) {
                            IntentUtil.newIntent(getContext(), AssayQueryActivity.class);
                        }
                        break;
                    case 7://维修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu维修)) {
                            IntentUtil.newIntent(getActivity(), RepairmentReportActivity.class);
                        }
                        break;
                    case 8://保养
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu保养)) {
                            IntentUtil.newIntent(getActivity(), MaintenReportActivity.class);
                        }
                        break;
                    case 9://检验
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu检验)) {
                            IntentUtil.newIntent(getActivity(), InspectReportActivity.class);
                        }
                        break;
                    case 10://外委维修
                        if(PowerUtils.isPowerShowToast(getContext(),PowerUtils.AppMenu外委维修)) {
                            IntentUtil.newIntent(getActivity(), RepairmentExReportActivity.class);
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

        //下拉刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //mRefreshLayout.setNoMoreData(false);
                requestNetworkData();
            }
        });
    }

    @Override
    public void refreshUI() {
        mDataList = new ArrayList<>();

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.baoxiu128);
        mHomeFunctionEntity.setName("我要报修");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.baoxiuchuli);
        mHomeFunctionEntity.setName("待处理报修");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.xunjian);
        mHomeFunctionEntity.setName("设备巡检");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.yujingtixing);
        mHomeFunctionEntity.setName("设备预警提醒");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.huaxueyiqi1);
        mHomeFunctionEntity.setName("化验仪器使用");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.shebeichaxun);
        mHomeFunctionEntity.setName("生产设备检索");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.chaxun);
        mHomeFunctionEntity.setName("化验设备检索");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.weixiu72);
        mHomeFunctionEntity.setName("维修");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.baoyang);
        mHomeFunctionEntity.setName("保养");
        mDataList.add(mHomeFunctionEntity);

        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.jianyan);
        mHomeFunctionEntity.setName("检验");
        mDataList.add(mHomeFunctionEntity);


        mHomeFunctionEntity = new HomeFunctionEntity();
        mHomeFunctionEntity.setImage(R.drawable.weixiu);
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
        initWarningInfo();
    }

    /**
     * 初始化预警信息
     */
    private void initWarningInfo() {
        CorporationEntity corp = SPUtils.getFirstLastLoginUserCorporations(getContext());
        if(corp != null){
            getWarinigInfo(corp.getID());
        }
    }

    /**
     * 获取预警信息
     *
     * @param corpID
     */
    private void getWarinigInfo(String corpID) {
        SoapUtils.getWarningInfoCount(this.getContext(),corpID,
                true,true,true,true,
                true,false,false,false,
                false,true,
                new SoapListener() {
            @Override
            public void onSuccess(int statusCode, SoapObject object) {

                if (object == null) {
                    ToastUtils.showLongToast(getContext(), "1获取预警信息数据失败" + statusCode);
                    return;
                }
                //判断接口连接是否成功
                if (statusCode != SoapHttpStatus.SUCCESS_CODE) {
                    ToastUtils.showLongToast(getContext(), "2获取预警信息数据失败" + statusCode);
                    return;
                }
                //接口返回信息正常
                String strData = object.getPropertyAsString(0);
                WarningInfoCountResult result = new Gson().fromJson(strData, WarningInfoCountResult.class);

                //校验接口返回代码
                if (result == null) {
                    ToastUtils.showLongToast(getContext(), "3获取预警信息数据失败" + statusCode);
                    return;
                } else if (result.code != Result.RESULT_CODE_SUCCSED) {
                    ToastUtils.showLongToast(getContext(), "4获取预警信息数据失败" + statusCode + result.msg);
                    return;
                }
                WarningInfoCountEntity data = result.getData();
                if (data != null) {
                    initWarningInfoCount(data);
                }
            }

            @Override
            public void onFailure(int statusCode, String content, Throwable error) {
                if(mRefreshLayout != null){
                    mRefreshLayout.finishRefresh(true);
                }
                ToastUtils.showLongToast(getContext(), "获取预警信息异常:" + error.getMessage());
            }

            @Override
            public void onFailure(int statusCode, SoapFault fault) {
                if(mRefreshLayout != null){
                    mRefreshLayout.finishRefresh(true);
                }
                ToastUtils.showLongToast(getContext(), "获取预警信息失败:" + fault);
            }
        });
    }

    /**
     * 初始化预警消息消息条数
     */
    private void initWarningInfoCount(WarningInfoCountEntity data) {
        if (data != null) {
            mAdapter.modifyItem(1,data.getReportFaultCount());

            int iRpCnt = data.getEquipRepairPlanCount();
            int iMtnCnt = data.getEquipMaintenPlanCount();
            int iIspCnt = data.getEquipInspectPlanCount();
            int iSpRpCnt = data.getEquipSpareReplacePlanCount();
            int iRpExCnt = data.getEquipRepairExPlanCount();

            int iAll = iRpCnt + iMtnCnt + iIspCnt + iSpRpCnt + iRpExCnt;
            mAdapter.modifyItem(3,iAll);
        }
//        mRefreshLayout.finishLoadMoreWithNoMoreData(); //设置SmartRefreshLayout 完成加载并标记没有更多数据
//        mRefreshLayout.finishLoadMore(true);//设置SmartRefreshLayout加载更多的完成标志
        if(mRefreshLayout != null){
            mRefreshLayout.finishRefresh(true);
        }

    }

}
