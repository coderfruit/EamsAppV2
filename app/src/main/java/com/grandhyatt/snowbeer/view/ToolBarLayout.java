package com.grandhyatt.snowbeer.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.grandhyatt.snowbeer.R;


/**
 * 标题栏控件
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/10 09:07
 */
public class ToolBarLayout extends RelativeLayout {
    private RelativeLayout mRL_ToolBar;
    private TextView mTitle;
    private TextView mMenuText;
    private RelativeLayout mBack;
    private RelativeLayout mMenu;

    /**
     * 构造函数
     */
    public ToolBarLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.view_activity_toolbar, this);

        mRL_ToolBar = (RelativeLayout) findViewById(R.id.mRL_ToolBar);
        mTitle = (TextView) findViewById(R.id.mTitle);
        mMenuText = (TextView) findViewById(R.id.mMenuText);
        mBack = (RelativeLayout) findViewById(R.id.mBack);
        mMenu = (RelativeLayout) findViewById(R.id.mMenu);

        mBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 显示菜单按钮
     */
    public void showBackButton() {
        if (mBack != null) {
            mBack.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏Back按钮
     */
    public void hideBackButton() {
        if (mBack != null) {
            mBack.setVisibility(GONE);
        }
    }

    /**
     * 显示菜单按钮
     */
    public void showMenuButton() {
        if (mMenu != null) {
            mMenu.setVisibility(VISIBLE);
        }
    }

    /**
     * 隐藏菜单按钮
     */
    public void hideMenuButton() {
        if (mMenu != null) {
            mMenu.setVisibility(GONE);
        }
    }

    /**
     * 设置标题
     */
    public void setTitle(String title) {
        if (mTitle != null && title != null) {
            mTitle.setText(title);
        }
    }

    /**
     * 设置菜单按钮文本
     */
    public void setMenuText(String text) {
        if (mMenuText != null && text != null) {
            mMenuText.setText(text);
        }
    }

    /**
     * 设置菜单按钮背景
     */
    public void setMenuBackground(Drawable drawable) {
        if (mMenuText != null && drawable != null) {
            mMenuText.setBackground(drawable);
        }
    }

    /**
     * 获取标题
     */
    public String getTitle() {
        if (mTitle != null) {
            return mTitle.getText().toString();
        } else {
            return "";
        }
    }

    /**
     * 设置背景色
     */
    public void setBackgroundColor(int color) {
        mRL_ToolBar.setBackgroundColor(color);
    }

    /**
     * 设置右菜单按钮单击监听
     */
    public void setMenuButtonOnClickListener(OnClickListener listener) {
        mMenu.setOnClickListener(listener);
    }
}
