package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.FailureReportingEntity;

/**
 * Created by ycm on 2018/8/30.
 */

public class StringResult  extends Result {
    public String data;
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
