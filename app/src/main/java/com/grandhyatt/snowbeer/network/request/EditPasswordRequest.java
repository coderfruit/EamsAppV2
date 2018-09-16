package com.grandhyatt.snowbeer.network.request;

/**
 * 
 *
 * @author
 * @email
 * @mobile
 * @create 2018/6/1 11:27
 */
public class EditPasswordRequest {

    /** 原始密码 */
    private String password;
    /** 新密码 */
    private String passwordNew;

    public EditPasswordRequest() {

    }

    public EditPasswordRequest(String password, String passwordNew) {
        this.password = password;
        this.passwordNew = passwordNew;
    }

    /**
     * 获取 password
     *
     * @return 返回 password
     */
    public String getPassword() {
        return password;
    }

    /**
     * 设置 password
     *
     * @return 返回 password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * 获取 passwordNew
     *
     * @return 返回 passwordNew
     */
    public String getPasswordNew() {
        return passwordNew;
    }

    /**
     * 设置 passwordNew
     *
     * @return 返回 passwordNew
     */
    public void setPasswordNew(String passwordNew) {
        this.passwordNew = passwordNew;
    }

    @Override
    public String toString() {
        return "EditPasswordRequest{" +
                "password='" + password + '\'' +
                ", passwordNew='" + passwordNew + '\'' +
                '}';
    }
}
