package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.RepairmentBillEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * Created by tongzhiqiang on 2018-09-19.
 */

public class RepairmentBillResult  extends Result {

    private List<RepairmentBillEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<RepairmentBillEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<RepairmentBillEntity> data) {
        this.data = data;
    }
}
