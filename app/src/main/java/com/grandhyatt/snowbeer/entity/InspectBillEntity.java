package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 设备检验记录
 * Created by tongzhiqiang on 2018-10-16.
 */

public class InspectBillEntity implements Serializable {

    private String ID;
    private String CorporationID;
    private String CorporationName;
    private String InspectionBillNO;
    private String InspectionDate;
    private String EquipmentID;
    private String EquipmentCode;
    private String EquipmentName;
    private String InspectionMode;
    private String InspectionItem;
    private String InspectionResult;
    private String InspectionCorp;
    private String InspectionUser;
    private String TotalMoney;
    private String MakeUser;
    private String MakeDate;
    private String IsActived;
    private String InspectionPlanID;
    private String Remark;
    private String NextInspectionDate;//下次检验日期

    public InspectBillEntity() {
    }

    public String getCorporationName() {
        return CorporationName;
    }

    public void setCorporationName(String corporationName) {
        CorporationName = corporationName;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCorporationID() {
        return CorporationID;
    }

    public void setCorporationID(String value) {
        this.CorporationID = value;
    }

    public String getInspectionBillNO() {
        return InspectionBillNO;
    }

    public void setInspectionBillNO(String value) {
        this.InspectionBillNO = value;
    }

    public String getInspectionDate() {
        return InspectionDate;
    }

    public void setInspectionDate(String value) {
        this.InspectionDate = value;
    }

    public String getEquipmentID() {
        return EquipmentID;
    }

    public void setEquipmentID(String value) {
        this.EquipmentID = value;
    }

    public String getInspectionMode() {
        return InspectionMode;
    }

    public void setInspectionMode(String value) {
        this.InspectionMode = value;
    }

    public String getInspectionItem() {
        return InspectionItem;
    }

    public void setInspectionItem(String value) {
        this.InspectionItem = value;
    }

    public String getInspectionResult() {
        return InspectionResult;
    }

    public void setInspectionResult(String value) {
        this.InspectionResult = value;
    }

    public String getInspectionCorp() {
        return InspectionCorp;
    }

    public void setInspectionCorp(String value) {
        this.InspectionCorp = value;
    }

    public String getInspectionUser() {
        return InspectionUser;
    }

    public void setInspectionUser(String value) {
        this.InspectionUser = value;
    }

    public String getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(String value) {
        this.TotalMoney = value;
    }

    public String getMakeUser() {
        return MakeUser;
    }

    public void setMakeUser(String value) {
        this.MakeUser = value;
    }

    public String getMakeDate() {
        return MakeDate;
    }

    public void setMakeDate(String value) {
        this.MakeDate = value;
    }

    public String getIsActived() {
        return IsActived;
    }

    public void setIsActived(String value) {
        this.IsActived = value;
    }

    public String getInspectionPlanID() {
        return InspectionPlanID;
    }

    public void setInspectionPlanID(String value) {
        this.InspectionPlanID = value;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String value) {
        this.Remark = value;
    }

    public String getNextInspectionDate() {
        return NextInspectionDate;
    }

    public void setNextInspectionDate(String nextInspectionDate) {
        NextInspectionDate = nextInspectionDate;
    }
}
