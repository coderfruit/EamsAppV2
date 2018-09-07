package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * Created by Wu on 2018-07-08.
 */

public class FirstCheckBillEntity implements Serializable {
    
    /** 周转单类型 */
    private String receivingBillType;
    /** 单号 */
    private String billNo;
    /** 纸箱名称 */
    private String packingName;
    /** 纸箱代码 */
    private String packingCode;
    /** 卡车号 */
    private String truckNo;
    /** 初检日期 */
    private String firstCheckDate;
    /** 初检用户 */
    private String firstCheckUser;

    /**
     * 获取 receivingBillType
     *
     * @return java.lang.String
     */
    public String getReceivingBillType() {
        return receivingBillType;
    }

    /**
     * 设置 receivingBillType
     *
     * @param receivingBillType
     * @return void
     */
    public void setReceivingBillType(String receivingBillType) {
        this.receivingBillType = receivingBillType;
    }

    /**
     * 获取 billNo
     *
     * @return java.lang.String
     */
    public String getBillNo() {
        return billNo;
    }

    /**
     * 设置 billNo
     *
     * @param billNo
     * @return void
     */
    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    /**
     * 获取 packingName
     *
     * @return java.lang.String
     */
    public String getPackingName() {
        return packingName;
    }

    /**
     * 设置 packingName
     *
     * @param packingName
     * @return void
     */
    public void setPackingName(String packingName) {
        this.packingName = packingName;
    }

    /**
     * 获取 packingCode
     *
     * @return java.lang.String
     */
    public String getPackingCode() {
        return packingCode;
    }

    /**
     * 设置 packingCode
     *
     * @param packingCode
     * @return void
     */
    public void setPackingCode(String packingCode) {
        this.packingCode = packingCode;
    }

    /**
     * 获取 truckNo
     *
     * @return java.lang.String
     */
    public String getTruckNo() {
        return truckNo;
    }

    /**
     * 设置 truckNo
     *
     * @param truckNo
     * @return void
     */
    public void setTruckNo(String truckNo) {
        this.truckNo = truckNo;
    }

    /**
     * 获取 firstCheckDate
     *
     * @return java.lang.String
     */
    public String getFirstCheckDate() {
        return firstCheckDate;
    }

    /**
     * 设置 firstCheckDate
     *
     * @param firstCheckDate
     * @return void
     */
    public void setFirstCheckDate(String firstCheckDate) {
        this.firstCheckDate = firstCheckDate;
    }

    /**
     * 获取 firstCheckUser
     *
     * @return java.lang.String
     */
    public String getFirstCheckUser() {
        return firstCheckUser;
    }

    /**
     * 设置 firstCheckUser
     *
     * @param firstCheckUser
     * @return void
     */
    public void setFirstCheckUser(String firstCheckUser) {
        this.firstCheckUser = firstCheckUser;
    }

    @Override
    public String toString() {
        return "FirstCheckBillEntity{" +
                "receivingBillType='" + receivingBillType + '\'' +
                ", billNo='" + billNo + '\'' +
                ", packingName='" + packingName + '\'' +
                ", packingCode='" + packingCode + '\'' +
                ", truckNo='" + truckNo + '\'' +
                ", firstCheckDate='" + firstCheckDate + '\'' +
                ", firstCheckUser='" + firstCheckUser + '\'' +
                '}';
    }
}
