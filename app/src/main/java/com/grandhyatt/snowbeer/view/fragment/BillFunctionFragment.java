package com.grandhyatt.snowbeer.view.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.grandhyatt.commonlib.utils.IntentUtil;
import com.grandhyatt.commonlib.view.fragment.IFragmentBase;
import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.loader.GlideImageLoader;
import com.grandhyatt.snowbeer.view.activity.WebViewActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BillFunctionFragment extends FragmentBase implements IFragmentBase{

    private final int ACTION_REFRESH_DATA = 0;

    @BindView(R.id.mBanner)
    Banner mBanner;

    private Unbinder mUnbinder;

    private List<Integer> mImageList = new ArrayList<>();

    private static BillFunctionFragment mFragmentInstance;
    public static BillFunctionFragment newInstance() {
        if (mFragmentInstance == null) {
            mFragmentInstance = new BillFunctionFragment();
        }
        return mFragmentInstance;
    }

    public BillFunctionFragment() {

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
        View view = inflater.inflate(R.layout.fragment_bill_function, null);

        mUnbinder = ButterKnife.bind(this,view);

        mImageList = new ArrayList<>();
        mImageList.add(R.drawable.guide_1);
        mImageList.add(R.drawable.guide_2);
        mImageList.add(R.drawable.guide_3);

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

    }

    @Override
    public void refreshUI() {
        loadADListToBanner();
    }

    @Override
    public void requestNetworkData() {

    }

    /**
     * 加载广告列表
     *  */
    private void loadADListToBanner(){
        if (null == mBanner) return;

        //设置banner样式
//        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        mBanner.setImages(mImageList);
        //设置banner动画效果
//        mBanner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
//        mBanner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        mBanner.isAutoPlay(true);
        //设置轮播时间
        mBanner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        mBanner.start();

        mBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put(WebViewActivity.EXTRA_DATA_URL,"http://www.baidu.com");
                IntentUtil.newIntent(getContext(),WebViewActivity.class,hashMap);
            }
        });
    }
}
