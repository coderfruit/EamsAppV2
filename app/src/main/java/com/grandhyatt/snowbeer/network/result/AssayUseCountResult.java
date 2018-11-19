package com.grandhyatt.snowbeer.network.result;

import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.AssayUseCountEntity;
import com.grandhyatt.snowbeer.entity.EquipmentEntity;


/**
 * 化学仪器使用次数
 * Created by ycm on 2018/8/18.
 */

public class AssayUseCountResult extends Result {
    private AssayUseCountEntity data;

    public AssayUseCountEntity getData() {
        return data;
    }

    public void setData(AssayUseCountEntity data) {
        this.data = data;
    }

}
