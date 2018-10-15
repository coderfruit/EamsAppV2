package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * Created by tongzhiqiang on 2018-09-17.
 */

public class RepairmentBillEntity implements Serializable {

    private String ID;

    private String CorporationID;


    private String EquipmentID;

    private String  RepairmentBillNO;
    private String Description;
    private String RepairUser;
    private String RepairmentLevel;
    private String FaultClass;

    private String StartTime;
    private String FinishTime;
    private String ShutDownMinutes;

    private String TotalMoney;
    private String MakeUser;

    private String MakeDate;


    private String Interval;

    private String IntervalUnit;

    private String LastRunningDate;

    private String NextRunningDate;

    private String WarningDays;





    private String REMARK;

    public RepairmentBillEntity() {

    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        TotalMoney = totalMoney;
    }
    public String getShutDownMinutes() {
        return ShutDownMinutes;
    }

    public void setShutDownMinutes(String shutDownMinutes) {
        ShutDownMinutes = shutDownMinutes;
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
    public String getFaultLevel() {
        return FaultClass;
    }

    public void setFaultLevel(String faultLevel) {
        FaultClass = faultLevel;
    }
    public String getRepairUser() {
        return RepairUser;
    }

    public void setRepairUser(String sepairUser) {
        RepairUser = sepairUser;
    }
    public String getRepairmentBillNO() {
        return RepairmentBillNO;
    }

    public void setRepairmentBillNO(String repairmentBillNO) {
        RepairmentBillNO = repairmentBillNO;
    }

    public String getCorporationID() {
        return CorporationID;
    }

    public void setCorporationID(String corporationID) {
        CorporationID = corporationID;
    }

    public String getEquipmentID() {
        return EquipmentID;
    }

    public void setEquipmentID(String equipmentID) {
        EquipmentID = equipmentID;
    }


    public String getRepairmentLevel() {
        return RepairmentLevel;
    }

    public void setRepairmentLevel(String repairmentLevel) {
        RepairmentLevel = repairmentLevel;
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

    public String getLastRunningDate() {
        return LastRunningDate;
    }

    public void setLastRunningDate(String lastRunningDate) {
        LastRunningDate = lastRunningDate;
    }

    public String getNextRunningDate() {
        return NextRunningDate;
    }

    public void setNextRunningDate(String nextRunningDate) {
        NextRunningDate = nextRunningDate;
    }

    public String getWarningDays() {
        return WarningDays;
    }

    public void setWarningDays(String warningDays) {
        WarningDays = warningDays;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
