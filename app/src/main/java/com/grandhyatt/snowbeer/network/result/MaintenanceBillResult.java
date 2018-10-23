package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.MaintenanceBillEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;

import java.util.List;

/**
 * 设备保养记录
 * Created by ycm on 2018/9/28.
 */

public class MaintenanceBillResult extends Result {

    private MaintenanceBillEntity data;
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
    public MaintenanceBillEntity getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(MaintenanceBillEntity data) {
        this.data = data;
    }

}
