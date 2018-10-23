package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;

import java.util.List;

/**
 * Created by tongzhiqiang on 2018-09-19.
 */

public class RepairmentEquipmentResult extends Result {
    private EquipmentEntity Equipmentdata;

    private List<EquipmentUseSpareEntity> SpareInEquipmentdata;

    public List<SpareInEquipmentEntity> getSpareE() {
        return SpareE;
    }

    public void setSpareE(List<SpareInEquipmentEntity> spareE) {
        SpareE = spareE;
    }

    private List<SpareInEquipmentEntity> SpareE;
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
    public void setRepairmentBillData(RepairmentBillEntity data) {
        this.rbdata = data;
    }
    public EquipmentEntity getData() {
        return Equipmentdata;
    }

    public void setData(EquipmentEntity data) {
        this.Equipmentdata = data;
    }




    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<EquipmentUseSpareEntity> getSpareInEquipmentData() {
        return SpareInEquipmentdata;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setSpareInEquipmentData(List<EquipmentUseSpareEntity> data) {
        this.SpareInEquipmentdata = data;
    }
}
