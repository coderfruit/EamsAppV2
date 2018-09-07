package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;


/**
 * Created by ycm on 2018/8/18.
 */

public class EquipmentResult extends Result {
    private EquipmentEntity data;

    public EquipmentEntity getData() {
        return data;
    }

    public void setData(EquipmentEntity data) {
        this.data = data;
    }

}
