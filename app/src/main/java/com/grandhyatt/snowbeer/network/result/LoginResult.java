package com.grandhyatt.snowbeer.network.result;


import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.LoginUserInfoEntity;

/**
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:18
 */
public class LoginResult extends Result {

    private String token;

    private LoginUserInfoEntity data;

    /**
     * 获取 token
     *
     * @return java.lang.String
     */
    public String getToken() {
        return token;
    }

    /**
     * 设置 token
     *
     * @param token
     * @return void
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public LoginUserInfoEntity getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(LoginUserInfoEntity data) {
        this.data = data;
    }
}

