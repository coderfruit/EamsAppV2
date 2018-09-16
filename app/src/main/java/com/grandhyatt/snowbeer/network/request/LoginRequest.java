package com.grandhyatt.snowbeer.network.request;

/**
 * 
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:27
 */
public class LoginRequest {

    /** 登录帐号 */
    private String account;

    /** 密码 */
    private String password;

    /** 构造函数 */
    public LoginRequest() {

    }

    /** 构造函数 */
    public LoginRequest(String account, String password) {
        this.account = account;
        this.password = password;
    }

    /**
     * 获取 account
     *
     * @return java.lang.String
     */
    public String getAccount() {
        return account;
    }

    /**
     * 设置 account
     *
     * @param account
     * @return void
     */
    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 获取 password
     *
     * @return java.lang.String
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置 password
     *
     * @param password
     * @return void
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * toString
     * */
    @Override
    public String toString() {
        return "LoginRequest{" +
                "account='" + account + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
