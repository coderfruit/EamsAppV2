package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.RepairmentPlanEntity;

import java.util.List;

/**
 * Created by ycm on 2018/9/17.
 */

public class RepairmentPlanResult  extends Result {

    private List<RepairmentPlanEntity> data;

    /**
     * 获取 data
     *
     * @return com.bose.rose.model.LoginUserInfo
     */
    public List<RepairmentPlanEntity> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @param data
     * @return void
     */
    public void setData(List<RepairmentPlanEntity> data) {
        this.data = data;
    }

}
