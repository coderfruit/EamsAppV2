package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * 设备保养计划
 * Created by ycm on 2018/9/28.
 */

public class MaintenPlanResult extends Result {

    private List<MaintenancePlanEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<MaintenancePlanEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<MaintenancePlanEntity> data) {
        this.data = data;
    }

}
