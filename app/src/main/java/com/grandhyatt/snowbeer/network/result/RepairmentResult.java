package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * Created by tongzhiqiang on 2018-10-15.
 */

public class RepairmentResult extends Result {
    private RepairmentBillEntity data;
    private  List<EquipmentUseSpareEntity> itemdate;
    private  List<RepairmentPlanEntity> plandate;
    /**
     * 获取 dateItemList
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<EquipmentUseSpareEntity> getitemdate() {
        return itemdate;
    }

    /**
     * 设置 data
     *
     * @param value
     * @return void
     */
    public void setitemdate(List<EquipmentUseSpareEntity> value) {
        this.itemdate = value;
    }

    /**
     * 获取 dateItemList
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<RepairmentPlanEntity> getplandate() {
        return plandate;
    }

    /**
     * 设置 data
     *
     * @param value
     * @return void
     */
    public void setplandate(List<RepairmentPlanEntity> value) {
        this.plandate = value;
    }

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public RepairmentBillEntity getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(RepairmentBillEntity data) {
        this.data = data;
    }
}
