package com.grandhyatt.snowbeer.entity;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by wulifu on 2018/7/19.
 */
@Entity
public class OrganizationEntity{


    @Id
    private long id;

    @Index
    private int org_id;
    private String org_name;
    private long createTime;

    public OrganizationEntity() {

    }

    public OrganizationEntity(long id, int org_id, String org_name, long createTime) {
        this.id = id;
        this.org_id = org_id;
        this.org_name = org_name;
        this.createTime = createTime;
    }


    /**
     * 获取 id
     *
     * @return 返回 id
     */
    public long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @return 返回 id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * 获取 org_id
     *
     * @return 返回 org_id
     */
    public int getOrg_id() {
        return org_id;
    }

    /**
     * 设置 org_id
     *
     * @return 返回 org_id
     */
    public void setOrg_id(int org_id) {
        this.org_id = org_id;
    }

    /**
     * 获取 org_name
     *
     * @return 返回 org_name
     */
    public String getOrg_name() {
        return org_name;
    }

    /**
     * 设置 org_name
     *
     * @return 返回 org_name
     */
    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    /**
     * 获取 createTime
     *
     * @return 返回 createTime
     */
    public long getCreateTime() {
        return createTime;
    }

    /**
     * 设置 createTime
     *
     * @return 返回 createTime
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "OrganizationEntity{" +
                "id=" + id +
                ", org_id=" + org_id +
                ", org_name='" + org_name + '\'' +
                ", createTime=" + createTime +
                '}';
    }
}
