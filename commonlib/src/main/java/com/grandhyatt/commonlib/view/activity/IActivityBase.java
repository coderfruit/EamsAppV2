package com.grandhyatt.commonlib.view.activity;

/**
 * Activity基类接口
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:50
 */
public interface IActivityBase {

    /** 初始化View */
    void initView();

    /** 绑定事件 */
    void bindEvent();

    /** 刷新UI */
    void refreshUI();

    /** 请求网络数据 */
    void requestNetworkData();

}
