package com.grandhyatt.snowbeer.entity;

import java.util.List;

/**
 * 设备保养计划
 * Created by ycm on 2018/9/28.
 */

public class MaintenanceEntity {


    private String Status;

    private String ID;

    private String CorporationID;

    private String MaintenanceBillNO;
    private String MaintenUser;

    private String CorporationName;

    private String EquipmentID;

    private String EquipmentCode;

    private String EquipmentName;

    private String DeptCode;

    private String DeptName;

    private String MaintenanceLevel;

    private String Interval;

    private String IntervalUnit;

    private String StartTime;

    private String FinishTime;

    private String TotalMoney;


    private int  ShutDownMinutes;

    private String MaintenancePlanID;

    private String MakeUser;

    private String MakeDate;

    private String Remark;

    private List<MaintenanceItemEntity> MaintenanceItemEntityList;

    public MaintenanceEntity() {
    }

    public List<MaintenanceItemEntity> getMaintenanceItemEntityList() {
        return MaintenanceItemEntityList;
    }

    public void setMaintenanceItemEntityList(List<MaintenanceItemEntity> value) {
        MaintenanceItemEntityList = value;
    }

    public String getMaintenUser() {
        return MaintenUser;
    }

    public void setMaintenUser(String value) {
        MaintenUser = value;
    }
    public String getMaintenanceBillNO() {
        return MaintenanceBillNO;
    }

    public void setMaintenanceBillNO(String value) {
        MaintenanceBillNO = value;
    }
    public String getMaintenancePlanID() {
        return MaintenancePlanID;
    }

    public void setMaintenancePlanID(String value) {
        MaintenancePlanID = value;
    }
    public int getShutDownMinutes() {
        return ShutDownMinutes;
    }

    public void setShutDownMinutes(int value) {
        ShutDownMinutes = value;
    }
    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String value) {
        TotalMoney = value;
    }
    public String getFinishTime() {
        return FinishTime;
    }

    public void setFinishTime(String finishTime) {
        FinishTime = finishTime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCorporationID() {
        return CorporationID;
    }

    public void setCorporationID(String corporationID) {
        CorporationID = corporationID;
    }

    public String getCorporationName() {
        return CorporationName;
    }

    public void setCorporationName(String corporationName) {
        CorporationName = corporationName;
    }

    public String getEquipmentID() {
        return EquipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        EquipmentID = equipmentID;
    }

    public String getEquipmentCode() {
        return EquipmentCode;
    }

    public void setEquipmentCode(String equipmentCode) {
        EquipmentCode = equipmentCode;
    }

    public String getEquipmentName() {
        return EquipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        EquipmentName = equipmentName;
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

    public String getMaintenanceLevel() {
        return MaintenanceLevel;
    }

    public void setMaintenanceLevel(String maintenanceLevel) {
        MaintenanceLevel = maintenanceLevel;
    }

    public String getInterval() {
        return Interval;
    }

    public void setInterval(String interval) {
        Interval = interval;
    }

    public String getIntervalUnit() {
        return IntervalUnit;
    }

    public void setIntervalUnit(String intervalUnit) {
        IntervalUnit = intervalUnit;
    }



    public String getMakeUser() {
        return MakeUser;
    }

    public void setMakeUser(String makeUser) {
        MakeUser = makeUser;
    }

    public String getMakeDate() {
        return MakeDate;
    }

    public void setMakeDate(String makeDate) {
        MakeDate = makeDate;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }
}
