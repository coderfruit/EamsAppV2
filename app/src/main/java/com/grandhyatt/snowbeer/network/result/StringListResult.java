package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;

import java.util.List;

/**
 * Created by ycm on 2018/8/30.
 */

public class StringListResult extends Result {
    public List<String> data;
    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
}
