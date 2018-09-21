package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.DepartmentEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;

import java.util.List;


/**
 * Created by ycm on 2018/8/18.
 */

public class DepartmentResult extends Result {

    private List<DepartmentEntity> data;

    public List<DepartmentEntity> getData() {
        return data;
    }

    public void setData(List<DepartmentEntity> data) {
        this.data = data;
    }

}
