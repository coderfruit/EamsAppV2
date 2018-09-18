package com.grandhyatt.snowbeer.entity;

/**
 * Created by ycm on 2018/9/17.
 */

public class SpareInEquipmentEntity {

    private String ID;

    private String Status;

    private String RepairmentLevel;

    private String EquipmentID;

    private String EquipmentCode;

    private String EquipmentName;

    private String EquipmentTypeID;

    private String EquipmentTypeName;

    private String SpareID;

    private String SpareCode;

    private String SpareName;

    private String Location;

    private String ReplaceCount;

    private String ReplaceCycles;

    private String ReplaceCyclesManual;

    private String LastReplaceDate;

    private String NextReplaceDate;

    private String MakeUser;

    private String MakeDate;

    private String REMARK;

    private String AllowCount;

    private String CurrentCount;

    public SpareInEquipmentEntity() {
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

    public String getEquipmentTypeID() {
        return EquipmentTypeID;
    }

    public void setEquipmentTypeID(String equipmentTypeID) {
        EquipmentTypeID = equipmentTypeID;
    }

    public String getEquipmentTypeName() {
        return EquipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        EquipmentTypeName = equipmentTypeName;
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

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getReplaceCount() {
        return ReplaceCount;
    }

    public void setReplaceCount(String replaceCount) {
        ReplaceCount = replaceCount;
    }

    public String getReplaceCycles() {
        return ReplaceCycles;
    }

    public void setReplaceCycles(String replaceCycles) {
        ReplaceCycles = replaceCycles;
    }

    public String getReplaceCyclesManual() {
        return ReplaceCyclesManual;
    }

    public void setReplaceCyclesManual(String replaceCyclesManual) {
        ReplaceCyclesManual = replaceCyclesManual;
    }

    public String getLastReplaceDate() {
        return LastReplaceDate;
    }

    public void setLastReplaceDate(String lastReplaceDate) {
        LastReplaceDate = lastReplaceDate;
    }

    public String getNextReplaceDate() {
        return NextReplaceDate;
    }

    public void setNextReplaceDate(String nextReplaceDate) {
        NextReplaceDate = nextReplaceDate;
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

    public String getAllowCount() {
        return AllowCount;
    }

    public void setAllowCount(String allowCount) {
        AllowCount = allowCount;
    }

    public String getCurrentCount() {
        return CurrentCount;
    }

    public void setCurrentCount(String currentCount) {
        CurrentCount = currentCount;
    }

    public String getRepairmentLevel() {
        this.RepairmentLevel = "定修";
        return RepairmentLevel;
    }
}
