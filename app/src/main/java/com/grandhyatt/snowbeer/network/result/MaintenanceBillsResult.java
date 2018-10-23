package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.MaintenanceBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;

import java.util.List;

/**
 * 保养录主表
 * Created by ycm on 2018/10/23.
 */

public class MaintenanceBillsResult extends Result {
    List<MaintenanceBillEntity> data;

    public List<MaintenanceBillEntity> getData() {
        return data;
    }

    public void setData(List<MaintenanceBillEntity> data) {
        this.data = data;
    }
}
