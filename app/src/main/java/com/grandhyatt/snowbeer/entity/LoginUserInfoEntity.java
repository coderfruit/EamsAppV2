package com.grandhyatt.snowbeer.entity;


import java.io.Serializable;
import java.util.List;

/**
 * 登录用户信息
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/23 14:21
 */
public class LoginUserInfoEntity implements Serializable {

    private int ID;
    private String UserCode;
    private String UserName;
    private String Password;
    private String Phone;
    private Boolean IsActived;
    private Boolean IsAdmin;

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {

        Phone = phone;
    }

    public List<ResourceEntity> Resources;

    public List<CorporationEntity> Corporations;

    public LoginUserInfoEntity() {
    }

    public LoginUserInfoEntity(int uid, String userCode, String userName,String Password,Boolean IsActived,Boolean IsAdmin,List<ResourceEntity> Resources,List<CorporationEntity> Corporations) {
        this.ID = uid;
        this.UserCode = userCode;
        this.UserName = userName;
        this.Password = Password;
        this.IsActived = IsActived;
        this.IsAdmin = IsAdmin;
        this.Resources = Resources;
        this.Corporations = Corporations;

    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getUserCode() {
        return UserCode;
    }

    public void setUserCode(String userName) {
        this.UserCode = userName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        this.Password = password;
    }

    public Boolean getIsActived() {
        return IsActived;
    }

    public void setIsActived(Boolean isActived) {
        this.IsActived = isActived;
    }

    public Boolean getIsAdmin() {
        return IsAdmin;
    }

    public void setIsAdmin(Boolean isAdmin) {
        this.IsAdmin = isAdmin;
    }

    public List<ResourceEntity> getResources() {
        return Resources;
    }

    public void setResources(List<ResourceEntity> Resources) {
        this.Resources = Resources;
    }

    public List<CorporationEntity> getCorporations() {
        return Corporations;
    }

    public void setCorporations(List<CorporationEntity> Corporations) {
        this.Corporations = Corporations;
    }

    @Override
    public String toString() {
        return "LoginUserInfoEntity{" +
                "ID=" + ID +
                ", UserCode='" + UserCode + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Password='" + Password + '\'' +
                ", Phone='" + Phone + '\'' +
                ", IsActived=" + IsActived +
                ", IsAdmin=" + IsAdmin +
                ", Resources=" + Resources +
                ", Corporations=" + Corporations +
                '}';
    }
}
