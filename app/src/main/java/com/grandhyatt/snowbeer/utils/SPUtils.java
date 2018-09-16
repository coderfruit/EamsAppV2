package com.grandhyatt.snowbeer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.entity.CorporationEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;
import com.grandhyatt.snowbeer.view.activity.LoginActivity;

/**
 * Created by Wu on 2018-07-01.
 */

public class SPUtils extends com.grandhyatt.commonlib.utils.SPUtils {

    /**
     * 保存HostUrl
     * */
    public static void setHostUrl(Context context, String hostUrl){
        put(context,"HOST_URL",hostUrl);
    }

    /**
     * 获取HostUrl
     * */
    public static String getHostUrl(Context context){
        return String.valueOf(get(context,"HOST_URL",""));
    }

    /**
     * 保存HostPort
     * */
    public static void setHostPort(Context context, String port){
        put(context,"HOST_PORT",port);
    }

    /**
     * 获取HostPort
     * */
    public static String getHostPort(Context context){
        return String.valueOf(get(context,"HOST_PORT","8080"));
    }

    /**
     * 保存Organization
     * */
    public static void setOrganization(Context context, String organization){
        put(context,"ORGANIZATION_NAME",organization);
    }

    /**
     * 获取Organization
     * */
    public static String getOrganization(Context context){
        return String.valueOf(get(context,"ORGANIZATION_NAME",""));
    }

    /**
     * 保存Organization
     * */
    public static void setAPIHostInfo(Context context, APIHostInfoEntity apiHostInfo){
        try {
            String strData = new Gson().toJson(apiHostInfo);
            put(context, "APIHOST_INFO", strData);
        }catch (Exception ex){

        }
    }

    /**
     * 获取Organization
     * */
    public static APIHostInfoEntity getAPIHostInfo(Context context){
        try {
            String strData = String.valueOf(get(context, "APIHOST_INFO", ""));
            APIHostInfoEntity apiHostInfo = new Gson().fromJson(strData, APIHostInfoEntity.class);
            return apiHostInfo;
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * 保存Guide
     * */
    public static void setGuided(Context context,int versionCode){
        put(context,"GUIDE_FIRST" + String.valueOf(versionCode),"FIRST");
    }

    /**
     * 获取Guide
     * */
    public static boolean getGuided(Context context, int versionCode){
        return TextUtils.isEmpty(String.valueOf(get(context,"GUIDE_FIRST" + String.valueOf(versionCode),"")));
    }

    /**
     * 保存StoreHouse
     * */
    public static void setStoreHouse(Context context, String storeHouse){
        put(context,"STORE_HOUSE",storeHouse);
    }

    /**
     * 获取StoreHouse
     * */
    public static String getStoreHouse(Context context){
        return String.valueOf(get(context,"STORE_HOUSE",""));
    }

    /**
     * 保存StoreHouse
     * */
    public static void setStoreHouseEntity(Context context, StoreHouseEntity storeHouseEntity){
        try {
            String strJsonData = new Gson().toJson(storeHouseEntity);
            put(context, "STORE_HOUSE_ENTITY", strJsonData);
        }catch (Exception ex){

        }
    }

    /**
     * 获取StoreHouse
     * */
    public static StoreHouseEntity getStoreHouseEntity(Context context){
        try {
            String strStoreHouseEntity = String.valueOf(get(context, "STORE_HOUSE_ENTITY", ""));
            StoreHouseEntity storeHouseEntity = new Gson().fromJson(strStoreHouseEntity, StoreHouseEntity.class);
            return storeHouseEntity;
        }catch (Exception ex){
            return null;
        }
    }

    /**
     * 获取上一次登录用户名
     * */
    public static String getLastLoginUserName(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_NAME",""));
    }

    /**
     * 设置上一次登录用户名
     * */
    public static void setLastLoginUserName(Context context, String userName){
        put(context,"LAST_LGOIN_USER_NAME",userName);
    }

    /**
     * 获取上一次登录用户密码
     * */
    public static String getLastLoginUserPassword(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_PASSWORD",""));
    }

    /**
     * 设置上一次登录用户密码
     * */
    public static void setLastLoginUserPassword(Context context, String password){
        put(context,"LAST_LGOIN_USER_PASSWORD",password);
    }

    /**
     * 获取上一次登录用户UID
     * */
    public static String getLastLoginUserID(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_ID","-1"));
    }

    /**
     * 设置上一次登录用户UID
     * */
    public static void setLastLoginUserID(Context context, String uid){
        put(context,"LAST_LGOIN_USER_ID",uid);
    }

    /**
     * 获取上一次登录用户UID
     * */
    public static String getLastLoginUserPhone(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_PHONE","-1"));
    }

    /**
     * 设置上一次登录用户UID
     * */
    public static void setLastLoginUserPhone(Context context, String phone){
        put(context,"LAST_LGOIN_USER_PHONE",phone);
    }

    /**
     * 获取上一次登录用户头像
     * */
    public static String getLastLoginUserAvatar(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_AVATAR",""));
    }

    /**
     * 设置上一次登录用户头像
     * */
    public static void setLastLoginUserAvatar(Context context, String avatar){
        put(context,"LAST_LGOIN_USER_AVATAR",avatar);
    }

    /**
     * 获取上一次登录用户权限
     * */
    public static String getLastLoginUserPower(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_POWER",""));
    }

    /**
     * 设置上一次登录用户权限
     * */
    public static void setLastLoginUserPower(Context context, String power){
        put(context,"LAST_LGOIN_USER_POWER",power);
    }

    /**
     * 清除上一次登录用户信息
     * */
    public static void clearLastLoginUserInfo(Context context){
        setLastLoginUserID(context,"-1");
        setLastLoginUserName(context,"");
        setLastLoginUserPassword(context,"");
    }

    /**
     * 获取用户登录后Token信息
     * */
    public static String getToken(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_TOKEN",""));
    }

    /**
     * 设置用户登录后Token信息
     * */
    public static void setToken(Context context, String token){
        put(context,"LAST_LGOIN_USER_TOKEN",token);
    }

    public static void setLastLoginUserCode(Context context,String userCode) {
        put(context,"LAST_LGOIN_USER_CODE",userCode);
    }

    public static String getLastLoginUserCode(Context context){
        return String.valueOf(get(context,"LAST_LGOIN_USER_CODE",""));
    }

    public static void setLastLoginUserCorporation(Context context,CorporationEntity corporation) {
        put(context,"LAST_LGOIN_USER_CORPORATION",corporation);
    }

    public static CorporationEntity getLastLoginUserCorporation(Context context){
        return  (CorporationEntity)get(context,"LAST_LGOIN_USER_CORPORATION","");
    }
}
