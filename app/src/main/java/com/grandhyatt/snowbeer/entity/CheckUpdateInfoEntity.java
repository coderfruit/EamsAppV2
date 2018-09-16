package com.grandhyatt.snowbeer.entity;

/**
 * 版本更新数据模型
 *
 * @author
 * @email
 * @mobile
 * @create 2018/7/12 09:03
 */
public class CheckUpdateInfoEntity {
    /** 类型:0普通更新,1建议更新,2强制更新 */
    private int Type;
    /** 版本号 */
    private String Version;;
    /** 下载地址 */
    private String Url;

    public CheckUpdateInfoEntity() {
    }

    public CheckUpdateInfoEntity(int type, String version, String url) {
        this.Type = type;
        this.Version = version;
        this.Url = url;
    }

    /**
     * 获取 type
     *
     * @return 返回 type
     */
    public int getType() {
        return Type;
    }

    /**
     * 设置 type
     *
     * @return 返回 type
     */
    public void setType(int type) {
        this.Type = type;
    }

    /**
     * 获取 version
     *
     * @return 返回 version
     */
    public String getVersion() {
        return Version;
    }

    /**
     * 设置 version
     *
     * @return 返回 version
     */
    public void setVersion(String version) {
        this.Version = version;
    }

    /**
     * 获取 url
     *
     * @return 返回 url
     */
    public String getUrl() {
        return Url;
    }

    /**
     * 设置 url
     *
     * @return 返回 url
     */
    public void setUrl(String url) {
        this.Url = url;
    }

    @Override
    public String toString() {
        return "CheckUpdateInfoEntity{" +
                "Type=" + Type +
                ", Version='" + Version + '\'' +
                ", Url='" + Url + '\'' +
                '}';
    }
}
