package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillItemEntity;

import java.util.List;

/**
 * 维修记录细表
 * Created by ycm on 2018/10/23.
 */

public class RepairmentBillItemResult extends Result {
    List<RepairmentBillItemEntity> data;

    public List<RepairmentBillItemEntity> getData() {
        return data;
    }

    public void setData(List<RepairmentBillItemEntity> data) {
        this.data = data;
    }
}
