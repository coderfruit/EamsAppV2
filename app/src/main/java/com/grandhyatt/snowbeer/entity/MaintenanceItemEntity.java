package com.grandhyatt.snowbeer.entity;

/**
 * Created by tongzhiqiang on 2018-10-08.
 */

public class MaintenanceItemEntity {
 private String ID;
 private String MaintenanceBillID ;
 private String MaterialID;
 private String UseCount;
 private String UseUnit;
 private String UsePrice;
 private String MakeUser;
 private String MakeDate;
 private String TotalMoney;
 private String Remark;
 private String MaterialName;
 private String MaterialCode;
 private String Standard;
 public MaintenanceItemEntity() {
 }
 public void setMaterialName(String value) {
  MaterialName = value;
 }
 public String getMaterialName() {
  return MaterialName;
 }
 public void setMaterialCode(String value) {
  MaterialCode = value;
 }
 public String getMaterialCode() {
  return MaterialCode;
 }
 public void setStandard(String value) {
  Standard = value;
 }
 public String getStandard() {
  return Standard;
 }
 public void setID(String value) {
  ID = value;
 }
 public String getID() {
  return ID;
 }
 public void setMaintenanceBillID(String value) {
  MaintenanceBillID = value;
 }
 public String getMaintenanceBillID() {
  return MaintenanceBillID;
 }
 public void setMaterialID(String value) {
  MaterialID = value;
 }
 public String getMaterialID() {
  return MaterialID;
 }
 public void setUseCount(String value) {
  UseCount = value;
 }
 public String getUseCount() {
  return UseCount;
 }
 public void setUseUnit(String value) {
  UseUnit = value;
 }
 public String getUseUnit() {
  return UseUnit;
 }
 public void setUsePrice(String value) {
  UsePrice = value;
 }
 public String getUsePrice() {
  return UsePrice;
 }
 public void setMakeUser(String value) {
  MakeUser = value;
 }
 public String getMakeUser() {
  return MakeUser;
 }
 public void setMakeDate(String value) {
  MakeDate = value;
 }
 public String getMakeDate() {
  return MakeDate;
 }
 public void setTotalMoney(String value) {
  TotalMoney = value;
 }
 public String getTotalMoney() {
  return TotalMoney;
 }
 public void setRemark(String value) {
  Remark = value;
 }
 public String getRemark() {
  return Remark;
 }


}
