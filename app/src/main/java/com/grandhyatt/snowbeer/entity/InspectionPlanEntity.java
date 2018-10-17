package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * Created by ycm on 2018/9/28.
 */

public class InspectionPlanEntity implements Serializable {

        private Boolean IsCheck;
        private String Status;

        private String ID;

        private String CorporationID;

        private String CorporationName;

        private String EquipmentID;

        private String EquipmentCode;

        private String EquipmentName;

        private String DeptCode;

        private String DeptName;

        private String InspectionMode;

        private String InspectionItem;

        private String Interval;

        private String IntervalUnit;

        private String LastRunningDate;

        private String NextRunningDate;

        private String WarningDays;

        private String Description;

        private String MakeUser;

        private String MakeDate;

        private String Remark;

        public InspectionPlanEntity() {
        }
        public Boolean getIsCheck() {
                return IsCheck;
        }

        public void setIsCheck(Boolean value) {
                IsCheck = value;
        }
        public String getStatus() {
                return Status;
        }

        public void setStatus(String status) {
                Status = status;
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

        public void setCorporationID(String corporationID) {
                CorporationID = corporationID;
        }

        public String getCorporationName() {
                return CorporationName;
        }

        public void setCorporationName(String corporationName) {
                CorporationName = corporationName;
        }

        public String getEquipmentID() {
                return EquipmentID;
        }

        public void setEquipmentID(String equipmentID) {
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

        public String getDeptCode() {
                return DeptCode;
        }

        public void setDeptCode(String deptCode) {
                DeptCode = deptCode;
        }

        public String getDeptName() {
                return DeptName;
        }

        public void setDeptName(String deptName) {
                DeptName = deptName;
        }

        public String getInspectionMode() {
                return InspectionMode;
        }

        public void setInspectionMode(String inspectionMode) {
                InspectionMode = inspectionMode;
        }

        public String getInspectionItem() {
                return InspectionItem;
        }

        public void setInspectionItem(String inspectionItem) {
                InspectionItem = inspectionItem;
        }

        public String getInterval() {
                return Interval;
        }

        public void setInterval(String interval) {
                Interval = interval;
        }

        public String getIntervalUnit() {
                return IntervalUnit;
        }

        public void setIntervalUnit(String intervalUnit) {
                IntervalUnit = intervalUnit;
        }

        public String getLastRunningDate() {
                return LastRunningDate;
        }

        public void setLastRunningDate(String lastRunningDate) {
                LastRunningDate = lastRunningDate;
        }

        public String getNextRunningDate() {
                return NextRunningDate;
        }

        public void setNextRunningDate(String nextRunningDate) {
                NextRunningDate = nextRunningDate;
        }

        public String getWarningDays() {
                return WarningDays;
        }

        public void setWarningDays(String warningDays) {
                WarningDays = warningDays;
        }

        public String getDescription() {
                return Description;
        }

        public void setDescription(String description) {
                Description = description;
        }

        public String getMakeUser() {
                return MakeUser;
        }

        public void setMakeUser(String makeUser) {
                MakeUser = makeUser;
        }

        public String getMakeDate() {
                return MakeDate;
        }

        public void setMakeDate(String makeDate) {
                MakeDate = makeDate;
        }

        public String getRemark() {
                return Remark;
        }

        public void setRemark(String remark) {
                Remark = remark;
        }
}
