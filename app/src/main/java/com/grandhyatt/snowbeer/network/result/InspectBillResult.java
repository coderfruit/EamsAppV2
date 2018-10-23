package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.InspectBillEntity;
import com.grandhyatt.snowbeer.entity.InspectionPlanEntity;

import java.util.List;

/**
 * 设备检验记录
 * Created by ycm on 2018/9/28.
 */

public class InspectBillResult extends Result {

    private InspectBillEntity data;

    public InspectBillEntity getData() {
        return data;
    }

    public void setData(InspectBillEntity data) {
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
