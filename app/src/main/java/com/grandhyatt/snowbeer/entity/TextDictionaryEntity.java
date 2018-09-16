package com.grandhyatt.snowbeer.entity;

/**
 * Created by ycm on 2018/8/16.
 */

public class TextDictionaryEntity {
    private long ID;

    private String Key;

    private String Text;

    private String Value;

    private String MakeUser;

    private String OccurDate;

    private String REMARK;

    public TextDictionaryEntity() {
    }

    public TextDictionaryEntity(long ID, String key, String text, String value, String makeUser, String occurDate, String REMARK) {
        this.ID = ID;
        Key = key;
        Text = text;
        Value = value;
        MakeUser = makeUser;
        OccurDate = occurDate;
        this.REMARK = REMARK;
    }

    @Override
    public String toString() {
        return "TextDictionaryEntity{" +
                "ID=" + ID +
                ", Key='" + Key + '\'' +
                ", Text='" + Text + '\'' +
                ", Value='" + Value + '\'' +
                ", MakeUser='" + MakeUser + '\'' +
                ", OccurDate='" + OccurDate + '\'' +
                ", REMARK='" + REMARK + '\'' +
                '}';
    }

    public long getID() {
        return ID;
    }

    public String getKey() {
        return Key;
    }

    public String getText() {
        return Text;
    }

    public String getValue() {
        return Value;
    }

    public String getMakeUser() {
        return MakeUser;
    }

    public String getOccurDate() {
        return OccurDate;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public void setKey(String key) {
        Key = key;
    }

    public void setText(String text) {
        Text = text;
    }

    public void setValue(String value) {
        Value = value;
    }

    public void setMakeUser(String makeUser) {
        MakeUser = makeUser;
    }

    public void setOccurDate(String occurDate) {
        OccurDate = occurDate;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }
}
