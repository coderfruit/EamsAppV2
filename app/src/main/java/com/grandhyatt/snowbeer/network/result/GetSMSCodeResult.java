package com.grandhyatt.snowbeer.network.result;


import com.grandhyatt.commonlib.Result;

/**
 * 获取短信验证码的返回数据结构
 *
 * @author
 * @email
 * @mobile
 * @create 2018-07-02 22:42
 */
public class GetSMSCodeResult extends Result {

    /** 短信验证码 */
    private String code;

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
