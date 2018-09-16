package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * Created by administrator on 2018/8/10.
 */

public class ResourceEntity  implements Serializable {
    private long ID;
    private String AppName;
    private String ResourceCode;
    private String ResourceName;
    private String Remark;

    public long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getAppName() {
        return AppName;
    }

    public void setAppName(String CorporationName) {
        this.AppName = AppName;
    }

    public String getResourceCode() {
        return ResourceCode;
    }

    public void setResourceCode(String ResourceCode) {
        this.ResourceCode = ResourceCode;
    }

    public String getResourceName() {
        return ResourceName;
    }

    public void setResourceName(String ResourceName) {
        this.ResourceName = ResourceName;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    @Override
    public String toString() {
        return "ResourceEntity{" +
                "ID=" + ID +
                ", AppName='" + AppName + '\'' +
                ", ResourceCode='" + ResourceCode + '\'' +
                ", ResourceName='" + ResourceName + '\'' +
                ", Remark='" + Remark + '\'' +
                '}';
    }
}
