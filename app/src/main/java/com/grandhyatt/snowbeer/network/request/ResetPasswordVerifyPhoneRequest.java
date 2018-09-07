package com.grandhyatt.snowbeer.network.request;

/**
 * 获取短信验证码
 *
 * @author
 * @email
 * @mobile
 * @create 2018-07-02 22:27
 */
public class ResetPasswordVerifyPhoneRequest {

    /** 手机号 */
    private String mobile;

    /** 手机验证码 */
    private String code;

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
}
