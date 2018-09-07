package com.grandhyatt.snowbeer.entity;

/**
 * Created by ycm on 2018/8/18.
 */

public class EquipmentEntity {

    private String ID;

    private long CorporationID;

    private String CorporationName;

    private long DepartmentID;

    private String DepartmentName;

    private long EquipmentTypeID;

    private String EquipmentTypeName;

    private String AssetCode;

    private String EquipmentCode;

    private String EquipmentName;

    private String EquipmentClass;

    private String EquipmentStatus;

    private String InstallLoaction;

    private String Standard;

    private String Manufacturer;

    private String StartDate;

    private String GetMode;

    private String KeeperName;

    private String OriginalValue;

    private int DepreciationYears;

    private String DepreciationMethod;

    private String NetResidualRate;

    private String MakeUser;

    private boolean ISACTIVED;

    private String REMARK;

    private String AssetTypeID;

    private String EquipImg;

    public EquipmentEntity(String ID, long corporationID, String corporationName, long departmentID, String departmentName, long equipmentTypeID, String equipmentTypeName, String assetCode, String equipmentCode, String equipmentName, String equipmentClass, String equipmentStatus, String installLoaction, String standard, String manufacturer, String startDate, String getMode, String keeperName, String originalValue, int depreciationYears, String depreciationMethod, String netResidualRate, String makeUser, boolean ISACTIVED, String REMARK, String assetTypeID, String equipImg) {
        this.ID = ID;
        CorporationID = corporationID;
        CorporationName = corporationName;
        DepartmentID = departmentID;
        DepartmentName = departmentName;
        EquipmentTypeID = equipmentTypeID;
        EquipmentTypeName = equipmentTypeName;
        AssetCode = assetCode;
        EquipmentCode = equipmentCode;
        EquipmentName = equipmentName;
        EquipmentClass = equipmentClass;
        EquipmentStatus = equipmentStatus;
        InstallLoaction = installLoaction;
        Standard = standard;
        Manufacturer = manufacturer;
        StartDate = startDate;
        GetMode = getMode;
        KeeperName = keeperName;
        OriginalValue = originalValue;
        DepreciationYears = depreciationYears;
        DepreciationMethod = depreciationMethod;
        NetResidualRate = netResidualRate;
        MakeUser = makeUser;
        this.ISACTIVED = ISACTIVED;
        this.REMARK = REMARK;
        AssetTypeID = assetTypeID;
        EquipImg = equipImg;
    }

    public EquipmentEntity() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getCorporationID() {
        return CorporationID;
    }

    public void setCorporationID(long corporationID) {
        CorporationID = corporationID;
    }

    public String getCorporationName() {
        return CorporationName;
    }

    public void setCorporationName(String corporationName) {
        CorporationName = corporationName;
    }

    public long getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(long departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public long getEquipmentTypeID() {
        return EquipmentTypeID;
    }

    public void setEquipmentTypeID(long equipmentTypeID) {
        EquipmentTypeID = equipmentTypeID;
    }

    public String getEquipmentTypeName() {
        return EquipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        EquipmentTypeName = equipmentTypeName;
    }

    public String getAssetCode() {
        return AssetCode;
    }

    public void setAssetCode(String assetCode) {
        AssetCode = assetCode;
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

    public String getEquipmentClass() {
        return EquipmentClass;
    }

    public void setEquipmentClass(String equipmentClass) {
        EquipmentClass = equipmentClass;
    }

    public String getEquipmentStatus() {
        return EquipmentStatus;
    }

    public void setEquipmentStatus(String equipmentStatus) {
        EquipmentStatus = equipmentStatus;
    }

    public String getInstallLoaction() {
        return InstallLoaction;
    }

    public void setInstallLoaction(String installLoaction) {
        InstallLoaction = installLoaction;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

    public String getManufacturer() {
        return Manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        Manufacturer = manufacturer;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getGetMode() {
        return GetMode;
    }

    public void setGetMode(String getMode) {
        GetMode = getMode;
    }

    public String getKeeperName() {
        return KeeperName;
    }

    public void setKeeperName(String keeperName) {
        KeeperName = keeperName;
    }

    public String getOriginalValue() {
        return OriginalValue;
    }

    public void setOriginalValue(String originalValue) {
        OriginalValue = originalValue;
    }

    public int getDepreciationYears() {
        return DepreciationYears;
    }

    public void setDepreciationYears(int depreciationYears) {
        DepreciationYears = depreciationYears;
    }

    public String getDepreciationMethod() {
        return DepreciationMethod;
    }

    public void setDepreciationMethod(String depreciationMethod) {
        DepreciationMethod = depreciationMethod;
    }

    public String getNetResidualRate() {
        return NetResidualRate;
    }

    public void setNetResidualRate(String netResidualRate) {
        NetResidualRate = netResidualRate;
    }

    public String getMakeUser() {
        return MakeUser;
    }

    public void setMakeUser(String makeUser) {
        MakeUser = makeUser;
    }

    public boolean ISACTIVED() {
        return ISACTIVED;
    }

    public void setISACTIVED(boolean ISACTIVED) {
        this.ISACTIVED = ISACTIVED;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getAssetTypeID() {
        return AssetTypeID;
    }

    public void setAssetTypeID(String assetTypeID) {
        AssetTypeID = assetTypeID;
    }

    public String getEquipImg() {
        return EquipImg;
    }

    public void setEquipImg(String equipImg) {
        EquipImg = equipImg;
    }

    @Override
    public String toString() {
        return "EquipmentEntity{" +
                "ID=" + ID +
                ", CorporationID=" + CorporationID +
                ", CorporationName='" + CorporationName + '\'' +
                ", DepartmentID=" + DepartmentID +
                ", DepartmentName='" + DepartmentName + '\'' +
                ", EquipmentTypeID=" + EquipmentTypeID +
                ", EquipmentTypeName='" + EquipmentTypeName + '\'' +
                ", AssetCode='" + AssetCode + '\'' +
                ", EquipmentCode='" + EquipmentCode + '\'' +
                ", EquipmentName='" + EquipmentName + '\'' +
                ", EquipmentClass='" + EquipmentClass + '\'' +
                ", EquipmentStatus='" + EquipmentStatus + '\'' +
                ", InstallLoaction='" + InstallLoaction + '\'' +
                ", Standard='" + Standard + '\'' +
                ", Manufacturer='" + Manufacturer + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", GetMode='" + GetMode + '\'' +
                ", KeeperName='" + KeeperName + '\'' +
                ", OriginalValue='" + OriginalValue + '\'' +
                ", DepreciationYears=" + DepreciationYears +
                ", DepreciationMethod='" + DepreciationMethod + '\'' +
                ", NetResidualRate='" + NetResidualRate + '\'' +
                ", MakeUser='" + MakeUser + '\'' +
                ", ISACTIVED=" + ISACTIVED +
                ", REMARK='" + REMARK + '\'' +
                ", AssetTypeID=" + AssetTypeID +
                ", EquipImg='" + EquipImg + '\'' +
                '}';
    }
}
