package com.grandhyatt.snowbeer.network.request;

/**
 * 
 *
 * @author
 * @email
 * @mobile
 * @create 2018/6/1 11:27
 */
public class CheckUpdateRequest {

    /** 用户当前版本号 */
    private int versionCode;

    private String platform;

    private String versionName;


    public CheckUpdateRequest() {

    }

    public CheckUpdateRequest(int versionCode, String platform, String versionName) {
        this.versionCode = versionCode;
        this.platform = platform;
        this.versionName = versionName;
    }

    /**
     * 获取 versionName
     *
     * @return 返回 versionName
     */
    public String getVersionName() {
        return versionName;
    }

    /**
     * 设置 versionName
     *
     * @return 返回 versionName
     */
    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    /**
     * 获取 versionCode
     *
     * @return 返回 versionCode
     */
    public int getVersionCode() {
        return versionCode;
    }

    /**
     * 设置 versionCode
     *
     * @return 返回 versionCode
     */
    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    /**
     * 获取 platform
     *
     * @return 返回 platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置 platform
     *
     * @return 返回 platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    @Override
    public String toString() {
        return "CheckUpdateRequest{" +
                "versionCode=" + versionCode +
                ", platform='" + platform + '\'' +
                ", versionName='" + versionName + '\'' +
                '}';
    }
}
