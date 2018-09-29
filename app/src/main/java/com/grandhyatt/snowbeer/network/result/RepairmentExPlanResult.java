package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.RepairmentExPlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * 设备外委维修计划
 * Created by ycm on 2018/9/29.
 */

public class RepairmentExPlanResult extends Result {

    private List<RepairmentExPlanEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<RepairmentExPlanEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<RepairmentExPlanEntity> data) {
        this.data = data;
    }

}
