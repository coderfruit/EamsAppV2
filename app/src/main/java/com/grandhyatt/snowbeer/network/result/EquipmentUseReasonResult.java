package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentUseReasonEntity;

import java.util.List;

/**
 * 化学仪器使用事由
 * Created by ycm on 2018/10/8.
 */

public class EquipmentUseReasonResult  extends Result {
    private List<EquipmentUseReasonEntity> data;

    public List<EquipmentUseReasonEntity> getData() {
        return data;
    }

    public void setData(List<EquipmentUseReasonEntity> data) {
        this.data = data;
    }
}
