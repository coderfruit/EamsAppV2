package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * 复检单
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/22 21:17
 */
public class SecondChkListEntity implements Serializable{

    /** 单号 */
    private String billNO;
    /** 纸箱名称 */
    private String packingName;
    /** 纸箱代码 */
    private String packingCode;
    /** 日期 */
    private String secondCheckDate;
    /** 用户 */
    private String secondCheckUser;
    /** 可修复箱 */
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
    /** 退押金额 */
    private int money;


    public SecondChkListEntity() {

    }

    public SecondChkListEntity(String billNO, String packingName, String packingCode, String secondCheckDate, String secondCheckUser, int badBoxCount, int brokenBoxCount, int secondCheckCount, int returnLoanCount, int returnForegiftCount, int depositCount, int money) {
        this.billNO = billNO;
        this.packingName = packingName;
        this.packingCode = packingCode;
        this.secondCheckDate = secondCheckDate;
        this.secondCheckUser = secondCheckUser;
        this.badBoxCount = badBoxCount;
        this.brokenBoxCount = brokenBoxCount;
        this.secondCheckCount = secondCheckCount;
        this.returnLoanCount = returnLoanCount;
        this.returnForegiftCount = returnForegiftCount;
        this.depositCount = depositCount;
        this.money = money;
    }

    /**
     * 获取 billNO
     *
     * @return 返回 billNO
     */
    public String getBillNO() {
        return billNO;
    }

    /**
     * 设置 billNO
     *
     * @return 返回 billNO
     */
    public void setBillNO(String billNO) {
        this.billNO = billNO;
    }

    /**
     * 获取 packingName
     *
     * @return 返回 packingName
     */
    public String getPackingName() {
        return packingName;
    }

    /**
     * 设置 packingName
     *
     * @return 返回 packingName
     */
    public void setPackingName(String packingName) {
        this.packingName = packingName;
    }

    /**
     * 获取 packingCode
     *
     * @return 返回 packingCode
     */
    public String getPackingCode() {
        return packingCode;
    }

    /**
     * 设置 packingCode
     *
     * @return 返回 packingCode
     */
    public void setPackingCode(String packingCode) {
        this.packingCode = packingCode;
    }

    /**
     * 获取 secondCheckDate
     *
     * @return 返回 secondCheckDate
     */
    public String getSecondCheckDate() {
        return secondCheckDate;
    }

    /**
     * 设置 secondCheckDate
     *
     * @return 返回 secondCheckDate
     */
    public void setSecondCheckDate(String secondCheckDate) {
        this.secondCheckDate = secondCheckDate;
    }

    /**
     * 获取 secondCheckUser
     *
     * @return 返回 secondCheckUser
     */
    public String getSecondCheckUser() {
        return secondCheckUser;
    }

    /**
     * 设置 secondCheckUser
     *
     * @return 返回 secondCheckUser
     */
    public void setSecondCheckUser(String secondCheckUser) {
        this.secondCheckUser = secondCheckUser;
    }

    /**
     * 获取 badBoxCount
     *
     * @return 返回 badBoxCount
     */
    public int getBadBoxCount() {
        return badBoxCount;
    }

    /**
     * 设置 badBoxCount
     *
     * @return 返回 badBoxCount
     */
    public void setBadBoxCount(int badBoxCount) {
        this.badBoxCount = badBoxCount;
    }

    /**
     * 获取 brokenBoxCount
     *
     * @return 返回 brokenBoxCount
     */
    public int getBrokenBoxCount() {
        return brokenBoxCount;
    }

    /**
     * 设置 brokenBoxCount
     *
     * @return 返回 brokenBoxCount
     */
    public void setBrokenBoxCount(int brokenBoxCount) {
        this.brokenBoxCount = brokenBoxCount;
    }

    /**
     * 获取 secondCheckCount
     *
     * @return 返回 secondCheckCount
     */
    public int getSecondCheckCount() {
        return secondCheckCount;
    }

    /**
     * 设置 secondCheckCount
     *
     * @return 返回 secondCheckCount
     */
    public void setSecondCheckCount(int secondCheckCount) {
        this.secondCheckCount = secondCheckCount;
    }

    /**
     * 获取 returnLoanCount
     *
     * @return 返回 returnLoanCount
     */
    public int getReturnLoanCount() {
        return returnLoanCount;
    }

    /**
     * 设置 returnLoanCount
     *
     * @return 返回 returnLoanCount
     */
    public void setReturnLoanCount(int returnLoanCount) {
        this.returnLoanCount = returnLoanCount;
    }

    /**
     * 获取 returnForegiftCount
     *
     * @return 返回 returnForegiftCount
     */
    public int getReturnForegiftCount() {
        return returnForegiftCount;
    }

    /**
     * 设置 returnForegiftCount
     *
     * @return 返回 returnForegiftCount
     */
    public void setReturnForegiftCount(int returnForegiftCount) {
        this.returnForegiftCount = returnForegiftCount;
    }

    /**
     * 获取 depositCount
     *
     * @return 返回 depositCount
     */
    public int getDepositCount() {
        return depositCount;
    }

    /**
     * 设置 depositCount
     *
     * @return 返回 depositCount
     */
    public void setDepositCount(int depositCount) {
        this.depositCount = depositCount;
    }

    /**
     * 获取 money
     *
     * @return 返回 money
     */
    public int getMoney() {
        return money;
    }

    /**
     * 设置 money
     *
     * @return 返回 money
     */
    public void setMoney(int money) {
        this.money = money;
    }


    @Override
    public String toString() {
        return "SecondChkListEntity{" +
                "billNO='" + billNO + '\'' +
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
