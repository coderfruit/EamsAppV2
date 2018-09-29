package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;
import com.grandhyatt.snowbeer.entity.MaintenancePlanEntity;

import java.util.List;

/**
 * 设备检验计划
 * Created by ycm on 2018/9/28.
 */

public class InspectPlanResult extends Result {

    private List<InspectionPlanEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<InspectionPlanEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<InspectionPlanEntity> data) {
        this.data = data;
    }

}
