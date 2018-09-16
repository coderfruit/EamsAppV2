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

    public static final String MAIN_HOME_FUNCTION_ONE = "AppMenu故障报修";//故障报修
    public static final String MAIN_HOME_FUNCTION_TWO = "AppMenu设备巡检";//设备巡检
    public static final String MAIN_HOME_FUNCTION_THREE = "AppMenu预警提醒";//预警提醒
    public static final String MAIN_HOME_FUNCTION_FOUR = "AppMenu化验仪器使用";//化验仪器使用
    public static final String MAIN_HOME_FUNCTION_FIVE = "AppMenu生产设备查询";//生产设备查询
    public static final String MAIN_HOME_FUNCTION_SIX = "AppMenu化验设备查询";//化验设备查询
    public static final String MAIN_HOME_FUNCTION_SEVEN = "AppMenu维修";//维修
    public static final String MAIN_HOME_FUNCTION_EIGHT = "AppMenu保养";//保养
    public static final String MAIN_HOME_FUNCTION_NINE = "AppMenu检验";//检验
    public static final String MAIN_HOME_FUNCTION_TEN = "AppMenu外委维修";//外委维修


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
