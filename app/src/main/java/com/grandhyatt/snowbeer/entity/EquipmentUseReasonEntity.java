package com.grandhyatt.snowbeer.entity;

/**
 * 化学仪器使用事由
 * Created by ycm on 2018/10/8.
 */

public class EquipmentUseReasonEntity {
    private String ID;

    private String EquipmentID;

    private String ReasonDesc;

    public EquipmentUseReasonEntity() {
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

    public String getReasonDesc() {
        return ReasonDesc;
    }

    public void setReasonDesc(String reasonDesc) {
        ReasonDesc = reasonDesc;
    }
}
