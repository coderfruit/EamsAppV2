package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentUseSpareEntity;

import java.util.List;


/**
 * Created by ycm on 2018/8/18.
 */

public class EquipmentUseSpareResult extends Result {

    private List<EquipmentUseSpareEntity> data;

    public List<EquipmentUseSpareEntity> getData() {
        return data;
    }

    public void setData(List<EquipmentUseSpareEntity> data) {
        this.data = data;
    }

}
