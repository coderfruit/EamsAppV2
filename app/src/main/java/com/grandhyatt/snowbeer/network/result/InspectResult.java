package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;
import com.grandhyatt.snowbeer.entity.InspectEntity;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceEntity;
import com.grandhyatt.snowbeer.entity.MaintenanceItemEntity;

import java.util.List;

/**
 * 设备保养计划
 * Created by ycm on 2018/9/28.
 */

public class InspectResult extends Result {

    private InspectEntity data;

    public InspectEntity getData() {
        return data;
    }

    public void setData(InspectEntity data) {
        this.data = data;
    }

    private List<InspectionPlanEntity> listInspPlan;

    public List<InspectionPlanEntity> getlistInspPlan() {
        return listInspPlan;
    }

    public void setlistInspPlan(List<InspectionPlanEntity> value) {
        this.listInspPlan = value;
    }

}
