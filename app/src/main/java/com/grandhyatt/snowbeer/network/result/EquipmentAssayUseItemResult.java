package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentAssayUseItemEntity;

import java.util.List;

/**
 * 化学仪器使用记录
 * Created by  yangcm on 2018-10-26.
 */

public class EquipmentAssayUseItemResult extends Result {

    private List<EquipmentAssayUseItemEntity> data;

    /**
     * 获取 data
     *
     * @return
     */
    public List<EquipmentAssayUseItemEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<EquipmentAssayUseItemEntity> data) {
        this.data = data;
    }
}
