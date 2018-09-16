package com.grandhyatt.snowbeer.entity;

import android.graphics.Bitmap;

import java.util.List;

/**
 * Created by ycm on 2018/8/27.
 */

public class FailureReportingAttachmentEntity {
    private long ID;

    private String AttachmentType;

    private String FailureReportingID;

    private String FileGuid;

    private String FileName;

    private String FileType;

    private String MakeDate;

    private String MakeUser;

    private String Remark;

    private String BmpUrl;

    private String VoiceUrl;

    public FailureReportingAttachmentEntity() {
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getAttachmentType() {
        return AttachmentType;
    }

    public void setAttachmentType(String attachmentType) {
        AttachmentType = attachmentType;
    }

    public String getFailureReportingID() {
        return FailureReportingID;
    }

    public void setFailureReportingID(String failureReportingID) {
        FailureReportingID = failureReportingID;
    }

    public String getFileGuid() {
        return FileGuid;
    }

    public void setFileGuid(String fileGuid) {
        FileGuid = fileGuid;
    }

    public String getFileName() {
        return FileName;
    }

    public void setFileName(String fileName) {
        FileName = fileName;
    }

    public String getFileType() {
        return FileType;
    }

    public void setFileType(String fileType) {
        FileType = fileType;
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

    public String getBmpUrl() {
        return BmpUrl;
    }

    public void setBmpUrl(String bmpUrl) {
        BmpUrl = bmpUrl;
    }

    public String getVoiceUrl() {
        return VoiceUrl;
    }

    public void setVoiceUrl(String voiceUrl) {
        VoiceUrl = voiceUrl;
    }
}
