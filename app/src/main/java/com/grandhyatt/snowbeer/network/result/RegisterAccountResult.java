package com.grandhyatt.snowbeer.network.result;


import com.grandhyatt.commonlib.Result;

/**
 * 注册账号返回的数据结构
 *
 * @author
 * @email
 * @mobile
 * @create 2018/7/3 15:47
 */
public class RegisterAccountResult extends Result {


    private UserInfo data;

    /**
     * 获取 data
     *
     * @return 返回 data
     */
    public UserInfo getData() {
        return data;
    }

    /**
     * 设置 data
     *
     * @return 返回 data
     */
    public void setData(UserInfo data) {
        this.data = data;
    }

    public class UserInfo{

        private String avatar;

        private String nickname;

        private int sex;

        private String mobile;

        private String regtime;

        private String look_time;

        /**
         * 获取 avatar
         *
         * @return 返回 avatar
         */
        public String getAvatar() {
            return avatar;
        }

        /**
         * 设置 avatar
         *
         * @return 返回 avatar
         */
        public UserInfo setAvatar(String avatar) {
            this.avatar = avatar;
            return this;
        }

        /**
         * 获取 nickname
         *
         * @return 返回 nickname
         */
        public String getNickname() {
            return nickname;
        }

        /**
         * 设置 nickname
         *
         * @return 返回 nickname
         */
        public UserInfo setNickname(String nickname) {
            this.nickname = nickname;
            return this;
        }

        /**
         * 获取 sex
         *
         * @return 返回 sex
         */
        public int getSex() {
            return sex;
        }

        /**
         * 设置 sex
         *
         * @return 返回 sex
         */
        public UserInfo setSex(int sex) {
            this.sex = sex;
            return this;
        }

        /**
         * 获取 mobile
         *
         * @return 返回 mobile
         */
        public String getMobile() {
            return mobile;
        }

        /**
         * 设置 mobile
         *
         * @return 返回 mobile
         */
        public UserInfo setMobile(String mobile) {
            this.mobile = mobile;
            return this;
        }

        /**
         * 获取 regtime
         *
         * @return 返回 regtime
         */
        public String getRegtime() {
            return regtime;
        }

        /**
         * 设置 regtime
         *
         * @return 返回 regtime
         */
        public UserInfo setRegtime(String regtime) {
            this.regtime = regtime;
            return this;
        }

        /**
         * 获取 look_time
         *
         * @return 返回 look_time
         */
        public String getLook_time() {
            return look_time;
        }

        /**
         * 设置 look_time
         *
         * @return 返回 look_time
         */
        public UserInfo setLook_time(String look_time) {
            this.look_time = look_time;
            return this;
        }
    }


}
