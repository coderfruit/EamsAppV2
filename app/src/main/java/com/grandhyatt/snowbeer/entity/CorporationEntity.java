package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;

/**
 * Created by administrator on 2018/8/10.
 */

public class CorporationEntity  implements Serializable {
    private long ID;
    private String CorporationName;
    private String LevelCode;

    public CorporationEntity(long ID,String CorporationName,String LevelCode) {
        this.ID = ID;
        this.CorporationName = CorporationName;
        this.LevelCode = LevelCode;
    }

    public long getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCorporationName() {
        return CorporationName;
    }

    public void setCorporationName(String CorporationName) {
        this.CorporationName = CorporationName;
    }

    public String getLevelCode() {
        return LevelCode;
    }

    public void setLevelCode(String LevelCode) {
        this.LevelCode = LevelCode;
    }

    @Override
    public String toString() {
        return "CorporationEntity{" +
                "ID=" + ID +
                ", CorporationName='" + CorporationName + '\'' +
                ", LevelCode='" + LevelCode + '\'' +
                '}';
    }

}
