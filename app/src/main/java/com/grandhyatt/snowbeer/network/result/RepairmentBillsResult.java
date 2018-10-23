package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;

import java.util.List;

/**
 * 维修记录主表
 * Created by ycm on 2018/10/23.
 */

public class RepairmentBillsResult  extends Result {
    List<RepairmentBillEntity> data;

    public List<RepairmentBillEntity> getData() {
        return data;
    }

    public void setData(List<RepairmentBillEntity> data) {
        this.data = data;
    }
}
