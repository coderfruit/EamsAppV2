package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;
import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;

/**
 * Created by wulifu on 2018/7/19.
 */
public class StoreHouseEntity implements Serializable {

    private Long id;

    private Long storeHouseTypeID;

    private Long corporationId;

    private String storeHouseCode;

    private String storeHouseName;

    private String remark;

    private Integer isActived;

    private String createBy;

    private Long createDate;

    public StoreHouseEntity() {
    }

    public StoreHouseEntity(Long id, Long storeHouseTypeID, Long corporationId, String storeHouseCode, String storeHouseName, String remark, Integer isActived, String createBy, Long createDate) {
        this.id = id;
        this.storeHouseTypeID = storeHouseTypeID;
        this.corporationId = corporationId;
        this.storeHouseCode = storeHouseCode;
        this.storeHouseName = storeHouseName;
        this.remark = remark;
        this.isActived = isActived;
        this.createBy = createBy;
        this.createDate = createDate;
    }

    /**
     * 获取 id
     *
     * @return 返回 id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id
     *
     * @return 返回 id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取 storeHouseTypeID
     *
     * @return 返回 storeHouseTypeID
     */
    public Long getStoreHouseTypeID() {
        return storeHouseTypeID;
    }

    /**
     * 设置 storeHouseTypeID
     *
     * @return 返回 storeHouseTypeID
     */
    public void setStoreHouseTypeID(Long storeHouseTypeID) {
        this.storeHouseTypeID = storeHouseTypeID;
    }

    /**
     * 获取 corporationId
     *
     * @return 返回 corporationId
     */
    public Long getCorporationId() {
        return corporationId;
    }

    /**
     * 设置 corporationId
     *
     * @return 返回 corporationId
     */
    public void setCorporationId(Long corporationId) {
        this.corporationId = corporationId;
    }

    /**
     * 获取 storeHouseCode
     *
     * @return 返回 storeHouseCode
     */
    public String getStoreHouseCode() {
        return storeHouseCode;
    }

    /**
     * 设置 storeHouseCode
     *
     * @return 返回 storeHouseCode
     */
    public void setStoreHouseCode(String storeHouseCode) {
        this.storeHouseCode = storeHouseCode;
    }

    /**
     * 获取 storeHouseName
     *
     * @return 返回 storeHouseName
     */
    public String getStoreHouseName() {
        return storeHouseName;
    }

    /**
     * 设置 storeHouseName
     *
     * @return 返回 storeHouseName
     */
    public void setStoreHouseName(String storeHouseName) {
        this.storeHouseName = storeHouseName;
    }

    /**
     * 获取 remark
     *
     * @return 返回 remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置 remark
     *
     * @return 返回 remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取 isActived
     *
     * @return 返回 isActived
     */
    public Integer getIsActived() {
        return isActived;
    }

    /**
     * 设置 isActived
     *
     * @return 返回 isActived
     */
    public void setIsActived(Integer isActived) {
        this.isActived = isActived;
    }

    /**
     * 获取 createBy
     *
     * @return 返回 createBy
     */
    public String getCreateBy() {
        return createBy;
    }

    /**
     * 设置 createBy
     *
     * @return 返回 createBy
     */
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    /**
     * 获取 createDate
     *
     * @return 返回 createDate
     */
    public Long getCreateDate() {
        return createDate;
    }

    /**
     * 设置 createDate
     *
     * @return 返回 createDate
     */
    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    @Override
    public String toString() {
        return "StoreHouseEntity{" +
                "id=" + id +
                ", storeHouseTypeID=" + storeHouseTypeID +
                ", corporationId=" + corporationId +
                ", storeHouseCode='" + storeHouseCode + '\'' +
                ", storeHouseName='" + storeHouseName + '\'' +
                ", remark='" + remark + '\'' +
                ", isActived=" + isActived +
                ", createBy='" + createBy + '\'' +
                ", createDate=" + createDate +
                '}';
    }
}
