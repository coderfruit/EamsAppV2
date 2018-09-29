package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.SpareInEquipmentEntity;

import java.util.List;

/**
 * 备件与设备关系
 * Created by ycm on 2018/9/17.
 */

public class SpareInEquipmentResult  extends Result {

    private List<SpareInEquipmentEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<SpareInEquipmentEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<SpareInEquipmentEntity> data) {
        this.data = data;
    }
}
