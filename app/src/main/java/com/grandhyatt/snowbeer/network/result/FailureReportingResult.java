package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;


/**
 * Created by ycm on 2018/8/18.
 */

public class FailureReportingResult extends Result {
    private FailureReportingEntity data;

    public FailureReportingEntity getData() {
        return data;
    }

    public void setData(FailureReportingEntity data) {
        this.data = data;
    }

}
