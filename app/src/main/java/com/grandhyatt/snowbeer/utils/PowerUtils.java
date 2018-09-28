package com.grandhyatt.snowbeer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.grandhyatt.commonlib.utils.ToastUtils;
import com.grandhyatt.snowbeer.App;

/**
 * Created by wulifu on 2018/7/23.
 */

public class PowerUtils {

    public static final String MAIN_HOME_FUNCTION = "10000000";//主页
    public static final String MAIN_BILL_FUNCTION = "20000000";//单据页
    public static final String MAIN_MY_FUNCTION = "30000000";//我的

    public static final String AppMenu故障报修 = "AppMenu故障报修";//故障报修
    public static final String AppMenu设备巡检 = "AppMenu设备巡检";//设备巡检
    public static final String AppMenu化验仪器使用 = "AppMenu化验仪器使用";//化验仪器使用
    public static final String AppMenu生产设备查询 = "AppMenu生产设备查询";//生产设备查询
    public static final String AppMenu化验设备查询 = "AppMenu化验设备查询";//化验设备查询
    public static final String AppMenu维修 = "AppMenu维修";//维修
    public static final String AppMenu保养 = "AppMenu保养";//保养
    public static final String AppMenu检验 = "AppMenu检验";//检验
    public static final String AppMenu外委维修 = "AppMenu外委维修";//外委维修
    public static final String AppMenu报修处理 = "AppMenu报修处理";//报修处理
    public static final String AppMenu生产设备预警提醒 = "AppMenu生产设备预警提醒";//生产设备预警提醒 包括（维修、保养、检验、备件更换提醒、外委维修）
    //暂时先不考虑化学仪器预警及维修
    //public static final String AppMenu化学仪器预警提醒 = "AppMenu化学仪器预警提醒";//化学仪器预警提醒 包括（仪器按次数提醒、仪器按周期提醒、仪器维修提醒）

    //建筑物无条码暂时在app端不考虑建筑物相关预警及维修
    //public static final String AppMenu建筑物预警提醒 = "AppMenu建筑物预警提醒";//建筑物预警提醒（建筑物维修计划提醒）

    /**
     * Powers 构造函数
     */
    private PowerUtils() {

    }

    /**
     * 判断当前用户是否有指定权限
     */
    public static boolean isPower(Context context,String powerId) {
        if (TextUtils.isEmpty(SPUtils.getLastLoginUserPower(context))) {
            return false;
        }

        return SPUtils.getLastLoginUserPower(App.getAppContext()).contains(powerId);
    }

    public static boolean isPowerShowToast(Context context,String powerId) {

        if (TextUtils.isEmpty(SPUtils.getLastLoginUserPower(App.getAppContext()))) {
            return false;
        }

        if (SPUtils.getLastLoginUserPower(App.getAppContext()).contains(powerId)) {
            return true;
        }

        ToastUtils.showToast(context, "对不起，您无此权限!");

        return false;
    }
}
