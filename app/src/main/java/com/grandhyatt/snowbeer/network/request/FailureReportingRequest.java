package com.grandhyatt.snowbeer.network.request;

import java.util.List;

/**
 * Created by ycm on 2018/8/18.
 */

public class FailureReportingRequest {
    private String ID;                  //报修ID
    private String CurrentLastIdx;      //当前行号，分页用
    private String EquipmentID;         //设备ID
    private String ReportDate;
    private String ReportDateFrom;         //发生时间
    private String ReportDateTo;         //发生时间
    private String FailureLevel;       //故障等级(一般、严重)
    private String FailureDesc;        //故障描述
    private String LinkUser;           //联系人
    private String LinkMobile;         //联系人电话
    private List<String> Base64Imgs;   //故障图片
    private String Base64Voice;        //语音
    private String Status;
    private String OperateResult;
    private String ReportUser;
    private String CorpID;      //报修信息所属工厂
    private String OperateUser;

    public FailureReportingRequest() {
    }


    public String getCorpID() {
        return CorpID;
    }

    public void setCorpID(String corpID) {
        this.CorpID = corpID;
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

    public String getReportDateFrom() {
        return ReportDateFrom;
    }

    public void setReportDateFrom(String reportDateFrom) {
        ReportDateFrom = reportDateFrom;
    }

    public String getReportDateTo() {
        return ReportDateTo;
    }

    public void setReportDateTo(String reportDateTo) {
        ReportDateTo = reportDateTo;
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

    public List<String> getBase64Imgs() {
        return Base64Imgs;
    }

    public void setBase64Imgs(List<String> base64Imgs) {
        Base64Imgs = base64Imgs;
    }

    public String getBase64Voice() {
        return Base64Voice;
    }

    public void setBase64Voice(String base64Voice) {
        Base64Voice = base64Voice;
    }

    public String getCurrentLastIdx() {
        return CurrentLastIdx;
    }

    public void setCurrentLastIdx(String currentLastIdx) {
        this.CurrentLastIdx = currentLastIdx;
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

    public String getReportUser() {
        return ReportUser;
    }

    public void setReportUser(String reportUser) {
        ReportUser = reportUser;
    }

    public String getReportDate() {
        return ReportDate;
    }

    public void setReportDate(String reportDate) {
        ReportDate = reportDate;
    }

    public String getOperateUser() {
        return OperateUser;
    }

    public void setOperateUser(String operateUser) {
        OperateUser = operateUser;
    }

    @Override
    public String toString() {
        return "FailureReportingRequest{" +
                "ID='" + ID + '\'' +
                ", CurrentLastIdx='" + CurrentLastIdx + '\'' +
                ", EquipmentID='" + EquipmentID + '\'' +
                ", ReportDate='" + ReportDate + '\'' +
                ", ReportDateFrom='" + ReportDateFrom + '\'' +
                ", ReportDateTo='" + ReportDateTo + '\'' +
                ", FailureLevel='" + FailureLevel + '\'' +
                ", FailureDesc='" + FailureDesc + '\'' +
                ", LinkUser='" + LinkUser + '\'' +
                ", LinkMobile='" + LinkMobile + '\'' +
                ", Base64Imgs=" + Base64Imgs +
                ", Base64Voice='" + Base64Voice + '\'' +
                ", Status='" + Status + '\'' +
                ", OperateResult='" + OperateResult + '\'' +
                ", ReportUser='" + ReportUser + '\'' +
                '}';
    }
}
