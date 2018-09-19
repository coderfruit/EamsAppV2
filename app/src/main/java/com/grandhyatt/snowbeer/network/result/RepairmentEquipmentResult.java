package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;

import java.util.List;

/**
 * Created by tongzhiqiang on 2018-09-19.
 */

public class RepairmentEquipmentResult extends Result {
    private EquipmentEntity data;
    private List<RepairmentPlanEntity> planData;
    private List<SpareInEquipmentEntity> SpareInEquipmentdata;
    private RepairmentBillEntity rbdata;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public RepairmentBillEntity getRepairmentBillData() {
        return rbdata;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setRepairmentBillData(List<RepairmentBillEntity> data) {
        this.rbdata = rbdata;
    }
    public EquipmentEntity getData() {
        return data;
    }

    public void setData(EquipmentEntity data) {
        this.data = data;
    }



    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<RepairmentPlanEntity> getPlanData() {
        return planData;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setPlanData(List<RepairmentPlanEntity> data) {
        this.planData = planData;
    }



    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<SpareInEquipmentEntity> getSpareInEquipmentData() {
        return SpareInEquipmentdata;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setSpareInEquipmentData(List<SpareInEquipmentEntity> data) {
        this.SpareInEquipmentdata = SpareInEquipmentdata;
    }
}
