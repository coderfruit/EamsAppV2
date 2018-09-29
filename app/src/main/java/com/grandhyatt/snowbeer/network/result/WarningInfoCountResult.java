package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.WarningInfoCountEntity;

/**
 * Created by ycm on 2018/9/28.
 */

public class WarningInfoCountResult extends Result {

    private WarningInfoCountEntity data;

    public WarningInfoCountEntity getData() {
        return data;
    }

    public void setData(WarningInfoCountEntity data) {
        this.data = data;
    }
}
