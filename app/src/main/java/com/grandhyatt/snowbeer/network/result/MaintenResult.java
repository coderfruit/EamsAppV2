package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.MaintenanceEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;

import java.util.List;

/**
 * 设备保养计划
 * Created by ycm on 2018/9/28.
 */

public class MaintenResult extends Result {

    private MaintenanceEntity data;
    private List<MaintenanceItemEntity> dateItemList;

    /**
     * 获取 dateItemList
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<MaintenanceItemEntity> getdateItemList() {
        return dateItemList;
    }

    /**
     * 设置 data
     *
     * @param value
     * @return void
     */
    public void setdateItemList(List<MaintenanceItemEntity> value) {
        this.dateItemList = value;
    }

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public MaintenanceEntity getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(MaintenanceEntity data) {
        this.data = data;
    }

}
