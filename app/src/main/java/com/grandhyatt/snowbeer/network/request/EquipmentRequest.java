package com.grandhyatt.snowbeer.network.request;

/**
 * Created by ycm on 2018/10/7.
 */

public class EquipmentRequest {
    String corpID;
    String deptID;
    String useState;
    String equipTypeID;
    String assetTypeID;
    String currentLastIdx;

    String EquipInfo;
    String Location;
    String Keeper;
    String Manu;


    public EquipmentRequest() {
    }

    public String getCorpID() {
        return corpID;
    }

    public void setCorpID(String corpID) {
        this.corpID = corpID;
    }

    public String getDeptID() {
        return deptID;
    }

    public void setDeptID(String deptID) {
        this.deptID = deptID;
    }

    public String getUseState() {
        return useState;
    }

    public void setUseState(String useState) {
        this.useState = useState;
    }

    public String getEquipTypeID() {
        return equipTypeID;
    }

    public void setEquipTypeID(String equipTypeID) {
        this.equipTypeID = equipTypeID;
    }

    public String getAssetTypeID() {
        return assetTypeID;
    }

    public void setAssetTypeID(String assetTypeID) {
        this.assetTypeID = assetTypeID;
    }

    public String getCurrentLastIdx() {
        return currentLastIdx;
    }

    public void setCurrentLastIdx(String currentLastIdx) {
        this.currentLastIdx = currentLastIdx;
    }

    public String getEquipInfo() {
        return EquipInfo;
    }

    public void setEquipInfo(String equipInfo) {
        EquipInfo = equipInfo;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getKeeper() {
        return Keeper;
    }

    public void setKeeper(String keeper) {
        Keeper = keeper;
    }

    public String getManu() {
        return Manu;
    }

    public void setManu(String manu) {
        Manu = manu;
    }
}
