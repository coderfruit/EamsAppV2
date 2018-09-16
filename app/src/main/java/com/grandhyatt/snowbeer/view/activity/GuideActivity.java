package com.grandhyatt.snowbeer.view.activity;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.grandhyatt.snowbeer.R;
import com.grandhyatt.snowbeer.adapter.pager.GuideViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import crossoverone.statuslib.StatusUtil;

public class GuideActivity extends ActivityBase implements ViewPager.OnPageChangeListener {

    private ViewPager vp;
    private GuideViewPagerAdapter vpAdapter;
    private List<View> views;

    private ImageView[] dots;// 底部小点图片
    private int currentIndex;// 记录当前选中位置
    private int page;// 记录当前页码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        // 第二个参数是状态栏色值:4183d7
        // 第三个参数是兼容5.0到6.0之间的状态栏颜色字体只能是白色，如果沉浸的颜色与状态栏颜色冲突, 设置一层浅色对比能显示出状态栏字体（可以找ui给一个合适颜色值）
        // 如果您的项目是6.0以上机型或者某些界面不适用沉浸, 推荐使用两个参数的setUseStatusBarColor
        StatusUtil.setUseStatusBarColor(this, Color.TRANSPARENT, Color.parseColor("#33000000"));
        // 第二个参数是是否沉浸,第三个参数是状态栏字体是否为黑色
        StatusUtil.setSystemStatus(this, true, true);

        initViews();// 初始化页面
        initDots();// 初始化底部小点

    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(this);

        views = new ArrayList<>();

        // 第一页面
        View firstView = inflater.inflate(R.layout.guide_one, null);
        ImageView firstImage = firstView.findViewById(R.id.guide_image_one);
        firstImage.setBackgroundResource(R.drawable.guide_1);

        // 第二页面
        View secondView = inflater.inflate(R.layout.guide_two, null);
        ImageView secondImage = secondView.findViewById(R.id.guide_image_two);
        secondImage.setBackgroundResource(R.drawable.guide_2);
        // 第三页面
        View threeView = inflater.inflate(R.layout.guide_three, null);
        ImageView threeImage = threeView.findViewById(R.id.guide_image_three);
        threeImage.setBackgroundResource(R.drawable.guide_3);

        views.add(firstView);
        views.add(secondView);
        views.add(threeView);

        vpAdapter = new GuideViewPagerAdapter(views, this);// 初始化Adapter

        vp = (ViewPager) findViewById(R.id.viewpager);
        vp.setAdapter(vpAdapter);
        vp.setOnPageChangeListener(this);// 绑定回调
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);

        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1 || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);

        currentIndex = position;
    }

    /**
     * 当当前页面被滑动时调用
     * */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        page = position;
    }

    /**
     * 当新的页面被选中时调用
     * */
    @Override
    public void onPageSelected(int position) {
        setCurrentDot(position);// 设置底部小点选中状态
    }

    /**
     * 当滑动状态改变时调用
     * */
    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
