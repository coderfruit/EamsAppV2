package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;

import java.util.List;


/**
 * 设备列表
 * Created by ycm on 2018/8/18.
 */

public class EquipmentsResult extends Result {
    private List<EquipmentEntity> data;

    public List<EquipmentEntity> getData() {
        return data;
    }

    public void setData( List<EquipmentEntity> data) {
        this.data = data;
    }

}
