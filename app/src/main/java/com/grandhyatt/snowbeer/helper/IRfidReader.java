package com.grandhyatt.snowbeer.helper;

/**
 * Created by wulifu on 2018/7/19.
 */

public interface IRfidReader {

    int initDevice();

    void closeDevice();

    String readRfidCardData();

}
