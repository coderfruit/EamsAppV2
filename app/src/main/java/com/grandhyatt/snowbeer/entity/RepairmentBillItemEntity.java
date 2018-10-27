package com.grandhyatt.snowbeer.entity;

/**
 * 维修记录细表
 * Created by ycm on 2018/10/27.
 */

public class RepairmentBillItemEntity {
    private String ID;
    private boolean IsCheck;
    private String BillCode;
    private String DeptID;
    private String DeptCode;
    private String DeptName;
    private String MakeDate;
    private String SpareID;
    private String SpareCode;
    private String SpareName;
    private String SpareStander;
    private String SpareUnit;
    private String SparePrice;
    private String SpareStatus;
    private String Count;           //备件库存
    private String UserInputCount;//用户录入数量

    public RepairmentBillItemEntity() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public boolean isCheck() {
        return IsCheck;
    }

    public void setCheck(boolean check) {
        IsCheck = check;
    }

    public String getBillCode() {
        return BillCode;
    }

    public void setBillCode(String billCode) {
        BillCode = billCode;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(String deptCode) {
        DeptCode = deptCode;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String deptName) {
        DeptName = deptName;
    }

    public String getMakeDate() {
        return MakeDate;
    }

    public void setMakeDate(String makeDate) {
        MakeDate = makeDate;
    }

    public String getSpareID() {
        return SpareID;
    }

    public void setSpareID(String spareID) {
        SpareID = spareID;
    }

    public String getSpareCode() {
        return SpareCode;
    }

    public void setSpareCode(String spareCode) {
        SpareCode = spareCode;
    }

    public String getSpareName() {
        return SpareName;
    }

    public void setSpareName(String spareName) {
        SpareName = spareName;
    }

    public String getSpareStander() {
        return SpareStander;
    }

    public void setSpareStander(String spareStander) {
        SpareStander = spareStander;
    }

    public String getSpareUnit() {
        return SpareUnit;
    }

    public void setSpareUnit(String spareUnit) {
        SpareUnit = spareUnit;
    }

    public String getSparePrice() {
        return SparePrice;
    }

    public void setSparePrice(String sparePrice) {
        SparePrice = sparePrice;
    }

    public String getSpareStatus() {
        return SpareStatus;
    }

    public void setSpareStatus(String spareStatus) {
        SpareStatus = spareStatus;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }

    public String getUserInputCount() {
        return UserInputCount;
    }

    public void setUserInputCount(String userInputCount) {
        UserInputCount = userInputCount;
    }
}
