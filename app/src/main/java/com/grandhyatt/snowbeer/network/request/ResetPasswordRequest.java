package com.grandhyatt.snowbeer.network.request;

/**
 * 
 *
 * @author
 * @email
 * @mobile
 * @create 2018/6/1 11:27
 */
public class ResetPasswordRequest {

    /** uid */
    private String uid;

    /** 手机号 */
    private String mobile;

    /** 手机验证码 */
    private String code;

    /** 密码 */
    private String pwd;

    /**
     * 获取 uid
     *
     * @return int
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

    /**
     * 获取 mobile
     *
     * @return java.lang.String
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置 mobile
     *
     * @param mobile
     * @return void
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获取 code
     *
     * @return java.lang.String
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置 code
     *
     * @param code
     * @return void
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取 pwd
     *
     * @return java.lang.String
     */
    public String getPwd() {
        return pwd;
    }

    /**
     * 设置 pwd
     *
     * @param pwd
     * @return void
     */
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
