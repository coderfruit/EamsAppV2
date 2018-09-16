package com.grandhyatt.snowbeer.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 账户结余信息
 *
 * @author wulifu
 * @email wulifu@travelsky.com
 * @mobile 18602438878
 * @create 2018/7/23 19:27
 */
public class AccountBalanceInfoEntity implements Serializable{

    /** 账户类型 */
    private int accountType;

    /** 账户名称 */
    private String accountName;

    private List<AccountBalanceInfo> data;

    /**
     * 获取 accountType
     *
     * @return 返回 accountType
     */
    public int getAccountType() {
        return accountType;
    }

    /**
     * 设置 accountType
     *
     * @return 返回 accountType
     */
    public void setAccountType(int accountType) {
        this.accountType = accountType;
    }

    /**
     * 获取 accountName
     *
     * @return 返回 accountName
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置 accountName
     *
     * @return 返回 accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取 data
     *
     * @return 返回 data
     */
    public List<AccountBalanceInfo> getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @return 返回 data
     */
    public void setData(List<AccountBalanceInfo> data) {
        this.data = data;
    }

    public static class AccountBalanceInfo implements Serializable{

        private int accountType;

        private String packingCode;

        private String packingName;

        private int count;

        public AccountBalanceInfo() {

        }

        public AccountBalanceInfo(int accountType, String packingCode, String packingName, int count) {
            this.accountType = accountType;
            this.packingCode = packingCode;
            this.packingName = packingName;
            this.count = count;
        }

        /**
         * 获取 accountType
         *
         * @return 返回 accountType
         */
        public int getAccountType() {
            return accountType;
        }

        /**
         * 设置 accountType
         *
         * @return 返回 accountType
         */
        public void setAccountType(int accountType) {
            this.accountType = accountType;
        }

        /**
         * 获取 packingCode
         *
         * @return 返回 packingCode
         */
        public String getPackingCode() {
            return packingCode;
        }

        /**
         * 设置 packingCode
         *
         * @return 返回 packingCode
         */
        public void setPackingCode(String packingCode) {
            this.packingCode = packingCode;
        }

        /**
         * 获取 packingName
         *
         * @return 返回 packingName
         */
        public String getPackingName() {
            return packingName;
        }

        /**
         * 设置 packingName
         *
         * @return 返回 packingName
         */
        public void setPackingName(String packingName) {
            this.packingName = packingName;
        }

        /**
         * 获取 count
         *
         * @return 返回 count
         */
        public int getCount() {
            return count;
        }

        /**
         * 设置 count
         *
         * @return 返回 count
         */
        public void setCount(int count) {
            this.count = count;
        }

        @Override
        public String toString() {
            return "AccountBalanceInfo{" +
                    "accountType=" + accountType +
                    ", packingCode='" + packingCode + '\'' +
                    ", packingName='" + packingName + '\'' +
                    ", count=" + count +
                    '}';
        }
    }




}
