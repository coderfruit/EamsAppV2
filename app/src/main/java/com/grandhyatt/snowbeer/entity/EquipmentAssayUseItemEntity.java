package com.grandhyatt.snowbeer.entity;

/**
 * 化学仪器使用记录
 * Created by ycm on 2018/10/26.
 */

public class EquipmentAssayUseItemEntity {

    private String ID;
    private String EquipmentID;
    private String EquipmentCode;
    private String EquipmentName;
    private String UseDate;
    private String UseUser;
    private String UseReason;
    private String MakeDate;
    private String MakeUser;
    private String Remark;
    private String UseCount;

    public EquipmentAssayUseItemEntity() {
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

    public String getUseDate() {
        return UseDate;
    }

    public void setUseDate(String useDate) {
        UseDate = useDate;
    }

    public String getUseUser() {
        return UseUser;
    }

    public void setUseUser(String useUser) {
        UseUser = useUser;
    }

    public String getUseReason() {
        return UseReason;
    }

    public void setUseReason(String useReason) {
        UseReason = useReason;
    }

    public String getMakeDate() {
        return MakeDate;
    }

    public void setMakeDate(String makeDate) {
        MakeDate = makeDate;
    }

    public String getMakeUser() {
        return MakeUser;
    }

    public void setMakeUser(String makeUser) {
        MakeUser = makeUser;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getUseCount() {
        return UseCount;
    }

    public void setUseCount(String useCount) {
        UseCount = useCount;
    }
}
