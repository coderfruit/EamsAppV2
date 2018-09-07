package com.grandhyatt.snowbeer;


import com.grandhyatt.commonlib.ApplicationBase;
import com.grandhyatt.snowbeer.entity.MyObjectBox;

import io.objectbox.BoxStore;

/**
 * Application实例
 *
 * @author 吴立富
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/6/1 10:54
 */
public class App extends ApplicationBase {

    private static BoxStore mBoxStore;

    @Override
    public void onCreate(){
        super.onCreate();

        mBoxStore = MyObjectBox.builder().androidContext(this).build();
    }

    /**
     * 获取ObjectBoxInstance
     *
     */
    public static BoxStore getBoxStoreInstance(){
        return mBoxStore;
    }

}
