package com.grandhyatt.snowbeer.entity;

/**
 * 化学仪器使用次数
 * Created by ycm on 2018/11/19.
 */

public class AssayUseCountEntity {

    /**
     * 化学仪器设备ID
     */
    private String AssayEquipID;
    /**
     * 当月使用次数
     */
    private String CurrentMonthCount;
    /**
     * 当周使用次数
     */
    private String CurrentWeekCount;
    /**
     * 当天使用次数
     */
    private String CurrentDayCount;

    public AssayUseCountEntity() {
    }

    public String getAssayEquipID() {
        return AssayEquipID;
    }

    public void setAssayEquipID(String assayEquipID) {
        AssayEquipID = assayEquipID;
    }

    public String getCurrentMonthCount() {
        return CurrentMonthCount;
    }

    public void setCurrentMonthCount(String currentMonthCount) {
        CurrentMonthCount = currentMonthCount;
    }

    public String getCurrentWeekCount() {
        return CurrentWeekCount;
    }

    public void setCurrentWeekCount(String currentWeekCount) {
        CurrentWeekCount = currentWeekCount;
    }

    public String getCurrentDayCount() {
        return CurrentDayCount;
    }

    public void setCurrentDayCount(String currentDayCount) {
        CurrentDayCount = currentDayCount;
    }
}
