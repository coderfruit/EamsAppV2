package com.grandhyatt.snowbeer.entity;

/**
 * Created by ycm on 2018/9/20.
 */

public class DepartmentEntity {

    private String ID;
    private String DepartmentName ;
    private String DepartmentCode ;

    public DepartmentEntity() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDepartmentName() {
        return DepartmentName;
    }

    public void setDepartmentName(String departmentName) {
        DepartmentName = departmentName;
    }

    public String getDepartmentCode() {
        return DepartmentCode;
    }

    public void setDepartmentCode(String departmentCode) {
        DepartmentCode = departmentCode;
    }
}
