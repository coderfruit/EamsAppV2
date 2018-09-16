package com.grandhyatt.snowbeer.db;

import com.grandhyatt.snowbeer.App;
import com.grandhyatt.snowbeer.entity.APIHostInfoEntity;
import com.grandhyatt.snowbeer.entity.LoginUserInfoEntity;

import java.util.List;

/**
 * ObjectBox工具类
 *
 * @author 吴立富
 * @email wulifu@travelsky.com
 * @mobile 18602438878
 * @create 2018/3/27 08:56
 */
public class ObjectBoxHelper {

    private static final String TAG = ObjectBoxHelper.class.getSimpleName();

    /**
     * 保存服务器地址信息
     */
    public static void saveAPIHostInfo(APIHostInfoEntity apiHostInfo) {
        App.getBoxStoreInstance().boxFor(APIHostInfoEntity.class).put(apiHostInfo);
    }

    /**
     * 删除服务器地址信息
     *
     * * @param null
     * @return
     */
    public static void removeAPIHostInfo(APIHostInfoEntity apiHostInfo) {
        App.getBoxStoreInstance().boxFor(APIHostInfoEntity.class).remove(apiHostInfo);
    }

    /**
     * 获取所有用户信息列表
     */
    public static List<APIHostInfoEntity> getAPIHostInfoList() {
        return App.getBoxStoreInstance().boxFor(APIHostInfoEntity.class).getAll();
    }

//    /**
//     * 获取所有用户信息列表
//     *
//     */
//    public static List<LoginUserInfoEntity> getUserInfoList(){
//        return App.getBoxStoreInstance().boxFor(LoginUserInfoEntity.class).getAll();
//    }
//
//    /**
//     * 使用用户ID查询用户信息
//     *
//     */
//    public static List<LoginUserInfoEntity> queryUserInfo(long userId){
//        return App.getBoxStoreInstance().boxFor(LoginUserInfoEntity.class).query().equal(LoginUserInfo_.user_id,userId).build().find();
//    }
//
    /**
     * 保存用户信息
     *
     */
    public static void saveUserInfo(LoginUserInfoEntity loginUserInfo){
        App.getBoxStoreInstance().boxFor(LoginUserInfoEntity.class).put(loginUserInfo);
    }

    /**
     * 查询用户信息
     *
     */
    public static LoginUserInfoEntity getUserInfo(long userId){
        return (LoginUserInfoEntity)App.getBoxStoreInstance().boxFor(LoginUserInfoEntity.class).getEntityInfo();
    }

}
