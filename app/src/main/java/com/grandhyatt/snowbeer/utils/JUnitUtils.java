package com.grandhyatt.snowbeer.utils;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * JUnitUtils
 *
 * @author wulifu
 * @email 18602438878@qq.com
 * @mobile 18602438878
 * @create 2018/7/20 10:06
 */
public class JUnitUtils {
    private static Boolean isRunningTest = null;

    /**
     * 检测是否在运行单元测试 
     * @return
     */
    public static Boolean isRunningTest() {
        if (null == isRunningTest) {
            StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
            List statckList = Arrays.asList(stackTrace);
            for (Iterator i = statckList.iterator(); i.hasNext(); ) {
                String stackString = i.next().toString();
                if (stackString.lastIndexOf("junit.runners") > -1) {
                    isRunningTest = true;
                    return isRunningTest;
                }
            }
            isRunningTest = false;
            return isRunningTest;
        } else {
            return isRunningTest;
        }
    }
} 
