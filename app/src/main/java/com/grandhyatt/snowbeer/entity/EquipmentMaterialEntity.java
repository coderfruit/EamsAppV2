package com.grandhyatt.snowbeer.entity;


import java.io.Serializable;

public class EquipmentMaterialEntity  implements Serializable {

    private  boolean IsCheck;
    private String ID;

    private String Unit;

    private String Standard;

    private String Price;

    private String MaterialCode;

    private  String MaterialID;

    private String REMARK;

    private String MCount;

    private String MaterialName;

    private String EquipmentID;

    private EquipmentMaterialEntity() {
    }

    public boolean getIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(boolean value) {
        IsCheck = value;
    }
    public String getMaterialID() {
        return MaterialID;
    }

    public void setMaterialID(String value) {
        MaterialID = value;
    }
    public String getMaterialCode() {
        return MaterialCode;
    }

    public void setMaterialCode(String value) {
        MaterialCode = value;
    }
    public String getMCount() {
        return MCount;
    }

    public void setMCount(String value) {
        MCount = value;
    }
    public String getMaterialName() {
        return MaterialName;
    }

    public void setMaterialName(String value) {
        MaterialName = value;
    }
    public String getEquipmentID() {
        return EquipmentID;
    }

    public void setEquipmentID(String value) {
        EquipmentID = value;
    }
    public String getPrice() {
        return Price;
    }

    public void setPrice(String value) {
        Price = value;
    }
    public String getUnit() {
        return Unit;
    }

    public void setUnit(String value) {
        Unit = value;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }




    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }





    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }




}
