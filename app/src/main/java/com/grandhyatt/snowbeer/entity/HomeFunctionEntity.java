package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * 主页GridView数据Bean
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-07 19:59
 */
public class HomeFunctionEntity implements Serializable {

    /** 图标 */
    private int image;

    /** 名称 */
    private String name;

    /**
     * 获取 image
     *
     * @return int
     */
    public int getImage() {
        return image;
    }

    /**
     * 设置 image
     *
     * @param image
     * @return void
     */
    public void setImage(int image) {
        this.image = image;
    }

    /**
     * 获取 name
     *
     * @return java.lang.String
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 name
     *
     * @param name
     * @return void
     */
    public void setName(String name) {
        this.name = name;
    }
}
