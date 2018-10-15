package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentMaterialEntity;

import java.util.List;


/**
 * Created by ycm on 2018/8/18.
 */

public class EquipmentMaterialResult extends Result {
    private List<EquipmentMaterialEntity> data;

    public List<EquipmentMaterialEntity> getData() {
        return data;
    }

    public void setData(List<EquipmentMaterialEntity> data) {
        this.data = data;
    }

}
