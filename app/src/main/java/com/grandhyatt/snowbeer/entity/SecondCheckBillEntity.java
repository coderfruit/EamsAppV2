package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 *
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-08 11:09
 */
public class SecondCheckBillEntity implements Serializable {

    /** 单号 */
    private String billNo;
    /** 纸箱 */
    private String packingName;
    /** 纸箱代码 */
    private String packingCode;
    /** 日期 */
    private String secondCheckDate;
    /** 用户 */
    private String secondCheckUser;
    /** 可修复数量 */
    private int badBoxCount;
    /** 不可修复箱 */
    private int brokenBoxCount;
    /** 验收数量 */
    private int secondCheckCount;
    /** 退借数量 */
    private int returnLoanCount;
    /** 退押数量 */
    private int returnForegiftCount;
    /** 代保管数量 */
    private int depositCount;
    /** 退押金金额 */
    private int money;

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
     * 获取 secondCheckDate
     *
     * @return java.lang.String
     */
    public String getSecondCheckDate() {
        return secondCheckDate;
    }

    /**
     * 设置 secondCheckDate
     *
     * @param secondCheckDate
     * @return void
     */
    public void setSecondCheckDate(String secondCheckDate) {
        this.secondCheckDate = secondCheckDate;
    }

    /**
     * 获取 secondCheckUser
     *
     * @return java.lang.String
     */
    public String getSecondCheckUser() {
        return secondCheckUser;
    }

    /**
     * 设置 secondCheckUser
     *
     * @param secondCheckUser
     * @return void
     */
    public void setSecondCheckUser(String secondCheckUser) {
        this.secondCheckUser = secondCheckUser;
    }

    /**
     * 获取 badBoxCount
     *
     * @return int
     */
    public int getBadBoxCount() {
        return badBoxCount;
    }

    /**
     * 设置 badBoxCount
     *
     * @param badBoxCount
     * @return void
     */
    public void setBadBoxCount(int badBoxCount) {
        this.badBoxCount = badBoxCount;
    }

    /**
     * 获取 brokenBoxCount
     *
     * @return int
     */
    public int getBrokenBoxCount() {
        return brokenBoxCount;
    }

    /**
     * 设置 brokenBoxCount
     *
     * @param brokenBoxCount
     * @return void
     */
    public void setBrokenBoxCount(int brokenBoxCount) {
        this.brokenBoxCount = brokenBoxCount;
    }

    /**
     * 获取 secondCheckCount
     *
     * @return int
     */
    public int getSecondCheckCount() {
        return secondCheckCount;
    }

    /**
     * 设置 secondCheckCount
     *
     * @param secondCheckCount
     * @return void
     */
    public void setSecondCheckCount(int secondCheckCount) {
        this.secondCheckCount = secondCheckCount;
    }

    /**
     * 获取 returnLoanCount
     *
     * @return int
     */
    public int getReturnLoanCount() {
        return returnLoanCount;
    }

    /**
     * 设置 returnLoanCount
     *
     * @param returnLoanCount
     * @return void
     */
    public void setReturnLoanCount(int returnLoanCount) {
        this.returnLoanCount = returnLoanCount;
    }

    /**
     * 获取 returnForegiftCount
     *
     * @return int
     */
    public int getReturnForegiftCount() {
        return returnForegiftCount;
    }

    /**
     * 设置 returnForegiftCount
     *
     * @param returnForegiftCount
     * @return void
     */
    public void setReturnForegiftCount(int returnForegiftCount) {
        this.returnForegiftCount = returnForegiftCount;
    }

    /**
     * 获取 depositCount
     *
     * @return int
     */
    public int getDepositCount() {
        return depositCount;
    }

    /**
     * 设置 depositCount
     *
     * @param depositCount
     * @return void
     */
    public void setDepositCount(int depositCount) {
        this.depositCount = depositCount;
    }

    /**
     * 获取 money
     *
     * @return int
     */
    public int getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @param money
     * @return void
     */
    public void setMoney(int money) {
        this.money = money;
    }

    @Override
    public String toString() {
        return "SecondCheckBillEntity{" +
                "billNo='" + billNo + '\'' +
                ", packingName='" + packingName + '\'' +
                ", packingCode='" + packingCode + '\'' +
                ", secondCheckDate='" + secondCheckDate + '\'' +
                ", secondCheckUser='" + secondCheckUser + '\'' +
                ", badBoxCount=" + badBoxCount +
                ", brokenBoxCount=" + brokenBoxCount +
                ", secondCheckCount=" + secondCheckCount +
                ", returnLoanCount=" + returnLoanCount +
                ", returnForegiftCount=" + returnForegiftCount +
                ", depositCount=" + depositCount +
                ", money=" + money +
                '}';
    }
}
