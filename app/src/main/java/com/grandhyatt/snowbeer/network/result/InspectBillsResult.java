package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;

import java.util.List;

/**
 * 检验记录主表
 * Created by ycm on 2018/10/23.
 */

public class InspectBillsResult extends Result {
    List<InspectBillEntity> data;

    public List<InspectBillEntity> getData() {
        return data;
    }

    public void setData(List<InspectBillEntity> data) {
        this.data = data;
    }
}
