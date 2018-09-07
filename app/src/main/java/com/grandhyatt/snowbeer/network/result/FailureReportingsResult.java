package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;

import java.util.List;

/**
 * Created by ycm on 2018/8/28.
 */

public class FailureReportingsResult  extends Result {
    private List<FailureReportingEntity> data;

    public List<FailureReportingEntity> getData() { return data; }

    public void setData(List<FailureReportingEntity> data) {
        this.data = data;
    }
}
