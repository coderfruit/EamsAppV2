package com.grandhyatt.snowbeer.entity;

import java.util.List;

/**
 * Created by ycm on 2018/8/27.
 */

public class FailureReportingEntity {
    private long ID ;

    private String ReportNO ;

    private long EquipmentID ;

    private String EquipmentCode ;

    private String EquipmentName ;

    private String EquipmentTypeName ;

    private String EquipmentTypeLevelCode ;

    private String EquipUseStatus ;

    private String ReportDate ;

    private String FailureLevel ;

    private String FailureDesc ;

    private String MakeDate ;

    private String MakeUser ;

    private String Remark ;

    private String Status ;

    private String OperateResult ;

    private String OperateID ;

    private String OperateDesc ;

    private long ReportUserID ;
    private String LinkUser;
    private String LinkMobile;
    private String OperateUser;
    private String OperateDate;


    private List<FailureReportingAttachmentEntity> FailureReportingAttachmentModelList;

    public FailureReportingEntity() {
    }

    public String getOperateUser() {
        return OperateUser;
    }

    public void setOperateUser(String operateUser) {
        OperateUser = operateUser;
    }

    public String getOperateDate() {
        return OperateDate;
    }

    public void setOperateDate(String operateDate) {
        OperateDate = operateDate;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getReportNO() {
        return ReportNO;
    }

    public void setReportNO(String reportNO) {
        ReportNO = reportNO;
    }

    public long getEquipmentID() {
        return EquipmentID;
    }

    public void setEquipmentID(long equipmentID) {
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

    public String getEquipmentTypeName() {
        return EquipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        EquipmentTypeName = equipmentTypeName;
    }

    public String getEquipmentTypeLevelCode() {
        return EquipmentTypeLevelCode;
    }

    public void setEquipmentTypeLevelCode(String equipmentTypeLevelCode) {
        EquipmentTypeLevelCode = equipmentTypeLevelCode;
    }

    public String getEquipUseStatus() {
        return EquipUseStatus;
    }

    public void setEquipUseStatus(String equipUseStatus) {
        EquipUseStatus = equipUseStatus;
    }

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }

    public String getFailureLevel() {
        return FailureLevel;
    }

    public void setFailureLevel(String failureLevel) {
        FailureLevel = failureLevel;
    }

    public String getFailureDesc() {
        return FailureDesc;
    }

    public void setFailureDesc(String failureDesc) {
        FailureDesc = failureDesc;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getOperateResult() {
        return OperateResult;
    }

    public void setOperateResult(String operateResult) {
        OperateResult = operateResult;
    }

    public String getOperateID() {
        return OperateID;
    }

    public void setOperateID(String operateID) {
        OperateID = operateID;
    }

    public String getOperateDesc() {
        return OperateDesc;
    }

    public void setOperateDesc(String operateDesc) {
        OperateDesc = operateDesc;
    }

    public long getReportUserID() {
        return ReportUserID;
    }

    public void setReportUserID(long reportUserID) {
        ReportUserID = reportUserID;
    }

    public String getLinkUser() {
        return LinkUser;
    }

    public void setLinkUser(String linkUser) {
        LinkUser = linkUser;
    }

    public String getLinkMobile() {
        return LinkMobile;
    }

    public void setLinkMobile(String linkMobile) {
        LinkMobile = linkMobile;
    }

    public List<FailureReportingAttachmentEntity> getFailureReportingAttachmentModelList() {
        return FailureReportingAttachmentModelList;
    }

    public void setFailureReportingAttachmentModelList(List<FailureReportingAttachmentEntity> failureReportingAttachmentModelList) {
        FailureReportingAttachmentModelList = failureReportingAttachmentModelList;
    }
}
