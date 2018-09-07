package com.grandhyatt.snowbeer.network.result;


import com.grandhyatt.commonlib.Result;

/**
 * 注册账号返回的数据结构
 *
 * @author
 * @email
 * @mobile
 * @create 2018/7/3 15:47
 */
public class ResetPasswordVerifyPhoneResult extends Result {

    /** uid */
    private String uid;

    /**
     * 获取 uid
     *
     * @return java.lang.String
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置 uid
     *
     * @param uid
     * @return void
     */
    public void setUid(String uid) {
        this.uid = uid;
    }
}
