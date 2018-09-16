package com.grandhyatt.snowbeer.network.result;


import com.grandhyatt.commonlib.Result;
import com.grandhyatt.snowbeer.entity.SecondChkListEntity;
import com.grandhyatt.snowbeer.entity.StoreHouseEntity;

import java.util.List;

/**
 *
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 11:18
 */
public class GetSecondChkListResult extends Result {

    public List<SecondChkListEntity> data;

    public String billNo;

    public int money;
}
