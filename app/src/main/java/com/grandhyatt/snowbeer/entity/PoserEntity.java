package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * 权限信息
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 14:23
 */
public class PoserEntity implements Serializable {


    private String powerId;

    private String powerName;

    private int powerValue;

    public PoserEntity() {

    }

    public PoserEntity(String powerId, String powerName, int powerValue) {
        this.powerId = powerId;
        this.powerName = powerName;
        this.powerValue = powerValue;
    }

    /**
     * 获取 powerId
     *
     * @return 返回 powerId
     */
    public String getPowerId() {
        return powerId;
    }

    /**
     * 设置 powerId
     *
     * @return 返回 powerId
     */
    public void setPowerId(String powerId) {
        this.powerId = powerId;
    }

    /**
     * 获取 powerName
     *
     * @return 返回 powerName
     */
    public String getPowerName() {
        return powerName;
    }

    /**
     * 设置 powerName
     *
     * @return 返回 powerName
     */
    public void setPowerName(String powerName) {
        this.powerName = powerName;
    }

    /**
     * 获取 powerValue
     *
     * @return 返回 powerValue
     */
    public int getPowerValue() {
        return powerValue;
    }

    /**
     * 设置 powerValue
     *
     * @return 返回 powerValue
     */
    public void setPowerValue(int powerValue) {
        this.powerValue = powerValue;
    }

    @Override
    public String toString() {
        return "PoserEntity{" +
                "powerId='" + powerId + '\'' +
                ", powerName='" + powerName + '\'' +
                ", powerValue=" + powerValue +
                '}';
    }
}
