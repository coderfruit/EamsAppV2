package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * 客户信息
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/24 01:15
 */
public class CustomerEntity implements Serializable{

    private Long id;

    private String salesRegionCode;

    private String salesRegionName;

    private Long customerTypeID;

    private String customerCode;

    private String customerName;

    private String customerLevel;

    private String province;

    private String pys;

    private  int type;

    public CustomerEntity() {

    }

    public CustomerEntity(Long id, String salesRegionCode, String salesRegionName, Long customerTypeID, String customerCode, String customerName, String customerLevel, String province, String pys, int type) {
        this.id = id;
        this.salesRegionCode = salesRegionCode;
        this.salesRegionName = salesRegionName;
        this.customerTypeID = customerTypeID;
        this.customerCode = customerCode;
        this.customerName = customerName;
        this.customerLevel = customerLevel;
        this.province = province;
        this.pys = pys;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @return 返回 id
     */
    public void setId(Long id) {
        this.id = id;
    }

    public String getSalesRegionCode() {
        return salesRegionCode;
    }

    /**
     * 设置 salesRegionCode
     *
     * @return 返回 salesRegionCode
     */
    public void setSalesRegionCode(String salesRegionCode) {
        this.salesRegionCode = salesRegionCode;
    }

    public String getSalesRegionName() {
        return salesRegionName;
    }

    /**
     * 设置 salesRegionName
     *
     * @return 返回 salesRegionName
     */
    public void setSalesRegionName(String salesRegionName) {
        this.salesRegionName = salesRegionName;
    }

    public Long getCustomerTypeID() {
        return customerTypeID;
    }

    /**
     * 设置 customerTypeID
     *
     * @return 返回 customerTypeID
     */
    public void setCustomerTypeID(Long customerTypeID) {
        this.customerTypeID = customerTypeID;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * 设置 customerCode
     *
     * @return 返回 customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置 customerName
     *
     * @return 返回 customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerLevel() {
        return customerLevel;
    }

    /**
     * 设置 customerLevel
     *
     * @return 返回 customerLevel
     */
    public void setCustomerLevel(String customerLevel) {
        this.customerLevel = customerLevel;
    }

    public String getProvince() {
        return province;
    }

    /**
     * 设置 province
     *
     * @return 返回 province
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获取 pys
     *
     * @return 返回 pys
     */
    public String getPys() {
        return pys;
    }

    /**
     * 设置 pys
     *
     * @return 返回 pys
     */
    public void setPys(String pys) {
        this.pys = pys;
    }

    /**
     * 获取 type
     *
     * @return 返回 type
     */
    public int getType() {
        return type;
    }

    /**
     * 设置 type
     *
     * @return 返回 type
     */
    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "CustomerEntity{" +
                "id=" + id +
                ", salesRegionCode='" + salesRegionCode + '\'' +
                ", salesRegionName='" + salesRegionName + '\'' +
                ", customerTypeID=" + customerTypeID +
                ", customerCode='" + customerCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerLevel='" + customerLevel + '\'' +
                ", province='" + province + '\'' +
                ", pys='" + pys + '\'' +
                ", type=" + type +
                '}';
    }
}
