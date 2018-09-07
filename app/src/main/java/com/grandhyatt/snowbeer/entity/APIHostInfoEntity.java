package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * 用户信息
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018-07-01 11:46
 */
@Entity
public class APIHostInfoEntity implements Serializable {
    @Id
    private long id;
    @Index
    private String host_url;

    private String port;


    /**
     * 获取 id
     *
     * @return long
     */
    public long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @param id
     * @return void
     */
    public void setId(long id) {
        this.id = id;
    }


    /**
     * 获取 host_url
     *
     * @return java.lang.String
     */
    public String getHost_url() {
        return host_url;
    }

    /**
     * 设置 host_url
     *
     * @param host_url
     * @return void
     */
    public void setHost_url(String host_url) {
        this.host_url = host_url;
    }

    /**
     * 获取 port
     *
     * @return int
     */
    public String getPort() {
        return port;
    }

    /**
     * 设置 port
     *
     * @param port
     * @return void
     */
    public void setPort(String port) {
        this.port = port;
    }


    @Override
    public String toString() {
        return "APIHostInfoEntity{" +
                "host_url='" + host_url + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}
